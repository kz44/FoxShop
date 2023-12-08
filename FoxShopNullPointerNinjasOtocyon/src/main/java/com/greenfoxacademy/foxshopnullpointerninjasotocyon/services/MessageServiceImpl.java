package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.mapper.MessageMapper;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Message;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.MessageRepository;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private static final int PAGE_SIZE = 10;
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private UserService userService;
    private MessageMapper messageMapper;

    /**
     * Retrieves information about conversations for the current user.
     * <p>
     * This method fetches information about conversations, including details about other users,
     * the last message sent, and the number of messages in each conversation.
     *
     * @return ResponseEntity with conversation information or a success message if no conversations exist.
     */

    @Override
    public ResponseEntity<?> getConversationInfo() {
        User user = userService.getUserFromSecurityContextHolder();
        Set<User> otherUsers = messageRepository.findOtherUsers(user);

        if (otherUsers == null || otherUsers.isEmpty()) {
            return ResponseEntity.ok(new SuccessMessageDTO("You have no conversations with other users yet."));
        }
        List<ConversationDTO> conversations = new ArrayList<>();
        for (User otherUser : otherUsers) {
            Message lastMessage = messageRepository.getLastMessage(user, otherUser);
            LocalDateTime lastMessageTime = lastMessage.getSent();
            boolean isLastMessageAlreadyRead = lastMessage.isSeen();
            long numberOfMessages = messageRepository.countMessagesBetweenUsers(user, otherUser);
            ConversationDTO conversationDTO = new ConversationDTO(otherUser.getUsername(),
                    new ConversationInfoDTO(lastMessageTime, isLastMessageAlreadyRead, numberOfMessages));
            conversations.add(conversationDTO);
        }
        return ResponseEntity.ok().body(conversations);
    }

    /**
     * Retrieves paginated messages between the current user and another user based on the specified page number.
     * <p>
     * This method validates the provided page number, checks the existence of the specified user,
     * and retrieves paginated and sorted messages between the two users.
     *
     * @param otherUsername The username of the other user.
     * @param pageNumber    The page number for pagination.
     * @return ResponseEntity with paginated and sorted messages or an error message if validation fails.
     */

    @Override
    public ResponseEntity<?> getMessagesPagination(String otherUsername,
                                                   Integer pageNumber) {
        ResponseEntity<?> pageValidationResponse = validatePageNumber(pageNumber);
        if (pageValidationResponse != null) {
            return pageValidationResponse;
        }
        User user = userService.getUserFromSecurityContextHolder();
        Optional<User> otherUser = userRepository.findByUsername(otherUsername);
        if (!otherUser.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("User does not exist."));
        }
        return processMessages(user, otherUser.get(), pageNumber);
    }

    /**
     * Retrieves paginated messages between two specified users.
     * <p>
     * This method is restricted to users with the "ADMIN" or "DEVELOPER" role.
     * It validates the provided page number and the existence of both users,
     * then retrieves paginated and sorted messages between the specified users.
     *
     * @param user1      The username of the first user.
     * @param user2      The username of the second user.
     * @param pageNumber The page number for pagination.
     * @return ResponseEntity with paginated and sorted messages or an error message if validation fails.
     */

    @Override
    public ResponseEntity<?> getConversationBetweenTwoUsers(String user1, String user2, Integer pageNumber) {
        if (!userService.checkUserRole().equals("ADMIN") && !userService.checkUserRole().equals("DEVELOPER")) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("You have no permission to the request."));
        }
        ResponseEntity<?> pageValidationResponse = validatePageNumber(pageNumber);
        if (pageValidationResponse != null) {
            return pageValidationResponse;
        }
        Optional<User> userOpt1 = userRepository.findByUsername(user1);
        Optional<User> userOpt2 = userRepository.findByUsername(user2);
        if (!userOpt1.isPresent() || !userOpt2.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("At least one of the users or both does not exist."));
        }
        return processMessages(userOpt1.get(), userOpt2.get(), pageNumber);
    }

    /**
     * Validates the provided page number for pagination.
     * <p>
     * This method checks whether the provided page number is non-negative.
     * If the validation fails, it returns an error response; otherwise, it returns null.
     *
     * @param pageNumber The page number to be validated.
     * @return ResponseEntity with an error message if the page number is invalid, otherwise null.
     */

    private ResponseEntity<?> validatePageNumber(Integer pageNumber) {
        if (pageNumber < 0) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Invalid page number."));
        }
        return null;
    }

    /**
     * Processes and retrieves paginated and sorted messages between two specified users.
     *
     * <p>This method retrieves messages between two users, applies sorting and pagination,
     * and optionally updates the "seen" status for messages received by the current user.
     * If the logged-in user has the role "USER," the method updates the "seen" status for messages
     * where the logged-in user is the receiver and "seen" is currently false.
     * The updated messages are then saved to the database.
     * The method returns a list of paginated and sorted messages mapped to DTOs.</p>
     *
     * @param user1      The first user.
     * @param user2      The second user.
     * @param pageNumber The page number for pagination.
     * @return ResponseEntity with paginated and sorted messages or a success message if no messages exist.
     */

    private ResponseEntity<?> processMessages(User user1, User user2, int pageNumber) {
        Sort sort = Sort.by(Sort.Direction.DESC, "sent");
        Page<Message> messagePagedAndSorted =
                messageRepository.findMessagesBetweenUsers(
                        user1,
                        user2,
                        PageRequest.of(pageNumber, PAGE_SIZE, sort));
        if (userService.checkUserRole().equals("USER")) {
            User currentUser = userService.getUserFromSecurityContextHolder();
            messagePagedAndSorted.getContent().stream()
                    .filter(m -> m.getReceiver().equals(currentUser))
                    .filter(m -> !m.isSeen()).forEach(m -> m.setSeen(true));
            messageRepository.saveAll(messagePagedAndSorted.getContent());
        }
        List<MessagePageableDTO> mapMessagesToDTO = messagePagedAndSorted
                .stream()
                .map(messageMapper::toDTO)
                .toList();
        if (mapMessagesToDTO.isEmpty()) {
            return ResponseEntity.ok().body(new SuccessMessageDTO("You have no messages with other users yet."));
        }
        return ResponseEntity.ok().body(mapMessagesToDTO);
    }
}

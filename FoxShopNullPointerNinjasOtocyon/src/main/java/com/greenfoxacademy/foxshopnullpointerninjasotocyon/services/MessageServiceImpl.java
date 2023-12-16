package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.MessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.SuccessMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.mapper.MessageMapper;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Message;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.MessageRepository;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.MessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.SuccessMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
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
        if (otherUsers == null) {
            return ResponseEntity.internalServerError().body(new ErrorMessageDTO(
                    "Unable to retrieve conversations with other users at the moment. " +
                            "This could be due to a connection issue with the database."));
        }
        if (otherUsers.isEmpty()) {
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
        Page<Message> messagePagedAndSorted =
                messageRepository.findMessagesBetweenUsers(
                        user1,
                        user2,
                        PageRequest.of(pageNumber, PAGE_SIZE));
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

    /**
     * Sends a message to the specified user and persists the message in the repository.
     *
     * @param receiverUsername The username of the message recipient.
     * @param content          The content of the message.
     * @return ResponseEntity containing information about the status of the message sending:
     * - 200 OK and a success message in the response body for a successful request.
     * - 400 Bad Request and an error message in the response body if the recipient is not registered.
     */

    @Override
    public ResponseEntity<?> sendMessageByUsername(String receiverUsername, MessageDTO content) {
        final var receiver = userService.getUserByUsername(receiverUsername);

        if (receiver == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("The given receiver username is not registered"));
        }

        messageRepository.save(Message.builder()
                .content(content.getContent())
                .sender(userService.getUserFromSecurityContextHolder())
                .receiver(receiver)
                .sent(LocalDateTime.now())
                .build());

        return ResponseEntity.ok().body(new SuccessMessageDTO("Message successfully send to " + receiver.getUsername()));
    }

    /**
     * Edits the content of the latest unseen message sent by the authenticated user within the last 10 minutes.
     *
     * @param receiverUsername The receiver username
     * @param newContent The new content to set for the message.
     * @return ResponseEntity, the status of the edit operation:
     * - 200 OK and a success message in the response body for a successful edit.
     * - 400 Bad Request and an error message in the response body if there is no eligible message within 10 minutes.
     */
    @Override
    public ResponseEntity<?> editMessage(String receiverUsername, MessageDTO newContent) {
        final var sender = userService.getUserFromSecurityContextHolder();
        final var receiver = userService.getUserByUsername(receiverUsername);
        LocalDateTime timeThreshold = LocalDateTime.now().minusMinutes(10);

        if (receiver == null){
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("There isn't user with the given username"));
        }

        var messageOpt = messageRepository.findUnseenMessageWithinMinutesDescLimit1(sender.getId(), receiver.getId(), timeThreshold);

        if (messageOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("There isn't new messages within last 10 minutes or they have already been read."));
        }

        var message = messageOpt.get();
        message.setContent(newContent.getContent());
        message.setSent(LocalDateTime.now());

        messageRepository.save(message);
        return ResponseEntity.ok().body(new SuccessMessageDTO("Message was successfully edited"));
    }
}

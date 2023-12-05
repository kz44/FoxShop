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
import org.springframework.data.domain.Pageable;
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
     * Service method to retrieve information about user conversations.
     * <p>
     * This method fetches details about the conversations the current user has with other users.
     * It includes information such as the last message sent, whether it has been read, and the total
     * number of messages in each conversation.
     *
     * @return List<ConversationDTO> - A list of ConversationDTO objects containing information about each conversation.
     *                                If the user has no conversations, an empty list is returned.
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
            boolean isLastMessageAlreadyRead = lastMessage.isAlreadyRead();
            long numberOfMessages = messageRepository.countMessagesBetweenUsers(user, otherUser);

            ConversationDTO conversationDTO = new ConversationDTO(otherUser.getUsername(),
                    new ConversationInfoDTO(lastMessageTime, isLastMessageAlreadyRead, numberOfMessages));
            conversations.add(conversationDTO);
        }
        return ResponseEntity.ok().body(conversations);
    }

    /**
     * Service method to retrieve paginated and sorted messages between the current user and another user.
     * <p>
     * This method fetches messages between the current user and the specified user, paginated and sorted by
     * the sent timestamp in descending order.
     *
     * @param otherUsername String - The username of the other user to retrieve messages with.
     * @param pageNumber int - The page number for pagination (starting from 0).
     * @param pageable Pageable - Object specifying pagination and sorting parameters.
     * @return ResponseEntity<List<MessagePageableDTO>> - A response entity containing a list of paginated
     *                                                  and sorted MessagePageableDTO objects.
     *                                                  If the request is invalid or the user does not exist,
     *                                                  an error message is returned in the response body.
     */

    @Override
    public ResponseEntity<?> getMessagesPagination(String otherUsername,
                                                   int pageNumber,
                                                   Pageable pageable) {
        if (pageNumber < 0 ) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Invalid page number."));
        }
        Optional<User> otherUser = userRepository.findByUsername(otherUsername);
        if (!otherUser.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("User does not exist."));
        }
        User user = userService.getUserFromSecurityContextHolder();
        Sort sort = Sort.by(Sort.Direction.DESC, "sent");
        int pageSize = PAGE_SIZE;
        List<MessagePageableDTO> messagePagedAndSorted =
                messageRepository.findMessagesBetweenUsers(
                user,
                otherUser.get(),
                PageRequest.of(pageNumber, pageSize, sort)).stream().map(messageMapper::toDTO).toList();
        return ResponseEntity.ok().body(messagePagedAndSorted);
    }
}

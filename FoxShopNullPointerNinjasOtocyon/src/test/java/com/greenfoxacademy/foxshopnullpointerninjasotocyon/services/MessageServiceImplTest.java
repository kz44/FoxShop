package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.MessagePageableDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.SuccessMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.mapper.MessageMapper;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Message;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.MessageRepository;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MessageServiceImplTest {
    private static final int PAGE_SIZE = 10;
    private static User testUser;

    static {
        testUser = new User();
        testUser.setUsername("testUser");
    }

    @MockBean
    private static MessageRepository messageRepository = Mockito.mock(MessageRepository.class);
    @MockBean
    private static UserRepository userRepository = Mockito.mock(UserRepository.class);
    @MockBean
    private static UserService userService = Mockito.mock(UserService.class);
    @MockBean
    private static MessageMapper messageMapper = Mockito.mock(MessageMapper.class);
    private static final MessageService messageService = new MessageServiceImpl(messageRepository, userRepository, userService, messageMapper);

    @BeforeAll
    static void mockUniversalUser() {
        Mockito.when(userService.getUserFromSecurityContextHolder()).thenReturn(testUser);
    }

    @Test
    void getConversationInfoParameterOtherUsersReturnsNull() {
        Set<User> otherUsers = null;
//        Mockito.when(userService.getUserFromSecurityContextHolder()).thenReturn(testUser);
        Mockito.when(messageRepository.findOtherUsers(testUser)).thenReturn(otherUsers);
        ResponseEntity<?> resultResponse = messageService.getConversationInfo();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resultResponse.getStatusCode());
        assertInstanceOf(ErrorMessageDTO.class, resultResponse.getBody());
        assertNotNull(resultResponse.getBody());
        ErrorMessageDTO errorDtoInstance = (ErrorMessageDTO) resultResponse.getBody();
        assertEquals(
                "Unable to retrieve conversations with other users at the moment. " +
                        "This could be due to a connection issue with the database.", errorDtoInstance.getError());
    }

    @Test
    void getConversationInfoParameterOtherUsersReturnsEmpty() {
        Set<User> otherUsers = new HashSet<>();
//        Mockito.when(userService.getUserFromSecurityContextHolder()).thenReturn(testUser);
        Mockito.when(messageRepository.findOtherUsers(testUser)).thenReturn(otherUsers);
        ResponseEntity<?> resultResponse = messageService.getConversationInfo();
        assertEquals(HttpStatus.OK, resultResponse.getStatusCode());
        assertInstanceOf(SuccessMessageDTO.class, resultResponse.getBody());
        assertNotNull(resultResponse.getBody());
        SuccessMessageDTO successDTOInstance = (SuccessMessageDTO) resultResponse.getBody();
        assertEquals("You have no conversations with other users yet.", successDTOInstance.getSuccess());
    }

    @Test
    void getConversationBetweenTwoUsersWithUnauthorizedUserRoleReturnsBadRequest() {
        Mockito.when(userService.checkUserRole()).thenReturn("USER");
        ResponseEntity<?> resultResponse = messageService.getConversationBetweenTwoUsers("testUser", "u2", 0);
        assertNotNull(resultResponse.getBody());
        assertEquals(resultResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertInstanceOf(ErrorMessageDTO.class, resultResponse.getBody());
        ErrorMessageDTO errorDTOInstance = (ErrorMessageDTO) resultResponse.getBody();
        assertEquals("You have no permission to the request.", errorDTOInstance.getError());
    }

    @Test
    void getConversationBetweenTwoUsersReturnsListOfMessageDTOs() {
        int pageNumber = 0;
        User sender = new User();
        sender.setUsername("mySender");
        User receiver = new User();
        receiver.setUsername("myReceiver");
        Message message = new Message(1L, "Hello", LocalDateTime.of(2024, 01, 24, 15, 59), true, sender, receiver);
//        messageRepository.save(message);
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        Page<Message> page = new PageImpl(List.of(message), pageable, 0);
        Mockito.when(userService.checkUserRole()).thenReturn("ADMIN");
        Mockito.when(messageRepository.findMessagesBetweenUsers(sender, receiver, PageRequest.of(pageNumber, PAGE_SIZE))).thenReturn(page);
        Mockito.when(userRepository.findByUsername("mySender")).thenReturn(Optional.of(sender));
        Mockito.when(userRepository.findByUsername("myReceiver")).thenReturn(Optional.of(receiver));

        List<MessagePageableDTO> mapMessagesToDTO = new ArrayList<>();
        mapMessagesToDTO.add(new MessageMapper().toDTO(message));

        ResponseEntity<?> successResponse = messageService.getConversationBetweenTwoUsers(sender.getUsername(), receiver.getUsername(), pageNumber);
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());
        assertNotNull(successResponse.getBody());
        assertInstanceOf(List.class, successResponse.getBody());
        List<MessagePageableDTO> responseDTOs = (List<MessagePageableDTO>) successResponse.getBody();
        for (int i = 0; i < mapMessagesToDTO.size(); i++) {
            assertEquals(mapMessagesToDTO.get(i).getContent(), responseDTOs.get(i).getContent());
            assertEquals(mapMessagesToDTO.get(i).getSent(), responseDTOs.get(i).getSent());
            assertEquals(mapMessagesToDTO.get(i).getSenderUsername(), responseDTOs.get(i).getSenderUsername());
            assertEquals(mapMessagesToDTO.get(i).getReceiverUsername(), responseDTOs.get(i).getReceiverUsername());
        }
    }


    @Test
    void getMessagesPaginationUsingInvalidPageNumberReturnsBadRequest() {
        int invalidPageNumber = -1;
        ResponseEntity<?> errorResponse = messageService.getMessagesPagination("myReceiver", invalidPageNumber);
        assertNotNull(errorResponse.getBody());
        assertInstanceOf(ErrorMessageDTO.class, errorResponse.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) errorResponse.getBody();
        assertEquals("Invalid page number.", errorMessageDTO.getError());
    }
    @Test
    void getMessagesPaginationReturnsOK() {
        int pageNumber = 0;
        int pageSize = 10;
        User otherUser = new User();
        otherUser.setUsername("user2");
        Mockito.when(userRepository.findByUsername("user2")).thenReturn(Optional.of(otherUser));
        Mockito.when(userService.checkUserRole()).thenReturn("USER");
        Mockito.when(userService.getUserFromSecurityContextHolder()).thenReturn(testUser);
        Message message = new Message(1L, "Hello", LocalDateTime.of(2024, 01, 24, 15, 59), true, otherUser, testUser);
//        messageRepository.save(message);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Message> page = new PageImpl(List.of(message), pageable, 0);
        Mockito.when(messageRepository.findMessagesBetweenUsers(testUser, otherUser, PageRequest.of(pageNumber, pageSize))).thenReturn(page);
        List<MessagePageableDTO> mapMessagesToDTO = new ArrayList<>();
        mapMessagesToDTO.add(new MessageMapper().toDTO(message));
        ResponseEntity<?> successResponse = messageService.getMessagesPagination("user2",0);
        assertNotNull(successResponse.getBody());
        assertInstanceOf(List.class, successResponse.getBody());
        List<MessagePageableDTO> responseDTOs = (List<MessagePageableDTO>) successResponse.getBody();
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());

    }


    @Test
    void sendMessageByUsername() {
    }

//    @Test
//    void editMessage() {
//    }
//
//    @Test
//    void deleteLastMessage() {
//    }
}
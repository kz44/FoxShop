package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.MessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.MessagePageableDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.SuccessMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.mapper.MessageMapper;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Message;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.MessageRepository;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageServiceImplTest {
    private static final int PAGE_SIZE = 10;
    private static final LocalDateTime MOCKED_NOW = LocalDateTime.of(2024, 1, 25, 21, 45);
    private static MockedStatic<LocalDateTime> mockDateTime = Mockito.mockStatic(LocalDateTime.class);
    private static User testUser;
    private static User receiver;
    private static User sender;

    static {
        testUser = new User();
        receiver = new User();
        sender = new User();
        testUser.setUsername("testUser");
        receiver.setUsername("receiver");
        sender.setUsername("sender");
        testUser.setId(1L);
        receiver.setId(2L);
        sender.setId(3L);
    }

    @MockBean
    private static MessageRepository messageRepository = Mockito.mock(MessageRepository.class);
    @MockBean
    private static UserRepository userRepository = Mockito.mock(UserRepository.class);
    @MockBean
    private static UserService userService = Mockito.mock(UserService.class);
    private static MessageService messageService = new MessageServiceImpl(messageRepository, userRepository, userService);

    @BeforeAll
    static void mockUniversalUser() {
        Mockito.when(userService.getUserFromSecurityContextHolder()).thenReturn(testUser);
        Mockito.when(userService.getUserByUsername("receiver")).thenReturn(receiver);
    }

    @Test
    void getConversationInfoParameterOtherUsersReturnsNull() {
        Set<User> otherUsers = null;
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
        Set<User> emptyUsers = new HashSet<>();
        Mockito.when(messageRepository.findOtherUsers(testUser)).thenReturn(emptyUsers);
        mockDateTime.when(LocalDateTime::now).thenReturn(MOCKED_NOW);
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
        ResponseEntity<?> resultResponse = messageService.getConversationBetweenTwoUsers("notCheckedUsername1", "notCheckedUsername2", 0);
        assertNotNull(resultResponse.getBody());
        assertEquals(resultResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertInstanceOf(ErrorMessageDTO.class, resultResponse.getBody());
        ErrorMessageDTO errorDTOInstance = (ErrorMessageDTO) resultResponse.getBody();
        assertEquals("You have no permission to the request.", errorDTOInstance.getError());
    }

    @Test
    void getConversationBetweenTwoUsersReturnsListOfMessageDTOs() {
        int pageNumber = 0;
        Message message = new Message(1L, "Hello", LocalDateTime.of(2024, 01, 24, 15, 59), true, sender, receiver);
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        Page<Message> page = new PageImpl(List.of(message), pageable, 0);
        Mockito.when(userService.checkUserRole()).thenReturn("ADMIN");
        Mockito.when(messageRepository.findMessagesBetweenUsers(sender, receiver, PageRequest.of(pageNumber, PAGE_SIZE))).thenReturn(page);
        Mockito.when(userRepository.findByUsername("sender")).thenReturn(Optional.of(sender));
        Mockito.when(userRepository.findByUsername("receiver")).thenReturn(Optional.of(receiver));

        List<MessagePageableDTO> mapMessagesToDTO = new ArrayList<>();
        mapMessagesToDTO.add(MessageMapper.toDTO(message));

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
        ResponseEntity<?> errorResponse = messageService.getMessagesPagination("notCheckedUsername", invalidPageNumber);
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
        Mockito.when(userRepository.findByUsername("sender")).thenReturn(Optional.of(sender));
        Mockito.when(userService.checkUserRole()).thenReturn("USER");
        Message message = new Message(1L, "Hello", LocalDateTime.of(2024, 01, 24, 15, 59), true, sender, testUser);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Message> page = new PageImpl(List.of(message), pageable, 0);
        Mockito.when(messageRepository.findMessagesBetweenUsers(testUser, sender, PageRequest.of(pageNumber, pageSize))).thenReturn(page);
        List<MessagePageableDTO> mapMessagesToDTO = new ArrayList<>();
        mapMessagesToDTO.add(MessageMapper.toDTO(message));
        ResponseEntity<?> successResponse = messageService.getMessagesPagination("sender", 0);
        assertNotNull(successResponse.getBody());
        assertInstanceOf(List.class, successResponse.getBody());
        List<MessagePageableDTO> responseDTOs = (List<MessagePageableDTO>) successResponse.getBody();
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }


    @Test
    void sendMessageByUsernameReturnsOK() {
        Mockito.when(userService.getUserByUsername("receiver")).thenReturn(receiver);
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("hi!");
        ResponseEntity<?> successResponse = messageService.sendMessageByUsername("receiver", messageDTO);
        assertInstanceOf(SuccessMessageDTO.class, successResponse.getBody());
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());
        SuccessMessageDTO successMessageDTO = (SuccessMessageDTO) successResponse.getBody();
        assertEquals("Message successfully send to " + receiver.getUsername(), successMessageDTO.getSuccess());
    }

    @Test
    void sendMessageByUsernameReturnsBadRequest() {
        Mockito.when(userService.getUserByUsername("receiver")).thenReturn(null);
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("hi!");
        ResponseEntity<?> badRequestResponse = messageService.sendMessageByUsername("receiver", messageDTO);
        assertInstanceOf(ErrorMessageDTO.class, badRequestResponse.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, badRequestResponse.getStatusCode());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) badRequestResponse.getBody();
        assertEquals("The given receiver username is not registered", errorMessageDTO.getError());
    }

    @Test
    void editMessageReturnsOK() {
        Mockito.when(userService.getUserByUsername("receiver")).thenReturn(receiver);
        Optional<Message> messageOptional = Optional.of(new Message(1L, "Hello", MOCKED_NOW.minusMinutes(3), true, receiver, testUser));
        mockDateTime.when(LocalDateTime::now).thenReturn(MOCKED_NOW);
        LocalDateTime timeThreshold = MOCKED_NOW.minusMinutes(10);
        Mockito.when(messageRepository.findUnseenMessageWithinMinutesDescLimit1(testUser.getId(), receiver.getId(), timeThreshold)).thenReturn(messageOptional);
        MessageDTO messageEditDTO = new MessageDTO();
        messageEditDTO.setContent("edited last message");
        ResponseEntity<?> successReponse = messageService.editMessage("receiver", messageEditDTO);
        assertNotNull(successReponse.getBody());
        assertInstanceOf(SuccessMessageDTO.class, successReponse.getBody());
        assertEquals(HttpStatus.OK, successReponse.getStatusCode());
        SuccessMessageDTO successMessageDTO = (SuccessMessageDTO) successReponse.getBody();
        assertEquals("Message was successfully edited", successMessageDTO.getSuccess());
    }

    @Test
    void editMessageReturnsEmptyAndBadRequest() {
        Mockito.when(userService.getUserByUsername("receiver")).thenReturn(receiver);
        mockDateTime.when(LocalDateTime::now).thenReturn(MOCKED_NOW);
        LocalDateTime timeThreshold = MOCKED_NOW.minusMinutes(10);
        Mockito.when(messageRepository.findUnseenMessageWithinMinutesDescLimit1(testUser.getId(), receiver.getId(), timeThreshold)).thenReturn(Optional.empty());
        MessageDTO messageEditDTO = new MessageDTO();
        messageEditDTO.setContent("edited last message");
        ResponseEntity<?> errorResponse = messageService.editMessage("receiver",messageEditDTO);
        assertNotNull(errorResponse.getBody());
        assertInstanceOf(ErrorMessageDTO.class, errorResponse.getBody());
        assertEquals(HttpStatus.BAD_REQUEST,errorResponse.getStatusCode());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) errorResponse.getBody();
        assertEquals("There is no message to edit within 10 minutes", errorMessageDTO.getError());
    }

    @Test
    void deleteLastMessageReturnsOK() {
        Mockito.when(userService.getUserByUsername("receiver")).thenReturn(receiver);
        mockDateTime.when(LocalDateTime::now).thenReturn(MOCKED_NOW);
        LocalDateTime timeThreshold = MOCKED_NOW.minusMinutes(10);
        Mockito.when(messageRepository.findUnseenMessageWithinMinutesDescLimit1(testUser.getId(), receiver.getId(), timeThreshold)).thenReturn(Optional.empty());
        ResponseEntity<?> errorResponse = messageService.deleteLastMessage("receiver");
        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
        assertNotNull(errorResponse.getBody());
        assertInstanceOf(ErrorMessageDTO.class,errorResponse.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) errorResponse.getBody();
        assertEquals("There are either no new messages within last 10 minutes or they have already been read.", errorMessageDTO.getError());
    }

    @Test
    void deleteMessageReturnsOK() {
        Optional<Message> messageOptional = Optional.of(new Message(1L, "Hello", MOCKED_NOW.minusMinutes(3), true, receiver, testUser));
        Mockito.when(userService.getUserByUsername("receiver")).thenReturn(receiver);
        mockDateTime.when(LocalDateTime::now).thenReturn(MOCKED_NOW);
        LocalDateTime timeThreshold = MOCKED_NOW.minusMinutes(10);
        Mockito.when(messageRepository.findUnseenMessageWithinMinutesDescLimit1(testUser.getId(), receiver.getId(), timeThreshold)).thenReturn(messageOptional);
        ResponseEntity<?> successResponse = messageService.deleteLastMessage("receiver");
        assertNotNull(successResponse.getBody());
        assertInstanceOf(SuccessMessageDTO.class,successResponse.getBody());
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());
        SuccessMessageDTO successMessageDTO = (SuccessMessageDTO) successResponse.getBody();
        assertEquals("Message was successfully deleted", successMessageDTO.getSuccess());

    }

}
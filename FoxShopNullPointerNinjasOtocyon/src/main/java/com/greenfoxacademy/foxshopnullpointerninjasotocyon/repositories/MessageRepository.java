package com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Message;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT DISTINCT otherUser FROM Message m JOIN User otherUser ON (m.sender = :user AND m.receiver = otherUser) OR (m.sender = otherUser AND m.receiver = :user)")
    Set<User> findOtherUsers(User user);

    @Query("SELECT m FROM Message m WHERE (m.sender = :user AND m.receiver = :otherUser) OR (m.sender = :otherUser AND m.receiver = :user) ORDER BY m.sent DESC LIMIT 1")
    Message getLastMessage(User user, User otherUser);

    @Query("SELECT COUNT(m.content) FROM Message m WHERE (m.sender = :user AND m.receiver = :otherUser) OR (m.sender = :otherUser AND m.receiver = :user)")
    int countMessagesBetweenUsers(User user, User otherUser);

    @Query("SELECT m FROM Message m WHERE (m.sender = :user AND m.receiver = :otherUser) OR (m.sender = :otherUser AND m.receiver = :user) ORDER BY m.sent DESC")
    Page<Message> findMessagesBetweenUsers(User user, User otherUser, Pageable pageable);

    /**
     * Finds the latest unseen message sent by a user within a specified time threshold.
     *
     * @param userId        The ID of the sender user.
     * @param receiverId    The ID of the receiver user.
     * @param timeThreshold The time threshold indicating the period within which the message was sent.
     * @return the latest unseen message, empty if no matching message is found.
     */
    @Query("SELECT msg FROM Message msg WHERE " +
            "msg.sender.id = :userId AND " +
            "msg.receiver.id = :receiverId AND " +
            "msg.seen = false AND " +
            "msg.sent >= :timeThreshold " +
            "ORDER BY msg.sent DESC LIMIT 1")
    Optional<Message> findUnseenMessageWithinMinutesDescLimit1(@Param("userId") Long userId,
                                                               @Param("receiverId") Long receiverId,
                                                               @Param("timeThreshold") LocalDateTime timeThreshold);
}

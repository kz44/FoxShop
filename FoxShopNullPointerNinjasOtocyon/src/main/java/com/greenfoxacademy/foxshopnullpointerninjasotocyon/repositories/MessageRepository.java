package com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Message;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT DISTINCT otherUser FROM Message m JOIN User otherUser ON (m.sender = :user AND m.receiver = otherUser) OR (m.sender = otherUser AND m.receiver = :user)")
    Set<User> findOtherUsers(User user);

    @Query("SELECT m FROM Message m WHERE (m.sender = :user AND m.receiver = :otherUser) OR (m.sender = :otherUser AND m.receiver = :user) ORDER BY m.sent DESC LIMIT 1")
    Message getLastMessage(User user, User otherUser);

    @Query("SELECT COUNT(m.content) FROM Message m WHERE (m.sender = :user AND m.receiver = :otherUser) OR (m.sender = :otherUser AND m.receiver = :user)")
    int countMessagesBetweenUsers(User user, User otherUser);

    @Query("SELECT m FROM Message m WHERE (m.sender = :user AND m.receiver = :otherUser) OR (m.sender = :otherUser AND m.receiver = :user) ORDER BY m.sent DESC")
    List<Message> findMessagesBetweenUsers(User user, User otherUser, Pageable pageable);
}

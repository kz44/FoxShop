package com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Message;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT DISTINCT CASE WHEN m.sender = :user THEN m.receiver ELSE m.sender END FROM Message m WHERE m.sender = :user OR m.receiver = :user")
    Set<User> findOtherUsers(User user);

    @Query("SELECT m FROM Message m WHERE (m.sender = :user AND m.receiver = :otherUser) OR (m.sender = :otherUser AND m.receiver = :user) ORDER BY m.sent DESC LIMIT 1")
    Message getLastMessage(User user, User otherUser);

    @Query("SELECT COUNT(m) FROM Message m WHERE (m.sender = :user AND m.receiver = :otherUser) OR (m.sender = :otherUser AND m.receiver = :user)")
    int countMessagesBetweenUsers(User user, User otherUser);
}

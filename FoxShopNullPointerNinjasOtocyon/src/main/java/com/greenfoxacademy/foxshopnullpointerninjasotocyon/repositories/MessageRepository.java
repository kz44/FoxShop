package com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Finds the latest unseen message sent by a user within a specified time threshold.
     *
     * @param userId        The ID of the sender user.
     * @param timeThreshold The time threshold indicating the period within which the message was sent.
     * @return the latest unseen message, empty if no matching message is found.
     */
    @Query("SELECT msg FROM Message msg WHERE " +
            "msg.sender.id = :userId AND " +
            "msg.seen = false AND " +
            "msg.sent >= :timeThreshold " +
            "ORDER BY msg.sent DESC LIMIT 1")
    Optional<Message> findUnseenMessageWithinMinutesDescLimit1(@Param("userId") Long userId,
                                                                                  @Param("timeThreshold")LocalDateTime timeThreshold);
}

package com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT msg FROM Message msg WHERE " +
            "msg.sender.id = :userId AND " +
            "msg.seen = false AND " +
            "msg.sent >= :timeThreshold " +
            "ORDER BY msg.sent DESC LIMIT 1")
    Optional<Message> findMessageByUserIdAndSeenFalseAndWithin10MinutesDescLimit1(@Param("userId") Long userId,
                                                                                  @Param("timeThreshold")LocalDateTime timeThreshold);
}

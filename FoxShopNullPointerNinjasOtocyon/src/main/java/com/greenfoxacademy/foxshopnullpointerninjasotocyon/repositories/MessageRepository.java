package com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT msg FROM Message msg WHERE " +
            "msg.sender.id = :userId AND " +
            "msg.seen = false AND " +
            "msg.sent BETWEEN msg.sent AND FUNCTION('DATE_ADD', msg.sent,  10, 'MINUTE') " +
            "ORDER BY msg.sent " +
            "DESC limit 1")
    Optional<Message> findMessageByUserIdAndSeenFalseAndWithin10MinutesDescLimit1(@Param("userId") Long userId);

}

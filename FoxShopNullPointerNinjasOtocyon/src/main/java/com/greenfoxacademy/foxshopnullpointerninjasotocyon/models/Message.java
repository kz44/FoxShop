package com.greenfoxacademy.foxshopnullpointerninjasotocyon.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime sent;

    @Column(nullable = false)
    @Builder.Default
    private boolean seen = false;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", sent=" + sent +
                ", seen=" + seen +
                ", sender=" + sender.getUsername() +
                ", receiver=" + receiver.getUsername() +
                '}';
    }
}

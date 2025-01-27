package com.TrungTinhBackend.codearena_backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatRoomName;

    private boolean isDeleted;

    @ManyToMany(mappedBy = "chatRooms")
    private List<User> users;

    @OneToOne
    @JoinColumn(name = "lesson_id",unique = true)
    private Lesson lesson;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<Chat> chats;
}

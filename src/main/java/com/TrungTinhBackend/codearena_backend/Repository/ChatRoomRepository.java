package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    @Query("SELECT c FROM ChatRoom c WHERE " +
            "(c.user1.id = :id1 AND c.user2.id = :id2) OR " +
            "(c.user1.id = :id2 AND c.user2.id = :id1)")
    ChatRoom findChatRoomByUserIds(@Param("id1") Long id1, @Param("id2") Long id2);

}

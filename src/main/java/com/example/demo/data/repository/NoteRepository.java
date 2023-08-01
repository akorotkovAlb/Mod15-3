package com.example.demo.data.repository;

import com.example.demo.data.entity.NoteEntity;
import com.example.demo.data.projection.NoteWithUserNameProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, UUID> {

    @Query(nativeQuery = true, value =
            "SELECT n.id AS noteId, n.title, n.content, u.username " +
                    "FROM note n LEFT JOIN users u ON u.id = n.user_id " +
                    "WHERE u.id = :userId")
    List<NoteWithUserNameProjection> findWithUsernameProjection(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {"user"})
    @Query("FROM NoteEntity ne WHERE ne.user.id = :userId")
    List<NoteEntity> findWithUsernameEntityGraph(@Param("userId") Long userId);
}

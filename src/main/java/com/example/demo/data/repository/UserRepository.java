package com.example.demo.data.repository;

import com.example.demo.data.entity.UserEntity;
import com.example.demo.data.projection.NoteWithUserNameProj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("FROM UserEntity ue WHERE ue.username = :username")
    Optional<UserEntity> findByUsername(@Param("username") String username);
}

package com.ecoverde.estateagency.repositories;

import com.ecoverde.estateagency.model.entity.BlogComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BlogCommentRepository extends JpaRepository<BlogComment,String> {
    Optional<BlogComment> findByTitle(String title);

    @Query("SELECT bc FROM BlogComment bc WHERE bc.author.username = :username")
    Set<BlogComment> findAllByAuthorUsername(@Param("username") String username);

    @Modifying
    @Query("DELETE FROM BlogComment bc WHERE bc.title = :title")
    void deleteByTitle(@Param("title") String title);
}

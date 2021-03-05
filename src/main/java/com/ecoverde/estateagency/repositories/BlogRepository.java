package com.ecoverde.estateagency.repositories;

import com.ecoverde.estateagency.model.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BlogRepository extends JpaRepository<Blog,String> {
    Optional<Blog> findByTitle(String title);

    @Query("SELECT b FROM Blog b WHERE b.author.username = :username")
    Set<Blog> findAllByAuthorUsername(@Param("username") String username);

    @Modifying
    @Query("DELETE FROM Blog b WHERE b.title = :title")
    void deleteByTitle(@Param("title") String title);
}

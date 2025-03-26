package ru.practicum.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.comment.model.Comment;

import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getCommentsByAuthorId(Long userId);

    @Query("SELECT c FROM Comment c WHERE c.event.id = :eventId")
    List<Comment> findAllCommentsForEvent(@Param("eventId") Long eventId, Pageable pageable);
}
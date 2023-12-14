package com.osipov.server.repository;

import com.osipov.server.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> getCommentByTask_Id(Long taskId);
}
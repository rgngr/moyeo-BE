package com.hanghae.finalProject.rest.comment.repository;

import com.hanghae.finalProject.rest.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {

}

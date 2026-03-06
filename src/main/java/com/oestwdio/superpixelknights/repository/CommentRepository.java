package com.oestwdio.superpixelknights.repository;

import com.oestwdio.superpixelknights.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
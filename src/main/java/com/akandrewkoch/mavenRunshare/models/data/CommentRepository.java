package com.akandrewkoch.mavenRunshare.models.data;

import com.akandrewkoch.mavenRunshare.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {

    Comment findById(int id);

    Page<Comment> findByDeletedCommentAndPrivateMessageOrderByDateCreatedDescTimeCreatedDesc(Boolean deletedComment,
                                                                          Boolean privateMessage, Pageable pageable);

    List<Comment> findFirst10ByOrderByDateCreatedDescTimeCreatedDesc();

    List<Comment> findByTrail_IdAndDeletedCommentOrderByDateCreatedDescTimeCreatedDesc(Integer trail,
                                                                                       Boolean deletedComment);

    List<Comment> findByRunSession_Id(Integer runSession);

    List<Comment> findByRunSession_IdAndDeletedCommentOrderByDateCreatedDescTimeCreatedDesc(Integer runSession,
                                                                                            Boolean deletedComment);

    Page<Comment> findByRunners_IdAndDeletedCommentOrderByDateCreatedDescTimeCreatedDesc(Integer Runner,
                                                                                                 Boolean deletedComment, Pageable pageable);
}

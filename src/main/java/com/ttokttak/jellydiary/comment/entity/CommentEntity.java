package com.ttokttak.jellydiary.comment.entity;

import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE comment SET is_deleted = true WHERE comment_id = ?")
@Table(name = "comment")
public class CommentEntity extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String commentContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_Id")
    private CommentEntity parent;

    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private DiaryPostEntity diaryPost;

    @Builder
    public CommentEntity(Long commentId, String commentContent, CommentEntity parent, Boolean isDeleted, UserEntity user, DiaryPostEntity diaryPost) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.parent = parent;
        this.isDeleted = isDeleted;
        this.user = user;
        this.diaryPost = diaryPost;
    }
}

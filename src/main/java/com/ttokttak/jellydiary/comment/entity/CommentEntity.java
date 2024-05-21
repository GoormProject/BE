package com.ttokttak.jellydiary.comment.entity;

import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comment")
public class CommentEntity extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String commentContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_Id")
    private CommentEntity parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private DiaryPostEntity diaryPost;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<CommentTagEntity> commentTag = new ArrayList<>();

    @Builder
    public CommentEntity(Long commentId, String commentContent, CommentEntity parent, UserEntity user, DiaryPostEntity diaryPost, List<CommentTagEntity> commentTag) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.parent = parent;
        this.user = user;
        this.diaryPost = diaryPost;
        this.commentTag = commentTag;
    }
}

package com.ttokttak.jellydiary.comment.entity;

import com.ttokttak.jellydiary.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comment_tag")
public class CommentTagEntity {
    @EmbeddedId
    private CommentTagCompositeKey id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @MapsId("commentId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private CommentEntity comment;

    @Builder
    public CommentTagEntity(CommentTagCompositeKey id, UserEntity user, CommentEntity comment) {
        this.id = id;
        this.user = user;
        this.comment = comment;
    }
}

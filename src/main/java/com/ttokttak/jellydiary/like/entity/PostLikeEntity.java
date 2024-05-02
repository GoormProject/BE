package com.ttokttak.jellydiary.like.entity;

import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post_like")
public class PostLikeEntity {
    @EmbeddedId
    private PostLikeCompositeKey id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @MapsId("diaryPostId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private DiaryPostEntity diaryPost;

    @Builder
    public PostLikeEntity(PostLikeCompositeKey id, UserEntity user, DiaryPostEntity diaryPost) {
        this.id = id;
        this.user = user;
        this.diaryPost = diaryPost;
    }
}

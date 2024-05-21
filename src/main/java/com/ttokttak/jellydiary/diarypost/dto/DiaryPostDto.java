package com.ttokttak.jellydiary.diarypost.dto;

import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class DiaryPostDto {
    private Long postId;
    private String postDate;
    private String postTitle;
    private String meal;
    private String snack;
    private String water;
    private String walk;
    private String toiletRecord;
    private String shower;
    private String weight;
    private String specialNote;
    private String weather;
    private String postContent;

    private Date createdAt;
    private Date modifiedAt;
    private Boolean isPublic;
    private Boolean isDeleted;
    private Long diaryId;
    private Long userId;
}


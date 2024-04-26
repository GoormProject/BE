package com.ttokttak.jellydiary.diarypost.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DiaryPostDto {
    private Long postId;
    private String postTitle;
    private String meal;
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
    private Boolean isDelete;
    private Long diaryId;
    private Long userId;
}


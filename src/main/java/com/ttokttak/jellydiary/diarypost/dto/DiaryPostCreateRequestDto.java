package com.ttokttak.jellydiary.diarypost.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DiaryPostCreateRequestDto {
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
    private Boolean isPublic;
}

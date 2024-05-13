package com.ttokttak.jellydiary.diarypost.service;

import com.ttokttak.jellydiary.diarypost.dto.DiaryPostCreateRequestDto;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DiaryPostService {

    ResponseDto<?> createDiaryPost(Long diaryId, DiaryPostCreateRequestDto diaryPostCreateRequestDto, List<MultipartFile> postImgs, CustomOAuth2User customOAuth2User) throws IOException;

    ResponseDto<?> updateDiaryPost(Long postId, DiaryPostCreateRequestDto diaryPostCreateRequestDto, List<Long> deleteImageIds, List<MultipartFile> newPostImgs, CustomOAuth2User customOAuth2User) throws IOException;

    ResponseDto<?> deleteDiaryPost(Long postId, CustomOAuth2User customOAuth2User);

    ResponseDto<?> getDiaryPostList(Long diaryId, CustomOAuth2User customOAuth2User);

    ResponseDto<?> getDiaryPostOne(Long postId, CustomOAuth2User customOAuth2User);
}

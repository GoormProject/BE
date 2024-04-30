package com.ttokttak.jellydiary.diarypost.mapper;

import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diarypost.dto.DiaryPostCreateRequestDto;
import com.ttokttak.jellydiary.diarypost.dto.DiaryPostCreateResponseDto;
import com.ttokttak.jellydiary.diarypost.dto.DiaryPostImgListResponseDto;
import com.ttokttak.jellydiary.diarypost.dto.DiaryPostListResponseDto;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring", imports = {LocalDate.class, DateTimeFormatter.class})
public interface DiaryPostMapper {
    DiaryPostMapper INSTANCE = Mappers.getMapper(DiaryPostMapper.class);

    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "postDate", expression = "java(LocalDate.parse(diaryPostCreateRequestDto.getPostDate(), DateTimeFormatter.ISO_DATE))")
    DiaryPostEntity diaryPostCreateRequestDtoToEntity(DiaryPostCreateRequestDto diaryPostCreateRequestDto, UserEntity user, DiaryProfileEntity diaryProfile);

    @Mapping(target = "postImgs", source = "postImgs")
    @Mapping(target = "diaryId", source = "diaryProfile.diaryId")
    @Mapping(target = "userId", source = "user.userId")
    DiaryPostCreateResponseDto entityToDiaryPostCreateResponseDto(DiaryPostEntity diaryPost, List<DiaryPostImgListResponseDto> postImgs, DiaryProfileEntity diaryProfile, UserEntity user);

    @Mapping(target = "diaryId", source = "diaryProfile.diaryId")
    @Mapping(target = "userId", source = "user.userId")
    DiaryPostListResponseDto entityToDiaryPostListResponseDto(DiaryPostEntity diaryPost, DiaryProfileEntity diaryProfile, UserEntity user);

}

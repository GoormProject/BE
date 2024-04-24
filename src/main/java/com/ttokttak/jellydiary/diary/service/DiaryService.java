package com.ttokttak.jellydiary.diary.service;

import com.ttokttak.jellydiary.diary.dto.DiaryProfileDto;
import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diarypost.mapper.DiaryPostMapper;
import com.ttokttak.jellydiary.diary.mapper.DiaryProfileMapper;
import com.ttokttak.jellydiary.diarypost.repository.DiaryPostRepository;
import com.ttokttak.jellydiary.diary.repository.DiaryProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class DiaryService {
    private final DiaryProfileRepository diaryProfileRepository;
    private final DiaryPostRepository diaryPostRepository;

    @Autowired
    private DiaryProfileMapper diaryProfileMapper;

    @Autowired
    private DiaryPostMapper diaryPostMapper;
    @Autowired
    public DiaryService(DiaryProfileRepository diaryProfileRepository, DiaryPostRepository diaryPostRepository) {
        this.diaryProfileRepository = diaryProfileRepository;
        this.diaryPostRepository = diaryPostRepository;
    }


    // 다이어리 생성
    public DiaryProfileDto createDiary(DiaryProfileDto diaryProfileDto) {
        DiaryProfileEntity diaryEntity = diaryProfileMapper.dtoToEntity(diaryProfileDto);

        diaryProfileDto.setIsDiaryDeleted(false);

        DiaryProfileEntity savedEntity = diaryProfileRepository.save(diaryEntity);

        return diaryProfileMapper.entityToDto(savedEntity);
    }


    // 다이어리 정보 수정
    public DiaryProfileDto updateDiaryProfile(DiaryProfileDto diaryProfileDto) {
        Optional<DiaryProfileEntity> diaryEntityOptional = diaryProfileRepository.findById(diaryProfileDto.getDiaryId());

        if (!diaryEntityOptional.isPresent()) {
            throw new EntityNotFoundException("다이어리를 ID로 찾을 수 없습니다: " + diaryProfileDto.getDiaryId());
        }

        DiaryProfileEntity diaryEntity = diaryEntityOptional.get();
        diaryProfileMapper.updateEntityFromDto(diaryProfileDto, diaryEntity);
        DiaryProfileEntity updatedEntity = diaryProfileRepository.save(diaryEntity);

        return diaryProfileMapper.entityToDto(updatedEntity);
    }

    // 다이어리 조회
    public DiaryProfileDto getDiaryProfile(Long diaryId) {
        Optional<DiaryProfileEntity> diaryEntityOptional = diaryProfileRepository.findById(diaryId);
        if (diaryEntityOptional.isPresent()) {
            return diaryProfileMapper.entityToDto(diaryEntityOptional.get());
        } else {
            throw new EntityNotFoundException("다이어리를 ID로 찾을 수 없습니다: " + diaryId);
        }
    }


}

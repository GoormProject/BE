package com.ttokttak.jellydiary.util.service;

import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserRoleEnum;
import com.ttokttak.jellydiary.diary.repository.DiaryProfileRepository;
import com.ttokttak.jellydiary.diary.repository.DiaryUserRepository;
import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import com.ttokttak.jellydiary.util.dto.SearchUserListResponseDto;
import com.ttokttak.jellydiary.util.mapper.SearchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.ErrorMsg.DIARY_ALREADY_DELETED;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.SEARCH_USER_SUCCESS;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final UserRepository userRepository;
    private final DiaryProfileRepository diaryProfileRepository;
    private final DiaryUserRepository diaryUserRepository;
    private final SearchMapper searchMapper;

    @Override
    public ResponseDto<?> getUserSearch(Long diaryId, String searchWord, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        List<UserEntity> userEntities = new ArrayList<>();

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        if(diaryProfileEntity.getIsDiaryDeleted())
            throw new CustomException(DIARY_ALREADY_DELETED);

        if (searchWord.contains(" ")) {
            throw new CustomException(SEARCH_WORD_MUST_NOT_BE_BLANK);
        }

        if (searchWord.isEmpty()) {
            userEntities.addAll(userRepository.findAll());
        } else {
            userEntities.addAll(userRepository.findAllByUserNameContains(searchWord));
        }

        List<SearchUserListResponseDto> searchUserListResponseDtos = new ArrayList<>();
        for (UserEntity user : userEntities) {
            Boolean isInvited;
            if (Objects.equals(user.getUserId(), userEntity.getUserId())) { //요청한 사용자와 같은 사용자면 리스트에 담지 않음
                continue;
            }
            Optional<DiaryUserEntity> dbDiaryUserEntity = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, user);
            if (dbDiaryUserEntity.isPresent()) {
                DiaryUserEntity diaryUserEntity = dbDiaryUserEntity.get();
                if (diaryUserEntity.getDiaryRole() == DiaryUserRoleEnum.CREATOR) {
                    continue;
                }
                isInvited = diaryUserEntity.getIsInvited();
            } else {
                isInvited = false;
            }

            if (isInvited == null) { //구독자일 경우 null이기 때문에 null을 false로 바꿔서 출력해준다.
                isInvited = false;
            }
            SearchUserListResponseDto searchUserListResponseDto = searchMapper.entityToUserListResponseDto(user, isInvited);
            searchUserListResponseDtos.add(searchUserListResponseDto);
        }

        return ResponseDto.builder()
                .statusCode(SEARCH_USER_SUCCESS.getHttpStatus().value())
                .message(SEARCH_USER_SUCCESS.getDetail())
                .data(searchUserListResponseDtos)
                .build();
    }
}

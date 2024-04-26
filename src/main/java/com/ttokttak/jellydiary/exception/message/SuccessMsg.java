package com.ttokttak.jellydiary.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Getter
@AllArgsConstructor
public enum SuccessMsg {
    /* 200 OK : 성공 */
    LOGIN_SUCCESS(OK, "로그인 완료"),
    SEARCH_DIARY_USER_LIST_SUCCESS(OK, "다이어리 참여자, 생성자 유저 검색 성공"),
//    SEARCH_USER_SUCCESS(OK, "유저 검색 성공"),
//    CHAT_HISTORY_SUCCESS(OK,"채팅 기록 조회 완료"),

    UPDATE_DIARY_PROFILE_SUCCESS(OK, "다이어리 프로필 수정 완료"),
//    UPDATE_PROJECT_SUCCESS(OK, "프로젝트 수정 완료"),
//    DELETE_PROJECT_SUCCESS(OK, "프로젝트 삭제 완료"),
//    SEARCH_PROJECT_SUCCESS(OK, "내 프로젝트 목록 검색 완료");

    /* 201 CREATED : 생성 */
    CREATE_DIARY_PROFILE_SUCCESS(CREATED, "다이어리 프로필 생성 완료");
//    CREATE_TEAM_SUCCESS(CREATED, "팀 생성 완료"),
//    CREATE_PROJECT_SUCCESS(CREATED, "프로젝트 생성 완료"),
//    CREATE_FILE_SUCCESS(CREATED, "파일 생성 완료"),
//    CREATE_FOLDER_SUCCESS(CREATED, "폴더 생성 완료");


    private final HttpStatus httpStatus;
    private final String detail;
}

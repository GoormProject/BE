package com.ttokttak.jellydiary.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorMsg {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    IMAGE_INVALID(BAD_REQUEST,"이미지가 잘못 되었습니다."),
//    INVALID_SEARCH_TERM(BAD_REQUEST, "검색어가 유효하지 않습니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "인증된 사용자가 아닙니다."),
    NOT_LOGGED_ID(UNAUTHORIZED, "로그인이 되어있지 않습니다."),

    /* 403 FORBIDDEN : 권한 없음 */
//    YOU_ARE_NOT_A_MEMBER_OF_THE_PROJECT_TEAM_AND_THEREFORE_CANNOT_PERFORM_THIS_ACTION(FORBIDDEN, "당신은 이 프로젝트 담당하는 팀의 구성원이 아님으로 권한이 없습니다."),
//    NO_AUTHORITY_TO_UPDATE_PROJECT(FORBIDDEN, " 리더가 아님으로 프로젝트 업데이트 권한이 없습니다."),
//    NO_AUTHORITY_TO_DELETE_PROJECT(FORBIDDEN, "프로젝트 삭제 권한이 없습니다."),
//    USER_NOT_IN_PROJECT_TEAM(FORBIDDEN, "프로젝트 팀의 구성원이 아닙니다."),
//    USER_NOT_IN_TEAM_PARTICIPANT(FORBIDDEN, "해당 팀의 구성원이 아닙니다."),
//    USER_NOT_TEAMLEADER(FORBIDDEN, "팀장 권한이 없습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(NOT_FOUND, "사용자가 존재하지 않습니다."),
    CHAT_ROOM_NOT_FOUND(NOT_FOUND, "채팅방이 존재하지 않습니다."),
    DIARY_NOT_FOUND(NOT_FOUND, "다이어리가 존재하지 않습니다."),


    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_USER(CONFLICT,"이미 가입된 사용자입니다."),
    DUPLICATE_EMAIL(CONFLICT,"중복된 이메일입니다.");


    /* 500 INTERNAL SERVER ERROR : 그 외 서버 에러 (컴파일 관련) */

    private final HttpStatus httpStatus;
    private final String detail;
}

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
    POST_DATE_IS_FROM_THE_PAST_TO_TODAY(BAD_REQUEST, "작성일자는 과거부터 오늘까지만 선택 가능합니다."),
    DIARY_CREATOR_CANNOT_BE_DELETED(BAD_REQUEST, "다이어리 생성자는 삭제 대상이 아닙니다."),
    CANNOT_FOLLOW_SELF(BAD_REQUEST, "자기 자신을 팔로우할 수 없습니다."),
//    INVALID_SEARCH_TERM(BAD_REQUEST, "검색어가 유효하지 않습니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "인증된 사용자가 아닙니다."),
    NOT_LOGGED_ID(UNAUTHORIZED, "로그인이 되어있지 않습니다."),

    /* 403 FORBIDDEN : 권한 없음 */
    YOU_ARE_NOT_A_DIARY_CREATOR(FORBIDDEN, " 다이어리 생성자가 아니므로 다이어리 프로필 업데이트, 참여자 추가, 삭제 권한이 없습니다."),
    YOU_DO_NOT_HAVE_PERMISSION_TO_WRITE_AND_UPDATE(FORBIDDEN, " 다이어리 생성자 이거나 쓰기 권한이 있는 사용자만이 게시물 생성 및 수정이 가능합니다."),
    YOU_DO_NOT_HAVE_PERMISSION_TO_DELETE(FORBIDDEN, " 다이어리 생성자만이 게시물 삭제가 가능합니다."),
    NO_PERMISSION_TO_APPROVE_INVITATION(FORBIDDEN, "해당 초대 요청을 승인할 권한이 없습니다."),
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
    DIARY_USER_NOT_FOUND(NOT_FOUND, "다이어리에 유저 정보가 존재하지 않습니다."),
    POST_NOT_FOUND(NOT_FOUND, "게시물이 존재하지 않습니다."),
    IMG_NOT_FOUND(NOT_FOUND, "이미지가 존재하지 않습니다."),
    DIARY_ALREADY_DELETED(NOT_FOUND, "삭제된 다이어리입니다."),
    POST_ALREADY_DELETED(NOT_FOUND, "삭제된 게시글입니다."),
    POST_IMG_ALREADY_DELETED(NOT_FOUND, "삭제된 게시글 이미지입니다."),
    POST_LIKE_NOT_FOUND(NOT_FOUND, "이 게시물은 아직 좋아요를 하지 않았습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_USER(CONFLICT,"이미 가입된 사용자입니다."),
    DUPLICATE_EMAIL(CONFLICT,"중복된 이메일입니다."),
    DUPLICATE_DIARY_USER(CONFLICT,"이미 해당 다이어리에 참여 중인 사용자입니다."),
    ALREADY_SUBSCRIBED_DIARY(CONFLICT,"이미 구독중인 다이어리입니다."),
    ALREADY_SENT_INVITATION(CONFLICT,"이미 초대 요청을 보낸 사용자입니다.");

    /* 500 INTERNAL SERVER ERROR : 그 외 서버 에러 (컴파일 관련) */

    private final HttpStatus httpStatus;
    private final String detail;
}

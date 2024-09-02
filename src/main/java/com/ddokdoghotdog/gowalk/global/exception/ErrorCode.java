package com.ddokdoghotdog.gowalk.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* Business Exceptions */
    // 400 BAD REQUEST
    INVALID_INPUT_VALUE("유효하지 않은 입력값입니다.", 400),
    TYPE_MISMATCH("입력된 enum값이 유효하지 않습니다.", 400),
    METHOD_NOT_ALLOWED("유효하지 않은 HTTP method입니다.", 400),
    MISSING_REQUEST_HEADER("HTTP 요청 헤더에 인증 값이 존재하지 않습니다.", 400),
    INVALID_IMAGE_FILE_TYPE("유효하지 않은 확장자입니다. 이미지 파일만 업로드 가능합니다.", 400),
    ILLEGAL_REGISTRATION_ID("유효하지 않은 OAuth Id입니다.", 400),
    ALREADY_LIKE_REQUEST("이미 찜을 누른 상품입니다.", 400),
    INVALID_REFUND_REQUEST("유효하지 않은 환불요청입니다.", 400),
    ALREADY_REFUND_REQUEST("이미 환불을 마친 상품입니다.", 400),
    ALREADY_CATEGORY_REQUEST("이미 등록을 한 카테고리입니다.", 400),
    ALREADY_VENDOR_REQUEST("이미 등록을 한 판매업체입니다.", 400),

    // 401 Unauthorized
    EXPIRED_TOKEN("만료된 토큰입니다. 재로그인이 필요합니다.", 401),
    LOGIN_REQUIRED("로그인이 필요합니다.", 401),
    INVALID_TOKEN("올바르지 않은 토큰입니다.", 401),
    INVALID_JWT_SIGNATURE("잘못된 JWT 시그니처입니다.", 401),

    // 403 FORBIDDEN
    NO_PERMISSION("권한이 없습니다.", 403),

    // 404 NOT FOUND
    MEMBER_NOT_FOUND("존재하지 않는 사용자 ID입니다.", 404),
    REVIEW_NOT_FOUND("존재하지 않는 댓글 ID입니다.", 404),
    ORDER_NOT_FOUND("존재하지 않는 주문 ID입니다.", 404),
    CM_CODE_NOT_FOUND("존재하지 않는 공통코드값입니다.", 404),
    BREED_NOT_FOUND("존재하지 않는 견종값입니다.", 404),
    PET_NOT_FOUND("존재하지 않는 펫입니다.", 404),
    ENTITY_NOT_FOUND("존재하지 않는 엔티티입니다.", 404), // 왠만하면 not found 만들어주세요.
    MEMBER_NICKNAME_NOT_FOUND("존재하지 않는 사용자 닉네임입니다.", 404),
    POST_NOT_FOUND("존재하지 않는 게시글입니다.", 404),
    BOARD_NOT_FOUND("존재하지 않는 게시판입니다.", 404),
    COMMENT_NOT_FOUND("존재하지 않는 댓글입니다.", 404),
    POST_OR_MEMBER_NOT_FOUND("게시글 혹은 유저가 존재하지 않습니다.", 404),
    QUEST_NOT_FOUND("존재하지 않는 퀘스트 ID입니다.", 404),

    WALK_NOT_FOUND("존재하지 않는 산책정보입니다.", 404),

    PRODUCT_NOT_FOUND("존재하지 않는 상품입니다.", 404),
    PRODUCT_LIKE_NOT_FOUND("찜을 누르지 않은 상품입니다.", 404),
    CARTITEM_NOT_FOUND("장바구니에 없는 상품입니다.", 404),
    VENDOR_NOT_FOUND("존재하지 않는 판매업체입니다.", 404),
    CATEGORY_NOT_FOUND("존재하지 않는 카테고리입니다.", 404),
    ORDER_ITEM_NOT_FOUND("존재하지 않는 주문 상품입니다.", 404),
    PAYMENT_NOT_FOUND("존재하지 않는 결제 내용입니다.", 404),

    // 406 Not Acceptable
    INVALID_PHONE_NUMBER("유효하지 않은 휴대폰번호입니다.", 406),
    LACK_STOCK("상품 재고가 부족합니다.", 406),
    LACK_POINT("포인트가 부족합니다.", 406),

    // 500 INTERNAL SERVER ERROR
    DB_QUERY_EXECUTION_ERROR("쿼리가 정상적으로 완료되지 않았습니다.", 500),
    AWS_S3_UPLOAD_ERROR("AWS S3 이미지 업로드에 실패하였습니다.", 500),
    MONGO_QUERY_EXECUTION_ERROR("쿼리가 정상적으로 완료되지 않았습니다.", 500),
    PATHPOINT_EXECUTION_ERROR("경로 저장중 오류가 발생했습니다.", 500),

    CART_ITEM_INSERT_ERROR("장바구니 담기에 실패하였습니다.", 500),
    PAYMENT_ERROR("결제에 실패하였습니다.", 500),
    REFUND_ERROR("환불에 실패하였습니다.", 500),
    VENDOR_DELETE_ERROR("해당 판매업체로 등록된 상품을 먼저 삭제해주십시오.", 500),
    CATEGORY_DELETE_ERROR("해당 카테고리로 등록된 상품을 먼저 삭제해주십시오.", 500),

    /* Spring Basic Exceptions */
    INTERNAL_SERVER_ERROR("서버 오류가 발생했습니다.", 500);

    private final String message;
    private final int status;

}
package com.findingjob.common.exception;

public enum ErrorCode {

    OK(200, "成功"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    BAD_REQUEST(400, "请求参数错误"),
    CONFLICT(409, "资源冲突"),

    // Business errors
    INVALID_TOKEN(1001, "无效的 Token"),
    TOKEN_EXPIRED(1002, "Token 已过期"),
    ROLE_MISMATCH(1003, "角色不匹配"),
    USER_NOT_FOUND(1004, "用户不存在"),
    DUPLICATE_ENTRY(1005, "重复记录"),
    FILE_UPLOAD_FAILED(1006, "文件上传失败"),
    FILE_TYPE_NOT_ALLOWED(1007, "文件类型不支持"),
    FILE_SIZE_EXCEEDED(1008, "文件大小超出限制"),
    RESUME_REQUEST_NOT_ALLOWED(1009, "无权进行此操作"),
    RATE_LIMIT_EXCEEDED(1010, "操作频率过高"),
    ACCOUNT_PENDING_DELETION(1011, "账号注销中");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

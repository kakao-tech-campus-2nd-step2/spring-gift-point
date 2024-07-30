package gift.global.config;

public record KakaoUrl(
    String requestFormat,
    String redirect,
    String token,
    String user,
    String defaultTemplateMsgMe
) {}

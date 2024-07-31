package gift.core.api.kakao.dto;

public record KakaoUserInfo(
	long id,
	String connected_at,
	Properties properties,
	KakaoAccount kakao_account
) {
	public record Properties(
		String nickname
	) {
	}
	public record KakaoAccount(
		boolean profile_nickname_needs_agreement,
		Profile profile,
		boolean has_email,
		boolean email_needs_agreement,
		boolean is_email_valid,
		boolean is_email_verified,
		String email
	) {
		public record Profile(
			String nickname,
			boolean is_default_nickname
		) {
		}
	}
}

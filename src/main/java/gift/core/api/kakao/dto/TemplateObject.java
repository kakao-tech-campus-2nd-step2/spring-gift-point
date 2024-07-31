package gift.core.api.kakao.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

public record TemplateObject(
	String object_type,
	String text,
	Link link,
	String button_title
) {
	public record Link(
		String web_url,
		String mobile_web_url
	) {
	}
	public static TemplateObject of(String text) {
		return new TemplateObject(
			"text", text, new Link("", ""), "바로 확인"
		);
	}

	public String toUrlEncoded() {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(this);
			return "template_object=" + java.net.URLEncoder.encode(json, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException("Failed to convert to URL encoded string", e);
		}
	}
}


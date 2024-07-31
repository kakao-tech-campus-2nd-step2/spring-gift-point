package gift.dto;



public enum SearchType {
	NAME("이름"),
	CATEGORY("카테고리");

	private final String description;

	SearchType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
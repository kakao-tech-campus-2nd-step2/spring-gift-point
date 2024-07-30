package gift.order;

import com.google.gson.annotations.SerializedName;

public record Link(
    @SerializedName("web_url")
    String webUrl
) { }

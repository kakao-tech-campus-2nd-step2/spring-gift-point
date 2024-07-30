package gift.order;

import com.google.gson.annotations.SerializedName;

public record MessageTemplate(
    @SerializedName("object_type")
    String objectType,
    String text,
    Link link
) { }

package com.ayan.redditclone.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListItem {
    public String subreddit_name_prefixed;
    public String name;
    public String title;
    public Long created;
    public Integer gilded;
    public Gildings gildings;
    public Integer ups;
    public Integer downs;
    public boolean is_self;
    public Integer num_comments;
    public String author;
    public String permalink;
    public String icon_img;
    public String domain;
    public String thumbnail;
    public String display_name_prefixed;
    public String header_img;
    public Integer score;
    public String body;
    public Integer depth;
    public Integer count;
    public String id;
    public ArrayList<String> children;

    @SerializedName("replies")
    @JsonAdapter(EmptyStringAsNullTypeAdapter.class)
    public ResponseModel replies;
}

final class EmptyStringAsNullTypeAdapter<T>
        implements JsonDeserializer<T> {

    // Let Gson instantiate it itself
    private EmptyStringAsNullTypeAdapter() {
    }

    @Override
    public T deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext context)
            throws JsonParseException {
        if (jsonElement.isJsonPrimitive()) {
            final JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
            if (jsonPrimitive.isString() && jsonPrimitive.getAsString().isEmpty()) {
                return null;
            }
        }
        return context.deserialize(jsonElement, type);
    }

}

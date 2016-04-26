package net.alfdev.demoissuelogansquare.responses;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alfdev on 13/04/16.
 */
@JsonObject
public class BaseListResponse<T> {

    @SerializedName("count")
    @JsonField(name = "count")
    public int Count;

    @SerializedName("previous")
    @JsonField(name = "previous")
    public String Previous;

    @SerializedName("next")
    @JsonField(name = "next")
    public String Next;

    @SerializedName("results")
    @JsonField(name = "results")
    public List<T> Items;
}

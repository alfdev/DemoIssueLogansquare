package net.alfdev.demoissuelogansquare.model;

import android.support.v4.util.SimpleArrayMap;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
//import com.bluelinelabs.logansquare.util.SimpleArrayMap;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alfdev on 21/04/16.
 */
@JsonObject
public class ValidationErrorsModel {

    @SerializedName("validation_errors")
    @JsonField(name = "validation_errors")
    public SimpleArrayMap<String, List<String>> validationErrors;
}

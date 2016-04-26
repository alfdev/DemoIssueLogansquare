package net.alfdev.demoissuelogansquare.model;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
@JsonObject
public class UserCreateModel extends ValidationErrorsModel {
  
    @SerializedName("email")
    @JsonField(name = "email")
    public String email = null;

    @SerializedName("password")
    @JsonField(name = "password")
    public String password = null;

    @SerializedName("fullname")
    @JsonField(name = "fullname")
    public String fullname = null;

    @SerializedName("base64image")
    @JsonField(name = "base64image")
    public String base64image = null;

    @SerializedName("facebook")
    @JsonField(name = "facebook")
    public boolean facebook;

    @SerializedName("twitter")
    @JsonField(name = "twitter")
    public boolean twitter;

}

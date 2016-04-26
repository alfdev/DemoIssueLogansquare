package net.alfdev.demoissuelogansquare.api;

import net.alfdev.demoissuelogansquare.model.UserCreateModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface UserApi {

  @POST("api/user/create")
  Call<UserCreateModel> create(
    @Body UserCreateModel model
  );


}

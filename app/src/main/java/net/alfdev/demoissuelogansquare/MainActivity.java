package net.alfdev.demoissuelogansquare;

import android.support.v7.app.AppCompatActivity;

import net.alfdev.demoissuelogansquare.api.UserApi;
import net.alfdev.demoissuelogansquare.model.UserCreateModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private UserApi api;

    @AfterViews
    protected void setup() {
        api = ApiClient.getInstance().createService(UserApi.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        signup("test@gmail.com", "1234567qqq", "test test", false, false);
    }

    @Background
    protected void signup(String username, String password, String fullname, boolean isFacebook, boolean isTwitter) {
        UserCreateModel model = new UserCreateModel();
        model.email = username;
        model.fullname = fullname;
        model.password = password;
        model.facebook = isFacebook;
        model.twitter = isTwitter;

        Call<UserCreateModel> call = api.create(model);

        call.enqueue(new Callback<UserCreateModel>() {
            @Override
            public void onResponse(Call<UserCreateModel> call, Response<UserCreateModel> response) {
                if (response.code() == 201) {
                    setResult(RESULT_OK);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<UserCreateModel> call, Throwable t) {
                int x = 0;
                t.printStackTrace();
            }
        });
    }
}

package net.alfdev.demoissuelogansquare;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by alfdev on 18/04/16.
 */
public abstract class AuthCallback<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.code() == 401) {
            onUnauthorized(response);

            return;
        }

        onSuccess(call, response);
    }

    public abstract void onSuccess(Call<T> cal, Response<T> response);

    @Override
    public abstract void onFailure(Call<T> call, Throwable t);

    public abstract void onUnauthorized(Response<T> response);
}

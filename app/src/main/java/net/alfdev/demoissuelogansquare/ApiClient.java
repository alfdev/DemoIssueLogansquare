package net.alfdev.demoissuelogansquare;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest.AuthenticationRequestBuilder;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.TokenRequestBuilder;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import net.alfdev.demoissuelogansquare.auth.ApiKeyAuth;
import net.alfdev.demoissuelogansquare.auth.HttpBasicAuth;
import net.alfdev.demoissuelogansquare.auth.OAuth;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;



public class ApiClient {
    private static ApiClient instance = null;

    private Map<String, Interceptor> apiAuthorizations;
    private OkHttpClient okClient;
    private Retrofit.Builder adapterBuilder;
    private Retrofit retrofit;

    public Retrofit getRetrofit() {
        return retrofit;
    }

    private ApiClient() {
        createDefaultAdapter();
    }

    /**
     * Basic constructor for single auth name
     * @param authName
     *//*
    private ApiClient(String authName) {
        this(new String[]{authName});
    }

    *//**
     * Helper constructor for single api key
     * @param authName
     * @param apiKey
     *//*
    private ApiClient(String authName, String apiKey) {
        this(authName);
        this.setApiKey(apiKey);
    }

    *//**
     * Helper constructor for single basic auth or password oauth2
     * @param authName
     * @param username
     * @param password
     *//*
    private ApiClient(String authName, String username, String password) {
        this(authName);
        this.setCredentials(username,  password);
    }

    *//**
     * Helper constructor for single password oauth2
     * @param authName
     * @param clientId
     * @param secret
     * @param username
     * @param password
     *//*
    private ApiClient(String authName, String clientId, String secret, String username, String password) {
        this(authName);
        this.getTokenEndPoint()
                .setClientId(clientId)
                .setClientSecret(secret)
                .setUsername(username)
                .setPassword(password);
    }*/

    public static ApiClient getInstance() {
        if (instance == null)
            instance = new ApiClient();

        return instance;
    }

   private void createDefaultAdapter() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

       OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //okClient = new OkHttpClient();

       HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
       logging.setLevel(HttpLoggingInterceptor.Level.BODY);
       builder.addInterceptor(logging);


        //String baseUrl = "http://52.58.47.111/";
       String baseUrl = "http://10.0.3.2:8000/";

        adapterBuilder = new Retrofit
                .Builder()
                .baseUrl(baseUrl)
                //.client(okClient)
                .client(builder.build())
                .addConverterFactory(LoganSquareConverterFactory.create())
                .addConverterFactory(GsonCustomConverterFactory.create(gson));

       retrofit = adapterBuilder.build();
    }

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    /**
     * Helper method to configure the first api key found
     * @param apiKey
     */
    private void setApiKey(String apiKey) {
        for(Interceptor apiAuthorization : apiAuthorizations.values()) {
            if (apiAuthorization instanceof ApiKeyAuth) {
                ApiKeyAuth keyAuth = (ApiKeyAuth) apiAuthorization;
                keyAuth.setApiKey(apiKey);
                return;
            }
        }
    }

    /**
     * Helper method to configure the username/password for basic auth or password oauth
     * @param username
     * @param password
     */
    private void setCredentials(String username, String password) {
        for(Interceptor apiAuthorization : apiAuthorizations.values()) {
            if (apiAuthorization instanceof HttpBasicAuth) {
                HttpBasicAuth basicAuth = (HttpBasicAuth) apiAuthorization;
                basicAuth.setCredentials(username, password);
                return;
            }
            if (apiAuthorization instanceof OAuth) {
                OAuth oauth = (OAuth) apiAuthorization;
                oauth.getTokenRequestBuilder().setUsername(username).setPassword(password);
                return;
            }
        }
    }

    /**
     * Helper method to configure the token endpoint of the first oauth found in the apiAuthorizations (there should be only one)
     * @return
     */
    public TokenRequestBuilder getTokenEndPoint() {
        for(Interceptor apiAuthorization : apiAuthorizations.values()) {
            if (apiAuthorization instanceof OAuth) {
                OAuth oauth = (OAuth) apiAuthorization;
                return oauth.getTokenRequestBuilder();
            }
        }
        return null;
    }

    /**
     * Helper method to configure authorization endpoint of the first oauth found in the apiAuthorizations (there should be only one)
     * @return
     */
    public AuthenticationRequestBuilder getAuthorizationEndPoint() {
        for(Interceptor apiAuthorization : apiAuthorizations.values()) {
            if (apiAuthorization instanceof OAuth) {
                OAuth oauth = (OAuth) apiAuthorization;
                return oauth.getAuthenticationRequestBuilder();
            }
        }
        return null;
    }

    /**
     * Helper method to pre-set the oauth access token of the first oauth found in the apiAuthorizations (there should be only one)
     * @param accessToken
     */
    public void setAccessToken(String accessToken) {
        for(Interceptor apiAuthorization : apiAuthorizations.values()) {
            if (apiAuthorization instanceof OAuth) {
                OAuth oauth = (OAuth) apiAuthorization;
                oauth.setAccessToken(accessToken);
                return;
            }
        }
    }

    /**
     * Helper method to configure the oauth accessCode/implicit flow parameters
     * @param clientId
     * @param clientSecret
     * @param redirectURI
     */
    public void configureAuthorizationFlow(String clientId, String clientSecret, String redirectURI) {
        for(Interceptor apiAuthorization : apiAuthorizations.values()) {
            if (apiAuthorization instanceof OAuth) {
                OAuth oauth = (OAuth) apiAuthorization;
                oauth.getTokenRequestBuilder()
                        .setClientId(clientId)
                        .setClientSecret(clientSecret)
                        .setRedirectURI(redirectURI);
                oauth.getAuthenticationRequestBuilder()
                        .setClientId(clientId)
                        .setRedirectURI(redirectURI);
                return;
            }
        }
    }

    /**
     * Configures a listener which is notified when a new access token is received.
     * @param accessTokenListener
     */
    public void registerAccessTokenListener(OAuth.AccessTokenListener accessTokenListener) {
        for(Interceptor apiAuthorization : apiAuthorizations.values()) {
            if (apiAuthorization instanceof OAuth) {
                OAuth oauth = (OAuth) apiAuthorization;
                oauth.registerAccessTokenListener(accessTokenListener);
                return;
            }
        }
    }

    /**
     * Adds an authorization to be used by the client
     * @param authName
     * @param authorization
     */
    public void addAuthorization(String authName, Interceptor authorization) {
        if (apiAuthorizations.containsKey(authName)) {
            throw new RuntimeException("auth name \"" + authName + "\" already in api authorizations");
        }
        apiAuthorizations.put(authName, authorization);
        okClient.interceptors().add(authorization);
    }

    public Map<String, Interceptor> getApiAuthorizations() {
        return apiAuthorizations;
    }

    public void setApiAuthorizations(Map<String, Interceptor> apiAuthorizations) {
        this.apiAuthorizations = apiAuthorizations;
    }

    public Retrofit.Builder getAdapterBuilder() {
        return adapterBuilder;
    }

    public void setAdapterBuilder(Retrofit.Builder adapterBuilder) {
        this.adapterBuilder = adapterBuilder;
    }

    public OkHttpClient getOkClient() {
        return okClient;
    }

    public void addAuthsToOkClient(OkHttpClient okClient) {
        for(Interceptor apiAuthorization : apiAuthorizations.values()) {
            okClient.interceptors().add(apiAuthorization);
        }
    }

    /**
     * Clones the okClient given in parameter, adds the auth interceptors and uses it to configure the Retrofit
     * @param okClient
     */
    public void configureFromOkclient(OkHttpClient okClient) {
        OkHttpClient clone = okClient.newBuilder().build();
        addAuthsToOkClient(clone);
        adapterBuilder.client(clone);
    }
}

/**
 * This wrapper is to take care of this case:
 * when the deserialization fails due to JsonParseException and the
 * expected type is String, then just return the body string.
 */
class GsonResponseBodyConverterToString<T> implements Converter<ResponseBody, T> {
	  private final Gson gson;
	  private final Type type;

	  GsonResponseBodyConverterToString(Gson gson, Type type) {
	    this.gson = gson;
	    this.type = type;
	  }

	  @Override public T convert(ResponseBody value) throws IOException {
	    String returned = value.string();
	    try {
	      return gson.fromJson(returned, type);
	    }
	    catch (JsonParseException e) {
                return (T) returned;
        }
	 }
}

class GsonCustomConverterFactory extends Converter.Factory
{
	public static GsonCustomConverterFactory create(Gson gson) {
	    return new GsonCustomConverterFactory(gson);
	  }

	  private final Gson gson;
	  private final GsonConverterFactory gsonConverterFactory;

	  private GsonCustomConverterFactory(Gson gson) {
	    if (gson == null) throw new NullPointerException("gson == null");
	    this.gson = gson;
	    this.gsonConverterFactory = GsonConverterFactory.create(gson);
	  }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if(type.equals(String.class))
            return new GsonResponseBodyConverterToString<Object>(gson, type);
        else
            return gsonConverterFactory.responseBodyConverter(type, annotations, retrofit);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            return gsonConverterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }
}


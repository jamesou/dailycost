package com.jamesou.dailycost.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
//    <string name="recognise_service">http://127.0.0.1:8090/</string>
//    <string name="web_login_url">http://192.168.68.67:8080/</string>
//<!--    <string name="web_login_url">http://148.100.78.14:8080/</string>-->
    private static final String BASE_URL =  "http://192.168.68.67:8090";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS) // 设置连接超时时间
                    .readTimeout(30, TimeUnit.SECONDS) // 设置读取超时时间
                    .writeTimeout(30, TimeUnit.SECONDS) // 设置写入超时时间
                    .addInterceptor(interceptor).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

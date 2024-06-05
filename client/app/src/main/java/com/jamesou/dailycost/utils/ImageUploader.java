package com.jamesou.dailycost.utils;

import android.os.AsyncTask;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ImageUploader {

    private static final String SERVER_URL =  "http://110.41.187.73:8090/ocr"; //huawei elastic cloud server
//    private static final String SERVER_URL =  "http://192.168.68.55:8090/ocr";
    public interface OnImageUploadListener {
        void onSuccess(String response);
        void onError(String error);
    }

    public static void uploadImage(File fileParam, OnImageUploadListener listener) {
        new SendImageTask(fileParam, listener).execute();
    }

    private static class SendImageTask extends AsyncTask<Void, Void, String> {
        private final File fileParam;
        private final OnImageUploadListener listener;
        private OkHttpClient client;
        public SendImageTask(File fileParam, OnImageUploadListener listener) {
            this.fileParam = fileParam;
            this.listener = listener;
            client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.MINUTES)
                    .writeTimeout(30, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.MINUTES).build();
        }
        @Override
        protected String doInBackground(Void... params) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), fileParam);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", fileParam.getName(), fileBody)
                    .build();
            Request request = new Request.Builder()
                    .url(SERVER_URL)
                    .post(requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return "Error: " + response.code();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }
        @Override
        protected void onPostExecute(String result) {
            if (listener != null) {
                if (result.startsWith("Error")) {
                    listener.onError(result);
                } else {
                    listener.onSuccess(result);
                }
            }
            // 请求完成后关闭 OkHttpClient 实例
            client.dispatcher().executorService().shutdown();
        }
    }
}

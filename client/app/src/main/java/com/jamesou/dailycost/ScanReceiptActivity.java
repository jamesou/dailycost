package com.jamesou.dailycost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jamesou.dailycost.adapter.ReceiptAdapter;
import com.jamesou.dailycost.db.AccountBean;
import com.jamesou.dailycost.db.DBManager;
import com.jamesou.dailycost.db.ReceiptBean;
import com.jamesou.dailycost.network.ApiService;
import com.jamesou.dailycost.network.RetrofitClient;
import com.jamesou.dailycost.utils.DatetimeUtil;
import com.jamesou.dailycost.utils.PromptMsgUtil;
import com.jamesou.dailycost.utils.TakePhotoHelper;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import me.iwf.photopicker.PhotoPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ScanReceiptActivity extends TakePhotoActivity {

    private ImageView iv_show_image;
    private ListView resultLv;

    private ApiService apiService;
    private LottieAnimationView progressBar;

    private ReceiptAdapter receiptAdapter;
    private List<ReceiptBean> dataList;

    private TakePhotoHelper takePhotoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_receipt);
        iv_show_image = (ImageView) findViewById(R.id.iv_show_image);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setAnimation(R.raw.scanning);

        resultLv = findViewById(R.id.display_result_lv);
        dataList = new ArrayList<ReceiptBean>();
        receiptAdapter = new ReceiptAdapter(this,dataList);
        resultLv.setAdapter(receiptAdapter);


        apiService = RetrofitClient.getClient().create(ApiService.class);
        //Save to DB
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataList!=null&&dataList.size()>0){
                    saveToDB(dataList);
                    PromptMsgUtil.promptMsg(view.getContext(),"Save successfully");
                    finish();
                }else{
                    PromptMsgUtil.promptMsg(view.getContext(),"Receipt list is empty, please select an image to recognise");
                }
            }
        });

        takePhotoHelper = new TakePhotoHelper();

        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoHelper.onClick(TakePhotoHelper.ACTION_TAKE_PHOTO,getTakePhoto());
            }
        });
        findViewById(R.id.btn_select_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotoHelper.onClick(TakePhotoHelper.ACTION_PICK_PHOTO,getTakePhoto());
            }
        });
    }

    private void saveToDB(List<ReceiptBean> dataList){
        float totalAmount = 0;
        for(ReceiptBean receiptBean:dataList){
            System.out.println("receiptBean-->"+receiptBean.toString());
            if(receiptBean.getItem_amount()!=null
                    &&!receiptBean.getItem_amount().trim().equals("")
                    &&receiptBean.getItem_qty()!=null
                    &&!receiptBean.getItem_qty().trim().equals("")
            ){
                String[] amountArray = receiptBean.getItem_amount().split("\\$");
                int qty = Integer.parseInt(receiptBean.getItem_qty());
                System.out.println("amountArray.length-->"+amountArray.length);
                if(amountArray.length>=2){
                    float amount = Float.parseFloat(amountArray[1]);
                    if(amountArray[0].equals("-")){//negative
                        totalAmount = totalAmount - amount;
                    } else{//positive
                        totalAmount = totalAmount + amount;
                    }
                }else{
                    Log.i("saveToDB"," amount["+receiptBean.getItem_amount()+"] format is not correct");
                }
            }else{
                Log.i("saveToDB",receiptBean.getItem_name()+", amount is null, skip it");
            }
        }
        AccountBean accountBean = new AccountBean();
        accountBean.setKind(0);
        accountBean.setCategoryName("Shopping");
        accountBean.setsImageId(R.mipmap.ic_shopping_fs);
        accountBean.setComment("Pak Save");
        accountBean.setMoney(totalAmount);
        accountBean.setTime(DatetimeUtil.getCurrentDateTime());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
        DBManager.insertItemToAccounttb(accountBean);
    }
    public void onClick(View v){
        finish();
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //show image and recognised result
//        if (resultCode == RESULT_OK && requestCode == PhotoPicker.CROP_CODE) {
////        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
//            iv_show_image.setVisibility(View.VISIBLE);
//            Uri imageUri = Uri.fromFile(new File(data.getStringExtra(PhotoPicker.KEY_CAMEAR_PATH)));
////            System.out.println("imageUri:"+imageUri);
//            Glide.with(getApplicationContext()).load(imageUri).into(iv_show_image);
//            uploadImage(imageUri);
//        }
//    }
@Override
public void takeCancel() {
    super.takeCancel();
    PromptMsgUtil.promptMsg(this,"Cancel");
}

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        PromptMsgUtil.promptMsg(this,"Get the image failed");
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        iv_show_image.setVisibility(View.VISIBLE);
//        System.out.println(result.getImage());
//        System.out.println(result.getImages());
//        System.out.println(result.getImage().getCompressPath());
        if(result.getImage().getCompressPath()!=null) {
            Uri imageUri = Uri.fromFile(new File(result.getImage().getCompressPath()));
            System.out.println(imageUri);
            Glide.with(getApplicationContext()).load(imageUri).into(iv_show_image);
            uploadImage(imageUri);
        }else{
            PromptMsgUtil.promptMsg(this,"Can not get image from storage");
        }
    }
    private void uploadImage(Uri imageUri) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.playAnimation();
        progressBar.loop(true); // 设置重复播放次数
        Context context = this;
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            File file = new File(getCacheDir(), "image.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            Call<ResponseBody> call = apiService.recogniseImage(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            String jsonResponse = response.body().string();
//                            System.out.println("jsonResponse:"+jsonResponse);
                            updateListView(jsonResponse);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("Upload", "Upload failed: " + response.message());
                        PromptMsgUtil.promptMsg(context,"Upload failed: "+response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("Upload", "Upload error: ", t);
                    PromptMsgUtil.promptMsg(context,"Upload error: "+t.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //@todo filter some unavailable data
    private void updateListView(String jsonResponse) {
        JsonElement jsonElement = new JsonParser().parse(jsonResponse);
        resultLv.setVisibility(View.VISIBLE);
        if (jsonElement.isJsonObject()) {
            JsonArray jsonArray = jsonElement.getAsJsonObject().get("reg_res").getAsJsonArray();
            dataList.clear();
            for (JsonElement element : jsonArray) {
                ReceiptBean receiptBean = new ReceiptBean();
                JsonObject jsonObj = element.getAsJsonObject();
//                System.out.println(jsonObj.get("item_name"));
//                System.out.println(jsonObj.get("item_name")!=null);
                receiptBean.setItem_name(jsonObj.get("item_name")!=null?jsonObj.get("item_name").getAsString():"");
                receiptBean.setItem_qty(jsonObj.get("item_qty")!=null?jsonObj.get("item_qty").getAsString():"");
                receiptBean.setItem_amount_unit(jsonObj.get("item_amount_unit")!=null?jsonObj.get("item_amount_unit").getAsString():"");
                receiptBean.setItem_amount(jsonObj.get("item_amount")!=null?jsonObj.get("item_amount").getAsString():"");
                dataList.add(receiptBean);
            }
//            System.out.println("dataList:"+dataList);
        }else{
            PromptMsgUtil.promptMsg(this,"Ocr result incorrect! please try again!");
        }
        receiptAdapter.notifyDataSetChanged();
    }
}
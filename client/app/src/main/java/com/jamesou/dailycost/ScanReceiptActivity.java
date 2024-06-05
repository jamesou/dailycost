package com.jamesou.dailycost;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

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
import com.jamesou.dailycost.utils.ImageUploader;
import com.jamesou.dailycost.utils.DatetimeUtil;
import com.jamesou.dailycost.utils.PromptMsgUtil;
import com.jamesou.dailycost.utils.TakePhotoHelper;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class ScanReceiptActivity extends TakePhotoActivity {

    private ImageView iv_show_image;
    private ListView resultLv;

//    private ApiService apiService;
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
        System.out.println("dataList-->"+dataList);
        int dataSize = dataList.size();
        int count = 1;
        for(ReceiptBean receiptBean:dataList){
            if(count==dataSize){ //don't calculate last row
                break;
            }
            if(receiptBean.getItem_amount()!=null){
                String[] amountArray = receiptBean.getItem_amount().split("\\$");
//                System.out.println("amountArray.length-->"+amountArray.length);
//                for(String amount:amountArray)
//                    System.out.println("amount-->"+amount);
                if(amountArray.length>=2){
                    float amount = 0;
                    if(amountArray[1].indexOf("EA")!=-1){
                        amount = Float.parseFloat(amountArray[1].split("EA")[0]);
                    }else{
                        amount = Float.parseFloat(amountArray[1]);
                    }
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
            count+=1;
        }
        System.out.println("calculate total money:"+totalAmount);
        if(dataSize>0){
            ReceiptBean receiptBean = dataList.get(dataSize-1);
            if(receiptBean.getItem_amount()!=null){
                String[] amountArray = receiptBean.getItem_amount().split("\\$");
                totalAmount =  Float.parseFloat(amountArray[1]);
                System.out.println("get from image total money:"+totalAmount);
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

            ImageUploader.uploadImage( file, new ImageUploader.OnImageUploadListener() {
                @Override
                public void onSuccess(String response) {
                    progressBar.setVisibility(View.GONE);
                    System.out.println("response:"+response);
                    updateListView(response);
                }

                @Override
                public void onError(String error) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("Upload", "Upload error: "+error);
                    PromptMsgUtil.promptMsg(context,"Upload error: "+error);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
            System.out.println("dataList:"+dataList);
        }else{
            PromptMsgUtil.promptMsg(this,"Ocr result incorrect! please try again!");
        }
        receiptAdapter.notifyDataSetChanged();
    }
}
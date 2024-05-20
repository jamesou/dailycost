package com.jamesou.dailycost.utils;


import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;

/**
 * - 支持通过相机拍照获取图片
 * - 支持从相册选择图片
 * - 支持从文件选择图片
 * - 支持多图选择
 * - 支持批量图片裁切
 * - 支持批量图片压缩
 * - 支持对图片进行压缩
 * - 支持对图片进行裁剪
 * - 支持对裁剪及压缩参数自定义
 * - 提供自带裁剪工具(可选)
 * - 支持智能选取及裁剪异常处理
 * - 支持因拍照Activity被回收后的自动恢复
 */
public class TakePhotoHelper {
    public static final String ACTION_TAKE_PHOTO="ACTION_TAKE_PHOTO";
    public static final String ACTION_PICK_PHOTO="ACTION_PICK_PHOTO";


    public  void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        takePhoto.setTakePhotoOptions(builder.create());
    }

    public  void configCompress(TakePhoto takePhoto) {
//        takePhoto.onEnableCompress(null, false);
//compress configuration
        int maxSize = 102400;
        int width = 800;
        int height = 800;
        boolean showProgressBar = true;
        boolean enableRawFile = true ;
        CompressConfig config  = new CompressConfig.Builder().setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(enableRawFile)
                .create();
        takePhoto.onEnableCompress(config, showProgressBar);
    }

    public CropOptions getCropOptions() {
        int height = 800;
        int width = 800;
        boolean withSystemCrop = true ;
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setOutputX(width).setOutputY(height);
        builder.setWithOwnCrop(withSystemCrop);
        return builder.create();
    }

    public void onClick(String action,TakePhoto takePhoto){
        File file = new File(Environment.getExternalStorageDirectory(), "/dailycost_temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);
        System.out.println(imageUri);
        configTakePhotoOption(takePhoto);
        configCompress(takePhoto);
        if(action.equals(ACTION_TAKE_PHOTO)){
            takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
        } else if (action.equals(ACTION_PICK_PHOTO)) {
            takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
        }else{
            Log.e("TakePhotoHelper","incorrect action in onClick method");
        }
    }
}

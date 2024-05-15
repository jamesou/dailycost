package com.jamesou.dailycost;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.jamesou.dailycost.utils.PhotoPicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScanReceipt extends Activity {


    private ArrayList<String> selectedPhotos = new ArrayList<>();

    private ImageView iv_show_image;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_receipt);
        iv_show_image = (ImageView) findViewById(R.id.iv_show_image);
        //todo get the recognised result
        recyclerView = (RecyclerView) findViewById(R.id.rv_display_result);
        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPicker.builder()
                        .setOpenCamera(true)
                        .setCrop(true)
                        .start(ScanReceipt.this);
            }
        });



        findViewById(R.id.btn_select_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setPreviewEnabled(false)
                        .setCrop(true)
                        .setCropXY(1, 1)
                        .setCropColors(R.color.colorPrimary, R.color.colorPrimaryDark)
                        .start(ScanReceipt.this);
            }
        });



//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
//                new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        if (photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
//                            PhotoPicker.builder()
//                                    .setPhotoCount(PhotoAdapter.MAX)
//                                    .setShowCamera(true)
//                                    .setPreviewEnabled(true)
//                                    .setSelected(selectedPhotos)
//                                    .start(MainActivity.this);
//                        } else {
//                            PhotoPreview.builder()
//                                    .setPhotos(selectedPhotos)
//                                    .setCurrentItem(position)
//                                    .start(MainActivity.this);
//                        }
//                    }
//                }));
    }
    //back
    public void onClick(View v){
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE)) {
            iv_show_image.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }

        }

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.CROP_CODE) {
            iv_show_image.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).load(Uri.fromFile(new File(data.getStringExtra(PhotoPicker.KEY_CAMEAR_PATH)))).into(iv_show_image);
        }
    }

}
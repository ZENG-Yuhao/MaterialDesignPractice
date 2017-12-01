package com.esigelec.zengyuhao.materialdesignpractice.CameraPhoto;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.esigelec.zengyuhao.materialdesignpractice.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends Activity {
    private static final String TAG = "CameraActivity";
    private static final int CAPTURE_PHOTO_REQUEST_CODE = 1153;
    private Button btn_take_photo;
    private ImageView img_photo;
    private Uri mPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btn_take_photo = (Button) findViewById(R.id.btn_take_photo);
        btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        img_photo = (ImageView) findViewById(R.id.img_photo);
    }

    private void takePhoto() {
        File photoFile = new File(getPhotosDir(), generateUniqueFileName());
        Uri photoUri = Uri.fromFile(photoFile);
        mPhotoUri = photoUri;

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(captureIntent, CAPTURE_PHOTO_REQUEST_CODE);
    }

    private File getPhotosDir() {
        File photosDir = new File(Environment.getExternalStorageDirectory() + "/MaterialDesignDemo/");
        if (!photosDir.exists())
            photosDir.mkdirs();
        return photosDir;
    }

    private String generateUniqueFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date(System.currentTimeMillis());
        return "Photo-" + format.format(date) + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
            Log.i(TAG, "Result OK.");
        else if (resultCode == RESULT_CANCELED)
            Log.i(TAG, "Result Canceled.");

        if (resultCode == RESULT_OK && requestCode == CAPTURE_PHOTO_REQUEST_CODE) {
            //Uri photoUri = getIntent().getParcelableExtra(MediaStore.EXTRA_OUTPUT);
            img_photo.setImageURI(mPhotoUri);
        }
    }
}

package com.example.photosend;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.FileProvider;

import com.example.photosend.databinding.ActivityStage1Binding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

public class Stage1Activity extends AppCompatActivity {
    private ActivityStage1Binding binding;
    private View view;
    private Intent intent;
    Bundle bundle;
    Bitmap bitmap;
    Uri galleryUri, cameraUri;

    /*@Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("viewImage", bitmap);
        super.onSaveInstanceState(outState);
    }*/

    private final ActivityResultLauncher resultPhotLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                galleryUri = loadImage(result.getData());
                binding.btnGallery.setText(R.string.gotoStage2);

                binding.btnGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(getApplicationContext(), Stage2Activity.class);
                        intent.putExtra("uriImage", galleryUri.toString());
                        startActivity(intent);
                    }
                });
            }

        }
    });


    private final ActivityResultLauncher resultCameraLauncher  = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                bitmap = (Bitmap) result.getData().getExtras().get("data");
                binding.viewPhotoS1.setImageBitmap(bitmap);

                binding.btnGallery.setText(R.string.gotoStage2);
                cameraUri = getImageUri(getApplicationContext(), bitmap);

                binding.btnGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(getApplicationContext(), Stage2Activity.class);
                        // intent.putExtra("viewImage", bitmap);
                        // intent.putExtra("uriImage", cameraUri);
                        intent.putExtra("uriImage", cameraUri.toString());
                        startActivity(intent);
                    }
                });


            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStage1Binding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        intent = getIntent();

        binding.btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerLoadPicture(view);
            }
        });

        binding.viewPhotoS1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerOpenCamera(view);
            }
        });

    }

    private void listenerLoadPicture(View view){
        Intent loadPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        loadPictureIntent.setType("image/*");
        resultPhotLauncher.launch(loadPictureIntent);
    }

    private void listenerOpenCamera(View view){
        Intent openCameraIntent;
        openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (openCameraIntent.resolveActivity(getPackageManager()) != null) {
            resultCameraLauncher.launch(openCameraIntent);
        }
    }

    private Uri loadImage(Intent data){
        Uri uri;
        InputStream Stream;

        try {
            uri = data.getData();
            Stream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(Stream);
            binding.viewPhotoS1.setImageBitmap(bitmap);
            return uri;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Uri getImageUri(Context inContext, Bitmap inImage){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, imageFileName,null);

        return  Uri.parse(path);
    }

}

package com.example.photosend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.photosend.databinding.ActivityStage1Binding;
import com.example.photosend.databinding.ActivityStage3Binding;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Stage3Activity extends AppCompatActivity {
    private ActivityStage3Binding binding;
    private View view;
    private Intent intent;
    private String uriString;
    private InputStream Stream;
    private Uri uri;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       binding = ActivityStage3Binding.inflate(getLayoutInflater());
       view = binding.getRoot();
       setContentView(view);
       intent = getIntent();

       Bitmap bitmap = (Bitmap) intent.getParcelableExtra("viewImage");
       binding.viewPhotoS3.setImageBitmap(bitmap);

       uriString = intent.getStringExtra("uriImage");
       uri = Uri.parse(intent.getStringExtra("uriImage"));

       try {
           Stream = getContentResolver().openInputStream(uri);
           bitmap = BitmapFactory.decodeStream(Stream);
           binding.viewPhotoS3.setImageBitmap(bitmap);
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }

    }
}

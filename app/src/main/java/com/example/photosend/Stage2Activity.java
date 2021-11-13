package com.example.photosend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.photosend.databinding.ActivityStage1Binding;
import com.example.photosend.databinding.ActivityStage2Binding;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Stage2Activity extends AppCompatActivity {
    private ActivityStage2Binding binding;
    private View view;
    private Intent intent;
    private Uri uri;
    private InputStream Stream;
    private String uriString;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStage2Binding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        intent = getIntent();

        // bitmap = (Bitmap) intent.getParcelableExtra("viewImage");
        // binding.viewPhotoS2.setImageBitmap(bitmap);

        uriString = intent.getStringExtra("uriImage");
        uri = Uri.parse(intent.getStringExtra("uriImage"));

        try {
            Stream = getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(Stream);
            binding.viewPhotoS2.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /* byteArray = intent.getByteArrayExtra("bitmap");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        binding.viewPhotoS2.setImageBitmap(bitmap); */

        binding.btnToStage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), Stage3Activity.class);
                // intent.putExtra("viewImage",bitmap);
                intent.putExtra("uriImage", uriString);
                startActivity(intent);
            }
        });

    }
}

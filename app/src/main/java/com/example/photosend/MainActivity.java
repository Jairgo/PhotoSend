package com.example.photosend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.photosend.databinding.ActivityMainBinding;
import com.example.photosend.databinding.ActivityStage1Binding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private View view;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), Stage1Activity.class);
                startActivity(intent);
            }
        });

    }
}
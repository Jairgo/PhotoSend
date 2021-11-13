package com.example.photosend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photosend.databinding.ActivityMainBinding;
import com.example.photosend.databinding.ActivityStage1Binding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private View view;
    private Intent intent;
    private String emailSent;
    private Toast message;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        intent = getIntent();

        if(intent != null){
            emailSent = intent.getStringExtra("emailSent");
            if(emailSent != null){
                message = Toast.makeText(this,"The email was sent successfully", Toast.LENGTH_LONG);
                message.getView().setBackgroundColor(Color.rgb(215, 234, 217));

                if(emailSent.equals("true"))
                    message.show();
            }
        }



        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), Stage1Activity.class);
                startActivity(intent);
            }
        });

    }
}
package com.example.photosend;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.photosend.databinding.ActivityStage1Binding;
import com.example.photosend.databinding.ActivityStage3Binding;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Stage3Activity extends AppCompatActivity {
    private ActivityStage3Binding binding;
    private View view;
    private Intent intent;
    private String uriString, name, email, message;
    private InputStream Stream;
    private Uri uri;

    private final ActivityResultLauncher resultMailLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK){
                binding.btnSendMail.setText("Email sent");
                Intent endIntent = new Intent(getApplicationContext(), MainActivity.class);

                endIntent.putExtra("emailSent", "true");
                startActivity(endIntent);
            }
        }
    });

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       binding = ActivityStage3Binding.inflate(getLayoutInflater());
       view = binding.getRoot();
       setContentView(view);
       intent = getIntent();

       name = intent.getStringExtra("name");
       email = intent.getStringExtra("email");
       message = "No message included";

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

       binding.textName.setText(name);
       binding.textEmail.setText(email);
       binding.edtMessage.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void afterTextChanged(Editable editable) {
               message = binding.edtMessage.getText().toString();
           }
       });

       binding.btnSendMail.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               composeEmail(email, "Hi, This is an email",uri, message);
           }
       });
    }

    @SuppressLint({"IntentReset", "QueryPermissionsNeeded"})
    private void composeEmail(String address, String subject, Uri attachment, String message){
       Intent sendMailIntent = new Intent(Intent.ACTION_SEND);
       sendMailIntent.setType("*/*");
       sendMailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
       sendMailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
       sendMailIntent.putExtra(Intent.EXTRA_STREAM, attachment);
       sendMailIntent.putExtra(Intent.EXTRA_TEXT, message);

        resultMailLauncher.launch(sendMailIntent);

       /*if (sendMailIntent.resolveActivity(getPackageManager()) != null) {
           binding.btnSendMail.setText("Email sent");
           startActivity(sendMailIntent);


       }*/
    }
}

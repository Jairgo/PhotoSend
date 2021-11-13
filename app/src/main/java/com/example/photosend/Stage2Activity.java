package com.example.photosend;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    private String uriString, name, email;
    private Bitmap bitmap;

    private final ActivityResultLauncher resultContactLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                selectContact(result.getData());

                binding.btnToStage3.setText(R.string.gotoStage3);

                binding.btnToStage3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(getApplicationContext(), Stage3Activity.class);

                        intent.putExtra("uriImage", uriString);
                        intent.putExtra("name", name);
                        intent.putExtra("email", email);

                        startActivity(intent);
                    }
                });
            }

        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStage2Binding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        intent = getIntent();

        uriString = intent.getStringExtra("uriImage");
        uri = Uri.parse(intent.getStringExtra("uriImage"));

        try {
            Stream = getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(Stream);
            binding.viewPhotoS2.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        binding.btnToStage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerLoadContact(view);
            }
        });

    }

    private void listenerLoadContact(View view){
        Intent loadContactIntent = new Intent(Intent.ACTION_PICK);
        loadContactIntent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE);

        resultContactLauncher.launch(loadContactIntent);
    }

    private void selectContact(Intent data){
        Uri uri = data.getData();
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.Contacts.DISPLAY_NAME};

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);

            name = cursor.getString(nameIndex);
            email = cursor.getString(emailIndex);

            binding.editTextPersonName.setText(name);
            binding.editTextPersonEmail.setText(email);

            // binding.editTextPersonName.setEnabled(true);
            // binding.editTextPersonEmail.setEnabled(true);
        }

        cursor.close();
    }
}

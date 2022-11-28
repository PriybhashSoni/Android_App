package com.pys.imgupload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pys.imgupload.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Uri imageuri;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImg();
            }
        });
    }

    private void uploadImg() {

        progressDialog =new ProgressDialog(this);
        progressDialog.setTitle("Upoading File...");
        progressDialog.show();

                  SimpleDateFormat formatter=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                  Date now=new Date();
                  String fileName=formatter.format(now);

                  storageReference= FirebaseStorage.getInstance().getReference("images/"+fileName);
                  storageReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          binding.imageView.setImageURI(null);

                          Toast.makeText(MainActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();

                       if(progressDialog.isShowing()){
                           progressDialog.dismiss();
                       }
                      }
                  }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          if(progressDialog.isShowing()){
                              progressDialog.dismiss();
                          }
                          Toast.makeText(MainActivity.this, "not Uploaded", Toast.LENGTH_SHORT).show();

                      }
                  });

    }

    private void selectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==100 && data!=null && data.getData()!=null){
            imageuri=data.getData();
            binding.imageView.setImageURI(imageuri);
        }
    }
}
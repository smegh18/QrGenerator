package com.example.qrgenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class MainActivity extends AppCompatActivity {
    EditText qrvalue;
    Button generatebtn, savebtn;
    ImageView qrimg;
    Bitmap bitmap ;
    QRGEncoder qrgEncoder;
    AppCompatActivity activity;
    String savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qrvalue = findViewById(R.id.qrinput);
        generatebtn = findViewById(R.id.qrbtn);
        savebtn = findViewById(R.id.svbtn);
        qrimg = findViewById(R.id.qrimage);
        activity = this;


        generatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = qrvalue.getText().toString();
                if (value.isEmpty()){
                    qrvalue.setError("Value Required");
                }
                else {
                    qrgEncoder = new QRGEncoder(value,null, QRGContents.Type.TEXT,500);
                    Bitmap bitmap = qrgEncoder.getBitmap();
                    qrimg.setImageBitmap(bitmap);

                }

            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean save;
                String result;
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    try {
                        qrgEncoder = new QRGEncoder(qrvalue.getText().toString(),null,QRGContents.Type.TEXT,500);
                        bitmap = qrgEncoder.getBitmap();
                        save = new QRGSaver().save(savePath, qrvalue.getText().toString(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
                        result = save ? "Image Saved" : "Image Not Saved";
                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
                }
            }
        });
    }
}
package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    String imgDir = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onTakePicture(View view){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);
    }

    public void onChoosePicture(View view){
        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }

    public void onConfirm(View view){
        if (imgDir.matches("")){
            return;
        }
        Intent next = new Intent(getApplicationContext(), Main2Activity.class);
        next.putExtra("Image Directory", imgDir);
        startActivity(next);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ImageView img = findViewById(R.id.pictureView);
        boolean passed = false;
        Bitmap selectedImage = null;
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    img.setImageBitmap(imageBitmap);
                    passed = true;
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "taking picture failed", Toast.LENGTH_SHORT);
                    toast.show();
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    img.setImageBitmap(imageBitmap);
                    passed = true;
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "getting picture from gallery failed", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
        if (passed){
            try{
                if (selectedImage == null){
                    Toast toast = Toast.makeText(getApplicationContext(), "How unfortunate, it failed", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    imgDir = saveToInternalStorage(selectedImage);
                }

            }catch (Exception e){

                Toast toast = Toast.makeText(getApplicationContext(), "How unfortunate, it failed", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

}

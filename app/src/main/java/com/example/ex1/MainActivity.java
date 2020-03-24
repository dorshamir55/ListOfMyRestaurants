package com.example.ex1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {
    private final int CAMERA_REQUEST = 1;
    private final int WRITE_PERMISSION_REQUEST = 1;
    private ImageView imageView;
    private Bitmap bitmap;
    //private Bitmap qualityBitmap;
    private Button listButton;
    private Button saveButton;
    private Button takePictureButton;
    private EditText restaurantNameEt;
    private EditText addressEt;
    private EditText foodTypeEt;
    private List<Restaurant> restaurants = new ArrayList<>();
    private static int count = 0;
    private String path;

    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.restaurant_picture_image);
        restaurantNameEt = (EditText) findViewById(R.id.restaurant_name_et);
        addressEt = ( EditText) findViewById(R.id.address_et);
        foodTypeEt = (EditText) findViewById(R.id.food_type_et);
        listButton = (Button) findViewById(R.id.list_btn);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, ListOfRestaurantsActivity.class);
                startActivity(intent);
            }
        });

        takePictureButton = (Button) findViewById(R.id.take_picture_btn);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "pic.jpg");

                file = new File(Environment.getExternalStorageDirectory(), "pic"+ count +".jpg");
                Uri imageUri = FileProvider.getUriForFile(MainActivity.this,"com.example.ex1.provider", file);
                //Toast.makeText(MainActivity.this, imageUri.toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        saveButton = (Button) findViewById(R.id.save_btn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String restaurantName = restaurantNameEt.getText().toString();
                String address = addressEt.getText().toString();
                String foodType = foodTypeEt.getText().toString();

                boolean nameAssert, addressAssert, typeAssert, bitmapAssert;
                if (restaurantName.trim().length() == 0) { //trim=remove spaces to avoid blank name
                    nameAssert = false;
                    restaurantNameEt.setError(getString(R.string.enter_name));
                } else {
                    nameAssert = true;
                }
                if (address.trim().length() == 0) { //trim=remove spaces to avoid blank name
                    addressAssert = false;
                    addressEt.setError(getString(R.string.enter_address));
                } else {
                    addressAssert = true;
                }
                if (foodType.trim().length() == 0) { //trim=remove spaces to avoid blank name
                    typeAssert = false;
                    foodTypeEt.setError(getString(R.string.enter_type));
                } else {
                    typeAssert = true;
                }
                if (bitmap==null) { //trim=remove spaces to avoid blank name
                    bitmapAssert = false;
                    Toast.makeText(MainActivity.this, R.string.enter_picture, Toast.LENGTH_SHORT).show();
                } else {
                    bitmapAssert = true;
                }
                if (nameAssert && addressAssert && typeAssert && bitmapAssert) {

                    count++;
                    Restaurant restaurant = new Restaurant(restaurantName, address, foodType, path, bitmap);
                    restaurants.add(restaurant);

                    RestaurantManager manager = RestaurantManager.getInstance(MainActivity.this);
                    manager.addRestaurant(restaurant);

                    restaurantNameEt.setText("");
                    addressEt.setText("");
                    foodTypeEt.setText("");
                    imageView.setImageBitmap(null);

                    Toast.makeText(MainActivity.this, R.string.save_restaurant, Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(Build.VERSION.SDK_INT>=23) {
            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(hasWritePermission!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST);
            }
            else{
                takePictureButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                listButton.setVisibility(View.VISIBLE);
                restaurantNameEt.setVisibility(View.VISIBLE);
                addressEt.setVisibility(View.VISIBLE);
                foodTypeEt.setVisibility(View.VISIBLE);
            }
        }
        else {
            takePictureButton.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);
            listButton.setVisibility(View.VISIBLE);
            restaurantNameEt.setVisibility(View.VISIBLE);
            addressEt.setVisibility(View.VISIBLE);
            foodTypeEt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==WRITE_PERMISSION_REQUEST){
            if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Can't take picture", Toast.LENGTH_SHORT).show();
            }
            else{
                takePictureButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                listButton.setVisibility(View.VISIBLE);
                restaurantNameEt.setVisibility(View.VISIBLE);
                addressEt.setVisibility(View.VISIBLE);
                foodTypeEt.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST && resultCode==RESULT_OK){
            path = file.getAbsolutePath();
            bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            //imageView = BitmapFactory.decodeFile(file.getAbsolutePath()).compress(Bitmap.CompressFormat.JPEG, 50;
            //bitmap = (Bitmap)data.getExtras().get("data");
            //imageView.setImageBitmap(bitmap);
        }
    }
}
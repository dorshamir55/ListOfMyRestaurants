package com.example.ex1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
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
import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    private final int CAMERA_REQUEST = 1;
    private final int WRITE_PERMISSION_REQUEST = 1;
    private ImageView imageView;
    private Bitmap bitmap;
    private Button listButton;
    private Button saveButton;
    private Button takePictureButton;
    private EditText restaurantNameEt;
    private EditText addressEt;
    private EditText phoneNumberEt;
    private List<Restaurant> restaurants = new ArrayList<>();
    private String path;
    private int count;

    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        count = prefs.getInt("counter", 0);

        imageView = (ImageView) findViewById(R.id.restaurant_picture_image);
        restaurantNameEt = (EditText) findViewById(R.id.restaurant_name_et);
        addressEt = ( EditText) findViewById(R.id.address_et);
        phoneNumberEt = (EditText) findViewById(R.id.phone_number_et);
        listButton = (Button) findViewById(R.id.back_btn);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (AddActivity.this, ListOfRestaurantsActivity.class);
                startActivity(intent);
            }
        });

        takePictureButton = (Button) findViewById(R.id.take_picture_btn);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "pic.jpg");

                file = new File(Environment.getExternalStorageDirectory(), "pic"+ count +".jpg");
                Uri imageUri = FileProvider.getUriForFile(AddActivity.this,"com.example.ex1.provider", file);
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
                String phoneNumber = phoneNumberEt.getText().toString();

                boolean nameAssert, addressAssert, phoneNumberAssert, bitmapAssert;
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
                if (phoneNumber.trim().length() == 0) { //trim=remove spaces to avoid blank name
                    phoneNumberAssert = false;
                    phoneNumberEt.setError(getString(R.string.enter_phone_number));
                } else {
                    phoneNumberAssert = true;
                }
                if (bitmap==null) { //trim=remove spaces to avoid blank name
                    bitmapAssert = false;
                    Toast.makeText(AddActivity.this, R.string.enter_picture, Toast.LENGTH_SHORT).show();
                } else {
                    bitmapAssert = true;
                }
                if (nameAssert && addressAssert && phoneNumberAssert && bitmapAssert) {

                    count++;
                    saveCount();
                    Restaurant restaurant = new Restaurant(restaurantName, address, phoneNumber, path, bitmap);
                    restaurants.add(restaurant);

                    RestaurantManager manager = RestaurantManager.getInstance(AddActivity.this);
                    manager.addRestaurant(restaurant);

                    restaurantNameEt.setText("");
                    addressEt.setText("");
                    phoneNumberEt.setText("");
                    imageView.setImageBitmap(null);

                    startActivity(new Intent(AddActivity.this, ListOfRestaurantsActivity.class));
                    Toast.makeText(AddActivity.this, R.string.save_restaurant, Toast.LENGTH_SHORT).show();
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
                phoneNumberEt.setVisibility(View.VISIBLE);
            }
        }
        else {
            takePictureButton.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);
            listButton.setVisibility(View.VISIBLE);
            restaurantNameEt.setVisibility(View.VISIBLE);
            addressEt.setVisibility(View.VISIBLE);
            phoneNumberEt.setVisibility(View.VISIBLE);
        }
    }

    private int firstRegister() {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("registered", false);
        editor.apply();
        return 0;
    }

    private void saveCount(){
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("counter", count);
        editor.apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==WRITE_PERMISSION_REQUEST){
            if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,getResources().getString(R.string.permission_msg), Toast.LENGTH_SHORT).show();
            }
            else{
                takePictureButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                listButton.setVisibility(View.VISIBLE);
                restaurantNameEt.setVisibility(View.VISIBLE);
                addressEt.setVisibility(View.VISIBLE);
                phoneNumberEt.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                path = file.getAbsolutePath();
                bitmap = BitmapFactory.decodeFile(path);
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, imageView.getWidth(), imageView.getHeight());
                imageView.setImageBitmap(bitmap);
                //imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                //imageView = BitmapFactory.decodeFile(file.getAbsolutePath()).compress(Bitmap.CompressFormat.JPEG, 50;
                //bitmap = (Bitmap)data.getExtras().get("data");
                //imageView.setImageBitmap(bitmap);
            } else {
                try {
                    //manager.removeRestaurant(viewHolder.getAdapterPosition());
                    
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                file = null;
                bitmap = null;
                imageView.setImageDrawable(null);
            }
        }
    }
}

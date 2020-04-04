package com.example.filterfeeders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

//camera functionality completed w/ assist from Atif Pervaiz' video: https://www.youtube.com/watch?v=LpL9akTG4hI
//camera roll saving functionality completed w/ assist from Brandan Jones' video: https://www.youtube.com/watch?v=_xIWkCJZCu0

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    public final static int PICK_IMAGE_CODE = 1046;
    Button btnEffect;
    Button photoBtn;
    Button rollBtn;
    Button saveBtn;
    ImageView photoView;

    Uri image_uri;

    // Holds the "bitmap" for the image currently being seen by the user
    Bitmap currentBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final PerlinNoise noise = new PerlinNoise(0);

        btnEffect = findViewById(R.id.btnEffect);
        photoView = findViewById(R.id.photo_view);
        photoBtn = findViewById(R.id.photo_btn);
        rollBtn = findViewById(R.id.roll_button);
        saveBtn = findViewById(R.id.save_button);

        /*//to add effects
        btnEffect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (currentBitmap != null)
                {
                    // Mask individual pixels
                    for (int x = 0; x < currentBitmap.getWidth(); x++) {
                        for (int y = 0; y < currentBitmap.getHeight(); y++) {
                            currentBitmap.setPixel(x, y, currentBitmap.getPixel(x, y) & 0xFFFF0000);
                        }
                    }

                    // Update image view
                    photoView.setImageBitmap(currentBitmap);
                }
                else
                {

                    Toast.makeText(MainActivity.this, "There is no image to  manipulate.", Toast.LENGTH_SHORT).show();
                }
            }
        }); */

        btnEffect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (currentBitmap != null)
                {
                    int width = currentBitmap.getWidth();
                    int height = currentBitmap.getHeight();

                    int[] pixels = new int[width * height];
                    currentBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

                    // Mask individual pixels
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            byte noiseValueR = (byte)Math.round(Clamp((noise.noise(x * 0.005, y * 0.005, 0) + .61) * 255, 0, 255));
                            byte noiseValueG = (byte)Math.round(Clamp((noise.noise(x * 0.005, y * 0.005, 1) + .61) * 255, 0, 255));
                            byte noiseValueB = (byte)Math.round(Clamp((noise.noise(x * 0.005, y * 0.005, 2) + .61) * 255, 0, 255));

                            pixels[x + y * width] &= 0xFF000000 | (noiseValueR << 16) | (noiseValueG << 8) | noiseValueB;

                            //currentBitmap.setPixel(x, y, currentBitmap.getPixel(x, y) & ((0xFF000000) | (noiseValueR << 16) /*| (noiseValueG << 8) | noiseValueB*/));
                        }
                    }

                    currentBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

                    // Update image view
                    photoView.setImageBitmap(currentBitmap);
                }
                else
                {

                    Toast.makeText(MainActivity.this, "There is no image to  manipulate.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //when button is clicked
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if system OS is marshmallow or higher, request runtime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED) {
                        //request permission if not enabled
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //popup to request permissions
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        //permission granted already
                        accessCamera();
                    }
                } else {
                    //system OS is less than marshmallow
                    accessCamera();
                }
            }
        });

        rollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Toast.makeText(MainActivity.this, "Accessed!", Toast.LENGTH_SHORT).show();
                onImageGalleryClicked(v);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Saved.", Toast.LENGTH_SHORT).show();
                imageToRoll();
            }
        });
    }

    private void accessCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    //to handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //method called when user presses Allow or Deny from Permission Request Popup
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //permission granted from the popup
                    accessCamera();
                } else {
                    //permission denied from popup
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //called when image captured from camera

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_CAPTURE_CODE) {
                //set photoView to the image we captured
                photoView.setImageURI(image_uri);

                Bitmap takenPhoto = ((BitmapDrawable) photoView.getDrawable()).getBitmap();
                currentBitmap = BitmapScaler.scaleToFitWidth(takenPhoto, 417);

                photoView.setImageBitmap(currentBitmap);
            }

                if (requestCode == PICK_IMAGE_CODE) {
                    //Toast.makeText(this, "Image Picked!", Toast.LENGTH_SHORT).show();

                    //get address of image on SD card
                    Uri imageUri = data.getData();

                    //declare a stream to read the image data from the SD card
                    InputStream inputStream;

                    try {
                        inputStream = getContentResolver().openInputStream(imageUri);

                        //get bitmap from stream
                        Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);

                        //save image to photoView
                        photoView.setImageBitmap(imageBitmap);

                        Bitmap takenPhoto = ((BitmapDrawable) photoView.getDrawable()).getBitmap();
                        currentBitmap = BitmapScaler.scaleToFitWidth(takenPhoto, 417);

                        photoView.setImageBitmap(currentBitmap);
                        Toast.makeText(this, "Image retrieved!", Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Unable to retrieve image", Toast.LENGTH_LONG).show();
                    }
                }
            }

    }

    private void imageToRoll(){
        photoView.buildDrawingCache();
        Bitmap image = photoView.getDrawingCache();  // Gets the Bitmap
        MediaStore.Images.Media.insertImage(getContentResolver(), image, "Altered Photo", "Made in FilterFeeders");  // Saves the image.
    }

    private void onImageGalleryClicked(View v){
        //invoke image gallery w/ implicit intent
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        //get data from where?
        File picDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String picDirectoryPath = picDirectory.getPath();

        //get a URI representation
        Uri data = Uri.parse(picDirectoryPath);

            //set the data and type. Get all image types.
        photoPickerIntent.setDataAndType(data, "image/*");

            //invoke activity
        startActivityForResult(photoPickerIntent, PICK_IMAGE_CODE);
    }

    double Clamp(double value, double min, double max)
    {
        return Math.max(min, Math.min(max, value));
    }
}


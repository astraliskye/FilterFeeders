package com.example.filterfeeders;

import androidx.annotation.NonNull;
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
import android.widget.SeekBar;
import android.widget.Toast;

import com.mukesh.image_processing.ImageProcessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

//camera functionality completed w/ assist from Atif Pervaiz' video: https://www.youtube.com/watch?v=LpL9akTG4hI
//camera roll saving functionality completed w/ assist from Brandan Jones' video: https://www.youtube.com/watch?v=_xIWkCJZCu0
//imported Mukesh Solanki's photo filter library: https://github.com/mukeshsolanki/photofilter

public class MainActivity extends AppCompatActivity
{
    public static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    public final static int PICK_IMAGE_CODE = 1046;

    //effect buttons
    Button perlinBtn;
    Button invertBtn;
    Button greyscaleBtn;
    Button blackFilterBtn;
    Button fleaEffectBtn;
    Button gaussianBtn;
    Button meanRemoveBtn;

    //sliders
    SeekBar redBar;
    SeekBar greenBar;
    SeekBar blueBar;
    int redLevel = 0;
    int greenLevel = 0;
    int blueLevel = 0;


    //camera buttons
    Button photoBtn;
    Button rollBtn;
    Button saveBtn;
    ImageView photoView;

    Uri image_uri;

    // Holds the "bitmap" for the image currently being seen by the user
    BitmapEffects bmpEffects = new BitmapEffects(null);

    ImageProcessor imageProcessor = new ImageProcessor();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final PerlinNoise noise = new PerlinNoise(0);

        //buttons for effects
        perlinBtn = findViewById(R.id.perlinBtn);
        invertBtn = findViewById(R.id.invertBtn);
        greyscaleBtn = findViewById(R.id.greyscaleBtn);
        blackFilterBtn = findViewById(R.id.blackFilterBtn);
        fleaEffectBtn = findViewById(R.id.fleaEffectBtn);
        gaussianBtn = findViewById(R.id.gaussianBtn);
        meanRemoveBtn = findViewById(R.id.meanRemoveBtn);

        //seekbars
        redBar = findViewById(R.id.redBar);
        greenBar = findViewById(R.id.greenBar);
        blueBar = findViewById(R.id.blueBar);

        //buttons for camera
        photoView = findViewById(R.id.photo_view);
        photoBtn = findViewById(R.id.photo_btn);
        rollBtn = findViewById(R.id.roll_button);
        saveBtn = findViewById(R.id.save_button);

        redBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                redLevel = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        greenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                greenLevel = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        blueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                blueLevel = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        perlinBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bitmap bmp = bmpEffects.applyPerlinEffect(redLevel, greenLevel, blueLevel);

                if (bmp == null)
                {
                    Toast.makeText(MainActivity.this, "There is no image to manipulate.", Toast.LENGTH_SHORT).show();
                    return;
                }

                photoView.setImageBitmap(bmp);
            }
        });

        invertBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bitmap bmp = bmpEffects.invert();

                if (bmp == null)
                {
                    Toast.makeText(MainActivity.this, "There is no image to manipulate.", Toast.LENGTH_SHORT).show();
                    return;
                }

                photoView.setImageBitmap(bmp);
            }
        });

        greyscaleBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bitmap bmp = bmpEffects.greyscale();

                if (bmp == null)
                {
                    Toast.makeText(MainActivity.this, "There is no image to manipulate.", Toast.LENGTH_SHORT).show();
                    return;
                }

                photoView.setImageBitmap(bmp);
            }
        });

        blackFilterBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bitmap bmp = bmpEffects.blackFilter();

                if (bmp == null)
                {
                    Toast.makeText(MainActivity.this, "There is no image to manipulate.", Toast.LENGTH_SHORT).show();
                    return;
                }

                photoView.setImageBitmap(bmp);
            }
        });

        fleaEffectBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bitmap bmp = bmpEffects.fleaEffect();

                if (bmp == null)
                {
                    Toast.makeText(MainActivity.this, "There is no image to manipulate.", Toast.LENGTH_SHORT).show();
                    return;
                }

                photoView.setImageBitmap(bmp);
            }
        });

        gaussianBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bitmap bmp = bmpEffects.gaussian();

                if (bmp == null)
                {
                    Toast.makeText(MainActivity.this, "There is no image to manipulate.", Toast.LENGTH_SHORT).show();
                    return;
                }

                photoView.setImageBitmap(bmp);
            }
        });

        meanRemoveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bitmap bmp = bmpEffects.meanRemove();

                if (bmp == null)
                {
                    Toast.makeText(MainActivity.this, "There is no image to manipulate.", Toast.LENGTH_SHORT).show();
                    return;
                }

                photoView.setImageBitmap(bmp);
            }
        });

        //when button is clicked
        photoBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // if system OS is marshmallow or higher, request runtime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED)
                    {
                        //request permission if not enabled
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //popup to request permissions
                        requestPermissions(permission, PERMISSION_CODE);
                    } else
                    {
                        //permission granted already
                        accessCamera();
                    }
                } else
                {
                    //system OS is less than marshmallow
                    accessCamera();
                }
            }
        });

        rollBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(MainActivity.this, "Accessed!", Toast.LENGTH_SHORT).show();
                onImageGalleryClicked(v);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(MainActivity.this, "Saved.", Toast.LENGTH_SHORT).show();
                imageToRoll();
            }
        });
    }

    private void accessCamera()
    {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        //method called when user presses Allow or Deny from Permission Request Popup
        switch (requestCode)
        {
            case PERMISSION_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED)
                {
                    //permission granted from the popup
                    accessCamera();
                } else
                {
                    //permission denied from popup
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //called when image captured from camera

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == IMAGE_CAPTURE_CODE)
            {
                //set photoView to the image we captured
                photoView.setImageURI(image_uri);

                Bitmap takenPhoto = ((BitmapDrawable) photoView.getDrawable()).getBitmap();
                Bitmap bmp = BitmapScaler.scaleToFitWidth(takenPhoto, 1000);

                bmpEffects.setBitmap(bmp);
                photoView.setImageBitmap(bmp);
            }

            if (requestCode == PICK_IMAGE_CODE)
            {
                //Toast.makeText(this, "Image Picked!", Toast.LENGTH_SHORT).show();

                //get address of image on SD card
                Uri imageUri = data.getData();

                //declare a stream to read the image data from the SD card
                InputStream inputStream;

                try
                {
                    inputStream = getContentResolver().openInputStream(imageUri);

                    //get bitmap from stream
                    Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);

                    //save image to photoView
                    photoView.setImageBitmap(imageBitmap);
                    Bitmap takenPhoto = ((BitmapDrawable) photoView.getDrawable()).getBitmap();
                    Bitmap bmp = BitmapScaler.scaleToFitWidth(takenPhoto, 1000);

                    bmpEffects.setBitmap(bmp);
                    photoView.setImageBitmap(bmp);
                    Toast.makeText(this, "Image retrieved!", Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to retrieve image", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    private void imageToRoll() {
        photoView.buildDrawingCache();
        Bitmap image = photoView.getDrawingCache();  // Gets the Bitmap
        MediaStore.Images.Media.insertImage(getContentResolver(), image, "Altered Photo", "Made in FilterFeeders");  // Saves the image.
    }

    private void onImageGalleryClicked(View v) {
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

    double Clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
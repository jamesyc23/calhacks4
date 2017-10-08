package edu.berkeley.jamesyc23;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.PictureCallback mPicture;

    private Button takePhotoButton;
    private Button analyzeButton;
    private Button retakePhotoButton;

    private Button profileButton;
    private Button settingsButton;
    private Button practitionerButton;
    private Button faqButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePhotoButton = (Button) findViewById(R.id.take_photo);
        analyzeButton = (Button) findViewById(R.id.analyze);
        retakePhotoButton = (Button) findViewById(R.id.retake_photo);

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        analyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        retakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retakePhoto();
            }
        });

        profileButton = (Button) findViewById(R.id.button_profile);
        settingsButton = (Button) findViewById(R.id.button_settings);
        practitionerButton = (Button) findViewById(R.id.button_practitioner);
        faqButton = (Button) findViewById(R.id.button_faq);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayProfileActivity.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplaySettingsActivity.class);
                startActivity(intent);
            }
        });

        practitionerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayPractitionerActivity.class);
                startActivity(intent);
            }
        });

        faqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayFaqActivity.class);
                startActivity(intent);
            }
        });


        mPicture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                Log.i(TAG, data.toString());

//                File pictureFile = null;
//
//                try {
//                    FileOutputStream fos = new FileOutputStream(pictureFile);
//                    fos.write(data);
//                    fos.close();
//                } catch (FileNotFoundException e) {
//                    Log.d(TAG, "File not found: " + e.getMessage());
//                } catch (IOException e) {
//                    Log.d(TAG, "Error accessing file: " + e.getMessage());
//                }
            }
        };

        openFeed();

    }

    public void openFeed() {

        if (mCamera == null) {
            try {
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);//you can use open(int) to use different cameras
            } catch (Exception e) {
                Log.d("ERROR", "Failed to get camera: " + e.getMessage());
            }
        }
        mCamera.setDisplayOrientation(90);

        mPreview = new CameraPreview(this, mCamera);
        RelativeLayout preview = (RelativeLayout) findViewById(R.id.camera_relative_layout);
        preview.addView(mPreview);

    }


    public void takePhoto() {

        mCamera.takePicture(null, null, mPicture);

        takePhotoButton.setVisibility(View.INVISIBLE);
        analyzeButton.setVisibility(View.VISIBLE);
        retakePhotoButton.setVisibility(View.VISIBLE);

    }

    public void retakePhoto() {

        mCamera.startPreview();

        takePhotoButton.setVisibility(View.VISIBLE);
        analyzeButton.setVisibility(View.INVISIBLE);
        retakePhotoButton.setVisibility(View.INVISIBLE);


    }

}

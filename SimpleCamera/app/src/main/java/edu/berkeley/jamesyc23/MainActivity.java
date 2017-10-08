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

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private Camera mCamera = null;
//    private CameraView mCameraView = null;

    private SurfaceView cameraView;

    private Button takePhotoButton;
    private Button analyzeButton;
    private Button retakePhotoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cameraView = (SurfaceView) findViewById(R.id.camera_view);

        takePhotoButton = (Button) findViewById(R.id.take_photo);
        analyzeButton = (Button) findViewById(R.id.analyze);
        retakePhotoButton = (Button) findViewById(R.id.retake_photo);

        openFeed();

        //btn to close the application
//        ImageButton imgClose = (ImageButton)findViewById(R.id.imgClose);
//        imgClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.exit(0);
//            }
//        });
    }

    public void openFeed() {

        if (mCamera == null) {
            try {
                mCamera = Camera.open();//you can use open(int) to use different cameras
            } catch (Exception e) {
                Log.d("ERROR", "Failed to get camera: " + e.getMessage());
            }
        }

//        Camera.Parameters parameters = mCamera.getParameters();
//        for (Camera.Size size : parameters.getSupportedPreviewSizes()){
//            Log.i(TAG, "Supported size: w = " + size.width  + ", h = " + size.height);
//        }
//        parameters.setPreviewSize(1088, 1088);
//        mCamera.setParameters(parameters);

        Camera camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        Camera.Parameters camParams = camera.getParameters();

        // Find a preview size that is at least the size of our IMAGE_SIZE
        Camera.Size previewSize = camParams.getSupportedPreviewSizes().get(0);
        for (Camera.Size size : camParams.getSupportedPreviewSizes()) {
            if (size.width >= 1024 && size.height >= 1024) {
                previewSize = size;
                break;
            }
        }
        camParams.setPreviewSize(previewSize.width, previewSize.height);

        // Try to find the closest picture size to match the preview size.
        Camera.Size pictureSize = camParams.getSupportedPictureSizes().get(0);
        for (Camera.Size size : camParams.getSupportedPictureSizes()) {
            if (size.width == previewSize.width && size.height == previewSize.height) {
                pictureSize = size;
                break;
            }
        }
        camParams.setPictureSize(pictureSize.width, pictureSize.height);

//        mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
//        cameraView.addView(mCameraView);//add the SurfaceView to the layout

//        Log.i(TAG, "" + mCamera.getParameters().getPreviewSize().width);
//        Log.i(TAG, "" + mCamera.getParameters().getPreviewSize().height);
//
//        Log.i(TAG, "w = " + mCameraView.getWidth());
//        Log.i(TAG, "h = " + mCameraView.getHeight());

    }

    /** Called when the user taps the Profile button */
    public void displayProfile(View view) {
        Intent intent = new Intent(this, DisplayProfileActivity.class);
        startActivity(intent);
    }

    /** Called when the user taps the FAQ button */
    public void displayFAQ(View view) {
        Intent intent = new Intent(this, DisplayFaqActivity.class);
        startActivity(intent);
    }

    /** Called when the user taps the Practitioner button */
    public void displayPractitioner(View view) {
        Intent intent = new Intent(this, DisplayPractitionerActivity.class);
        startActivity(intent);
    }

    /** Called when the user taps the Settings button */
    public void displaySettings(View view) {
        Intent intent = new Intent(this, DisplaySettingsActivity.class);
        startActivity(intent);
    }

    public void takePhoto(View view) {

        mCamera.takePicture(null, null, null, null);

        takePhotoButton.setVisibility(View.INVISIBLE);
        analyzeButton.setVisibility(View.VISIBLE);
        retakePhotoButton.setVisibility(View.VISIBLE);

    }

    public void retakePhoto(View view) {

//        Log.i(TAG, "w = " + mCameraView.getWidth());
//        Log.i(TAG, "h = " + mCameraView.getHeight());

        mCamera.startPreview();


    }

}

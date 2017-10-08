package com.yahoo.chrisliambender.simplecamera;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private Camera mCamera;
    private CameraPreview mCameraPreview;
    private RelativeLayout mRelativeLayout;
    private Camera.PictureCallback mPictureCallback;

    private Button profileButton;
    private Button practitionerButton;
    private Button faqButton;
    private Button settingsButton;

    private Button takePictureButton;
    private Button analyzeButton;
    private Button retakeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_camera);
        initalizeCamera();

        mPictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {

                Log.i(TAG, bytes.toString());

            }
        };


        profileButton = (Button) findViewById(R.id.button_profile);
        practitionerButton = (Button) findViewById(R.id.button_practitioner);
        faqButton = (Button) findViewById(R.id.button_faq);
        settingsButton = (Button) findViewById(R.id.button_settings);

        takePictureButton = (Button) findViewById(R.id.button_take_picture);
        analyzeButton = (Button) findViewById(R.id.button_analyze);
        retakeButton = (Button) findViewById(R.id.button_retake);


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayProfileActivity.class);
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
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplaySettingsActivity.class);
                startActivity(intent);
            }
        });


        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
        analyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                analyze();
            }
        });
        retakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retakePicture();
            }
        });


    }

    private void initalizeCamera(){

        if (mCamera == null) {
            try {
                mCamera = Camera.open();//you can use open(int) to use different cameras
            } catch (Exception e) {
                Log.d("ERROR", "Failed to get camera: " + e.getMessage());
            }
        }

        mCamera.setDisplayOrientation(90);
        mCameraPreview = new CameraPreview(this, mCamera);

        mRelativeLayout.removeAllViews();
        mRelativeLayout.addView(mCameraPreview);

    }


    private void takePicture(){

        mCamera.takePicture(null, null, mPictureCallback);

        takePictureButton.setVisibility(View.INVISIBLE);
        analyzeButton.setVisibility(View.VISIBLE);
        retakeButton.setVisibility(View.VISIBLE);

    }

    private void retakePicture(){

        mCamera.startPreview();

        takePictureButton.setVisibility(View.VISIBLE);
        analyzeButton.setVisibility(View.VISIBLE);
        retakeButton.setVisibility(View.VISIBLE);

    }


    private void analyze(){

        final AlertDialog waitingDialog = new AlertDialog.Builder(this)
                .setTitle("Computing results ...")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        retakePicture();
                    }
                })
                .create();
        waitingDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                waitingDialog.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        double percent = (double) Math.round((1 + 9 * Math.random()) * 100) / 100;
                        String recommendation;

                        if (percent < 5){
                            recommendation = "Your skin appears to be normal.";
                        } else {
                            recommendation = "We recommend waiting two weeks and performing another scan.";
                        }

                        final AlertDialog resultDialog = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Results")
                                .setMessage("Our models indicate a " + percent + "% chance of malignance.\n\n" + recommendation)
                                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        retakePicture();
                                    }
                                }).create();
                        resultDialog.show();

                    }
                });

            }
        }).start();


    }

    @Override
    protected void onPause() {
        super.onPause();
        mCamera.release();
        mCamera = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initalizeCamera();
    }
}

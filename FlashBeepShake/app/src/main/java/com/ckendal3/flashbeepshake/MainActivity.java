package com.ckendal3.flashbeepshake;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 50;
    CameraManager mCameraManager;
    String cameraId;
    ToneGenerator tone;
    Switch vibrateSwitch, soundSwitch, lightSwitch;
    Vibrator mVibrate;
    TextView lightStatus, soundStatus, vibrateStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lightSwitch = findViewById(R.id.lightSwitch);
        lightStatus = findViewById(R.id.lightStatus);
        lightSwitch.setTextColor(Color.GREEN);


        soundSwitch = findViewById(R.id.soundSwitch);
        soundStatus = findViewById(R.id.soundStatus);
        soundSwitch.setTextColor(Color.BLUE);

        vibrateSwitch = findViewById(R.id.vibrateSwitch);
        vibrateStatus = findViewById(R.id.vibrateStatus);
        vibrateSwitch.setTextColor(Color.RED);


        lightSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);

                if (lightSwitch.isChecked()) {
                    lightOn();
                } else {
                    lightOff();
                }
            }
        });

        soundSwitch.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (soundSwitch.isChecked()) {
                soundOn();
            } else {
                soundOff();
            }
        }
    });

        vibrateSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vibrateSwitch.isChecked()) {
                    vibrateOn();
                } else {
                    vibrateOff();
                }
            }
        });
}

    public void lightOn() {

        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = mCameraManager.getCameraIdList()[0];
            mCameraManager.setTorchMode(cameraId, true);
            lightStatus.setText(lightSwitch.getTextOn().toString());

        } catch (CameraAccessException e) {
            Log.d("Camera Turned On: ", "Camera does not exist");
        }
    }

    public void lightOff() {

        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = mCameraManager.getCameraIdList()[0];
            mCameraManager.setTorchMode(cameraId, false);
            lightStatus.setText(lightSwitch.getTextOff().toString());

        } catch (CameraAccessException e) {
            Log.d("Camera Turned Off: ", "Camera does not exist");
        }
    }

    public  void soundOn() {
        tone = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        tone.startTone(ToneGenerator.TONE_DTMF_P);
        soundStatus.setText(soundSwitch.getTextOn().toString());
    }

    public void soundOff() {
        tone.stopTone();
        soundStatus.setText(soundSwitch.getTextOff().toString());
    }

    public  void vibrateOn() {
        mVibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 1000, 100};
        if(mVibrate != null){
            mVibrate.vibrate(pattern, 0);
            vibrateStatus.setText(vibrateSwitch.getTextOn().toString());
        }
    }

    public void vibrateOff() {
        mVibrate.cancel();
        vibrateStatus.setText(vibrateSwitch.getTextOff().toString());
    }

}
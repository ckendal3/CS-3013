package com.exercise.seekcolor;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.SurfaceView;
import android.widget.SeekBar;
import android.widget.TextView;

class MainActivity extends Activity {

    private int seekR, seekG, seekB;
    SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    ConstraintLayout mScreen;
    TextView textView_R, textView_B, textView_G;
    SurfaceView sView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sView = (SurfaceView) findViewById(R.id.surfaceView);

        textView_R = (TextView) findViewById(R.id.textView_R);
        textView_G = (TextView) findViewById(R.id.textView_G);
        textView_B = (TextView) findViewById(R.id.textView_B);

        mScreen = (ConstraintLayout) findViewById(R.id.myScreen);
        redSeekBar = (SeekBar) findViewById(R.id.mySeekingBar_R);
        greenSeekBar = (SeekBar) findViewById(R.id.mySeekingBar_G);
        blueSeekBar = (SeekBar) findViewById(R.id.mySeekingBar_B);
        updateBackground();

        redSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        greenSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        blueSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener
            = new SeekBar.OnSeekBarChangeListener()
    {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
// TODO Auto-generated method stub
            updateBackground();
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
// TODO Auto-generated method stub
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
// TODO Auto-generated method stub
        }
    };

    private void updateBackground()
    {
        seekR = redSeekBar.getProgress();
        textView_R.setText(Integer.toString(seekR));

        seekG = greenSeekBar.getProgress();
        textView_G.setText(Integer.toString(seekG));

        seekB = blueSeekBar.getProgress();
        textView_B.setText(Integer.toString(seekB));


        sView.setBackgroundColor(
                0xff000000
                        + seekR * 0x10000
                        + seekG * 0x100
                        + seekB
        );
    }
}
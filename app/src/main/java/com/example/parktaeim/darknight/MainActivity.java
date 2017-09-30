package com.example.parktaeim.darknight;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbSeekbar;

import me.itangqi.waveloadingview.WaveLoadingView;

public class MainActivity extends AppCompatActivity {
    private TextView percentTextView;
    private float bright;
    private SeekBar seekBar;
    private Button button;
    private WaveLoadingView waveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Settings.System.putInt(getContentResolver(), "screen_brightness",10);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                Toast.makeText(this, "onCreate: Already Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "onCreate: Not Granted. Permission Requested", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        //자동 밝기모드
        try {
            if(Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) !=1){
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,1);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        waveView = (WaveLoadingView) findViewById(R.id.waveView);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        button = (Button) findViewById(R.id.startBtn);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress<10){
                    progress = 10;
                    seekBar.setProgress(progress);
                }
                waveView.setProgressValue(progress);
                System.out.println(progress);
                String stringProgress = Integer.toString(progress);
                percentTextView.setText(stringProgress);
                bright = (float) progress/100;

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this,"button Click",Toast.LENGTH_SHORT).show();
                        Settings.System.putInt(getContentResolver(), "screen_brightness",10);
                        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                        layoutParams.screenBrightness = bright;
                        getWindow().setAttributes(layoutParams);
                    }
                });

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                seekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
////                    @Override
////                    public void valueChanged(Number value) {
////                        String stringValue = value.toString();
////                        bright = Integer.parseInt(stringValue);
////                        percentTextView.setText(stringValue);
////                    }
////                });
//                Toast.makeText(MainActivity.this,"click",Toast.LENGTH_SHORT).show();
////                Settings.System.putInt(getContentResolver(), "screen_brightness",60);
////                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
////                layoutParams.screenBrightness = 0.65f;
////                getWindow().setAttributes(layoutParams);
//            }
//        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                Toast.makeText(this, "onResume: Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }


}

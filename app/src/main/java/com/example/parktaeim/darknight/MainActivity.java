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
    private int now_bright_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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



        waveView = (WaveLoadingView) findViewById(R.id.waveView);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        button = (Button) findViewById(R.id.startBtn);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 10) {
                    progress = 10;
                    seekBar.setProgress(progress);
                }
                waveView.setProgressValue(progress);
                System.out.println(progress);
                String stringProgress = Integer.toString(progress);
                percentTextView.setText(stringProgress);
                bright = (float) progress / 100;

                if(button.getText().toString().equals("STOP")){


                    Settings.System.putInt(getContentResolver(), "screen_brightness", (int) (bright * 255));
                    Log.d("system bright----------", String.valueOf((int) (bright * 255)));
                    WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                    layoutParams.screenBrightness = bright;
                    getWindow().setAttributes(layoutParams);


                }else if(button.getText().toString().equals("START")){

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button.getText().toString().equals("START")){
                    try {
                        now_bright_status = android.provider.Settings.System.getInt(getContentResolver(),
                                android.provider.Settings.System.SCREEN_BRIGHTNESS);
                    } catch (Exception e) {
                        Log.e("Exception e " + e.getMessage(), null);
                    }
                    Log.d("현재밝기--------------",String.valueOf(now_bright_status));
                    button.setText("STOP");

                    Settings.System.putInt(getContentResolver(), "screen_brightness", (int) (bright * 255));
                    Log.d("system bright----------", String.valueOf((int) (bright * 255)));
                    WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                    layoutParams.screenBrightness = bright;
                    getWindow().setAttributes(layoutParams);

                    //자동 밝기모드
                    try {
                        if (Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == 1) {
                            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 0);
                        }
                    } catch (Settings.SettingNotFoundException e) {
                        e.printStackTrace();
                    }

                }else if(button.getText().toString().equals("STOP")){
                    button.setText("START");
                    Settings.System.putInt(getContentResolver(), "screen_brightness", now_bright_status);
                    WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                    layoutParams.screenBrightness = now_bright_status/255;
                    getWindow().setAttributes(layoutParams);

                    //자동 밝기모드
                    try {
                        if (Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) != 1) {
                            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 1);
                        }
                    } catch (Settings.SettingNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Settings.System.putInt(getContentResolver(), "screen_brightness", now_bright_status);
    }
}

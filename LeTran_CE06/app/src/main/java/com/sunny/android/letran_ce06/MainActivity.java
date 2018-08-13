
    // Name: Tran Le
    // JAV1 - 1808
    // File name: MainActivity.java

package com.sunny.android.letran_ce06;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

    public class MainActivity extends AppCompatActivity implements TimerAsyncTask.OnFinished {

    private EditText minutes_1;
    private EditText seconds_1;
    private TextView minutes_2;
    private TextView seconds_2;
    private Button start;
    private Button stop;

    private TimerAsyncTask timerTask = null;

    private Toast feedback;

    private static final String TAG = "In your face";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        minutes_1 = (EditText)findViewById(R.id.etx_Minutes);
        seconds_1 = (EditText)findViewById(R.id.etx_Seconds);
        minutes_2 = (TextView)findViewById(R.id.txt_Minutes);
        seconds_2 = (TextView)findViewById(R.id.txt_Seconds);
        start = (Button)findViewById(R.id.btn_Start);
        stop = (Button)findViewById(R.id.btn_Stop);
        stop.setEnabled(false);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String beforeMin = minutes_1.getText().toString();
                String beforeSec = seconds_1.getText().toString();
                String afterMin = beforeMin.replace("0", "");
                String afterSec = beforeSec.replace("0", "");

                if (feedback != null) {
                    feedback.cancel();
                }

                if (afterMin.length() == 0 && afterSec.length() == 0) {
                    feedback = Toast.makeText(MainActivity.this, R.string.toast_empty_text, Toast.LENGTH_SHORT);
                    feedback.show();
                } else {
                    start.setEnabled(false);
                    stop.setEnabled(true);

                    try {
                        Integer numOfMin = Integer.parseInt(beforeMin);
                        Integer numOfSec = Integer.parseInt(beforeSec);
                        Integer totalSeconds = numOfMin * 60 + numOfSec;

                        if (numOfSec > 60) {
                            numOfMin += (int)Math.floor(numOfSec/60);
                            numOfSec = (int)Math.floor(numOfSec%60);
                        }
                        minutes_2.setText(String.format(Locale.US, "%02d", numOfMin));
                        minutes_2.setVisibility(View.VISIBLE);
                        seconds_2.setText(String.format(Locale.US, "%02d", numOfSec));
                        seconds_2.setVisibility(View.VISIBLE);
                        minutes_1.setVisibility(View.INVISIBLE);
                        seconds_1.setVisibility(View.INVISIBLE);

                        timerTask = new TimerAsyncTask(MainActivity.this, MainActivity.this);
                        timerTask.execute(totalSeconds);

                        Log.i(TAG, "Minutes: "+numOfMin+", seconds: "+numOfSec);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Assign listener to handle the cancel event when user taps Stop
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(true);
                stop.setEnabled(false);

                if (timerTask != null) {
                    timerTask.cancel(true);
                }
            }
        });
    }

    @Override
    public void onFinished() {
        start.setEnabled(true);
        stop.setEnabled(false);
        minutes_1.setText("00");
        minutes_1.setVisibility(View.VISIBLE);
        minutes_2.setText("");
        minutes_2.setVisibility(View.INVISIBLE);
        seconds_1.setText("00");
        seconds_1.setVisibility(View.VISIBLE);
        seconds_2.setText("");
        seconds_2.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCancelled() {
        start.setEnabled(true);
        stop.setEnabled(false);
        minutes_1.setText("00");
        minutes_1.setVisibility(View.VISIBLE);
        minutes_2.setText("");
        minutes_2.setVisibility(View.INVISIBLE);
        seconds_1.setText("00");
        seconds_1.setVisibility(View.VISIBLE);
        seconds_2.setText("");
        seconds_2.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onUpdate(Integer[] currentTime) {
        minutes_2.setText(String.format(Locale.getDefault(), "%02d", currentTime[0]));
        seconds_2.setText(String.format(Locale.getDefault(), "%02d", currentTime[1]));
    }
}

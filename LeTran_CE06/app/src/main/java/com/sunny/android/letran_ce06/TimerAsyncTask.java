
    // Name: Tran Le
    // JAV1 - 1808
    // File name: TimerAsyncTask.java

package com.sunny.android.letran_ce06;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class TimerAsyncTask extends AsyncTask<Integer, Float, Float> {

    static final private String TAG = "New Day";
    static final private long SLEEP = 900;

    final private Context hostContext;
    final private OnFinished finishedInterface;

    interface OnFinished {
        void onFinished();
        void onCancelled();
        void onUpdate(Integer[] currentTime);
    }

    TimerAsyncTask(Context _context, OnFinished _finished) {
        hostContext = _context;
        finishedInterface = _finished;
    }

    // This worked!
    @Override
    protected void onPreExecute() {
        Toast.makeText(hostContext, R.string.toast_start, Toast.LENGTH_SHORT).show();
    }

    // TODO: doInBackground function
    @Override
    protected Float doInBackground(Integer... integers) {
        if (integers == null || integers.length <= 0) {
            return 0.0f;
        }
        Long startTime = System.currentTimeMillis();
        Float remainTime = (float)integers[0];
        Float passedTime = 0.0f;

        while (remainTime > 0 && !isCancelled()) {

            try {
                Thread.sleep(SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Long endTime = System.currentTimeMillis();
            passedTime = (endTime - startTime)/1000.0f;
            remainTime = integers[0] - passedTime;
            publishProgress(remainTime);

            Log.i(TAG, "doInBackground: "+remainTime+" passed time: "+passedTime);
        }

        return passedTime;
    }

        // TODO: implement progress update
    @Override
    protected void onProgressUpdate(Float... values) {
        if (finishedInterface != null) {
            Integer minute = (int)Math.floor(values[0]/60);
            Integer second = (int)Math.floor(values[0]%60);
            Integer[] time = {minute, second};
            finishedInterface.onUpdate(time);
            Log.i(TAG, "onProgressUpdate: "+minute+" "+second);
        }
    }

    // TODO: implement post execute
    @Override
    protected void onPostExecute(Float aFloat) {
        if (finishedInterface != null) {
            finishedInterface.onFinished();

            AlertDialog.Builder builder = new AlertDialog.Builder(hostContext);
            builder.setTitle(R.string.alert_title);
            builder.setPositiveButton(R.string.alert_pos_button, null);
            builder.show();
        }
    }

    // TODO: implement cancel
    @Override
    protected void onCancelled(Float aFloat) {
        if (finishedInterface != null) {
            finishedInterface.onCancelled();

            Integer minute = (int)Math.floor(aFloat/60);
            Integer second = (int)Math.floor(aFloat%60);

            AlertDialog.Builder builder = new AlertDialog.Builder(hostContext);
            builder.setTitle(R.string.alert_title);
            builder.setMessage("Elapsed time: " + minute + " minute(s) " + second + " second(s)");
            builder.setPositiveButton(R.string.alert_pos_button, null);
            builder.show();
        }
    }
}

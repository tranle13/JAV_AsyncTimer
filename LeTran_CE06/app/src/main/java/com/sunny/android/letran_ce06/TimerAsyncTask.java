
    // Name: Tran Le
    // JAV1 - 1808
    // File name: TimerAsyncTask.java

package com.sunny.android.letran_ce06;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

class TimerAsyncTask extends AsyncTask<Integer, Float, Float> {

    static final private long SLEEP = 210;

    final private Context hostContext;
    final private OnFinished finishedInterface;

    // Create interface
    interface OnFinished {
        void onFinished();
        void onCancelled();
        void onUpdate(Integer[] currentTime);
    }

    // Constructor
    TimerAsyncTask(Context _context, OnFinished _finished) {
        hostContext = _context;
        finishedInterface = _finished;
    }

    // Create toast to let user know timer starts
    @Override
    protected void onPreExecute() {
        Toast.makeText(hostContext, R.string.toast_start, Toast.LENGTH_SHORT).show();
    }

    // Code to calculate the time
    @Override
    protected Float doInBackground(Integer... integers) {
        if (integers == null || integers.length <= 0) {
            return 0.0f;
        }
        Long startTime = System.currentTimeMillis();
        Float initialInputTime = (float)integers[0];
        Float remain = initialInputTime;

        while (remain > 0 && !isCancelled()) {

            try {
                Thread.sleep(SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Long current = System.currentTimeMillis();
            remain = initialInputTime - (current - startTime)/1000f;
            publishProgress(remain);
        }

        return (System.currentTimeMillis() - startTime)/1000f;
    }

    // Code to update UI while background does the calculation
    @Override
    protected void onProgressUpdate(Float... values) {
        if (finishedInterface != null) {
            Integer minute = (int)Math.floor(values[0]/60);
            Integer second = (int)Math.ceil(values[0]%60);
            Integer[] time = {minute, second};
            finishedInterface.onUpdate(time);
        }
    }

    // Code to display alert dialog after timer has ended
    @Override
    protected void onPostExecute(Float aFloat) {
        if (finishedInterface != null) {
            finishedInterface.onFinished();
            buildDialog(0, aFloat);
        }
    }

    // Code to display alert dialog to show user how long has passed
    @Override
    protected void onCancelled(Float aFloat) {
        if (finishedInterface != null) {
            finishedInterface.onCancelled();
            buildDialog(1, aFloat);
        }
    }

    // Function to build alertDialog
    private void buildDialog(int isCancelAlert, Float time) {

        AlertDialog.Builder builder = new AlertDialog.Builder(hostContext);
        builder.setTitle(R.string.alert_title);
        if (isCancelAlert == 1) {
            Integer minute = (int)Math.floor(time/60);
            Integer second = (int)Math.floor(time%60);
            builder.setMessage("Elapsed time: " + minute + " minute(s) " + second + " second(s)");
        }
        builder.setPositiveButton(R.string.alert_pos_button, null);
        builder.show();
    }
}

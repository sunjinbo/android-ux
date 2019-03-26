package com.android.ux.ux.asynchronous;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.ux.ux.R;
import com.github.anrwatchdog.ANRError;
import com.github.anrwatchdog.ANRWatchDog;

public class ANRActivity extends Activity implements ANRWatchDog.ANRListener,
        ANRWatchDog.ANRInterceptor,
        ANRWatchDog.InterruptionListener {

    private Object mWaitObject;
    private ANRWatchDog mWatchDog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anr);
        mWaitObject = new Object();
        mWatchDog = new ANRWatchDog(1000);
        mWatchDog.setANRInterceptor(this);
        mWatchDog.setANRListener(this);
        mWatchDog.setInterruptionListener(this);
        mWatchDog.setReportMainThreadOnly();
        mWatchDog.setIgnoreDebugger(true);
        mWatchDog.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWatchDog.interrupt();
    }

    public void onANRClick(View view) throws InterruptedException {
        Log.d("anr", "ANRActivity.onANRClick(View view)");

        synchronized (mWaitObject) {
            try {
                Log.d("anr", "start waiting..");
                mWaitObject.wait();
                Log.d("anr", "end waiting!");
            } catch (InterruptedException e) {
                Log.e("anr", e.getMessage());
            } catch (Exception e) {
                Log.e("anr", e.getMessage());
            }
        }
    }

    @Override
    public long intercept(long duration) {
        Log.d("anr", "ANRActivity.intercept(long duration)");
        return 0;
    }

    @Override
    public void onAppNotResponding(ANRError error) {
        Log.d("anr", "ANRActivity.onAppNotResponding(ANRError error)");
        synchronized (mWaitObject) {
            try {
                mWaitObject.notifyAll();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ANRActivity.this, "ANR is relieved!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e("anr", e.getMessage());
            }
        }
    }

    @Override
    public void onInterrupted(InterruptedException exception) {
        Log.d("anr", "ANRActivity.onInterrupted(InterruptedException exception)");
    }
}

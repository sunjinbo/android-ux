package com.android.ux.ux;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "ux";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView iv = findViewById(R.id.iv_capture);
        final Button btn = findViewById(R.id.btn_generate_off_screen_bitmap);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_off_screen, null);
                        if (v == null) {
                            Log.d(TAG, "we can't inflate a view from xml resource.");
                        } else {
                            Log.d(TAG, "we can inflate a view from xml resource.");
                            final Bitmap bitmap = convertViewToBitmap(v, 500, 500);
                            saveBitmapToFile("test.jpg", bitmap);

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iv.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    //convert a view to bitmap
    public static Bitmap convertViewToBitmap(View view, int width, int height){
        view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
    }

    public static void saveBitmapToFile(String folderName, String fileName, Bitmap bitmap, boolean debug) {
        if (TextUtils.isEmpty(fileName) || bitmap == null) return;

        String outputPath = Environment.getExternalStorageDirectory() + File.separator + (debug ? "output_test" : "output");
        File outputFolder = new File(outputPath);
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        try {
            File fd = new File(outputFolder, folderName);
            if (!fd.exists()) {
                fd.mkdir();
            }

            File f = new File(fd, fileName);
            f.createNewFile();
            FileOutputStream fOut;
            fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveBitmapToFile(String fileName, Bitmap bitmap) {

        if (TextUtils.isEmpty(fileName) || bitmap == null) return;

        String folder = Environment.getExternalStorageDirectory() + File.separator + "output";
        File outputFolder = new File(folder);
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "output" + File.separator + fileName);
            f.createNewFile();
            FileOutputStream fOut;
            fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

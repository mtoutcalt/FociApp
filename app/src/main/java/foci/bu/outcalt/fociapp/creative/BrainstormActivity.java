package foci.bu.outcalt.fociapp.creative;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import foci.bu.outcalt.fociapp.R;

/**
 * Created by mark on 4/22/2017.
 */

public class BrainstormActivity extends Activity {

    private BrainstormCanvas customCanvas;
    String[] topics = {"Topic one", "Topic two"};
    int topicIndex = 0;
    Bitmap savedBitmap = null;
    private static final int PERMISSION_REQUEST_CODE = 1;
    int screenshotCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brainstorm_layout);
        setPermissions();
        customCanvas = (BrainstormCanvas) findViewById(R.id.signature_canvas);
    }

    private void setPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to WRITE EXTERNAL STORAGE - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }
    }

    public void clearCanvas(View v) {
        customCanvas.clearCanvas();
    }

    public void nextTopic(View v) {
        TextView topicText = (TextView) findViewById(R.id.topicTextView);
        if (topicIndex != (topics.length)) {
            String topicShow = topics[topicIndex];
            topicText.setText(topicShow);
            topicIndex++;
            customCanvas.clearCanvas();
        }
        else {
            topicText.setText("Go Again?  Press button again to start over.");
            topicIndex = 0;
            customCanvas.clearCanvas();
        }
    }

    public void screenShot(View mview) {
        FrameLayout view = (FrameLayout) findViewById(R.id.testit);
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        savedBitmap = bitmap;
        makeScreenshot(savedBitmap);
    }

    public void makeScreenshot(Bitmap bitmap) {
        String extr = Environment.getExternalStorageDirectory().toString();
        String fileName = "brainstormScreenshot" + getCurrentDateString() + ".jpg";
        Toast.makeText(this, "Creating file: " + fileName, Toast.LENGTH_LONG).show();
        File myPath = new File(extr, "brainstormScreenshot" + getCurrentDateString() + ".jpg");
        screenshotCounter++;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Screen", "screen");
            Toast.makeText(this, "Screenshot Saved", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrentDateString() {
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd-hh-mm-ss");
        return dt.format(new Date());
    }

}

package foci.bu.outcalt.fociapp.calm;


import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import foci.bu.outcalt.fociapp.R;

public class BreathActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breathe_animate);

        ImageView mImageViewFilling = (ImageView) findViewById(R.id.animate_breathe);
        ((AnimationDrawable) mImageViewFilling.getBackground()).start();

    }
}

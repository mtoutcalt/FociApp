package foci.bu.outcalt.fociapp.tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import foci.bu.outcalt.fociapp.R;


public class Tab2 extends Fragment {

    public Tab2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_layout_fragment2, container, false);
    }
}

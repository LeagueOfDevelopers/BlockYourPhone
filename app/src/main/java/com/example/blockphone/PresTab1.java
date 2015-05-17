package com.example.blockphone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Жамбыл on 16.05.2015.
 */
public class PresTab1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pres1, container, false);

        startLocalUI(v);

        return v;
    }
    void startLocalUI(View v) {

    }
}

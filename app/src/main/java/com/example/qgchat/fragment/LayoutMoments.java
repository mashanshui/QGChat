package com.example.qgchat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qgchat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LayoutMoments extends Fragment {
    private View rootView;

    public LayoutMoments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_layout_moments, container, false);
        return rootView;
    }

}

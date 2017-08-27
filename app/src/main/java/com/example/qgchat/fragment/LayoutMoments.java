package com.example.qgchat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qgchat.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class LayoutMoments extends Fragment {
    @BindView(R.id.momentsRecycleView)
    RecyclerView momentsRecycleView;
    Unbinder unbinder;
    private View rootView;

    public LayoutMoments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout_moments, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initAdapter();
        return rootView;
    }

    private void initAdapter() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

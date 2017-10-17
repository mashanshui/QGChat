package com.example.qgchat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qgchat.R;
import com.example.qgchat.adapter.MomentsRecycleAdapter;
import com.example.qgchat.bean.UserMoments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private List<UserMoments> userMomentsList = new ArrayList<>();
    private MomentsRecycleAdapter adapter;

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
        SimpleDateFormat format3 = new SimpleDateFormat("HH:mm");
        String time3 = format3.format(Calendar.getInstance().getTime());
        for (int i = 0; i < 10; i++) {
            UserMoments userMoments=new UserMoments("","马山水",time3,"空");
            userMomentsList.add(userMoments);
        }

        adapter = new MomentsRecycleAdapter(userMomentsList);
        momentsRecycleView.setAdapter(adapter);
        momentsRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

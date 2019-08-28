package com.example.rural_healthy_mom_to_be.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rural_healthy_mom_to_be.R;

public class WeightGraphFragment extends Fragment {
    View vWeightGraph;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vWeightGraph = inflater.inflate(R.layout.weighttrackerfragment, container, false);
        return vWeightGraph;
    }
}
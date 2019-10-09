package com.example.rural_healthy_mom_to_be.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rural_healthy_mom_to_be.R;

public class AddFoodInDiaryFragment extends Fragment {
    View vFoodAdd;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vFoodAdd = inflater.inflate(R.layout.add_a_meal, container, false);
        return vFoodAdd;

    }
}

package com.example.golf.ui.match;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.golf.MainActivity;
import com.example.golf.databinding.FragmentMatchBinding;

public class MatchFragment extends Fragment {
    private FragmentMatchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentMatchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
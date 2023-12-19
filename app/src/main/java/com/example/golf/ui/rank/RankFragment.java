package com.example.golf.ui.rank;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.golf.databinding.FragmentRankBinding;

public class RankFragment extends Fragment {

    private FragmentRankBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RankViewModel galleryViewModel =
                new ViewModelProvider(this).get(RankViewModel.class);

        binding = FragmentRankBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textRank;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
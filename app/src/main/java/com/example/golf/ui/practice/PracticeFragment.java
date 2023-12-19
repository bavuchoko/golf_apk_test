package com.example.golf.ui.practice;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.golf.MainActivity;
import com.example.golf.R;
import com.example.golf.api.ApiService;
import com.example.golf.api.RetrofitClient;
import com.example.golf.databinding.FragmentPracticeBinding;
import com.example.golf.dto.PagedResponse;
import com.example.golf.dto.PracticeGame;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PracticeFragment extends Fragment {
    ApiService api;
    private FragmentPracticeBinding binding;
    private ListView listView;
    private ArrayAdapter<PracticeGame> adapter;
    private List<PracticeGame> practiceGames;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPracticeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listView = root.findViewById(R.id.list_practice);

        practiceGames = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, practiceGames);

        // 어댑터를 ListView에 설정
        listView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPractices();
    }

    private void getPractices() {
        api = RetrofitClient.getRetrofit().create(ApiService.class);
        String startDate="2023-01-01T00:00:00";
        String endDate="2023-12-31T00:00:00";
        int page =0;
        int size=10;
        Call<PagedResponse<PracticeGame>> call = api.getPractices(startDate, endDate, page, size);
        call.enqueue(new Callback<PagedResponse<PracticeGame>>() {
            @Override
            public void onResponse(Call<PagedResponse<PracticeGame>> call, Response<PagedResponse<PracticeGame>> response) {
                if (response.isSuccessful()) {
                    practiceGames.clear();
                    PagedResponse<PracticeGame> pagedResponse = response.body();
                    // 리스트에 데이터를 추가하는 부분을 수정
                    practiceGames.addAll(pagedResponse.getEmbedded().getPractices());
                    adapter = new PracticeArrayAdapter(requireContext(), practiceGames);
                    listView.setAdapter(adapter);

                } else {
                    // 서버 응답이 실패한 경우 처리
                }
            }

            @Override
            public void onFailure(Call<PagedResponse<PracticeGame>> call, Throwable t) {
                // 네트워크 오류 등에 대한 처리
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
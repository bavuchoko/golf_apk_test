package com.example.golf.ui.login;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.golf.api.ApiService;
import com.example.golf.api.RetrofitClient;
import com.example.golf.common.CommonMethod;
import com.example.golf.common.KeyType;
import com.example.golf.databinding.FragmentLoginBinding;
import com.example.golf.dto.Account;
import com.example.golf.dto.AccountResponse;

import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    ApiService api;
    private FragmentLoginBinding binding;
    boolean constraint =false;
    EditText username;
    EditText password;
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        username= binding.userId;
        password= binding.passwd;
        TextView alert = binding.loginAlert;
        Button loginBtn = binding.btnLogin;
        // 아이디 입력 감지
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String username = charSequence.toString();
                if(!TextUtils.isDigitsOnly(username) || username.length() != 11){
                    alert.setText("아이디는 11자리 숫자입니다");
                    constraint = false;
                }else{
                    EditText use_password = binding.passwd;
                    if(use_password.getEditableText().length()<1){
                        alert.setText("비밀번호를 입력하세요");
                        constraint = false;
                    }else{
                        alert.setText("로그인 하세요");
                        constraint = true;
                    }
                }
                setLoginBtnColor(constraint);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String passwd = charSequence.toString();
                if(passwd.length() < 1){
                    alert.setText("비밀번호를 입력하세요");
                    constraint = false;
                }else{
                    EditText use_username = binding.userId;
                    if(!TextUtils.isDigitsOnly(use_username.getEditableText()) || use_username.getEditableText().length() != 11){
                        alert.setText("아이디는 11자리 숫자입니다");
                        constraint = false;
                    }else{
                        alert.setText("로그인 하세요");
                        constraint = true;
                    }
                }
                setLoginBtnColor(constraint);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethod.showLoading(requireContext());
                if (constraint) {
                    Account account= new Account();
                    account.setUsername(username.getText().toString());
                    account.setPassword(password.getText().toString());
                    api = RetrofitClient.getRetrofit().create(ApiService.class);
                    api.login(account).enqueue(new Callback<AccountResponse>() {
                        @Override
                        public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {

                            CommonMethod.hideLoading();

                            if (response.isSuccessful()) {
                                Headers headers = response.headers();
                                List<String> cookies = headers.values("Set-Cookie");
                                for (String cookie : cookies) {
                                    if (cookie.contains("refreshToken")) {
                                        String[] cookieParts = cookie.split(";");
                                        for (String cookiePart : cookieParts) {
                                            if (cookiePart.contains("refreshToken")) {
                                                String[] keyValue = cookiePart.split("=");
                                                String refreshToken = keyValue.length > 1 ? keyValue[1] : "";
                                                CommonMethod.saveRefreshToken(requireContext(), refreshToken);
                                            }
                                        }
                                    }
                                }

                                AccountResponse authenticationResponse = response.body();
                                CommonMethod.saveAccessToken(requireContext(),  authenticationResponse.getAccessToken());
                                CommonMethod.saveInfotoStorage(requireContext(), KeyType.NAME.getValue(), authenticationResponse.getName());
                                CommonMethod.saveInfotoStorage(requireContext(), KeyType.BIRTH.getValue(), authenticationResponse.getBirth());
                                CommonMethod.saveInfotoStorage(requireContext(), KeyType.JOIN_DATE.getValue(), authenticationResponse.getJoinDate());


                                requireActivity().getSupportFragmentManager().popBackStack();
                            }else{
                                CommonMethod.logout(requireContext());
                            }
                        }

                        //로그인 실패시 저장소에 토큰이 있으면 삭제
                        @Override
                        public void onFailure(Call<AccountResponse> call, Throwable t) {
                            CommonMethod.hideLoading();
                            CommonMethod.logout(requireContext());
                            CommonMethod.showAlert(requireContext(),"로그인에 실패하였습니다.");
                        }
                    });
                } else {
                    CommonMethod.showAlert(requireContext(),"로그인 정보를 입력하세요");
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setLoginBtnColor(Boolean bool) {
        Button loginBtn = binding.btnLogin;
        if(bool){
            loginBtn.setBackgroundColor(Color.parseColor("#263177"));
        }else{
            loginBtn.setBackgroundColor(Color.parseColor("#7E7E7E"));
        }

    }


}

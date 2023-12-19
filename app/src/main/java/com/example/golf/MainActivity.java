package com.example.golf;

import static com.example.golf.common.CommonMethod.clearValueFromSharedPreferences;
import static com.example.golf.common.CommonMethod.getAccessToken;
import static com.example.golf.common.CommonMethod.getInfoFromStorage;
import static com.example.golf.common.CommonMethod.getInfoFromToken;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.golf.common.CommonMethod;
import com.example.golf.common.KeyType;
import com.example.golf.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private String accessToken;
    LinearLayout beforeLogin;
    LinearLayout afterLogin;
    TextView goToLogin;
    TextView userName;
    ImageView rightArrow;

    TextView join;
    TextView logout;

    Button match;
    Button practice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_rank, R.id.nav_practice, R.id.nav_login)
                .setOpenableLayout(drawer)
                .build();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_clear_all_24);

        View headerView = navigationView.getHeaderView(0);
        ImageButton tvNaviClose = headerView.findViewById(R.id.tv_navi_close);

        //네이게이션 헤더의 close버튼
        tvNaviClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
            }
        });



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupWithNavController(navigationView, navController);

        goToLogin = headerView.findViewById(R.id.login_btn);
        userName = headerView.findViewById(R.id.user_name);
        rightArrow = headerView.findViewById(R.id.right_arrow);
        join = headerView.findViewById(R.id.join);
        logout = headerView.findViewById(R.id.logout);
        beforeLogin = headerView.findViewById(R.id.before_login);
        afterLogin = headerView.findViewById(R.id.after_login);
        match = findViewById(R.id.btn_match);
        practice = findViewById(R.id.btn_practice);

        match.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                navController.navigate(R.id.nav_match);
            }
        });

        practice.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                navController.navigate(R.id.nav_practice);
            }
        });
        goToLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                navController.navigate(R.id.nav_login);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethod.logout(MainActivity.this);
            }
        });

    }



    @Override
    public boolean onSupportNavigateUp() {
        accessToken = getAccessToken(this);
        if (accessToken != null) {
            String username = getInfoFromStorage(this, KeyType.NAME.getValue());
            beforeLogin.setVisibility(View.INVISIBLE);
            afterLogin.setVisibility(View.VISIBLE);
            userName.setText(username);
        }
        DrawerLayout drawer = binding.drawerLayout;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }

        return true;
    }


    public void openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START);
    }
}
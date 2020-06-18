package com.example.proyectoclauz.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.proyectoclauz.Fragments.HomeFragment;
import com.example.proyectoclauz.Fragments.InventoryFragment;
import com.example.proyectoclauz.Fragments.UserFragment;
import com.example.proyectoclauz.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectFragment= new HomeFragment();

                    break;
                case R.id.navigation_dashboard:
                    selectFragment= new InventoryFragment();

                    break;
                case R.id.navigation_notifications:
                    selectFragment = new UserFragment();

                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.relative_layout_fragments,selectFragment).commit();
            return true;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        Fragment fragment_inicio= new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.relative_layout_fragments,fragment_inicio).commit();
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

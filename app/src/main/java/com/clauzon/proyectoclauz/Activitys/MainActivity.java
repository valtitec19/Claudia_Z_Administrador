package com.clauzon.proyectoclauz.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.clauzon.proyectoclauz.Fragments.HomeFragment;
import com.clauzon.proyectoclauz.Fragments.InventoryFragment;
import com.clauzon.proyectoclauz.Fragments.UserFragment;
import com.clauzon.proyectoclauz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Random;
import java.util.UUID;

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
        //recuperar_token_dispositivo();
        FirebaseMessaging.getInstance().subscribeToTopic("domicilio").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.e("TAG", "Suscrito" );
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void recuperar_token_dispositivo() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }
                        String value = UUID.randomUUID().toString();
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("token_admin");
                        reference.child(value).setValue(token);

                    }
                });

    }

}

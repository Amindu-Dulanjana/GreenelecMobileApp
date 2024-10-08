package lk.ads.app.greenelec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import lk.ads.app.greenelec.model.BroadcastReceiver;
import lk.ads.app.greenelec.model.User;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar materialToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        IntentFilter intentFilter = new IntentFilter("android.intent.action.BATTERY_LOW");
        BroadcastReceiver mbr = new BroadcastReceiver();
        registerReceiver(mbr, intentFilter);

        drawerLayout = findViewById(R.id.home_drawerLayout);
        navigationView = findViewById(R.id.side_Navigation);
        materialToolbar = findViewById(R.id.materialToolbar);

        setSupportActionBar(materialToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("data",Context.MODE_PRIVATE);
                Menu menu = navigationView.getMenu();
                if (preferences.contains("email")){
                    MenuItem profileItem = menu.findItem(R.id.sideNavProfile);
                    profileItem.setVisible(true);
                    MenuItem favoriteItem = menu.findItem(R.id.sideNavFavorite);
                    favoriteItem.setVisible(true);
                    MenuItem messageItem = menu.findItem(R.id.sideNavMessage);
                    messageItem.setVisible(true);
                    MenuItem settingsItem = menu.findItem(R.id.sideNavSettings);
                    settingsItem.setVisible(true);
                    MenuItem logoutItem = menu.findItem(R.id.sideNavLogout);
                    logoutItem.setVisible(true);
                    MenuItem loginItem = menu.findItem(R.id.sideNavLogin);
                    loginItem.setVisible(false);
                }else {
                    MenuItem profileItem = menu.findItem(R.id.sideNavProfile);
                    profileItem.setVisible(false);
                    MenuItem favoriteItem = menu.findItem(R.id.sideNavFavorite);
                    favoriteItem.setVisible(false);
                    MenuItem messageItem = menu.findItem(R.id.sideNavMessage);
                    messageItem.setVisible(false);
                    MenuItem settingsItem = menu.findItem(R.id.sideNavSettings);
                    settingsItem.setVisible(false);
                    MenuItem logoutItem = menu.findItem(R.id.sideNavLogout);
                    logoutItem.setVisible(false);
                    MenuItem loginItem = menu.findItem(R.id.sideNavLogin);
                    loginItem.setVisible(true);
                }
                drawerLayout.open();
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sideNavHome){
            FragmentLoad(new HomeFragment());
            drawerLayout.close();
        } else if (id == R.id.sideNavProfile) {
            FragmentLoad(new ProfileFragment());
            drawerLayout.close();
        } else if (id == R.id.sideNavLogin){
            Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
            startActivity(intent);
            drawerLayout.close();
        } else if (id == R.id.sideNavLogout){
            SharedPreferences preferences = getSharedPreferences("data",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.sideNavHelp) {
            FragmentLoad(new AboutUsFragment());
            drawerLayout.close();
        } else if (id == R.id.sideNavCart) {
            Intent intent = new Intent(HomeActivity.this,CartActivity.class);
            startActivity(intent);
            drawerLayout.close();
        }
        return true;
    }

    public void FragmentLoad(Fragment fragment){
        FragmentManager supFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }
}
package com.kyluandkylu.android.logiword.NavigationHandler;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.kyluandkylu.android.logiword.FriendList.FriendListFragment;
import com.kyluandkylu.android.logiword.GlobalScore.ScoreFragment;
import com.kyluandkylu.android.logiword.LoadingScreen.LoadingScreenFragment;
import com.kyluandkylu.android.logiword.LocalScore.LocalScoreFragment;
import com.kyluandkylu.android.logiword.MainMenu.MainMenu;
import com.kyluandkylu.android.logiword.Profile.ProfileFragment;
import com.kyluandkylu.android.logiword.R;
import com.kyluandkylu.android.logiword.Settings.SettingsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainMenu()).commit();
            navigationView.setCheckedItem(R.id.nav_menu);
        }

        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoadingScreenFragment(getApplication(), getSupportActionBar())).commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                toolbar.setTitle(R.string.profileString);
                break;
            case R.id.nav_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainMenu()).commit();
                toolbar.setTitle(R.string.mainMenuString);
                break;
            case R.id.nav_scores:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScoreFragment()).commit();
                toolbar.setTitle(R.string.scoresString);
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                toolbar.setTitle(R.string.settingsString);
                break;
            case R.id.nav_friend_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FriendListFragment()).commit();
                toolbar.setTitle(R.string.friendListString);
                break;
            case R.id.nav_local_scores:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LocalScoreFragment()).commit();
                toolbar.setTitle(R.string.localScoresString);
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
}

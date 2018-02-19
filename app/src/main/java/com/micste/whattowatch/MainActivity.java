package com.micste.whattowatch;

import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.micste.whattowatch.fragments.DiscoverFragment;
import com.micste.whattowatch.fragments.FinderFragment;
import com.micste.whattowatch.fragments.HomeFragment;
import com.micste.whattowatch.fragments.WatchListFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView = findViewById(R.id.nvView);
        fragmentManager = getSupportFragmentManager();

        setupDrawerContent(navigationView);

        if (savedInstanceState == null) {
            Fragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.mainContent, homeFragment).commit();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }

                });
    }

    public void selectDrawerItem(MenuItem menuItem) {

        Fragment fragment = null;
        Class fragmentClass;

        switch(menuItem.getItemId()) {
            case R.id.nav_home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_discover:
                fragmentClass = DiscoverFragment.class;
                break;
            case R.id.nav_mylist:
                fragmentClass = WatchListFragment.class;
                break;
            default:
                fragmentClass = HomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();

        } catch (Exception e) {

            e.printStackTrace();

        }

        fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();

    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }
}

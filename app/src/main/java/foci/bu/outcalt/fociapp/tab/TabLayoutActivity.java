package foci.bu.outcalt.fociapp.tab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import java.net.URL;

import foci.bu.outcalt.fociapp.R;
import foci.bu.outcalt.fociapp.calm.BreatheActivity;
import foci.bu.outcalt.fociapp.habit.HabitActivity;
import foci.bu.outcalt.fociapp.home.HomeActivity;
import foci.bu.outcalt.fociapp.timer.TimerActivity;
import foci.bu.outcalt.fociapp.todo.ToDoActivity;

public class TabLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        //add the tabs to layout
        tabLayout.addTab(tabLayout.newTab().setText("Pomodoro"));
        tabLayout.addTab(tabLayout.newTab().setText("Break the Chain"));
        tabLayout.addTab(tabLayout.newTab().setText("Risk-Averse Motivation"));
        tabLayout.addTab(tabLayout.newTab().setText("About"));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        //send layout to tab adapter
        final PagerAdapter adapter = new TabPagerAdapter (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                                               @Override
                                               public void onTabSelected(TabLayout.Tab tab) {
                                                   viewPager.setCurrentItem(tab.getPosition());
                                               }

                                               @Override
                                               public void onTabUnselected(TabLayout.Tab tab) {
                                               }

                                               @Override
                                               public void onTabReselected(TabLayout.Tab tab) {
                                               }
                                           });
    }

    public void showWebPageTab1(View view) {
        //Pomodoro
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cirillocompany.de/pages/pomodoro-technique"));
        startActivity(intent);
    }

    public void showWebPageTab2(View view) {
        //Don't Break the Chain
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://lifehacker.com/5886128/how-seinfelds-productivity-secret-fixed-my-procrastination-problem"));
        startActivity(intent);
    }

    public void showWebPageTab3(View view) {
        // risk-averse motivation
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://nonprofitquarterly.org/2013/02/01/the-anti-charity-system-first-choose-a-cause-you-despise/"));
        startActivity(intent);
    }

    public void showWebPageTab4(View view) {
        //about me
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mtoutcalt/FociApp"));
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.foci_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_home:
                intent = new Intent(this, HomeActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_todo:
                intent = new Intent(this, ToDoActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_habit:
                intent = new Intent(this, HabitActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_timer:
                intent = new Intent(this, TimerActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_breathe:
                intent = new Intent(this, BreatheActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_info:
                intent = new Intent(this, TabLayoutActivity.class);
                this.startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }


}

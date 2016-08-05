package cn.crepusculo.subway_ticket_android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.ui.adapter.PagerAdapter;
import cn.crepusculo.subway_ticket_android.ui.adapter.PositionTranslationAdapter;

public class TicketManagerActivity extends BaseActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_ticket;
    }

    @Override
    protected void initView() {
//        if(Build.VERSION.SDK_INT > 21){
//            getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
//            getWindow()
//                    .getDecorView()
//                    .setSystemUiVisibility(
//                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }
        initToolbar();
        initTabLayout();
        initViewPager();
        checkPagePosition();
    }


    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    protected void initTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.ticket_tab1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.ticket_tab2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.ticket_tab3));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    protected void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("ViewPager", "onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    protected void checkPagePosition(){
        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        int position = bundle.getInt("TYPE",0);
        /* Correspond id type */
        position = PositionTranslationAdapter.DrawerIdtoTablayoutId(position);
        Log.e("Bundle in Ticket",""+position);

        tabLayout.setScrollPosition(position,0f,true);
        viewPager.setCurrentItem(position);
    }


}

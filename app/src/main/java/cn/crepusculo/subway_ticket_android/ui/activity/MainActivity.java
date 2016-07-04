package cn.crepusculo.subway_ticket_android.ui.activity;

import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import cn.crepusculo.subway_ticket_android.R;

public class MainActivity extends cn.crepusculo.subway_ticket_android.ui.activity.BaseActivity
                                implements Drawer.OnDrawerItemClickListener {
    private class SideNavBtn {
        public final static int GET_QR = 0;
        public final static int BILLS = 1;
        public final static int PROFILE = 2;
        public final static int SETTINGS = 3;
        public final static int EXIT = 9;
    }
    private View view;

    private Drawer drawer;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        view = this.findViewById(R.id.main_activity);

        initToolbar();
        initFab();
        initDrawer();
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initFab(){

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

    }

    private void initDrawer(){
        /* get side menu resource */
        String[] drawerItemsNames = getResources().getStringArray(R.array.drawer_items);
        int[] drawerItemIcons = {
                R.drawable.ic_find_in_page_white_24dp,
                R.drawable.ic_find_in_page_white_24dp,
                R.drawable.ic_find_in_page_white_24dp,
                R.drawable.ic_find_in_page_white_24dp,
                R.drawable.ic_find_in_page_white_24dp
        };

        /* init side menu concept */
        PrimaryDrawerItem[] primaryDrawerItems = {
                new PrimaryDrawerItem()
                        .withName(drawerItemsNames[SideNavBtn.GET_QR])
                        .withIcon(drawerItemIcons[SideNavBtn.GET_QR])
                        .withIdentifier(SideNavBtn.GET_QR),

                new PrimaryDrawerItem()
                        .withName(drawerItemsNames[SideNavBtn.BILLS])
                        .withIcon(drawerItemIcons[SideNavBtn.BILLS])
                        .withIdentifier(SideNavBtn.BILLS),

                new PrimaryDrawerItem()
                        .withName(drawerItemsNames[SideNavBtn.PROFILE])
                        .withIcon(drawerItemIcons[SideNavBtn.PROFILE])
                        .withIdentifier(SideNavBtn.PROFILE),

                new PrimaryDrawerItem()
                        .withName(drawerItemsNames[SideNavBtn.SETTINGS])
                        .withIcon(drawerItemIcons[SideNavBtn.SETTINGS])
                        .withIdentifier(SideNavBtn.SETTINGS)
        };


        /* build side Menu */
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.header_drawer)
                .withTranslucentNavigationBar(false)
                .withCloseOnClick(true)
                .withActionBarDrawerToggle(false)
                .withSelectedItem(-1)
                .withDrawerGravity(Gravity.START)
                .addDrawerItems(
                        primaryDrawerItems[SideNavBtn.GET_QR],
                        primaryDrawerItems[SideNavBtn.BILLS],
                        new DividerDrawerItem(),
                        primaryDrawerItems[SideNavBtn.PROFILE],
                        primaryDrawerItems[SideNavBtn.SETTINGS]
                )
                .addStickyDrawerItems(new PrimaryDrawerItem()
                                        .withName(R.string.common_exit)
                                        .withSelectable(true))
                .withOnDrawerItemClickListener(this)
                .withCloseOnClick(true)
                .build();
    }
    // --------------------------------- listener --------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_locate:
                return true;
            case R.id.action_search:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        long id = drawerItem.getIdentifier();
        if (id == SideNavBtn.GET_QR) {

            drawer.closeDrawer();
        } else if (id == SideNavBtn.BILLS) {

            drawer.closeDrawer();
        } else if (id == SideNavBtn.PROFILE) {

            drawer.closeDrawer();
        } else if (id == SideNavBtn.SETTINGS) {

            drawer.closeDrawer();
        } else if (id == SideNavBtn.EXIT) {
            drawer.closeDrawer();
            finish();
        }
        return true;
    }

}



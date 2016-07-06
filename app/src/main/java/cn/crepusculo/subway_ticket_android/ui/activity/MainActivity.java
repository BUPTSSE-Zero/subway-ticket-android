package cn.crepusculo.subway_ticket_android.ui.activity;

import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.application.MyApplication;
import cn.crepusculo.subway_ticket_android.preferences.Info;

public class MainActivity extends cn.crepusculo.subway_ticket_android.ui.activity.BaseActivity
                    implements Drawer.OnDrawerItemClickListener,
                               com.getbase.floatingactionbutton.FloatingActionButton.OnClickListener{
    private class SideNavBtn {
        public final static int GET_QR = 0;
        public final static int BILLS = 1;
        public final static int CITYS = 2;
        public final static int PROFILE = 3;
        public final static int SETTINGS = 4;
        public final static int EXIT = 9;

    }
    private View view;

    /* Info */
    private Info info;

    /* nav */
    private View nav;

    /* Side Menu */
    private Drawer drawer;

    /* Fabs */
    private com.github.jorgecastilloprz.FABProgressCircle fabProgressCircle;
    private com.getbase.floatingactionbutton.FloatingActionButton fab_settings;
    private com.getbase.floatingactionbutton.FloatingActionButton fab_unfoucs;
    private com.getbase.floatingactionbutton.FloatingActionButton fab_locate;
    private com.getbase.floatingactionbutton.FloatingActionsMenu fab_menu;
    private com.getbase.floatingactionbutton.FloatingActionButton fab_subway;
    private com.getbase.floatingactionbutton.FloatingActionButton fab_bills;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        view = this.findViewById(R.id.main_activity);
        initInfo();
        initToolbar();
        initFab();
        initDrawer();
    }


    private void initInfo(){
        info = Info.getInstance();
        info.initTest();
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initFab(){
        fab_unfoucs = (FloatingActionButton)
                findViewById(R.id.action_unfoucs);

        fabProgressCircle = (FABProgressCircle)
                findViewById(R.id.fab_progress_circle);

        fab_settings = (com.getbase.floatingactionbutton.FloatingActionButton)
                findViewById(R.id.action_settings);

        fab_menu = (com.getbase.floatingactionbutton.FloatingActionsMenu)
                findViewById(R.id.multiple_actions);

        fab_subway = (com.getbase.floatingactionbutton.FloatingActionButton)
                findViewById(R.id.action_subway);

        fab_locate = (com.getbase.floatingactionbutton.FloatingActionButton)
                findViewById(R.id.action_locate);

        fab_bills = (com.getbase.floatingactionbutton.FloatingActionButton)
                findViewById(R.id.action_bills);

        fab_menu.removeButton(fab_bills);
        showHint(fab_bills, fab_menu);

        /* fab menu listener register */
        fab_bills.setOnClickListener(this);
        fab_subway.setOnClickListener(this);
        fab_locate.setOnClickListener(this);
        /* fab listener register */
        fab_settings.setOnClickListener(this);

    }

    private void initDrawer(){
        /* get side menu resource */
        String[] drawerItemsNames = getResources().getStringArray(R.array.drawer_items);
        int[] drawerItemIcons = {
                R.drawable.ic_find_in_page_white_24dp,
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
                        .withName(drawerItemsNames[SideNavBtn.CITYS])
                        .withIcon(drawerItemIcons[SideNavBtn.CITYS])
                        .withIdentifier(SideNavBtn.CITYS),

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
                        primaryDrawerItems[SideNavBtn.CITYS],
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
            fab_bills.setVisibility(View.INVISIBLE);
            drawer.closeDrawer();
        } else if (id == SideNavBtn.BILLS) {
            fab_bills.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View view) {
        fab_bills.setVisibility(View.VISIBLE);
        int id = view.getId();
        Log.e("id",""+id);
        switch (id) {
            case R.id.action_bills:
                fab_menu.collapse();
                info.ticket.setTicketsCode(null);
                Log.e("At onClick",""+info.ticket.getCount());
                showHint(fab_bills,fab_menu);
                return;
            case R.id.action_subway:
                fab_menu.collapse();
                return;
            case R.id.action_locate:
                fab_menu.collapse();
                return;
            case R.id.action_settings:
                drawer.openDrawer();
        }

    }

    private void showHint(com.getbase.floatingactionbutton.FloatingActionButton fab,
                          com.getbase.floatingactionbutton.FloatingActionsMenu fab_menu){
        Log.e("At shouHint",""+info.ticket.getCount());
        if (info.ticket.getCount() > 0){
            fab_menu.addButton(fab);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fabProgressCircle.show();
                }
            },2000);
        }
        else {
                fabProgressCircle.beginFinalAnimation();
                fab_menu.removeButton(fab_bills);
            }
    }

}



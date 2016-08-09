package cn.crepusculo.subway_ticket_android.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bm.library.PhotoView;
import com.google.gson.Gson;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.DimEffect;
import com.mingle.sweetpick.SweetSheet;
import com.subwayticket.database.model.StationMessage;

import java.util.ArrayList;
import java.util.List;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.Station;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.ui.activity.login.LoginActivity;
import cn.crepusculo.subway_ticket_android.ui.activity.settings.ApplicationSettings;
import cn.crepusculo.subway_ticket_android.ui.activity.settings.PersonalSettings;
import cn.crepusculo.subway_ticket_android.util.CircularAnimUtil;
import cn.crepusculo.subway_ticket_android.util.SubwayLineUtil;
import cn.crepusculo.subway_ticket_android.util.TestUtils;

public class MainActivity extends cn.crepusculo.subway_ticket_android.ui.activity.BaseActivity
        implements Drawer.OnDrawerItemClickListener,
        com.getbase.floatingactionbutton.FloatingActionButton.OnClickListener, TextWatcher {
    /* Static constant */
    /* Sheet */
    private SweetSheet sheet;
    /* Storage */
    private Station startStation;
    private Station endStation;
    /* view */
    private View view;
    private ViewGroup viewGroup;
    /* Info */
    private Info info;
    /* nav */
    private View nav;
    /* Side Menu */
    private Drawer drawer;
    private PrimaryDrawerItem[] primaryDrawerItems;
    /* Fabs */
    private com.getbase.floatingactionbutton.FloatingActionButton fab_settings;
    private android.support.design.widget.FloatingActionButton fab_submitorder;
    /* EditText */
    private ImageButton editTextBtn;
    private EditText editTextStart;
    private EditText editTextEnd;
    private ImageView imageStart, imageEnd;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        /**
         * If android version > 4.4 set statusBarColor programmatically
         */
        view = this.findViewById(R.id.main_activity);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT > 21) {
            window.setStatusBarColor(getResources().getColor(R.color.primary));
        }
        /**
         * Init compacts
         */
        initBackGround();
        initInfo();
        initToolbar();
        initFab();
        initDrawer();
        initSearchEditTextView();
        initSheet();
    }

    private void initBackGround() {
        PhotoView map = (PhotoView) findViewById(R.id.map);
        map.enable();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    private void initInfo() {
        info = Info.getInstance();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Init all fab here including fab menu
     */
    private void initFab() {
        /**
         * Bind compacts
         */
        fab_submitorder = (android.support.design.widget.FloatingActionButton)
                findViewById(R.id.action_submit_order);

        fab_settings = (com.getbase.floatingactionbutton.FloatingActionButton)
                findViewById(R.id.action_settings);

        /**
         * fab listener register
         *
         */
        fab_settings.setOnClickListener(this);
        fab_submitorder.setOnClickListener(this);
    }

    private void initDrawer() {
        /**
         *  get side menu resource
         **/
        String[] drawerItemsNames = getResources().getStringArray(R.array.drawer_items);
        int[] drawerItemIcons = {
                R.drawable.ic_ticket_light_24dp,
                R.drawable.ic_find_in_page_light_24dp,
                R.drawable.ic_settings_light_24dp,
                R.drawable.ic_info_outline,
                R.drawable.ic_exit,
        };

        /* init side menu concept */
        primaryDrawerItems = new PrimaryDrawerItem[]{
                new PrimaryDrawerItem()
                        .withSelectable(false)
                        .withName(drawerItemsNames[SideNavBtn.BILLS])
                        .withTextColorRes(R.color.light_primary_text)
                        .withIconColorRes(R.color.light_primary_text)
                        .withIcon(drawerItemIcons[SideNavBtn.BILLS])
                        .withIdentifier(SideNavBtn.BILLS),

                new PrimaryDrawerItem()
                        .withSelectable(false)
                        .withName(drawerItemsNames[SideNavBtn.CITES])
                        .withIconColorRes(R.color.light_primary_text)
                        .withTextColorRes(R.color.light_primary_text)
                        .withIcon(drawerItemIcons[SideNavBtn.CITES])
                        .withIdentifier(SideNavBtn.CITES),

                new PrimaryDrawerItem()
                        .withSelectable(false)
                        .withName(drawerItemsNames[SideNavBtn.PROFILE])
                        .withTextColorRes(R.color.light_primary_text)
                        .withIconColorRes(R.color.light_primary_text)
                        .withIcon(drawerItemIcons[SideNavBtn.PROFILE])
                        .withIdentifier(SideNavBtn.PROFILE),

                new PrimaryDrawerItem()
                        .withSelectable(false)
                        .withName(drawerItemsNames[SideNavBtn.ABOUT_ME])
                        .withTextColorRes(R.color.light_primary_text)
                        .withIconColorRes(R.color.light_primary_text)
                        .withIcon(drawerItemIcons[SideNavBtn.ABOUT_ME])
                        .withIdentifier(SideNavBtn.ABOUT_ME),

                new PrimaryDrawerItem()
                        .withSelectable(false)
                        .withName(drawerItemsNames[SideNavBtn.EXIT])
                        .withTextColorRes(R.color.light_primary_text)
                        .withIconColorRes(R.color.light_primary_text)
                        .withIcon(drawerItemIcons[SideNavBtn.EXIT])
                        .withIdentifier(SideNavBtn.EXIT)
        };

        for (PrimaryDrawerItem item : primaryDrawerItems
                ) {
            item.withTextColor(getResources().getColor(R.color.light_primary_text));
        }

        /* build side Menu */
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.nav_header_main)
                .withTranslucentNavigationBar(true)
                .withDisplayBelowStatusBar(false)
                .withCloseOnClick(true)
                .withTranslucentNavigationBar(true)
                .withActionBarDrawerToggle(false)
                .withSelectedItem(-1)
                .withDrawerGravity(Gravity.START)
                .addDrawerItems(
                        primaryDrawerItems[SideNavBtn.CITES],
                        new DividerDrawerItem(),
                        primaryDrawerItems[SideNavBtn.ABOUT_ME],
                        primaryDrawerItems[SideNavBtn.EXIT]
                )
                .withOnDrawerItemClickListener(this)
                .withCloseOnClick(true)
                .build();

        drawer.getHeader().findViewById(R.id.nav_header_portrait_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(info.getToken() == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    protected void initSearchEditTextView() {
        /**
         * Bind compacts
         */
        imageStart = (ImageView) findViewById(R.id.edittext_drawable_come);
        imageEnd = (ImageView) findViewById(R.id.edittext_drawable_go);
        editTextStart = (EditText) findViewById(R.id.edittext_come);
        editTextEnd = (EditText) findViewById(R.id.edittext_go);
        editTextBtn = (ImageButton) findViewById(R.id.edittext_btn);

        editTextStart.setLongClickable(false);
        editTextEnd.setLongClickable(false);
        editTextBtn.setBackgroundColor(getResources().getColor(R.color.transparent));

        /**
         * Set listener
         */

        imageStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStation = null;
                setEditText(editTextStart, null);
                updateEditTextDrawable();
            }
        });

        imageEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endStation = null;
                setEditText(editTextEnd, null);
                updateEditTextDrawable();
            }
        });

        editTextStart.addTextChangedListener(this);

        editTextStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearch(SearchActivity.EDIT_TEXT_REQUEST_CODE_START);
            }
        });
        editTextEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearch(SearchActivity.EDIT_TEXT_REQUEST_CODE_END);
            }
        });

        editTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (endStation != null && startStation != null) {
                    Station swap = startStation;
                    startStation = endStation;
                    endStation = swap;

                } else if (endStation == null && startStation != null) {
                    endStation = startStation;
                    startStation = null;
                } else if (endStation != null) {
                    startStation = endStation;
                    endStation = null;
                }
                setEditText(editTextStart, startStation);
                setEditText(editTextEnd, endStation);
            }
        });
    }

    private void changeDrawer(){
        TextView userIdView = (TextView)drawer.getHeader().findViewById(R.id.nav_header_userid_main);
        if(info.getToken() != null){
            userIdView.setText(info.user.getId());
            if(drawer.getDrawerItem(SideNavBtn.PROFILE) == null)
                drawer.addItemAtPosition(primaryDrawerItems[SideNavBtn.PROFILE], 0);
            if(drawer.getDrawerItem(SideNavBtn.BILLS) == null)
                drawer.addItemAtPosition(primaryDrawerItems[SideNavBtn.BILLS], 0);
        }else{
            userIdView.setText(R.string.click_avatar_to_login);
            drawer.removeItem(SideNavBtn.BILLS);
            drawer.removeItem(SideNavBtn.PROFILE);
        }
    }

    // --------------------------------- listener --------------------------------------------------


    @Override
    protected void onResume() {
        super.onResume();
        changeDrawer();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            this.finishApp();
        }
    }

    /* ---- Text Change Listener Start ---- */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        updateEditTextDrawable();
    }

    @Override
    public void afterTextChanged(Editable editable) {
        updateEditTextDrawable();
    }
    /* ---- Text Change Listener End ---- */

    /**
     * @param requestCode requestCode send by MainActivity
     *                    has 2 status:
     *                    EDIT_TEXT_REQUEST_CODE_START
     *                    EDIT_TEXT_REQUEST_CODE_END
     * @param resultCode  resultCode return by searchActivity
     *                    has 4 status:
     *                    EDIT_TEXT_REQUEST_CODE_START
     *                    data   KEY_Start : √         KEY_end: ×
     *                    <p/>
     *                    EDIT_TEXT_REQUEST_CODE_END
     *                    data   KEY_Start : ×         KEY_end: √
     *                    <p/>
     *                    EDIT_TEXT_REQUEST_CODE_BOTH
     *                    data   KEY_Start : √         KEY_end: √
     *                    <p/>
     *                    EDIT_TEXT_REQUEST_CODE_EMPTY
     *                    data   KEY_Start : ×         KEY_end: ×
     *
     * @param data        Intent data back from SearchActivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SearchActivity.EDIT_TEXT_REQUEST_CODE_START) {
            // Only write start_edit_text
            Log.e("MainActivity", "GetResult" + "START");
            String obj_str = data.getStringExtra(
                    SearchActivity.KEY_START);
            startStation = new Gson().fromJson(obj_str, Station.class);
            setEditText(editTextStart, startStation);
        } else if (resultCode == SearchActivity.EDIT_TEXT_REQUEST_CODE_END) {
            // Only write end_edit_text
            Log.e("MainActivity", "GetResult" + "END");
            String obj_str = data.getStringExtra(
                    SearchActivity.KEY_END);
            endStation = new Gson().fromJson(obj_str, Station.class);
            setEditText(editTextEnd, endStation);
        } else if (resultCode == SearchActivity.EDIT_TEXT_REQUEST_CODE_BOTH) {
            // Both write two edit_text
            String obj_str_e = data.getStringExtra(
                    SearchActivity.KEY_END);
            String obj_str_s = data.getStringExtra(
                    SearchActivity.KEY_START);
            endStation = new Gson().fromJson(obj_str_e, Station.class);
            startStation = new Gson().fromJson(obj_str_s, Station.class);

            setEditText(editTextStart, startStation);
            setEditText(editTextEnd, endStation);

        } else if (resultCode == SearchActivity.EDIT_TEXT_REQUEST_CODE_EMPTY) {
            // None of edit_text will be rewrite
        } else {
            Log.e("MainActivity/Result", "No Result Receive");
        }
        updateEditTextDrawable();
    }

    /**
     * Drawable Side Menu ClickListener
     *
     * @param view        View
     * @param position   Count from 0 , position in side menu
     * @param drawerItem DrawerItem
     * @return Keep true
     */
    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        long id = drawerItem.getIdentifier();
        /**
         * Jump to Ticket Message Manager 'Pay'
         */
        if (id == SideNavBtn.BILLS) {
            Bundle bundle = new Bundle();
            bundle.putInt("TYPE", 0);
            jumpToActivity(TicketManagerActivity.class, bundle);
            drawer.closeDrawer();
        }
        /**
         * Show Cites choose dialog
         */
        else if (id == SideNavBtn.CITES) {
            showCityDialog();
            drawer.closeDrawer();
        }
        /**
         * Show profile
         */
        else if (id == SideNavBtn.PROFILE) {
            showProfileView();
            drawer.closeDrawer();
        }
        /**
         * Show about me
         */
        else if (id == SideNavBtn.ABOUT_ME) {
            showSettingsView();
            drawer.closeDrawer();
        }
        else if (id == SideNavBtn.EXIT) {
            finishApp();
            drawer.closeDrawer();
        }
        return true;
    }

    /**
     * FAB
     * @param view Float Action Button and Float Action Button in Float Action Menu
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        Log.e("id", "" + id);
        switch (id) {
            case R.id.action_settings:
                drawer.openDrawer();
                return;
            case R.id.action_submit_order:
                List<String> route = new ArrayList<>();
                Bundle b = new Bundle();
                b.putString("route_start", new Gson().toJson(startStation));
                b.putString("route_end", new Gson().toJson(endStation));
                Intent intent = new Intent(MainActivity.this, SubmitActivity.class);
                intent.putExtras(b);
                CircularAnimUtil.startActivity(
                        MainActivity.this,
                        intent,
                        findViewById(R.id.action_submit_order),
                        getResources().getColor(R.color.primary),
                        300);
        }

    }

    // -------------------------------- listener end -----------------------------

    /**
     * @param type start/end decide request code using to launch searchActivity
     */
    private void showSearch(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("requestCode", type);
        if (type == SearchActivity.EDIT_TEXT_REQUEST_CODE_START) {
            jumpToActivityWithResult(SearchActivity.class, bundle, SearchActivity.EDIT_TEXT_REQUEST_CODE_START);
        } else {
            jumpToActivityWithResult(SearchActivity.class, bundle, SearchActivity.EDIT_TEXT_REQUEST_CODE_END);
        }
    }

    /**
     * Show city choose menu dialog
     * <br />
     * with no preference in beta version
     */
    private void showCityDialog() {
        new MaterialDialog.Builder(this)
                .titleColor(getResources().getColor(R.color.primary))
                .positiveColor(getResources().getColor(R.color.primary))
                .title(R.string.choice_city)
                .items(R.array.supported_citys)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Log.e("Main/Choice", "Dialog choice" + which);
                        //TODO:持久化
                        return true;
                    }
                })
                .positiveText(R.string.choose)
                .show();
    }

    private void finishApp() {
        finish();
    }

    /**
     * UpdateEditTextDrawable
     * Bind with callback listener `onTextChanged`
     * <p/>
     * Set drawable fill-in when EditText not null
     * <p/>
     * If both EditText are not null, show search FAB, else dismiss it
     */
    private void updateEditTextDrawable() {
        if (TextUtils.isEmpty(editTextStart.getText())) {
            imageStart.setImageDrawable(getResources().getDrawable(R.drawable.ic_hexagon_outline));
        } else {
            imageStart.setImageDrawable(getResources().getDrawable(R.drawable.ic_hexagon));
        }
        if (TextUtils.isEmpty(editTextEnd.getText())) {
            imageEnd.setImageDrawable(getResources().getDrawable(R.drawable.ic_hexagon_outline));
        } else {
            imageEnd.setImageDrawable(getResources().getDrawable(R.drawable.ic_hexagon));
        }

        if (!TextUtils.isEmpty(editTextStart.getText()) && !TextUtils.isEmpty(editTextEnd.getText())) {
            fab_submitorder.setVisibility(View.VISIBLE);
            fab_submitorder.startAnimation(AnimationUtils.loadAnimation(
                    MainActivity.this, R.anim.fade_in_center
                    )
            );
        } else {
            if (fab_submitorder.isShown()) {
                fab_submitorder.startAnimation(
                        AnimationUtils.loadAnimation(
                                MainActivity.this, R.anim.fade_out_center
                        )
                );
            }
            fab_submitorder.setVisibility(View.INVISIBLE);
        }
    }

    private void setEditText(EditText e, Station s) {
        if (s != null) {
            e.setText(
                    SubwayLineUtil.ConnectLineNameStr(
                            s.getLine(),
                            s.getName()
                    ));
        } else {
            e.setText(null);
        }
        updateEditTextDrawable();
    }

    protected void showProfileView() {
        jumpToActivity(PersonalSettings.class);
    }

    protected void showSettingsView() {
        jumpToActivity(ApplicationSettings.class);
    }

    private void initSheet() {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.main_activity);
        sheet = new SweetSheet(rl);
        sheet.setBackgroundEffect(new DimEffect(12f));
    }

    protected void getLocation() {
        /**
         * hide fab-search at first
         */
        if (fab_submitorder.isShown()) {
            fab_submitorder.startAnimation(
                    AnimationUtils.loadAnimation(
                            MainActivity.this, R.anim.fade_out_center
                    )
            );
            fab_submitorder.setVisibility(View.INVISIBLE);
        }
        /**
         * Inflater view
         */
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.main_activity);
        View view = LayoutInflater.from(this).inflate(R.layout.item_locate_station, rl, false);
        /**
         * Bind view with delegate animation
         */
        CustomDelegate customDelegate = new CustomDelegate(true, CustomDelegate.AnimationType.DuangAnimation);

        /**
         * For beta version, Using fake data here
         *
         * Pretend to we try our best to locate our position by science power
         */
//        Station s = LocateUtil.getInstance().getMeMyPosition()
        Station s = Station.SubwayStationAdapter(TestUtils.BuildSubwayStation(3));
        s.setAvailable(false);
        StationMessage message = new StationMessage();
        message.setContent("因捕鱼行动进行," + s.getName() + "站关闭");
        s.setStationMessage(message);

        TextView textName = (TextView) view.findViewById(R.id.txtName);
        textName.setText(s.getName());
        TextView textLine = (TextView) view.findViewById(R.id.txtLine);
        textLine.setText(s.getLine() + " 号线");

        TextView textMessage = (TextView) view.findViewById(R.id.message);
        if (s.isAvailable()) {
            textMessage.setText(R.string.normal);
        } else {
            String strClose = getString(R.string.close);
            String strMessage = s.getStationMessage().getContent();
            String content = strClose + '\n' + strMessage;
            textMessage.setText(content);
        }

        final Station station = s;
        view.findViewById(R.id.comeFromThere).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStation = station;
                setEditText(editTextStart, startStation);
                sheet.dismiss();
            }
        });
        view.findViewById(R.id.goToThere).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endStation = station;
                setEditText(editTextEnd, endStation);

                sheet.dismiss();
            }
        });

        customDelegate.setCustomView(view);
        sheet.setDelegate(customDelegate);
        sheet.show();
    }

    private class SideNavBtn {
        public final static int BILLS = 0;
        public final static int CITES = 1;
        public final static int PROFILE = 2;
        public final static int ABOUT_ME = 3;
        public final static int EXIT = 4;

    }

}



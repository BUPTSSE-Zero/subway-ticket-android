package cn.crepusculo.subway_ticket_android.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import com.subwayticket.database.model.TicketOrder;
import com.subwayticket.model.result.OrderListResult;

import java.util.ArrayList;
import java.util.List;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.Station;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.ui.activity.settings.ApplicationSettings;
import cn.crepusculo.subway_ticket_android.ui.activity.settings.PersonalSettings;
import cn.crepusculo.subway_ticket_android.utils.CircularAnimUtil;
import cn.crepusculo.subway_ticket_android.utils.NetworkUtils;
import cn.crepusculo.subway_ticket_android.utils.SubwayLineUtil;

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
    /* Fabs */
    private com.github.jorgecastilloprz.FABProgressCircle fabProgressCircle;
    private com.getbase.floatingactionbutton.FloatingActionButton fab_settings;
    private com.getbase.floatingactionbutton.FloatingActionButton fab_unfoucs;
    private com.getbase.floatingactionbutton.FloatingActionButton fab_locate;
    private com.getbase.floatingactionbutton.FloatingActionsMenu fab_menu;
    private com.getbase.floatingactionbutton.FloatingActionButton fab_subway;
    private com.getbase.floatingactionbutton.FloatingActionButton fab_bills;
    private android.support.design.widget.FloatingActionButton fab_search;
    /* EditText */
    private ImageButton editText_btn;
    private EditText editText_start;
    private EditText editText_end;
    private ImageView i_come, i_go;

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
        fab_menu = (com.getbase.floatingactionbutton.FloatingActionsMenu)
                findViewById(R.id.multiple_actions);

        fab_subway = (com.getbase.floatingactionbutton.FloatingActionButton)
                findViewById(R.id.action_subway);

        fab_locate = (com.getbase.floatingactionbutton.FloatingActionButton)
                findViewById(R.id.action_locate);

        fab_bills = (com.getbase.floatingactionbutton.FloatingActionButton)
                findViewById(R.id.action_bills);


        fab_search = (android.support.design.widget.FloatingActionButton)
                findViewById(R.id.action_search);

        fab_settings = (com.getbase.floatingactionbutton.FloatingActionButton)
                findViewById(R.id.action_settings);


        updateHint();

        /**
         *  fab menu listener register
         *
         */
        fab_bills.setOnClickListener(this);
        fab_subway.setOnClickListener(this);
        fab_locate.setOnClickListener(this);
        /**
         * fab listener register
         *
         */
        fab_settings.setOnClickListener(this);
        fab_search.setOnClickListener(this);
    }

    private void initDrawer() {
        /**
         *  get side menu resource
         **/
        String[] drawerItemsNames = getResources().getStringArray(R.array.drawer_items);
        int[] drawerItemIcons = {
                R.drawable.ic_qr_24dp,
                R.drawable.ic_ticket_light_24dp,
                R.drawable.ic_find_in_page_light_24dp,
                R.drawable.ic_account_light_24dp,
                R.drawable.ic_settings_light_24dp,
                R.drawable.ic_settings_light_24dp
        };

        /* init side menu concept */
        PrimaryDrawerItem[] primaryDrawerItems = {
                new PrimaryDrawerItem()
                        .withName(drawerItemsNames[SideNavBtn.GET_QR])
                        .withIcon(drawerItemIcons[SideNavBtn.GET_QR])
                        .withTextColorRes(R.color.light_primary_text)
                        .withIconColorRes(R.color.light_primary_text)
                        .withIdentifier(SideNavBtn.GET_QR),

                new PrimaryDrawerItem()
                        .withName(drawerItemsNames[SideNavBtn.BILLS])
                        .withTextColorRes(R.color.light_primary_text)
                        .withIconColorRes(R.color.light_primary_text)
                        .withIcon(drawerItemIcons[SideNavBtn.BILLS])
                        .withIdentifier(SideNavBtn.BILLS),

                new PrimaryDrawerItem()
                        .withName(drawerItemsNames[SideNavBtn.CITES])
                        .withIconColorRes(R.color.light_primary_text)
                        .withTextColorRes(R.color.light_primary_text)
                        .withIcon(drawerItemIcons[SideNavBtn.CITES])
                        .withIdentifier(SideNavBtn.CITES),

                new PrimaryDrawerItem()
                        .withName(drawerItemsNames[SideNavBtn.PROFILE])
                        .withTextColorRes(R.color.light_primary_text)
                        .withIconColorRes(R.color.light_primary_text)
                        .withIcon(drawerItemIcons[SideNavBtn.PROFILE])
                        .withIdentifier(SideNavBtn.PROFILE),

                new PrimaryDrawerItem()
                        .withName(drawerItemsNames[SideNavBtn.SETTINGS])
                        .withTextColorRes(R.color.light_primary_text)
                        .withIconColorRes(R.color.light_primary_text)
                        .withIcon(drawerItemIcons[SideNavBtn.SETTINGS])
                        .withIdentifier(SideNavBtn.SETTINGS)
        };

        for (PrimaryDrawerItem item : primaryDrawerItems
                ) {
            item.withTextColor(getResources().getColor(R.color.light_primary_text));
        }

        /* build side Menu */
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.header_drawer)
                .withTranslucentNavigationBar(true)
                .withDisplayBelowStatusBar(false)
                .withCloseOnClick(true)
                .withTranslucentNavigationBar(true)
                .withActionBarDrawerToggle(false)
                .withSelectedItem(-1)
                .withDrawerGravity(Gravity.START)
                .addDrawerItems(
                        primaryDrawerItems[SideNavBtn.GET_QR],
                        primaryDrawerItems[SideNavBtn.BILLS],
                        primaryDrawerItems[SideNavBtn.CITES],
                        new DividerDrawerItem(),
                        primaryDrawerItems[SideNavBtn.PROFILE],
                        primaryDrawerItems[SideNavBtn.SETTINGS]
                )
                .addStickyDrawerItems(new PrimaryDrawerItem()
                        .withName(R.string.common_exit)
                        .withSelectable(true)
                        .withIdentifier(SideNavBtn.EXIT)
                        .withOnDrawerItemClickListener(this))
                .withOnDrawerItemClickListener(this)
                .withCloseOnClick(true)
                .build();
    }

    protected void initSearchEditTextView() {
        /**
         * Bind compacts
         */
        i_come = (ImageView) findViewById(R.id.edittext_drawable_come);
        i_go = (ImageView) findViewById(R.id.edittext_drawable_go);
        editText_start = (EditText) findViewById(R.id.edittext_come);
        editText_end = (EditText) findViewById(R.id.edittext_go);
        editText_btn = (ImageButton) findViewById(R.id.edittext_btn);

        editText_start.setLongClickable(false);
        editText_end.setLongClickable(false);
        editText_btn.setBackgroundColor(getResources().getColor(R.color.transparent));

        /**
         * Set listener
         */

        i_come.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText_start.setText(null);
                updateEditTextDrawable();
            }
        });

        i_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText_end.setText(null);
                updateEditTextDrawable();
            }
        });

        editText_start.addTextChangedListener(this);

        editText_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearch(SearchActivity.EDIT_TEXT_REQUEST_CODE_START);
            }
        });
        editText_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearch(SearchActivity.EDIT_TEXT_REQUEST_CODE_END);
            }
        });

        editText_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (endStation != null && startStation != null) {
                    Station swap = startStation;
                    startStation = endStation;
                    endStation = swap;

                } else if (endStation == null && startStation != null) {
                    endStation = startStation;
                } else if (endStation != null) {
                    startStation = endStation;
                }
                SetEditText(editText_start, startStation);
                SetEditText(editText_end, endStation);
            }
        });
    }


    // --------------------------------- listener --------------------------------------------------

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
     * @param data        Intent data back from SearchActivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SearchActivity.EDIT_TEXT_REQUEST_CODE_START) {
            // Only write start_edit_text
            String obj_str = data.getStringExtra(
                    SearchActivity.KEY_START);
            startStation = new Gson().fromJson(obj_str, Station.class);
            SetEditText(editText_start, startStation);
        } else if (resultCode == SearchActivity.EDIT_TEXT_REQUEST_CODE_END) {
            // Only write end_edit_text
            String obj_str = data.getStringExtra(
                    SearchActivity.KEY_END);
            endStation = new Gson().fromJson(obj_str, Station.class);
            SetEditText(editText_end, endStation);
        } else if (resultCode == SearchActivity.EDIT_TEXT_REQUEST_CODE_BOTH) {
            // Both write two edit_text
            String obj_str_e = data.getStringExtra(
                    SearchActivity.KEY_END);
            String obj_str_s = data.getStringExtra(
                    SearchActivity.KEY_START);
            endStation = new Gson().fromJson(obj_str_e, Station.class);
            startStation = new Gson().fromJson(obj_str_s, Station.class);

            SetEditText(editText_start, startStation);
            SetEditText(editText_end, endStation);

        } else if (resultCode == SearchActivity.EDIT_TEXT_REQUEST_CODE_EMPTY) {
            // None of edit_text will be rewrite
        } else {
            Log.e("MainActivity/Result", "No Result Receive");
        }
        updateEditTextDrawable();
    }

    /**
     * @param view       View
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
        if (id == SideNavBtn.GET_QR) {
            Bundle bundle = new Bundle();
            bundle.putInt("TYPE", position + 1);
            jumpToActivity(TicketManagerActivity.class, bundle);
            drawer.closeDrawer();
        }
        /**
         * Jump to Ticket Message Manager 'History'
         */
        else if (id == SideNavBtn.BILLS) {
            Bundle bundle = new Bundle();
            bundle.putInt("TYPE", position + 1);
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
         * Show profile
         */
        else if (id == SideNavBtn.SETTINGS) {
            showSettingsView();
            drawer.closeDrawer();
        }
        /**
         * Show finish app dialog
         */
        else if (id == SideNavBtn.EXIT) {
            finishApp();
            drawer.closeDrawer();
        }
        return true;
    }

    /**
     * @param view Float Action Button and Float Action Button in Float Action Menu
     */
    @Override
    public void onClick(View view) {
        fab_bills.setVisibility(View.VISIBLE);
        int id = view.getId();
        Log.e("id", "" + id);
        switch (id) {
            case R.id.action_bills:
                fab_menu.collapse();

                updateHint();

                Bundle bundle = new Bundle();
                bundle.putInt("TYPE", 2);
                jumpToActivity(TicketManagerActivity.class, bundle);
                return;
            case R.id.action_subway:
////                Date date = new Date(2016, 7, 27);
//                Toast.makeText(this, "" + System.currentTimeMillis()
//                        , Toast.LENGTH_LONG).show();
                fab_menu.collapse();
                return;
            case R.id.action_locate:
                getLocation();
                fab_menu.collapse();
                return;
            case R.id.action_settings:
                drawer.openDrawer();
                return;
            case R.id.action_search:
                List<String> route = new ArrayList<>();
                Bundle b = new Bundle();
                b.putString("route_start", new Gson().toJson(startStation));
                b.putString("route_end", new Gson().toJson(endStation));
                Intent intent = new Intent(MainActivity.this, PayActivity.class);
                intent.putExtras(b);
                CircularAnimUtil.startActivity(
                        MainActivity.this,
                        intent,
                        findViewById(R.id.action_search),
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
     * Request server to check is any NOT_EXTRACT_TICKET here
     * if any, show fab button fab_bills
     */
    private void updateHint() {
        int ticketCount = 0;
        NetworkUtils.ticketOrderGetOrderListByStatusAndStartTimeAndEndTime(
                "" + TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET,
                "" + 0,
                "" + System.currentTimeMillis(),
                info.getToken(),
                new Response.Listener<OrderListResult>() {
                    @Override
                    public void onResponse(OrderListResult response) {
                        int ticketCount = response.getTicketOrderList().size();
                        if (ticketCount > 0) {
                            if (fab_menu.getChildCount() == 2)
                                fab_menu.addButton(fab_bills);
                        } else {
                            if (fab_menu.getChildCount() == 3)
                                fab_menu.removeButton(fab_bills);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (fab_menu.getChildCount() == 3)
                            fab_menu.removeButton(fab_bills);
                    }
                }
        );

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
        int type = 0;
        new MaterialDialog.Builder(this)
                .titleColor(getResources().getColor(R.color.primary))
                .positiveColor(getResources().getColor(R.color.primary))
                .negativeColor(getResources().getColor(R.color.primary))
                .title(R.string.exit_dialog)
                .content(R.string.exit_dialog_content)
                .positiveText(R.string.agree)
                .autoDismiss(true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        MainActivity.this.finish();
                    }
                })
                .negativeText(R.string.cancel)
                .show();
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
        if (editText_start.getText().toString().trim().length() < 1) {
            i_come.setImageDrawable(getResources().getDrawable(R.drawable.ic_hexagon_outline));
        } else {
            i_come.setImageDrawable(getResources().getDrawable(R.drawable.ic_hexagon));
        }
        if (editText_end.getText().toString().trim().length() < 1) {
            i_go.setImageDrawable(getResources().getDrawable(R.drawable.ic_hexagon_outline));
        } else {
            i_go.setImageDrawable(getResources().getDrawable(R.drawable.ic_hexagon));
        }

        if (editText_start.getText().toString().trim().length() >= 1 && editText_end.getText().toString().trim().length() >= 1) {
            fab_search.setVisibility(View.VISIBLE);
        } else {
            fab_search.setVisibility(View.INVISIBLE);
        }
    }

    private void SetEditText(EditText e, Station s) {
        if (s != null) {
            e.setText(
                    SubwayLineUtil.ConnectLineNameStr(
                            s.getLine(),
                            s.getName()
                    ));
        } else {
            e.setText(null);
        }
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
        Station s = new Station(3);


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
            String content = strClose + strMessage;
            textMessage.setText(content);
        }

        view.findViewById(R.id.comeFromThere).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheet.dismiss();
            }
        });
        view.findViewById(R.id.goToThere).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheet.dismiss();
            }
        });

        customDelegate.setCustomView(view);
        sheet.setDelegate(customDelegate);

        sheet.show();
    }

    private class SideNavBtn {
        public final static int GET_QR = 0;
        public final static int BILLS = 1;
        public final static int CITES = 2;
        public final static int PROFILE = 3;
        public final static int SETTINGS = 4;
        public final static int ABOUT_ME = 5;
        public final static int EXIT = 9;

    }

}



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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bm.library.PhotoView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.utils.SubwayLineUtil;

public class MainActivity extends cn.crepusculo.subway_ticket_android.ui.activity.BaseActivity
        implements Drawer.OnDrawerItemClickListener,
        com.getbase.floatingactionbutton.FloatingActionButton.OnClickListener, TextWatcher {
    /* Static constant */

    /* view */
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
        view = this.findViewById(R.id.main_activity);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT > 21) {
            window.setStatusBarColor(getResources().getColor(R.color.primary));
        }
        initBackGround();
        initInfo();
        initToolbar();
        initFab();
        initDrawer();
        initSelectButton();
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

    private void initFab() {
        fab_search = (android.support.design.widget.FloatingActionButton)
                findViewById(R.id.action_search);

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

        updateHint();

        /* fab menu listener register */
        fab_bills.setOnClickListener(this);
        fab_subway.setOnClickListener(this);
        fab_locate.setOnClickListener(this);
        /* fab listener register */
        fab_settings.setOnClickListener(this);

        fab_search.setOnClickListener(this);
    }

    private void initDrawer() {
        /* get side menu resource */
        String[] drawerItemsNames = getResources().getStringArray(R.array.drawer_items);
        int[] drawerItemIcons = {
                R.drawable.ic_find_in_page_black_24dp,
                R.drawable.ic_find_in_page_black_24dp,
                R.drawable.ic_find_in_page_black_24dp,
                R.drawable.ic_find_in_page_black_24dp,
                R.drawable.ic_find_in_page_black_24dp,
                R.drawable.ic_find_in_page_black_24dp
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
                .withTranslucentNavigationBar(true)
                .withDisplayBelowStatusBar(false)
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
                        .withSelectable(true)
                        .withIdentifier(SideNavBtn.EXIT)
                        .withOnDrawerItemClickListener(this))
                .withOnDrawerItemClickListener(this)
                .withCloseOnClick(true)
                .build();
    }

    protected void initSelectButton() {
        i_come = (ImageView) findViewById(R.id.edittext_drawable_come);
        i_go = (ImageView) findViewById(R.id.edittext_drawable_go);
        editText_start = (EditText) findViewById(R.id.edittext_come);
        editText_end = (EditText) findViewById(R.id.edittext_go);
        editText_btn = (ImageButton) findViewById(R.id.edittext_btn);
        editText_start.addTextChangedListener(this);
        editText_start.setLongClickable(false);
        editText_end.setLongClickable(false);

        editText_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearch(SearchActivity.ET_START);
            }
        });
        editText_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearch(SearchActivity.ET_END);
            }
        });

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
        editText_btn.setBackgroundColor(getResources().getColor(R.color.transparent));
        editText_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c = editText_end.getText().toString().trim();
                String g = editText_start.getText().toString().trim();
                editText_end.setText(g);
                editText_start.setText(c);
            }
        });
    }

    private void showSearch(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", type);
        if (type == SearchActivity.ET_END) {
            jumpToActivityWithResult(SearchActivity.class, bundle, SearchActivity.EDIT_TEXT_REQUEST_CODE_END);
        } else {
            jumpToActivityWithResult(SearchActivity.class, bundle, SearchActivity.EDIT_TEXT_REQUEST_CODE_START);
        }
    }


    private void updateHint() {
        int ticketCount = 0;
        // net request here

        if (ticketCount > 0) {
            if (fab_menu.getChildCount() == 2)
                fab_menu.addButton(fab_bills);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fabProgressCircle.show();
                }
            }, 3000);
        } else {
            if (fabProgressCircle.isActivated())
                fabProgressCircle.hide();
            if (fab_menu.getChildCount() == 3)
                fab_menu.removeButton(fab_bills);
        }
    }

    private void showCitysDialog() {
        new MaterialDialog.Builder(this)
                .titleColor(getResources().getColor(R.color.primary))
                .positiveColor(getResources().getColor(R.color.primary))
                .title(R.string.choice_city)
                .items(R.array.supported_citys)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Log.e("Main/Choice", "Dialog Chioce" + which);
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
    // --------------------------------- listener --------------------------------------------------


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            finishApp();
//            super.onBackPressed();
        }
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SearchActivity.EDIT_TEXT_REQUEST_CODE_START) {
            // Only write start_edit_text
            int line = data.getIntExtra(
                    SearchActivity.KEY_LINE_START, 0);
            String name = data.getStringExtra(
                    SearchActivity.KEY_NAME_START);

            editText_start.setText(
                    SubwayLineUtil.ConnectLineNameStr(line, name));

        } else if (resultCode == SearchActivity.EDIT_TEXT_REQUEST_CODE_END) {
            // Only write end_edit_text
            int line = data.getIntExtra(
                    SearchActivity.KEY_LINE_END, 0);
            String name = data.getStringExtra(
                    SearchActivity.KEY_NAME_END);

            editText_end.setText(
                    SubwayLineUtil.ConnectLineNameStr(line, name));

        } else if (resultCode == SearchActivity.EDIT_TEXT_REQUEST_CODE_BOTH) {
            // Both write two edit_text
            int line_s = data.getIntExtra(
                    SearchActivity.KEY_LINE_START, 0);
            String name_s = data.getStringExtra(
                    SearchActivity.KEY_NAME_START);
            int line_e = data.getIntExtra(
                    SearchActivity.KEY_LINE_END, 0);
            String name_e = data.getStringExtra(
                    SearchActivity.KEY_NAME_END);

            editText_start.setText(SubwayLineUtil.ConnectLineNameStr(line_s, name_s));
            editText_end.setText(SubwayLineUtil.ConnectLineNameStr(line_e, name_e));

        } else if (resultCode == SearchActivity.EDIT_TEXT_REQUEST_CODE_EMPTY) {
            // None of edit_text will be rewrite
        } else {
            Log.e("MainActivity/Result", "No Result Receive");
        }
        updateEditTextDrawable();
    }

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
        switch (id) {
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
            Bundle bundle = new Bundle();
            bundle.putInt("TYPE", position + 1);
            Log.e("Bundle in Main", "" + position);
            jumpToActivity(TicketManagerActivity.class, bundle);
            drawer.closeDrawer();
        } else if (id == SideNavBtn.BILLS) {
            Bundle bundle = new Bundle();
            bundle.putInt("TYPE", position + 1);
            Log.e("Bundle in Main", "" + position);
            jumpToActivity(TicketManagerActivity.class, bundle);
            drawer.closeDrawer();
        } else if (id == SideNavBtn.CITYS) {
            showCitysDialog();
            drawer.closeDrawer();
        } else if (id == SideNavBtn.PROFILE) {

            drawer.closeDrawer();
        } else if (id == SideNavBtn.SETTINGS) {

            drawer.closeDrawer();
        } else if (id == SideNavBtn.EXIT) {
            finishApp();
            drawer.closeDrawer();
        }
        return true;
    }

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
                fab_menu.collapse();
                return;
            case R.id.action_settings:
                drawer.openDrawer();
                return;
            case R.id.action_search:
                List<String> route = new ArrayList<>();
                Bundle b = new Bundle();
                b.putString("route_start", editText_start.getText().toString().trim());
                b.putString("route_end", editText_end.getText().toString().trim());
                jumpToActivity(PayActivity.class, b);
        }

    }


    private class SideNavBtn {
        public final static int GET_QR = 0;
        public final static int BILLS = 1;
        public final static int CITYS = 2;
        public final static int PROFILE = 3;
        public final static int SETTINGS = 4;
        public final static int EXIT = 9;

    }

}



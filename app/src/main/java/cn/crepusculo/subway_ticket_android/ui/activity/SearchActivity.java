package cn.crepusculo.subway_ticket_android.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.Station;
import cn.crepusculo.subway_ticket_android.ui.adapter.StationAdapter;

public class SearchActivity extends BaseActivity implements
        SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {
    public static int ET_START = 1;
    public static int ET_END = -1;

    /* ID back to main*/
    public static int EDIT_TEXT_REQUEST_CODE_START = 1000;
    public static int EDIT_TEXT_REQUEST_CODE_END = 1001;
    public static int EDIT_TEXT_REQUEST_CODE_BOTH = 1002;
    public static int EDIT_TEXT_REQUEST_CODE_EMPTY = 1003;

    public static String KEY_NAME_START = "name_start";
    public static String KEY_LINE_START = "line_start";
    public static String KEY_NAME_END = "name_end";
    public static String KEY_LINE_END = "line_end";
    /* var */
    private int SEARCH_STATUS;
    private Bundle result;
    /* search list */
    private ArrayList<Station> stationArrayList;
    private StationAdapter stationAdapter;

    /* compats */
    private SearchView searchView;
    private ListView listView;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_search;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                onBackPressed();
                return false;
            }
        });
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchItem.expandActionView();

        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    protected void initView() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        searchView = (SearchView) findViewById(R.id.search_view);
        listView = (ListView) findViewById(R.id.list);

        stationArrayList = new ArrayList<Station>();
        loadData(stationArrayList);

        stationAdapter = new StationAdapter(SearchActivity.this, stationArrayList);
        listView.setAdapter(stationAdapter);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(this);
        Bundle bundle = getBundle();
        initString();
        SEARCH_STATUS = bundle.getInt("TYPE", 0);
        listView.clearChoices();

//        searchView.setOnQueryTextListener(this);
//        searchView.setSubmitButtonEnabled(false);
//        searchView.setIconifiedByDefault(false);
//        searchView.setQueryHint("Search Here");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(EDIT_TEXT_REQUEST_CODE_EMPTY, intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in_center, R.anim.fade_out_center);
    }

    private void initString() {
        result = new Bundle();
    }

    private void setSearchResult(int result_come_line, String result_come, int result_go_line, String result_go) {
        result.putString(KEY_NAME_START, result_come);
        result.putString(KEY_NAME_END, result_go);
        result.putInt(KEY_LINE_START, result_come_line);
        result.putInt(KEY_LINE_END, result_go_line);

        putIntent(result);
    }

    private void setSearchResult(Station start, Station end) {
        result.putString(KEY_NAME_START, start.getName());
        result.putString(KEY_NAME_END, end.getName());
        result.putInt(KEY_LINE_START, start.getLine());
        result.putInt(KEY_LINE_END, end.getLine());
        putIntent(result);
    }

    private void setSearchResult(Station s) {
        if (SEARCH_STATUS == ET_START) {
            result.putString(KEY_NAME_START, s.getName());
            result.putInt(KEY_LINE_START, s.getLine());
        } else if (SEARCH_STATUS == ET_END) {
            result.putString(KEY_NAME_END, s.getName());
            result.putInt(KEY_LINE_END, s.getLine());
        }
        putIntent(this.result);
    }

    private void putIntent(Bundle b) {
        Intent intent = new Intent();
        intent.putExtras(b);
        if (SEARCH_STATUS == ET_START)
            setResult(EDIT_TEXT_REQUEST_CODE_START, intent);
        else if (SEARCH_STATUS == ET_END)
            setResult(EDIT_TEXT_REQUEST_CODE_END, intent);
        else
            setResult(EDIT_TEXT_REQUEST_CODE_BOTH, intent);
    }

    public void loadData(ArrayList<Station> stationArrayList) {
        stationArrayList.add(new Station("ABC", 4));
        stationArrayList.add(new Station("ACB", 5));
        stationArrayList.add(new Station("BVF", 6));
        stationArrayList.add(new Station("南京路", 1));
        stationArrayList.add(new Station("北京路", 2));
        stationArrayList.add(new Station("东京路", 7));
        stationArrayList.add(new Station("BRT", 13));
        stationArrayList.add(new Station("ANM", 1));
        stationArrayList.add(new Station("太平洋广场", 4));
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        if (TextUtils.isEmpty(newText)) {
            // Clear the text filter.
            listView.clearTextFilter();
        } else {
            // Sets the initial value for the text filter.
            listView.setFilterText(newText);
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        TextView lineView = (TextView) view.findViewById(R.id.txtLine);
        TextView nameView = (TextView) view.findViewById(R.id.txtName);

        String name = nameView.getText().toString().trim();
        int line = Integer.parseInt(lineView.getText().toString().trim());
        setSearchResult(new Station(name, line));
        this.finish();
    }
}

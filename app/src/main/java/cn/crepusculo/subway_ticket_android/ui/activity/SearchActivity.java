package cn.crepusculo.subway_ticket_android.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.Station;
import cn.crepusculo.subway_ticket_android.ui.adapter.StationAdapter;

public class SearchActivity extends BaseActivity implements
        SearchView.OnQueryTextListener {
    public static int ET_COME = 1;
    public static int ET_GO = -1;
    public static int EDIT_TEXT_REQUEST_CODE_COME = 1000;
    public static int EDIT_TEXT_REQUEST_CODE_GO = 1001;
    /* var */
    private int search_object;
    private Bundle result;
    private String result_come;
    private String result_go;
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

        Bundle bundle = getBundle();
        initString();
        search_object = bundle.getInt("TYPE", 0);
        listView.clearChoices();

//        searchView.setOnQueryTextListener(this);
//        searchView.setSubmitButtonEnabled(false);
//        searchView.setIconifiedByDefault(false);
//        searchView.setQueryHint("Search Here");
    }

    @Override
    public void onBackPressed() {
        setSearchResult();
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in_center, R.anim.fade_out_center);
    }

    private void initString() {
        result = new Bundle();
        result_come = "23333";
        result_go = "xinbaqi";
    }

    private void setSearchResult() {
        result.putString("result_come", result_come);
        result.putString("result_go", result_go);

        Intent intent = new Intent();
        intent.putExtras(result);
        if (search_object == ET_COME)
            setResult(EDIT_TEXT_REQUEST_CODE_COME, intent);
        else
            setResult(EDIT_TEXT_REQUEST_CODE_GO, intent);
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
}

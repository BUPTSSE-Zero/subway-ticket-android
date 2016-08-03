package cn.crepusculo.subway_ticket_android.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.subwayticket.database.model.PreferRoute;
import com.subwayticket.database.model.SubwayLine;
import com.subwayticket.database.model.SubwayStation;
import com.subwayticket.model.result.SubwayLineListResult;
import com.subwayticket.model.result.SubwayStationListResult;

import java.util.ArrayList;
import java.util.List;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.Station;
import cn.crepusculo.subway_ticket_android.ui.adapter.RecycleViewDivider;
import cn.crepusculo.subway_ticket_android.ui.adapter.SearchAdapter;
import cn.crepusculo.subway_ticket_android.ui.adapter.SearchHistoryAdapter;
import cn.crepusculo.subway_ticket_android.utils.NetworkUtils;
import cn.crepusculo.subway_ticket_android.utils.SubwayLineUtil;

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
    private SearchAdapter searchAdapter;
    private SearchHistoryAdapter searchHistoryAdapter;

    /* compats */
    private RecyclerView listView;
    private RecyclerView historyView;

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
        listView = (RecyclerView) findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(this));

        boolean status = initData();
        searchAdapter.animateTo(stationArrayList);
        listView.setVisibility(View.GONE);
        /* TEST DATA */
        Station station1 = new Station("2333",4);
        Station station2 = new Station("23333",5);
        PreferRoute preferRoute1 = new PreferRoute("99999999999",111,181);
        SubwayStation ss1 = new SubwayStation(111);
        ss1.setSubwayStationName("五道口");
        SubwayStation ss2 = new SubwayStation(181);
        ss2.setSubwayStationName("什刹海");
        preferRoute1.setStartStation(ss1);
        preferRoute1.setEndStation(ss2);
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(station1);
        objects.add(station2);
        objects.add(preferRoute1);
//        NetworkUtils.subwayGetTicketPriceByStartStationAndEndStation()
        searchHistoryAdapter = new SearchHistoryAdapter(this, objects);
        historyView = (RecyclerView) findViewById(R.id.history);
        historyView.setLayoutManager(new LinearLayoutManager(this));
        RecycleViewDivider dividerLine = new RecycleViewDivider(RecycleViewDivider.VERTICAL);
        dividerLine.setSize(4);
        dividerLine.setColor(0xFFDDDDDD);
        historyView.addItemDecoration(dividerLine);
        historyView.setAdapter(searchHistoryAdapter);



    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(EDIT_TEXT_REQUEST_CODE_EMPTY, intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in_center, R.anim.fade_out_center);
    }


    private boolean initData() {
        stationArrayList = new ArrayList<>();
        Log.e("Data", "initData!");
        String beijingId = "1";
        NetworkUtils.subwayGetLineListByCity(
                beijingId,
                new Response.Listener<SubwayLineListResult>() {
                    @Override
                    public void onResponse(SubwayLineListResult response) {
                        List<SubwayLine> lineList = response.getSubwayLineList();
                        for (SubwayLine line : lineList) {
                            Log.e("Data", "Find Line!" + line.getSubwayLineName() + SubwayLineUtil.ToClientTypeId(line.getSubwayLineId()));

                            NetworkUtils.subwayGetStationByLine(
                                    "" + line.getSubwayLineId(),
                                    new Response.Listener<SubwayStationListResult>() {
                                        @Override
                                        public void onResponse(SubwayStationListResult response) {
                                            List<SubwayStation> stationList = response.getSubwayStationList();
                                            for (SubwayStation s : stationList
                                                    ) {
                                                Station station = Station.SubwayStationAdapter(s);
                                                stationArrayList.add(station);
                                                Log.e("Data", "Add:!" + s.getSubwayStationId() + station.getName());
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
//                                            showErrorDialog();
                                        }
                                    });
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showErrorDialog();
                    }
                });

        searchAdapter = new SearchAdapter(SearchActivity.this, stationArrayList);
        listView.setAdapter(searchAdapter);
        searchAdapter.animateTo(stationArrayList);
        return true;
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

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        listView.setVisibility(View.VISIBLE);
        historyView.setVisibility(View.GONE);
        final List<Station> filteredModelList = filter(stationArrayList, newText);
        searchAdapter.animateTo(filteredModelList);
        listView.scrollToPosition(0);
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

    private List<Station> filter(List<Station> list, String query) {
        if (TextUtils.isEmpty(query)) {
            return stationArrayList;
        } else {
            query = query.toLowerCase();
            final List<Station> filteredModelList = new ArrayList<>();
            for (Station station : list) {
                final String text = station.getName();
                if (text.contains(query)) {
                    filteredModelList.add(station);
                }
            }
            return filteredModelList;
        }
    }

    public void showErrorDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(SearchActivity.this)
                .title(R.string.error)
                .content(R.string.error_failure_to_get_subway_list)
                .positiveText(R.string.retry)
                .positiveColor(getResources().getColor(R.color.primary))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        initData();
                        dialog.dismiss();
                    }
                })
                .negativeText(R.string.cancel)
                .negativeColor(getResources().getColor(R.color.primary))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        finish();
                    }
                })
                .show();
    }
}

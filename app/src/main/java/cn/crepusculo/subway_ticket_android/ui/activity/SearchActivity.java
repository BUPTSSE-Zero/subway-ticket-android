package cn.crepusculo.subway_ticket_android.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.subwayticket.database.model.HistoryRoute;
import com.subwayticket.database.model.PreferRoute;
import com.subwayticket.database.model.SubwayLine;
import com.subwayticket.database.model.SubwayStation;
import com.subwayticket.model.result.HistoryRouteListResult;
import com.subwayticket.model.result.SubwayLineListResult;
import com.subwayticket.model.result.SubwayStationListResult;

import java.util.ArrayList;
import java.util.List;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.Station;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.ui.adapter.RecycleViewDivider;
import cn.crepusculo.subway_ticket_android.ui.adapter.SearchAdapter;
import cn.crepusculo.subway_ticket_android.ui.adapter.SearchHistoryAdapter;
import cn.crepusculo.subway_ticket_android.utils.GsonUtils;
import cn.crepusculo.subway_ticket_android.utils.NetworkUtils;
import cn.crepusculo.subway_ticket_android.utils.SubwayLineUtil;

public class SearchActivity extends BaseActivity implements
        SearchView.OnQueryTextListener {

    /* ID back to main*/
    public static int EDIT_TEXT_REQUEST_CODE_START = 1000;
    public static int EDIT_TEXT_REQUEST_CODE_END = 1001;
    public static int EDIT_TEXT_REQUEST_CODE_BOTH = 1002;
    public static int EDIT_TEXT_REQUEST_CODE_EMPTY = 1003;

    public static String KEY_START = "start";
    public static String KEY_END = "end";
    /* search list */
    ArrayList<Object> objects;
    /* var */
    private int SEARCH_STATUS;
    private Bundle result;
    private ArrayList<Station> stationArrayList;
    private SearchAdapter searchAdapter;


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
        initString();

        listView = (RecyclerView) findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(this));

        boolean status = initData();
        searchAdapter = new SearchAdapter(
                SearchActivity.this,
                stationArrayList,
                new SearchAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Station data) {
                        SEARCH_STATUS = getBundle().getInt("requestCode");
                        setSearchResult(data);
                        finish();
                    }
                });
        listView.setAdapter(searchAdapter);
        searchAdapter.animateTo(stationArrayList);
        searchAdapter.animateTo(stationArrayList);
        listView.setVisibility(View.GONE);

        /* ----------------TEST DATA------------------------- */
        Station station1 = new Station("安贞门", 4, 1101);
        Station station2 = new Station("安定门", 5, 121);

        final PreferRoute preferRoute1 = new PreferRoute("99999999999", 111, 181);
        SubwayLine line1 = new SubwayLine(13);
        SubwayLine line2 = new SubwayLine(8);

        SubwayStation ss1 = new SubwayStation(111);
        ss1.setSubwayStationName("五道口");
        ss1.setSubwayLine(line1);

        SubwayStation ss2 = new SubwayStation(181);
        ss2.setSubwayStationName("什刹海");
        ss2.setSubwayLine(line2);

        preferRoute1.setStartStation(ss1);
        preferRoute1.setEndStation(ss2);
        // -----------------TEST DATA end----------------------

        objects = new ArrayList<>();

        NetworkUtils.preferenceHistory(
                Info.getInstance().getToken(),
                new Response.Listener<HistoryRouteListResult>() {
                    @Override
                    public void onResponse(HistoryRouteListResult response) {
                        ArrayList<HistoryRoute> historyRoutes =
                                new ArrayList<>(response.getHistoryRouteList());
                        if (!historyRoutes.isEmpty())
                            objects.add(historyRoutes);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        objects.add(station1);
        objects.add(station2);
        objects.add(preferRoute1);

        SearchHistoryAdapter searchHistoryAdapter;
        searchHistoryAdapter = new SearchHistoryAdapter(
                this,
                objects,
                new SearchHistoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, Object data, int mode) {
                        switch (mode) {
                            case SearchHistoryAdapter.STATUS_COME:
                                SEARCH_STATUS = EDIT_TEXT_REQUEST_CODE_START;
                                setSearchResult((Station) data);
                                break;
                            case SearchHistoryAdapter.STATUS_GO:
                                SEARCH_STATUS = EDIT_TEXT_REQUEST_CODE_END;
                                setSearchResult((Station) data);
                                break;
                            case SearchHistoryAdapter.STATUS_ROUTE:
                                SEARCH_STATUS = EDIT_TEXT_REQUEST_CODE_BOTH;
                                PreferRoute preferRoute = (PreferRoute) data;
                                ArrayList<Station> stations = new ArrayList<>(Station.PreferRouteAdapter(preferRoute));
                                setSearchResult(stations.get(0), stations.get(1));
                                break;
                        }
                        finish();
                    }
                });
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
        finish();
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
                        /**
                         * Avoid using dialog due to unload context
                         * Avoid using snackbar for the same reason and skb will hover it
                         */
//                        showErrorDialog();
                        try {
                            GsonUtils.Response r = GsonUtils.resolveErrorResponse(error);
//                            Snackbar.make(findViewById(R.id.activity_search), r.result_description, Snackbar.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), r.result_description, Toast.LENGTH_LONG).show();
                        } catch (NullPointerException e) {
//                            Snackbar.make(findViewById(R.id.activity_search), "网络访问超时", Snackbar.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "网络访问超时", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        return true;
    }

    private void initString() {
        result = new Bundle();
    }

    private void setSearchResult(Station start, Station end) {
        Gson gson = new Gson();
        result.putString(KEY_START, gson.toJson(start));
        putIntent(result);
        result.putString(KEY_END, gson.toJson(end));
        putIntent(result);
    }

    private void setSearchResult(Station s) {
        Gson gson = new Gson();
        result.putString(
                SEARCH_STATUS == EDIT_TEXT_REQUEST_CODE_START ? KEY_START : KEY_END,
                gson.toJson(s));
        putIntent(result);
    }

    private void putIntent(Bundle b) {
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        intent.putExtras(b);
        if (SEARCH_STATUS == EDIT_TEXT_REQUEST_CODE_START)
            setResult(EDIT_TEXT_REQUEST_CODE_START, intent);
        else if (SEARCH_STATUS == EDIT_TEXT_REQUEST_CODE_END)
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

    /**
     * @param list  Station Data (All list)
     * @param query Filter key word
     * @return List after filter
     */
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
        MaterialDialog dialog = new MaterialDialog.Builder(getApplicationContext())
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

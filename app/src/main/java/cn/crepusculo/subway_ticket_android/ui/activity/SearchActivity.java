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
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.subwayticket.database.model.HistoryRoute;
import com.subwayticket.database.model.PreferRoute;
import com.subwayticket.database.model.PreferSubwayStation;
import com.subwayticket.database.model.SubwayLine;
import com.subwayticket.database.model.SubwayStation;
import com.subwayticket.model.request.AddPreferRouteRequest;
import com.subwayticket.model.request.AddPreferStationRequest;
import com.subwayticket.model.result.HistoryRouteListResult;
import com.subwayticket.model.result.PreferRouteListResult;
import com.subwayticket.model.result.PreferStationListResult;
import com.subwayticket.model.result.Result;
import com.subwayticket.model.result.SubwayLineListResult;
import com.subwayticket.model.result.SubwayStationListResult;

import java.util.ArrayList;
import java.util.List;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.ui.adapter.RecycleViewDivider;
import cn.crepusculo.subway_ticket_android.ui.adapter.SearchAdapter;
import cn.crepusculo.subway_ticket_android.ui.adapter.SearchPreferAdapter;
import cn.crepusculo.subway_ticket_android.util.GsonUtils;
import cn.crepusculo.subway_ticket_android.util.NetworkUtils;
import cn.crepusculo.subway_ticket_android.util.SubwayLineUtil;

public class SearchActivity extends BaseActivity implements
        SearchView.OnQueryTextListener {

    /* ID back to main*/
    public static int EDIT_TEXT_REQUEST_CODE_START = 1000;
    public static int EDIT_TEXT_REQUEST_CODE_END = 1001;
    public static int EDIT_TEXT_REQUEST_CODE_BOTH = 1002;
    public static int EDIT_TEXT_REQUEST_CODE_EMPTY = 1003;

    public static String KEY_START = "start";
    public static String KEY_END = "end";
    /* var */
    private int SEARCH_STATUS;
    private Bundle result;
    private ArrayList<SubwayStation> stationArrayList;
    private SearchAdapter searchAdapter;


    /* compats */
    private RecyclerView listView;
    private RecyclerView preferListView;

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

        initData();
        searchAdapter = new SearchAdapter(
                SearchActivity.this,
                stationArrayList,
                new SearchAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(SubwayStation data, final View v) {
                        if(v.getId() == R.id.btn_add_prefer_station){
                            NetworkUtils.preferenceAddPreferStation(new AddPreferStationRequest(data.getSubwayStationId()),
                                    Info.getInstance().getToken(),
                                    new Response.Listener<Result>() {
                                        @Override
                                        public void onResponse(Result response) {
                                            Toast.makeText(SearchActivity.this, response.getResultDescription(), Toast.LENGTH_SHORT).show();
                                            v.setVisibility(View.GONE);
                                            updateSearchPreferAdapter();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            updateSearchPreferAdapter();
                                        }
                                    });
                        }else {
                            SEARCH_STATUS = getBundle().getInt("requestCode");
                            setSearchResult(data);
                            finish();
                        }
                    }
                });
        listView.setAdapter(searchAdapter);
        listView.setVisibility(View.GONE);
        searchAdapter.animateTo(stationArrayList);
        searchAdapter.animateTo(stationArrayList);

        preferListView = (RecyclerView) findViewById(R.id.prefer_list);
        preferListView.setLayoutManager(new LinearLayoutManager(this));
        RecycleViewDivider dividerLine = new RecycleViewDivider(RecycleViewDivider.VERTICAL);
        dividerLine.setSize(4);
        dividerLine.setColor(0xFFDDDDDD);
        preferListView.addItemDecoration(dividerLine);
        updateSearchPreferAdapter();
    }


    private void updateSearchPreferAdapter(){
        if(Info.getInstance().getToken() == null){
            Info.historyRoutes = null;
            Info.preferRoutes = null;
            Info.preferStations = null;
            setSearchPreferAdapter();
        }else {
            NetworkUtils.preferencePreferStation(Info.getInstance().getToken(),
                    new Response.Listener<PreferStationListResult>() {
                        @Override
                        public void onResponse(PreferStationListResult response) {
                            Info.preferStations = response.getPreferSubwayStationList();
                            setSearchPreferAdapter();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Info.preferStations = null;
                            setSearchPreferAdapter();
                        }
                    });

            NetworkUtils.preferencePrefeRoute(Info.getInstance().getToken(),
                    new Response.Listener<PreferRouteListResult>() {
                        @Override
                        public void onResponse(PreferRouteListResult response) {
                            Info.preferRoutes = response.getPreferRouteList();
                            setSearchPreferAdapter();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Info.preferRoutes = null;
                            setSearchPreferAdapter();
                        }
                    });

            NetworkUtils.preferenceHistory(
                    Info.getInstance().getToken(),
                    new Response.Listener<HistoryRouteListResult>() {
                        @Override
                        public void onResponse(HistoryRouteListResult response) {
                            Info.historyRoutes = response.getHistoryRouteList();
                            setSearchPreferAdapter();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Info.historyRoutes = null;
                            setSearchPreferAdapter();
                        }
                    });

        }
    }

    private void setSearchPreferAdapter(){
        ArrayList<Object> objects = new ArrayList<>();
        if(Info.preferStations != null) {
            for (PreferSubwayStation ps : Info.preferStations) {
                objects.add(ps.getSubwayStation());
            }
        }
        if(Info.preferRoutes != null){
            for (PreferRoute pr : Info.preferRoutes){
                objects.add(pr);
            }
        }
        if(Info.historyRoutes != null){
            for (HistoryRoute hr : Info.historyRoutes){
                boolean addFlag = true;
                if(Info.preferRoutes != null){
                    for (PreferRoute pr : Info.preferRoutes){
                        if(hr.getStartStationId() == pr.getStartStationId() &&
                                hr.getEndStartionId() == pr.getEndStationId()){
                            addFlag = false;
                            break;
                        }
                    }
                }
                if(addFlag)
                    objects.add(hr);
            }
        }
        SearchPreferAdapter searchPreferAdapter = new SearchPreferAdapter(
                this,
                objects,
                new SearchPreferAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, Object data, int mode) {
                        PreferRoute preferRoute;
                        HistoryRoute historyRoute;
                        switch (mode) {
                            case SearchPreferAdapter.STATUS_COME:
                                SEARCH_STATUS = EDIT_TEXT_REQUEST_CODE_START;
                                setSearchResult((SubwayStation) data);
                                break;
                            case SearchPreferAdapter.STATUS_GO:
                                SEARCH_STATUS = EDIT_TEXT_REQUEST_CODE_END;
                                setSearchResult((SubwayStation) data);
                                break;
                            case SearchPreferAdapter.STATUS_REMOVE_PREFER_ROUTE:
                                preferRoute = (PreferRoute) data;
                                NetworkUtils.preferenceRemovePreferRoute(preferRoute.getStartStationId(), preferRoute.getEndStationId(),
                                        Info.getInstance().getToken(), new Response.Listener<Result>() {
                                            @Override
                                            public void onResponse(Result response) {
                                                Toast.makeText(SearchActivity.this, response.getResultDescription(), Toast.LENGTH_SHORT).show();
                                                updateSearchPreferAdapter();
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                updateSearchPreferAdapter();
                                            }
                                        });
                                return;
                            case SearchPreferAdapter.STATUS_ADD_PREFER_ROUTE:
                                historyRoute = (HistoryRoute)data;
                                NetworkUtils.preferenceAddPreferRoute(new AddPreferRouteRequest(historyRoute.getStartStationId(),
                                                historyRoute.getEndStartionId()), Info.getInstance().getToken(),
                                        new Response.Listener<Result>() {
                                            @Override
                                            public void onResponse(Result response) {
                                                Toast.makeText(SearchActivity.this, response.getResultDescription(), Toast.LENGTH_SHORT).show();
                                                updateSearchPreferAdapter();
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                updateSearchPreferAdapter();
                                            }
                                        });
                                return;
                            case SearchPreferAdapter.STATUS_REMOVE_PREFER_STATION:
                                NetworkUtils.preferenceRemovePreferStation(((SubwayStation)data).getSubwayStationId(),
                                        Info.getInstance().getToken(),
                                        new Response.Listener<Result>() {
                                            @Override
                                            public void onResponse(Result response) {
                                                Toast.makeText(SearchActivity.this, response.getResultDescription(), Toast.LENGTH_SHORT).show();
                                                updateSearchPreferAdapter();
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                updateSearchPreferAdapter();
                                            }
                                        });
                                return;
                            case SearchPreferAdapter.VIEW_TYPE_PREFER_ROUTE:
                                SEARCH_STATUS = EDIT_TEXT_REQUEST_CODE_BOTH;
                                preferRoute = (PreferRoute) data;;
                                setSearchResult(preferRoute.getStartStation(), preferRoute.getEndStation());
                                break;
                            case SearchPreferAdapter.VIEW_TYPE_HISTORY_ROUTE:
                                SEARCH_STATUS = EDIT_TEXT_REQUEST_CODE_BOTH;
                                historyRoute = (HistoryRoute)data;
                                setSearchResult(historyRoute.getStartStation(), historyRoute.getEndStation());
                                break;
                        }
                        finish();
                    }
                });

        preferListView.setAdapter(searchPreferAdapter);
        listView.setAdapter(searchAdapter);
    }

    @Override
    public void onBackPressed() {
        if(listView.getVisibility() == View.VISIBLE){
            listView.setVisibility(View.GONE);
            preferListView.setVisibility(View.VISIBLE);
            return;
        }
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
                                                stationArrayList.add(s);
                                                Log.e("Data", "Add:!" + s.getSubwayStationId() + s.getSubwayStationName());
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
                        try {
                            GsonUtils.Response r = GsonUtils.resolveErrorResponse(error);
                            Toast.makeText(getApplicationContext(), r.result_description, Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException e) {
                            Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return true;
    }

    private void initString() {
        result = new Bundle();
    }

    private void setSearchResult(SubwayStation start, SubwayStation end) {
        Gson gson = new Gson();
        result.putString(KEY_START, gson.toJson(start));
        putIntent(result);
        result.putString(KEY_END, gson.toJson(end));
        putIntent(result);
    }

    private void setSearchResult(SubwayStation s) {
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
        preferListView.setVisibility(View.GONE);
        searchAdapter.animateTo(filter(stationArrayList, newText));
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
    private List<SubwayStation> filter(List<SubwayStation> list, String query) {
        if (TextUtils.isEmpty(query)) {
            return stationArrayList;
        } else {
            query = query.toLowerCase();
            final List<SubwayStation> filteredModelList = new ArrayList<>();
            for (SubwayStation station : list) {
                if (station.getSubwayStationName().contains(query)) {
                    filteredModelList.add(station);
                }else if(station.getSubwayStationEnglishName().toLowerCase().contains(query)){
                    filteredModelList.add(station);
                }else if(station.getSubwayStationAbbrName().toLowerCase().contains(query)){
                    filteredModelList.add(station);
                }
            }
            return filteredModelList;
        }
    }
}

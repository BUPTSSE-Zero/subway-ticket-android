package cn.crepusculo.subway_ticket_android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dpizarro.uipicker.library.picker.PickerUI;
import com.dpizarro.uipicker.library.picker.PickerUISettings;
import com.google.gson.Gson;
import com.subwayticket.database.model.SubwayLine;
import com.subwayticket.database.model.SubwayStation;
import com.subwayticket.model.result.SubwayLineListResult;
import com.subwayticket.model.result.SubwayStationListResult;

import java.util.ArrayList;
import java.util.List;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.Station;
import cn.crepusculo.subway_ticket_android.ui.adapter.SearchHistoryAdapter;
import cn.crepusculo.subway_ticket_android.ui.adapter.StationDisplayAdapter;
import cn.crepusculo.subway_ticket_android.utils.GsonUtils;
import cn.crepusculo.subway_ticket_android.utils.NetworkUtils;

public class StationDisplayActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private TextView progressTextView;
    private PickerUI pickerUI;
    private FloatingActionButton fab;

    private int SEARCH_STATUS;
    private int id;
    private int shownFlag = 0;

    private List<String> linesName;
    private List<SubwayLine> lines;
    private ArrayList<SubwayStation> stations;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_station_display;
    }

    @Override
    protected void initView() {
        bindViews();
        initLines();
        initPickUI();
        initFAB();
        initRecycleView();
    }

    private void bindViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressTextView = (TextView) findViewById(R.id.progress_text_view);
        pickerUI = (PickerUI) findViewById(R.id.picker_ui_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void initLines() {
        linesName = new ArrayList<>();
        lines = new ArrayList<>();
        NetworkUtils.subwayGetLineListByCity(
                getString(R.string.BEIJING_ID),
                new Response.Listener<SubwayLineListResult>() {
                    @Override
                    public void onResponse(SubwayLineListResult response) {
                        lines = response.getSubwayLineList();
                        for (SubwayLine line : lines
                                ) {
                            linesName.add(line.getSubwayLineName());
                            pickerUI.setItems(StationDisplayActivity.this, linesName);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /**
                         * 获取失败
                         */
                        try {
                            GsonUtils.Response r = GsonUtils.resolveErrorResponse(error);
                            progressTextView.setText(r.result_description);
                        } catch (NullPointerException e) {
                            Snackbar.make(findViewById(R.id.view), "网络访问超时", Snackbar.LENGTH_LONG).show();
                            progressTextView.setText("网络访问超时");
                        }
                    }
                });
    }

    private void initPickUI() {
        id = 0;
        PickerUISettings pickerUISettings = new PickerUISettings.Builder()
                .withUseBlur(false)
                .withItemsClickables(true)
                .withAutoDismiss(false)
                .withColorTextCenter(R.color.primary)
                .withLinesColor(R.color.primary_light)
                .build();
        pickerUI.setSettings(pickerUISettings);
        pickerUI.setOnClickItemPickerUIListener(new PickerUI.PickerUIItemClickListener() {
            @Override
            public void onItemClickPickerUI(int which, int position, String valueResult) {
                shownFlag += 1;

                final int mPosition = position;
                final String mValueResult = valueResult;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shownFlag -= 1;
                        Log.e("flag", shownFlag + "");
                        if (shownFlag <= 1) {
                            shownFlag = 0;
                            id = mPosition;
                            pickerUI.slide();
                            fab.setVisibility(View.VISIBLE);
                            fab.startAnimation(AnimationUtils.loadAnimation(
                                    StationDisplayActivity.this, R.anim.fade_in_center
                                    )
                            );
                            if (!lines.isEmpty()) {
                                reloadRecycleView();
                            }
                        }
                    }
                }, 1500);
            }
        });

    }

    private void initFAB() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shownFlag += 1;
                if (!pickerUI.isPanelShown()) {
                    pickerUI.slide(id);
                    fab.startAnimation(
                            AnimationUtils.loadAnimation(
                                    StationDisplayActivity.this, R.anim.fade_out_center
                            )
                    );
                    fab.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initRecycleView() {
        stations = new ArrayList<>();

        if (!lines.isEmpty()) {
            reloadRecycleView();
        }

        if (!stations.isEmpty()) {
            RecyclerView.LayoutManager layoutManager;
            layoutManager = new LinearLayoutManager(this);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            StationDisplayAdapter adapter = new StationDisplayAdapter(
                    StationDisplayActivity.this,
                    stations,
                    new StationDisplayAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position, SubwayStation data, int mode) {
                            switch (mode) {
                                case SearchHistoryAdapter.STATUS_COME:
                                    setSearchResult(Station.SubwayStationAdapter(data));
                                    break;
                                case SearchHistoryAdapter.STATUS_GO:
                                    setSearchResult(Station.SubwayStationAdapter(data));
                                    break;

                            }
                            finish();
                        }
                    });
            recyclerView.setAdapter(adapter);
        }
    }


    private void reloadRecycleView() {

        NetworkUtils.subwayGetStationByLine(
                "" + lines.get(id).getSubwayLineId(),
                new Response.Listener<SubwayStationListResult>() {
                    @Override
                    public void onResponse(SubwayStationListResult response) {
                        stations = new ArrayList<>(response.getSubwayStationList());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            GsonUtils.Response r = GsonUtils.resolveErrorResponse(error);
                            progressTextView.setText(r.result_description);
                        } catch (NullPointerException e) {
                            progressTextView.setText("网络访问超时");
                        }
                    }
                }
        );
    }

    private void setSearchResult(Station s) {
        Gson gson = new Gson();
        Bundle result = new Bundle();
        result.putString(
                SEARCH_STATUS == SearchActivity.EDIT_TEXT_REQUEST_CODE_START ?
                        SearchActivity.KEY_START : SearchActivity.KEY_END,
                gson.toJson(s));
        putIntent(result);
    }

    private void putIntent(Bundle b) {
        Intent intent = new Intent(StationDisplayActivity.this, MainActivity.class);
        intent.putExtras(b);
        if (SEARCH_STATUS == SearchActivity.EDIT_TEXT_REQUEST_CODE_START)
            setResult(SearchActivity.EDIT_TEXT_REQUEST_CODE_START, intent);
        else if (SEARCH_STATUS == SearchActivity.EDIT_TEXT_REQUEST_CODE_END)
            setResult(SearchActivity.EDIT_TEXT_REQUEST_CODE_END, intent);
        else
            setResult(SearchActivity.EDIT_TEXT_REQUEST_CODE_BOTH, intent);
    }
}

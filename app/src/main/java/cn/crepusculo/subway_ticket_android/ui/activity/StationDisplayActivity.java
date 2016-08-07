package cn.crepusculo.subway_ticket_android.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.dpizarro.uipicker.library.picker.PickerUI;
import com.dpizarro.uipicker.library.picker.PickerUISettings;

import cn.crepusculo.subway_ticket_android.R;

public class StationDisplayActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private TextView progressTextView;
    private PickerUI pickerUI;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_station_display;
    }

    @Override
    protected void initView() {
        bindViews();
        initPickUI();

    }

    private void bindViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressTextView = (TextView) findViewById(R.id.progress_text_view);
        pickerUI = (PickerUI) findViewById(R.id.picker_ui_view);
    }

    private void initPickUI() {
        PickerUISettings pickerUISettings = new PickerUISettings.Builder()
                .build();
        pickerUI.setSettings(pickerUISettings);
        pickerUI.slide();
    }
}

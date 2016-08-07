package cn.crepusculo.subway_ticket_android.ui.activity;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.dpizarro.uipicker.library.picker.PickerUI;
import com.dpizarro.uipicker.library.picker.PickerUISettings;

import cn.crepusculo.subway_ticket_android.R;

public class StationDisplayActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private TextView progressTextView;
    private PickerUI pickerUI;
    private FloatingActionButton fab;
    private int id;
    private int shownFlag = 0;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_station_display;
    }

    @Override
    protected void initView() {
        bindViews();
        initPickUI();
        initFAB();
    }

    private void bindViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressTextView = (TextView) findViewById(R.id.progress_text_view);
        pickerUI = (PickerUI) findViewById(R.id.picker_ui_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
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
                            Toast.makeText(StationDisplayActivity.this, mValueResult + " " + mPosition, Toast.LENGTH_SHORT).show();
                            id = mPosition;
                            pickerUI.slide();
                            fab.setVisibility(View.VISIBLE);
                            fab.startAnimation(AnimationUtils.loadAnimation(
                                    StationDisplayActivity.this, R.anim.fade_in_center
                                    )
                            );
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
}

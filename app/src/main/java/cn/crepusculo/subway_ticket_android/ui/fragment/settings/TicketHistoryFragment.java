package cn.crepusculo.subway_ticket_android.ui.fragment.settings;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.ui.activity.TicketDialogActivity;
import cn.crepusculo.subway_ticket_android.content.BillsCardViewContent;
import cn.crepusculo.subway_ticket_android.ui.adapter.TicketRecyclerAdapter;
import cn.crepusculo.subway_ticket_android.ui.fragment.BaseFragment;
import cn.crepusculo.subway_ticket_android.utils.SubwayLineUtil;


public class TicketHistoryFragment  extends BaseFragment {

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_ticket_history;
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView;
        recyclerView = (RecyclerView)mRootView.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        ArrayList<BillsCardViewContent> itemsData = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            BillsCardViewContent b = new BillsCardViewContent();
            b.ForTest(i);
            b.start_line = i+1;
            b.destination_line = i+1;
            itemsData.add(b);
        }

        TicketRecyclerAdapter adapter = new TicketRecyclerAdapter(this.getActivity(), itemsData,
                new TicketRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BillsCardViewContent item) {
                Log.e("Fragment/onItemClick",""+item);
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//                    final Scene scene = Scene.getSceneForLayout(mContainer, R.layout.item_dialog_bills, mActivity);
//                    TransitionManager.go(scene);
                }
                showDialog(item);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void showDialog(BillsCardViewContent item){
//        Bundle bundle = new Bundle();
//        bundle.putString("BILLS",new Gson().toJson(item));
//        jumpToActivity(TicketDialogActivity.class,bundle);
        Rect displayRectangle = new Rect();
        Window window = mActivity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        /* inflate and adjust layout */
        LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_dialog_bills, null);
        layout.setMinimumWidth((int)(displayRectangle.width()));
        layout.setMinimumHeight((int)(displayRectangle.height() * 0.7f));

        final Dialog dialog = new Dialog(mContext,R.style.AppTheme_Dialog);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (int)(getResources().getDisplayMetrics().widthPixels * 0.8);
        dialog.getWindow().setLayout(width, height);

        TextView start;
        TextView destination;
        TextView status;
        TextView date;
        BillsCardViewContent bills;
        List<Button> buttons;

        final ExpandableLinearLayout subTitleLayout;
        final ExpandableRelativeLayout buttonLayout;
        final ExpandableLinearLayout qrLayout;

        dialog.setContentView(layout);
        dialog.getWindow().setLayout(width,height);
        dialog.show();

        start = (TextView)dialog.findViewById(R.id.start_dialog);
        destination = (TextView)dialog.findViewById(R.id.destination_dialog);
        status =(TextView)dialog.findViewById(R.id.status);
        date = (TextView)dialog.findViewById(R.id.date);

        subTitleLayout = (ExpandableLinearLayout)dialog.findViewById(R.id.expanded_sub_title);
        buttonLayout = (ExpandableRelativeLayout)dialog.findViewById(R.id.expanded_btn);
        qrLayout = (ExpandableLinearLayout)dialog.findViewById(R.id.expanded_qr);

        start.setText(item.start);
        destination.setText(item.destination);
        date.setText("2017-4-26");
        status.setText(item.getStatus());
        ImageView v_s = (ImageView)dialog.findViewById(R.id.come_dialog);
        ImageView v_d = (ImageView)dialog.findViewById(R.id.go_dialog);
//        v_s.setColorFilter(R.color.accent);
        BillsCardViewContent.setTagColor(mContext, v_s, SubwayLineUtil.getColor(item.start_line),
                v_d,SubwayLineUtil.getColor(item.destination_line));

        buttons = new ArrayList<>();

        buttons.add((Button)dialog.findViewById(R.id.dialog_btn_3));
        buttons.add((Button)dialog.findViewById(R.id.dialog_btn_1));
        buttons.add((Button)dialog.findViewById(R.id.dialog_btn_2));

        for (final Button button : buttons){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()){
                        case R.id.dialog_btn_3:
                            dialog.dismiss();
                            return;
                        case R.id.dialog_btn_1:
                            if(subTitleLayout.isExpanded()){
                                subTitleLayout.collapse();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        qrLayout.expand();
                                    }
                                },300);}
                            else {
                                qrLayout.collapse();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        subTitleLayout.expand();
                                    }
                                },400);}
                            return;
                        case R.id.dialog_btn_2:
                            return;
                        default:
                    }
                }
            });
        } // for
    }
}
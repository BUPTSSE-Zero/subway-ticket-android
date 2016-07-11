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
import cn.crepusculo.subway_ticket_android.content.TicketDialogMaker;
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
                TicketDialogMaker t = new TicketDialogMaker(mActivity,mContext,item);
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
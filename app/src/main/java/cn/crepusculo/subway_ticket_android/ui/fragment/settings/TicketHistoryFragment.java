package cn.crepusculo.subway_ticket_android.ui.fragment.settings;

import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.subwayticket.model.result.CityListResult;

import java.util.ArrayList;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.BillsCardViewContent;
import cn.crepusculo.subway_ticket_android.content.TicketDialogMaker;
import cn.crepusculo.subway_ticket_android.ui.adapter.TicketRecyclerAdapter;
import cn.crepusculo.subway_ticket_android.ui.fragment.BaseFragment;
import cn.crepusculo.subway_ticket_android.utils.NetworkUtils;


public class TicketHistoryFragment extends BaseFragment {

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_ticket_history;
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView;
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        ArrayList<BillsCardViewContent> itemsData = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            BillsCardViewContent b = new BillsCardViewContent();
            b.ForTest(i);
            b.start.setLine(i + 1);
            b.end.setLine(i + 1);
            itemsData.add(b);
        }

        TicketRecyclerAdapter adapter = new TicketRecyclerAdapter(this.getActivity(), itemsData,
                new TicketRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BillsCardViewContent item) {
                        Log.e("Fragment/onItemClick", "" + item);
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//                    final Scene scene = Scene.getSceneForLayout(mContainer, R.layout.item_dialog_bills, mActivity);
//                    TransitionManager.go(scene);
                        }
                        TicketDialogMaker t = new TicketDialogMaker(mActivity, mContext, item);
                    }
                });
        recyclerView.setAdapter(adapter);
    }
}
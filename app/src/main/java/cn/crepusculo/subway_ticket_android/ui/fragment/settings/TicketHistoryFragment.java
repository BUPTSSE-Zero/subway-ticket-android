package cn.crepusculo.subway_ticket_android.ui.fragment.settings;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.TicketDialogMaker;
import cn.crepusculo.subway_ticket_android.content.TicketOrder;
import cn.crepusculo.subway_ticket_android.ui.adapter.TicketRecyclerAdapter;
import cn.crepusculo.subway_ticket_android.ui.fragment.BaseFragment;
import cn.crepusculo.subway_ticket_android.util.TestUtils;


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

        // Fake date here to display
        ArrayList<TicketOrder> itemsData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            char c;
            c = (i > 18 ? TicketOrder.ORDER_STATUS_FINISHED : TicketOrder.ORDER_STATUS_REFUNDED);
            TicketOrder b = TestUtils.BuildTicketOrder(i, 30 - i, c);
            itemsData.add(b);
        }

        TicketRecyclerAdapter adapter = new TicketRecyclerAdapter(this.getActivity(), itemsData,
                new TicketRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(TicketOrder item, TicketRecyclerAdapter.Holder holder) {
                        new TicketDialogMaker(mActivity, mContext, item);
                        Log.e("Fragment/onClick", "" + item);
                    }
                });
        recyclerView.setAdapter(adapter);
    }
}
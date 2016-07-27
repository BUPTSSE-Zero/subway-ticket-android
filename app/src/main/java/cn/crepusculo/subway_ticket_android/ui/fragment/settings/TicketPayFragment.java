package cn.crepusculo.subway_ticket_android.ui.fragment.settings;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.subwayticket.database.model.TicketOrder;
import com.subwayticket.model.result.OrderListResult;

import java.util.ArrayList;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.BillsCardViewContent;
import cn.crepusculo.subway_ticket_android.content.TicketDialogMaker;
import cn.crepusculo.subway_ticket_android.ui.adapter.TicketRecyclerAdapter;
import cn.crepusculo.subway_ticket_android.ui.fragment.BaseFragment;
import cn.crepusculo.subway_ticket_android.utils.NetworkUtils;

public class TicketPayFragment extends BaseFragment {

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_ticket_pay;
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
        NetworkUtils.ticketOrderGetOrderListByStatusAndStartTimeAndEndTime(
                ""+TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET,
                ""+0,
                ""+(long)System.currentTimeMillis(),
                "", // FIXME::ERROR AUTHTOKEN
                new Response.Listener<OrderListResult>() {
                    @Override
                    public void onResponse(OrderListResult response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mActivity,"ERROR",Toast.LENGTH_SHORT).show();
                    }
                });
        for (int i = 0; i < 30; i++) {
            itemsData.add(new BillsCardViewContent());
        }

        TicketRecyclerAdapter adapter = new TicketRecyclerAdapter(this.getActivity(), itemsData,
                new TicketRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BillsCardViewContent item) {
                        TicketDialogMaker t = new TicketDialogMaker(mActivity, mContext, item);
                    }
                });
        recyclerView.setAdapter(adapter);
    }


}
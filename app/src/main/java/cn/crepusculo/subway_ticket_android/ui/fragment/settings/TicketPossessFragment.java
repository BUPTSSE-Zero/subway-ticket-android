package cn.crepusculo.subway_ticket_android.ui.fragment.settings;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.subwayticket.database.model.TicketOrder;
import com.subwayticket.model.result.OrderListResult;

import java.util.ArrayList;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.BillsCardViewContent;
import cn.crepusculo.subway_ticket_android.content.Station;
import cn.crepusculo.subway_ticket_android.content.TicketDialogMaker;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.ui.adapter.TicketRecyclerAdapter;
import cn.crepusculo.subway_ticket_android.ui.fragment.BaseFragment;
import cn.crepusculo.subway_ticket_android.utils.GsonUtils;
import cn.crepusculo.subway_ticket_android.utils.NetworkUtils;

public class TicketPossessFragment extends BaseFragment {
    final public ArrayList<TicketOrder> ticketOrderArrayList = new ArrayList<>();

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_ticket_possess;
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
                "" + TicketOrder.ORDER_STATUS_NOT_PAY,
                "0",
                System.currentTimeMillis() + "",
                Info.getInstance().getToken(),
                new Response.Listener<OrderListResult>() {
                    @Override
                    public void onResponse(OrderListResult response) {
                        Log.e("Get Response!", "list");
                        for (TicketOrder order : response.getTicketOrderList()
                                ) {
                            ticketOrderArrayList.add(order);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            GsonUtils.Response r = GsonUtils.resolveErrorResponse(error);

                        } catch (NullPointerException e) {
                            Snackbar.make(mRootView, "网络访问超时", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
        );

        for (int i = 0; i < ticketOrderArrayList.size(); i++) {
            TicketOrder order = ticketOrderArrayList.get(i);
            BillsCardViewContent content = new BillsCardViewContent();
            content.start = Station.SubwayStationAdapter(order.getStartStation());
            content.end = Station.SubwayStationAdapter(order.getEndStation());
            content.date = order.getTicketOrderTime().toString();
            content.status = order.getStatus();
            content.price = order.getTicketPrice();
            itemsData.add(content);
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

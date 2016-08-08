package cn.crepusculo.subway_ticket_android.ui.fragment.settings;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.subwayticket.database.model.TicketOrder;
import com.subwayticket.model.result.OrderListResult;

import java.util.ArrayList;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.TicketDialogMaker;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.ui.adapter.TicketRecyclerAdapter;
import cn.crepusculo.subway_ticket_android.ui.fragment.BaseFragment;
import cn.crepusculo.subway_ticket_android.utils.GsonUtils;
import cn.crepusculo.subway_ticket_android.utils.NetworkUtils;

public class TicketPayFragment extends BaseFragment {
    ArrayList<TicketOrder> serverResult = new ArrayList<>();
    ArrayList<cn.crepusculo.subway_ticket_android.content.TicketOrder> itemsData = new ArrayList<>();

    TextView textView;
    RecyclerView recyclerView;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_ticket_pay;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        textView = (TextView) mRootView.findViewById(R.id.textView);

        // init serverResult here
        initArrayFromServer();

        if (!serverResult.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            /**
             * Get Data from Sever Successful
             */
            for (TicketOrder result : serverResult
                    ) {
                /**
                 * Get server result and load them to @params itemData
                 */
                cn.crepusculo.subway_ticket_android.content.TicketOrder data =
                        new cn.crepusculo.subway_ticket_android.content.TicketOrder(result);
                itemsData.add(data);

                /**
                 * Build recyclerView manager
                 */
                RecyclerView.LayoutManager layoutManager;
                layoutManager = new LinearLayoutManager(getActivity());

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);

                /**
                 * Build recyclerView adapter
                 */
                TicketRecyclerAdapter adapter = new TicketRecyclerAdapter(this.getActivity(), itemsData,
                        new TicketRecyclerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(cn.crepusculo.subway_ticket_android.content.TicketOrder item, TicketRecyclerAdapter.Holder holde) {
                                TicketDialogMaker t = new TicketDialogMaker(mActivity, mContext, item);
                            }
                        });
                recyclerView.setAdapter(adapter);
            }
        } else {
            /**
             * Else load cache text view
             */
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }


    }

    protected void initArrayFromServer() {
        NetworkUtils.ticketOrderGetOrderListByStatusAndStartTimeAndEndTime(
                "" + TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET,
                "0",
                "" + System.currentTimeMillis(),
                Info.getInstance().getToken(),
                new Response.Listener<OrderListResult>() {
                    @Override
                    public void onResponse(OrderListResult response) {
                        for (TicketOrder order : response.getTicketOrderList()
                                ) {
                            serverResult.add(order);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            GsonUtils.Response r = GsonUtils.resolveErrorResponse(error);
                            textView.setText(r.result_description);
                        } catch (NullPointerException e) {
                            Snackbar.make(mRootView, "网络访问超时", Snackbar.LENGTH_LONG).show();
                            textView.setText("网络访问超时");
                        }
                    }
                });
    }
}
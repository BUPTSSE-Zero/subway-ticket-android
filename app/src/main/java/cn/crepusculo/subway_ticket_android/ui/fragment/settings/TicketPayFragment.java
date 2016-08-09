package cn.crepusculo.subway_ticket_android.ui.fragment.settings;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import cn.crepusculo.subway_ticket_android.util.GsonUtils;
import cn.crepusculo.subway_ticket_android.util.NetworkUtils;

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

        initCacheView();
        initArrayFromServer();


    }

    protected void initCacheView() {
        textView = (TextView) mRootView.findViewById(R.id.textView);
    }

    protected void initArrayFromServer() {
        Log.e("PayFragment", "initArrayFromServer");
        NetworkUtils.ticketOrderGetOrderListByStatusAndStartTimeAndEndTime(
                "" + TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET,
                "0",
                "" + System.currentTimeMillis(),
                Info.getInstance().getToken(),
                new Response.Listener<OrderListResult>() {
                    @Override
                    public void onResponse(OrderListResult response) {
                        Log.e("PayFragment", "Success get TicketOrder" + response.getTicketOrderList().size());
                        serverResult = new ArrayList<TicketOrder>(response.getTicketOrderList());
                        Log.e("PayFragment", "Local TicketOrder Size" + serverResult.size());
                        convertData();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            GsonUtils.Response r = GsonUtils.resolveErrorResponse(error);
                            Log.e("PayFragment", "Error" + r.result_description);
                            textView.setText(r.result_description);
                        } catch (NullPointerException e) {
                            if (error != null) {
                                Snackbar.make(mRootView, error.getMessage(), Snackbar.LENGTH_LONG).show();
                                textView.setText(error.getMessage());
                            } else {
                                Snackbar.make(mRootView, "网络访问超时", Snackbar.LENGTH_LONG).show();
                                textView.setText("网络访问超时");
                            }
                        }
                    }
                });
    }

    private void showDisplayView(int mode) {
        if (mode == Mode.PROGRESS) {
            recyclerView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else if (mode == Mode.RECYCLE) {
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }
    }

    private void convertData() {
        if (!serverResult.isEmpty()) {
            /**
             * Get Data from Sever Successful
             */
            for (TicketOrder result : serverResult
                    ) {
                /**
                 * Get server result and load them to @params itemData
                 */
                Log.e("PayFragment", "Load a time");
                cn.crepusculo.subway_ticket_android.content.TicketOrder data =
                        new cn.crepusculo.subway_ticket_android.content.TicketOrder(result);
                itemsData.add(data);
            }
            Log.e("PayFragment", "ItemsDate.Size()" + itemsData.size());
            RecyclerView.LayoutManager layoutManager;
            layoutManager = new LinearLayoutManager(getActivity());

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);

            TicketRecyclerAdapter adapter = new TicketRecyclerAdapter(this.getActivity(), itemsData,
                    new TicketRecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(cn.crepusculo.subway_ticket_android.content.TicketOrder item, TicketRecyclerAdapter.Holder holder) {
                            TicketDialogMaker t = new TicketDialogMaker(mActivity, mContext, item);
                        }
                    });
            recyclerView.setAdapter(adapter);
            showDisplayView(Mode.RECYCLE);
        } else {
            /**
             * Else load cache text view
             */
            showDisplayView(Mode.PROGRESS);
        }
    }

    private class Mode {
        public static final int PROGRESS = 1;
        public static final int RECYCLE = 2;
    }
}

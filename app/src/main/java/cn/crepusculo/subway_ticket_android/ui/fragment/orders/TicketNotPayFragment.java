package cn.crepusculo.subway_ticket_android.ui.fragment.orders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.subwayticket.database.model.TicketOrder;
import com.subwayticket.model.result.OrderListResult;

import java.util.ArrayList;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.ui.activity.DisplayActivity;
import cn.crepusculo.subway_ticket_android.ui.activity.TicketManagerActivity;
import cn.crepusculo.subway_ticket_android.ui.adapter.TicketRecyclerAdapter;
import cn.crepusculo.subway_ticket_android.ui.fragment.BaseFragment;
import cn.crepusculo.subway_ticket_android.util.GsonUtils;
import cn.crepusculo.subway_ticket_android.util.NetworkUtils;

public class TicketNotPayFragment extends BaseFragment {
    private ArrayList<TicketOrder> serverResult = new ArrayList<>();
    private TextView textView;
    private RecyclerView recyclerView;
    private boolean refreshFlag = false;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_ticket_not_pay;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        textView = (TextView) mRootView.findViewById(R.id.textView);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(refreshFlag){
            ((TicketManagerActivity)getActivity()).setRefreshNotExtract(true);
            ((TicketManagerActivity)getActivity()).setRefreshNotPay(false);
            ((TicketManagerActivity)getActivity()).setRefreshHistory(true);
            refreshDataFromServer();
            refreshFlag = false;
        }
    }

    public void refreshDataFromServer() {
        Log.e("NotpayFragment", "refreshDataFromServer");
        NetworkUtils.ticketOrderGetOrderListByStatusAndStartTimeAndEndTime(
                "" + TicketOrder.ORDER_STATUS_NOT_PAY,
                "0",
                "" + System.currentTimeMillis(),
                Info.getInstance().getToken(),
                new Response.Listener<OrderListResult>() {
                    @Override
                    public void onResponse(OrderListResult response) {
                        Log.e("NotpayFragment", "Success get TicketOrder" + response.getTicketOrderList().size());
                        serverResult.clear();
                        serverResult.addAll(response.getTicketOrderList());
                        convertData();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        serverResult.clear();
                        try {
                            GsonUtils.Response r = GsonUtils.resolveErrorResponse(error);
                            Log.e("NotpayFragment", "Error" + r.result_description);
                            textView.setText(r.result_description);
                        } catch (NullPointerException e) {
                            textView.setText(R.string.network_error);
                        }
                        convertData();
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

            RecyclerView.LayoutManager layoutManager;
            layoutManager = new LinearLayoutManager(getActivity());

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);

            TicketRecyclerAdapter adapter = new TicketRecyclerAdapter(this.getActivity(), serverResult,
                    new TicketRecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(TicketOrder item, TicketRecyclerAdapter.Holder holde) {
                            Bundle b = new Bundle();
                            b.putString(DisplayActivity.BUNDLE_KEY_ORDER, new Gson().toJson(item));
                            Intent intent = new Intent(getActivity(), DisplayActivity.class);
                            intent.putExtras(b);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
                            refreshFlag = true;
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

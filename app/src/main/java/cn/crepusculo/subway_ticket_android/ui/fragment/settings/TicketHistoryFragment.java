package cn.crepusculo.subway_ticket_android.ui.fragment.settings;

import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.BillsCardViewContent;
import cn.crepusculo.subway_ticket_android.content.TicketDialogMaker;
import cn.crepusculo.subway_ticket_android.ui.adapter.TicketRecyclerAdapter;
import cn.crepusculo.subway_ticket_android.ui.fragment.BaseFragment;


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
                    public void onItemClick(BillsCardViewContent item, TicketRecyclerAdapter.Holder holder) {
                        Log.e("Fragment/onItemClick", "" + item);

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//                    final Scene scene = Scene.getSceneForLayout(mContainer, R.layout.item_dialog_bills, mActivity);
//                    TransitionManager.go(scene);
                        }
                        TicketDialogMaker t = new TicketDialogMaker(mActivity, mContext, item);
                        holder.blurLayout.showHover();
                        holder.hover.bringToFront();
                        Log.e("Fragment/onItemClick", "" + holder.hover.isShown());
                    }
                });
        recyclerView.setAdapter(adapter);
    }
}
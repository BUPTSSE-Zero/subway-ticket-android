package cn.crepusculo.subway_ticket_android.ui.adapter;

import android.content.Context;
import android.content.res.Resources;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;

import java.util.ArrayList;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.ui.activity.content.BillsCardViewContent;

public class TicketRecyclerAdapter extends RecyclerView.Adapter<TicketRecyclerAdapter.Holder> {
    private Context context;
    private ArrayList<BillsCardViewContent> dataset = new ArrayList<>();
    private Resources res;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(BillsCardViewContent item);
    }

    public TicketRecyclerAdapter(Context context, ArrayList<BillsCardViewContent> dataset
            , OnItemClickListener listener) {
        this.context = context;
        this.dataset.clear();
        this.dataset.addAll(dataset);
        this.res = context.getResources();
        this.listener = listener;
    }

    /**
     * @param  position: position in list
     * @return  viewType: int viewType
     */
    @Override
    public int getItemViewType(int position) {
        // custom view by its position here
        // default return 0
//        int viewType = super.getItemViewType(position);
        int viewType = 0;
        return viewType;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_layout_ticket, parent, false);
        return new Holder(v);
    }

    public static class Holder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public CardView mCardView;
        public ExpandableLinearLayout expandableLinearLayout_raw;
        public ExpandableLinearLayout expandableLinearLayout_expand;

        public Holder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.card_view);
            expandableLinearLayout_expand = (ExpandableLinearLayout)v.findViewById(R.id.compat_expand);
            expandableLinearLayout_raw = (ExpandableLinearLayout)v.findViewById(R.id.compat_collapse);
        }

        public void bind(final BillsCardViewContent item, final OnItemClickListener listener){
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                    Log.e("Adapter/onClick",""+item);
                    if (expandableLinearLayout_raw.isExpanded()) {
                        expandableLinearLayout_raw.collapse();
                        expandableLinearLayout_expand.expand();
                    }
                    else {
                        expandableLinearLayout_raw.expand();
                        expandableLinearLayout_expand.collapse();
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        initCardView(holder.mCardView);
        holder.bind(dataset.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    protected void initCardView(CardView cardView){

    }

}

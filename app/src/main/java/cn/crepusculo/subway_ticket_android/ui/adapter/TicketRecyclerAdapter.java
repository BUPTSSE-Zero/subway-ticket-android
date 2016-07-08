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
        public CardView mCardView;
        public ExpandableLinearLayout expandableLinearLayout_raw;
        public ExpandableLinearLayout expandableLinearLayout_expand;
        public TextView titleCollapse;
        public TextView subtitleCollapse;
        public TextView statusCollapse;
        public TextView dateCollapse;
        public TextView titleExpanded1;
        public TextView titleExpanded2;
        public TextView subtitleExpanded;
        public TextView statusExpanded;
        public TextView dateExpanded;

        public Holder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.card_view);

            titleCollapse= (TextView) v.findViewById(R.id.title_collapse);
            subtitleCollapse= (TextView) v.findViewById(R.id.sub_title_collapse);
            statusCollapse= (TextView) v.findViewById(R.id.status_collapse);
            dateCollapse= (TextView) v.findViewById(R.id.date_collapse);


        }

        public void bind(final BillsCardViewContent item, final OnItemClickListener listener){
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /* its not work */
                    listener.onItemClick(item);
                    Log.e("Adapter/onClick",""+item);
                    }


            });
        }
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        initCardView(holder.mCardView);
        updateText(holder,position);
        holder.bind(dataset.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    protected void initCardView(CardView cardView){

    }

    protected void updateText(Holder holder,int p){
        ArrayList<TextView> a = new ArrayList<>();
        holder.titleCollapse.setText(dataset.get(p).title_collapse);
        holder.subtitleCollapse.setText(dataset.get(p).subtitle_collapse);
        holder.statusCollapse.setText(dataset.get(p).status_collapse);
//        holder.dateCollapse.setText(dataset.get(p).date_collapse);

    }

}

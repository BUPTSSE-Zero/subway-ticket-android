package cn.crepusculo.subway_ticket_android.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.BillsCardViewContent;
import cn.crepusculo.subway_ticket_android.utils.SubwayLineUtil;


public class StationRecyclerAdapter extends RecyclerView.Adapter<StationRecyclerAdapter.Holder> {
    private Context context;
    private ArrayList<BillsCardViewContent> dataset = new ArrayList<>();
    private Resources res;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(BillsCardViewContent item);
    }

    public StationRecyclerAdapter(Context context, ArrayList<BillsCardViewContent> dataset
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

        public ImageView start_p;
        public TextView start;

        public TextView destination;
        public ImageView destination_p;

        public TextView date;
        public TextView status;
        public View v;

        public Holder(View v) {
            super(v);
            this.v = v;
            mCardView = (CardView) v.findViewById(R.id.card_view);

            start= (TextView) v.findViewById(R.id.start);
            destination = (TextView) v.findViewById(R.id.destination);

            date = (TextView) v.findViewById(R.id.date);
            status= (TextView) v.findViewById(R.id.status);

            start_p = (ImageView) v.findViewById(R.id.come);
            destination_p = (ImageView) v.findViewById(R.id.go);
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
        holder.mCardView.animate();
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
        holder.start.setText(dataset.get(p).start.getName());
        holder.destination.setText(dataset.get(p).end.getName());
        holder.date.setText("231-4-26");
        holder.status.setText(dataset.get(p).getStatus());

        GradientDrawable grad_s = (GradientDrawable) holder.start.getBackground();
        GradientDrawable grad_d = (GradientDrawable) holder.destination.getBackground();

//        SubwayLineUtil.getColor(dataset.get(p).start_line);
//        SubwayLineUtil.getColor(dataset.get(p).destination_line);

        BillsCardViewContent.setTagColor(context, holder.start_p,SubwayLineUtil.getColor(dataset.get(p).start.getLine()),
                holder.destination_p,SubwayLineUtil.getColor(dataset.get(p).end.getLine()));
    }

}

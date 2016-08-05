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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.androidviewhover.BlurLayout;

import java.util.ArrayList;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.TicketOrder;
import cn.crepusculo.subway_ticket_android.utils.SubwayLineUtil;


public class TicketRecyclerAdapter extends RecyclerView.Adapter<TicketRecyclerAdapter.Holder> {
    private Context context;
    private ArrayList<TicketOrder> dataset = new ArrayList<>();
    private Resources res;
    private OnItemClickListener listener;

    public TicketRecyclerAdapter(Context context, ArrayList<TicketOrder> dataset
            , OnItemClickListener listener) {
        this.context = context;
        this.dataset.clear();
        this.dataset.addAll(dataset);
        this.res = context.getResources();
        this.listener = listener;
    }

    /**
     * @param position: position in list
     * @return viewType: int viewType
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

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.mCardView.animate();
        initCardView(holder.mCardView);
        updateText(holder, position);

        holder.hover = LayoutInflater.from(context).inflate(R.layout.item_ticket_hover, null);

        holder.blurLayout.setHoverView(holder.hover);
        holder.blurLayout.addChildAppearAnimator(holder.hover, R.id.button, Techniques.Bounce);
        holder.blurLayout.addChildDisappearAnimator(holder.hover, R.id.button, Techniques.Bounce);
        holder.hover.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Tada)
                        .duration(550)
                        .playOn(view);
            }
        });
        holder.bind(dataset.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    protected void initCardView(CardView cardView) {
    }

    protected void updateText(Holder holder, int p) {
        ArrayList<TextView> a = new ArrayList<>();
        holder.start.setText(dataset.get(p).getStartStation().getSubwayStationName());
        holder.destination.setText(dataset.get(p).getStartStation().getSubwayStationName());
        // FIXME:: Need to Decode
        holder.date.setText("2016/8/6");
        holder.status.setText(dataset.get(p).getStatus());

        GradientDrawable grad_s = (GradientDrawable) holder.start.getBackground();
        GradientDrawable grad_d = (GradientDrawable) holder.destination.getBackground();

        SubwayLineUtil.setColor(
                holder.start_p,
                SubwayLineUtil.getColor(
                        SubwayLineUtil.ToClientTypeId(
                                dataset.get(p).getStartStation().getSubwayLine().getSubwayLineId()
                        )
                )
        );
        SubwayLineUtil.setColor(
                holder.destination_p,
                SubwayLineUtil.getColor(
                        SubwayLineUtil.ToClientTypeId(
                                dataset.get(p).getEndStation().getSubwayLine().getSubwayLineId()
                        )
                )
        );

    }

    public interface OnItemClickListener {
        void onItemClick(TicketOrder item, Holder holder);
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

        public BlurLayout blurLayout;
        public View hover;

        public Holder(View v) {
            super(v);
            this.v = v;
            mCardView = (CardView) v.findViewById(R.id.card_view);

            start = (TextView) v.findViewById(R.id.start);
            destination = (TextView) v.findViewById(R.id.destination);

            date = (TextView) v.findViewById(R.id.date);
            status = (TextView) v.findViewById(R.id.status);

            start_p = (ImageView) v.findViewById(R.id.come);
            destination_p = (ImageView) v.findViewById(R.id.go);

            blurLayout = (BlurLayout) v.findViewById(R.id.hover);
        }

        public void bind(final TicketOrder item, final OnItemClickListener listener) {
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item, Holder.this);
                    Log.e("Adapter/onClick", "" + item);
                }


            });
        }
    }

}

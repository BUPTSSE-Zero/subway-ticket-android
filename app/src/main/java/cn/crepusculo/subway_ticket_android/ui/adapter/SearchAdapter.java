package cn.crepusculo.subway_ticket_android.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.subwayticket.database.model.PreferRoute;
import com.subwayticket.database.model.PreferSubwayStation;
import com.subwayticket.database.model.SubwayStation;

import java.util.ArrayList;
import java.util.List;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.Station;
import cn.crepusculo.subway_ticket_android.preferences.Info;
import cn.crepusculo.subway_ticket_android.util.SubwayLineUtil;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private final LayoutInflater mInflater;
    private List<SubwayStation> list;
    private Context mContext;
    private OnItemClickListener mListener;

    public SearchAdapter(Context context, List<SubwayStation> list, OnItemClickListener listener) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mListener = listener;
        this.list = new ArrayList<>(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.item_row_station, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SubwayStation station = list.get(position);
        holder.bind(station);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if(Info.getInstance().getToken() == null || isStationPrefer(list.get(holder.getAdapterPosition()))){
            holder.preferButton.setVisibility(View.GONE);
        }else{
            holder.preferButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Register animation
     *
     * @param models Data ArrayList
     */
    public void animateTo(List<SubwayStation> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<SubwayStation> newModels) {
        for (int i = list.size() - 1; i >= 0; i--) {
            final SubwayStation model = list.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<SubwayStation> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final SubwayStation model = newModels.get(i);
            if (!list.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<SubwayStation> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final SubwayStation model = newModels.get(toPosition);
            final int fromPosition = list.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public SubwayStation removeItem(int position) {
        final SubwayStation model = list.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, SubwayStation station) {
        list.add(position, station);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final SubwayStation station = list.remove(fromPosition);
        list.add(toPosition, station);
        notifyItemMoved(fromPosition, toPosition);
    }

    public interface OnItemClickListener {
        void onItemClick(SubwayStation data, View v);
    }

    private boolean isStationPrefer(SubwayStation s){
        if(Info.preferStations == null)
            return false;
        for(PreferSubwayStation pss : Info.preferStations){
            if(pss.getStationId() == s.getSubwayStationId())
                return true;
        }
        return false;
    }

    /**
     * ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private final TextView txtName;
        private final TextView txtLine;
        private final ImageView imageView;
        private final ImageButton preferButton;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtLine = (TextView) itemView.findViewById(R.id.txtLine);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            preferButton = (ImageButton) itemView.findViewById(R.id.btn_add_prefer_station);
        }

        /**
         * Call in `onBindViewHolder`
         */
        public void bind(final SubwayStation station) {
            String name = station.getSubwayStationName();
            String line = station.getSubwayLine().getSubwayLineName();
            txtName.setText(name);
            txtLine.setText(line);
            imageView.getDrawable().setColorFilter(
                    mContext.getResources().getColor(SubwayLineUtil.getColor(Station.SubwayStationAdapter(station).getLine())),
                    PorterDuff.Mode.SRC_ATOP);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(station, view);
                }
            });
            preferButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(station, view);
                }
            });
        }
    }
}

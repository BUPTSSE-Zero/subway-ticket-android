package cn.crepusculo.subway_ticket_android.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.subwayticket.database.model.SubwayStation;

import java.util.ArrayList;
import java.util.List;

import cn.crepusculo.subway_ticket_android.R;

public class StationDisplayAdapter extends RecyclerView.Adapter<StationDisplayAdapter.ViewHolder> {
    public static final int STATUS_START = 1;
    public static final int STATUS_END = 2;

    private final LayoutInflater mInflater;
    public Context context;
    public ArrayList<SubwayStation> stationArrayList;
    private OnItemClickListener listener;

    public StationDisplayAdapter(Context context, ArrayList<SubwayStation> stationArrayList, OnItemClickListener listener) {
        super();
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
        this.stationArrayList = stationArrayList;
    }

    @Override
    public int getItemCount() {
        return stationArrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.item_station_display, parent, false);
        Log.e("onCreateViewHolder", "inflate item_row_history_station");
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SubwayStation station = (SubwayStation) stationArrayList.get(position);
        holder.txtName.setText(station.getSubwayStationName());
        holder.goToThere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position, stationArrayList.get(position), STATUS_END);
            }
        });
        holder.comeFromThere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position, stationArrayList.get(position), STATUS_START);
            }
        });
        if (station.getStationMessage() != null) {
            holder.txtMessageTitle.setText(station.getStationMessage().getTitle());
            holder.txtMessageContent.setText(station.getStationMessage().getContent());
            String tail = station.getStationMessage().getPublisher() +
                    '\n' + station.getStationMessage().getReleaseTime().getTime();
            holder.txtMessageTail.setText(tail);
        }
        if (!station.isAvailable()) {
            ImageView v = (ImageView) holder.view.findViewById(R.id.close_image_view);
            v.setColorFilter(context.getResources().getColor(R.color.error), PorterDuff.Mode.SRC_ATOP);
            v.setVisibility(View.VISIBLE);
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
        for (int i = stationArrayList.size() - 1; i >= 0; i--) {
            final SubwayStation model = stationArrayList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<SubwayStation> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final SubwayStation model = newModels.get(i);
            if (!stationArrayList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<SubwayStation> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final SubwayStation model = newModels.get(toPosition);
            final int fromPosition = stationArrayList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public SubwayStation removeItem(int position) {
        final SubwayStation model = stationArrayList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, SubwayStation station) {
        stationArrayList.add(position, station);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final SubwayStation station = stationArrayList.remove(fromPosition);
        stationArrayList.add(toPosition, station);
        notifyItemMoved(fromPosition, toPosition);
    }

    public interface OnItemClickListener {
        void onItemClick(int position, SubwayStation data, int mode);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView txtName;
        private TextView txtMessageTitle;
        private TextView txtMessageContent;
        private TextView txtMessageTail;
        private Button goToThere;
        private Button comeFromThere;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            txtMessageTitle = (TextView) itemView.findViewById(R.id.message_title_text_view);
            txtMessageContent = (TextView) itemView.findViewById(R.id.message_content_text_view);
            txtMessageTail = (TextView) itemView.findViewById(R.id.message_tail_text_view);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            goToThere = (Button) itemView.findViewById(R.id.goToThere);
            comeFromThere = (Button) itemView.findViewById(R.id.comeFromThere);
        }
    }
}
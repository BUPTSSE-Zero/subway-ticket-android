package cn.crepusculo.subway_ticket_android.ui.adapter;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.Station;
import cn.crepusculo.subway_ticket_android.utils.SubwayLineUtil;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private final LayoutInflater mInflater;
    private List<Station> list;
    private Context mContext;

    interface OnItemClickListener {
        void onItemClick(int position, Station data);
    }

    public SearchAdapter(Context context, List<Station> list){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.list = new ArrayList<>(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtName;
        private final TextView txtLine;
        private final ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtLine = (TextView) itemView.findViewById(R.id.txtLine);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

        public void bind(Station station) {
            String name = station.getName();
            String line = station.getLine()+"号线";
            txtName.setText(name);
            txtLine.setText(line);
//            imageView.setColorFilter(SubwayLineUtil.getColor(station.getLine()));
            imageView.setColorFilter(R.color.primary);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.item_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Station station = list.get(position);
        holder.bind(station);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setStations(List<Station> newList){
        list = new ArrayList<>(newList);
    }

    public void animateTo(List<Station> models){
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Station> newModels) {
        for (int i = list.size() - 1; i >= 0; i--) {
            final Station model = list.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Station> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Station model = newModels.get(i);
            if (!list.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Station> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Station model = newModels.get(toPosition);
            final int fromPosition = list.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Station removeItem(int position) {
        final Station model = list.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Station station) {
        list.add(position, station);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Station station = list.remove(fromPosition);
        list.add(toPosition, station);
        notifyItemMoved(fromPosition, toPosition);
    }
}

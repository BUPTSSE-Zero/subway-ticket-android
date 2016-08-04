package cn.crepusculo.subway_ticket_android.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.subwayticket.database.model.PreferRoute;

import java.util.ArrayList;
import java.util.List;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.Station;


public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {
    private final LayoutInflater mInflater;
    private List<Object> list;
    public static final int STATUS_STATIONS = 0;
    public static final int STATUS_ROUTE = 1;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position, Object data);
    }

    public SearchHistoryAdapter(Context context, List<Object> list, OnItemClickListener listener) {
        mInflater = LayoutInflater.from(context);
        this.mListener = listener;
        this.list = new ArrayList<>(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView txtStart;
        private TextView txtEnd;

        private TextView txtName;
        private Button goToThere;
        private Button comeFromThere;

        public ViewHolder(View itemView, int type) {
            super(itemView);
            this.itemView = (View) itemView;
            if (type == STATUS_ROUTE) {
                //(R.layout.item_row_history_route)
                txtStart = (TextView) itemView.findViewById(R.id.txtNameStart);
                txtEnd = (TextView) itemView.findViewById(R.id.txtNameEnd);
            } else if (type == STATUS_STATIONS) {
                //(R.layout.item_row_history_station)
                txtName = (TextView) itemView.findViewById(R.id.txtName);
                goToThere = (Button) itemView.findViewById(R.id.goToThere);
                comeFromThere = (Button) itemView.findViewById(R.id.comeFromThere);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getClass().getName().equals(PreferRoute.class.getName())) {
            Log.e("getItemViewType", "TYPE:route");
            return STATUS_ROUTE;
        } else {
            Log.e("getItemViewType", "TYPE:stations");
            return STATUS_STATIONS;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == STATUS_ROUTE) {
            final View itemView = mInflater.inflate(R.layout.item_row_history_route, parent, false);
            Log.e("onCreateViewHolder", "inflate item_row_history_route");
            return new ViewHolder(itemView, STATUS_ROUTE);
        } else /*if(viewType == STATUS_STATIONS)*/ {
            final View itemView = mInflater.inflate(R.layout.item_row_history_station, parent, false);
            Log.e("onCreateViewHolder", "inflate item_row_history_station");
            return new ViewHolder(itemView, STATUS_STATIONS);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (list.get(position).getClass().getName().equals(Station.class.getName())) {
            Log.e("onBindViewHolder", "Status station");
            final Station station = (Station) list.get(position);
            holder.txtName.setText(station.getName());
            holder.goToThere.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position,list.get(position));
                }
            });
            holder.comeFromThere.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position,list.get(position));
                }
            });
        } else {
            Log.e("onBindViewHolder", "Status route");
            final PreferRoute route = (PreferRoute) list.get(position);
            holder.txtStart.setText(route.getStartStation().getSubwayStationName());
            holder.txtEnd.setText(route.getEndStation().getSubwayStationName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position,list.get(position));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

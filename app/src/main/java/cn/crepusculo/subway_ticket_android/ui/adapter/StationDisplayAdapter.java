package cn.crepusculo.subway_ticket_android.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.subwayticket.database.model.SubwayStation;

import java.util.ArrayList;

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
        final View itemView = mInflater.inflate(R.layout.item_row_history_station, parent, false);
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
    }

    public interface OnItemClickListener {
        void onItemClick(int position, SubwayStation data, int mode);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private Button goToThere;
        private Button comeFromThere;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            goToThere = (Button) itemView.findViewById(R.id.goToThere);
            comeFromThere = (Button) itemView.findViewById(R.id.comeFromThere);
        }
    }

}
package cn.crepusculo.subway_ticket_android.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.subwayticket.database.model.PreferRoute;
import com.subwayticket.database.model.SubwayLine;

import java.util.ArrayList;
import java.util.List;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.Station;


public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {
    private final LayoutInflater mInflater;
    private List<Object> list;

    public static final int STATUS_STAIONS = 0;
    public static final int STATUS_ROUTE= 1;

    interface OnItemClickListener {
        void onItemClick(int position, Station data);
    }

    public SearchHistoryAdapter(Context context, List<Object> list){
        mInflater = LayoutInflater.from(context);
        this.list = new ArrayList<>(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView txtStart;
        private TextView txtEnd;

        private TextView txtName;
        private Button goToThere;
        private Button comeFromThere;

        public ViewHolder(View itemView, int type) {
            super(itemView);
            if (type == STATUS_ROUTE) {
                //(R.layout.item_row_history_route)
                txtStart = (TextView) itemView.findViewById(R.id.txtNameStart);
                txtEnd = (TextView) itemView.findViewById(R.id.txtNameEnd);
            }
            else if (type == STATUS_STAIONS) {
                //(R.layout.item_row_history_station)
                txtName     = (TextView) itemView.findViewById(R.id.txtName);
                goToThere     = (Button) itemView.findViewById(R.id.goToThere);
                comeFromThere = (Button) itemView.findViewById(R.id.comeFromThere);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == STATUS_ROUTE){
            final View itemView = mInflater.inflate(R.layout.item_row_history_route, parent, false);
            return new ViewHolder(itemView,viewType);
        }
        else /*if(viewType == STATUS_STAIONS)*/{
            final View itemView = mInflater.inflate(R.layout.item_row_history_station, parent, false);
            return new ViewHolder(itemView,viewType);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (list.get(position).getClass().getName().equals(Station.class.getName())){
            final Station station = (Station)list.get(position);
        }
        else{
            final PreferRoute route= (PreferRoute)list.get(position);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

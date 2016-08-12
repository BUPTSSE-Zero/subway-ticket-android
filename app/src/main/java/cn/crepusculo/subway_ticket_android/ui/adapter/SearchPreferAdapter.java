package cn.crepusculo.subway_ticket_android.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.subwayticket.database.model.HistoryRoute;
import com.subwayticket.database.model.PreferRoute;
import com.subwayticket.database.model.SubwayStation;

import java.util.List;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.ui.dialog.StationMessageDialog;


public class SearchPreferAdapter extends RecyclerView.Adapter<SearchPreferAdapter.ViewHolder> {
    public static final int VIEW_TYPE_STATIONS = 0;
    public static final int VIEW_TYPE_PREFER_ROUTE = 1;
    public static final int VIEW_TYPE_HISTORY_ROUTE = 2;
    public static final int STATUS_COME = 3;
    public static final int STATUS_GO = 4;
    public static final int STATUS_REMOVE_PREFER_STATION = 6;
    public static final int STATUS_ADD_PREFER_ROUTE = 7;
    public static final int STATUS_REMOVE_PREFER_ROUTE = 8;

    private final LayoutInflater mInflater;
    private List<Object> list;
    private OnItemClickListener mListener;

    public SearchPreferAdapter(Context context, List<Object> list, OnItemClickListener listener) {
        mInflater = LayoutInflater.from(context);
        this.mListener = listener;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        /**
         * Judge type with class name
         *
         * @return Status of class on this position
         */
        if (list.get(position) instanceof PreferRoute) {
            Log.e("getItemViewType", "TYPE:perfer route");
            return VIEW_TYPE_PREFER_ROUTE;
        } else if(list.get(position) instanceof SubwayStation){
            Log.e("getItemViewType", "TYPE:prefer stations");
            return VIEW_TYPE_STATIONS;
        } else if(list.get(position) instanceof HistoryRoute){
            Log.e("getItemViewType", "TYPE:history route");
            return VIEW_TYPE_HISTORY_ROUTE;
        }
        return -1;
    }

    /**
     * @param parent   parent view
     * @param viewType Decide by classType in `getItemViewType` method
     * @return ViewHolder contain view inflate with their type
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PREFER_ROUTE) {
            final View itemView = mInflater.inflate(R.layout.item_row_prefer_route, parent, false);
            Log.e("onCreateViewHolder", "inflate item_row_prefer_route");
            return new ViewHolder(itemView, VIEW_TYPE_PREFER_ROUTE);
        } else if(viewType == VIEW_TYPE_STATIONS) {
            final View itemView = mInflater.inflate(R.layout.item_row_prefer_station, parent, false);
            Log.e("onCreateViewHolder", "inflate item_row_prefer_station");
            return new ViewHolder(itemView, VIEW_TYPE_STATIONS);
        }
        else if(viewType == VIEW_TYPE_HISTORY_ROUTE){
            final View itemView = mInflater.inflate(R.layout.item_row_history_route, parent, false);
            Log.e("onCreateViewHolder", "inflate item_row_history_route");
            return new ViewHolder(itemView, VIEW_TYPE_HISTORY_ROUTE);
        }
        return null;
    }

    /**
     * Bind data and ui here with different type
     *
     * @param holder    ViewHolder automatically call onCerateViewHolder and then put in here
     * @param position  position, never try to deal with the warning
     *
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (list.get(position) instanceof SubwayStation) {
            Log.e("onBindViewHolder", "Status station");
            final SubwayStation station = (SubwayStation) list.get(position);
            holder.txtName.setText(station.getDisplayName());
            holder.goToThere.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position, list.get(position), STATUS_GO);
                }
            });
            holder.comeFromThere.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position, list.get(position), STATUS_COME);
                }
            });
            holder.preferButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position, list.get(position), STATUS_REMOVE_PREFER_STATION);
                }
            });
            if(!station.isAvailable()){
                holder.txtName.setTextColor(holder.itemView.getResources().getColor(R.color.grey_400));
            }
            if(station.getStationMessage() != null){
                holder.stationInfoButton.setVisibility(View.VISIBLE);
                holder.stationInfoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new StationMessageDialog(view.getContext(), station.getStationMessage())
                                .show();
                    }
                });
            }
        } else if (list.get(position) instanceof PreferRoute) {
            Log.e("onBindViewHolder", "Status prefer route");
            final PreferRoute route = (PreferRoute) list.get(position);
            holder.txtStart.setText(route.getStartStation().getSubwayStationName());
            holder.txtStartLine.setText(route.getStartStation().getSubwayLine().getSubwayLineName());
            holder.txtEnd.setText(route.getEndStation().getSubwayStationName());
            holder.txtEndLine.setText(route.getEndStation().getSubwayLine().getSubwayLineName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position, list.get(position), VIEW_TYPE_PREFER_ROUTE);
                }
            });
            holder.preferButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position, list.get(position), STATUS_REMOVE_PREFER_ROUTE);
                }
            });
            if(!route.getStartStation().isAvailable()){
                holder.txtStartLine.setTextColor(holder.itemView.getResources().getColor(R.color.grey_400));
                holder.txtStart.setTextColor(holder.itemView.getResources().getColor(R.color.grey_400));
            }
            if(route.getStartStation().getStationMessage() != null){
                holder.startStationInfoButton.setVisibility(View.VISIBLE);
                holder.startStationInfoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new StationMessageDialog(view.getContext(), route.getStartStation().getStationMessage())
                                .show();
                    }
                });
            }
            if(!route.getEndStation().isAvailable()){
                holder.txtEndLine.setTextColor(holder.itemView.getResources().getColor(R.color.grey_400));
                holder.txtEnd.setTextColor(holder.itemView.getResources().getColor(R.color.grey_400));
            }
            if(route.getEndStation().getStationMessage() != null){
                holder.endStationInfoButton.setVisibility(View.VISIBLE);
                holder.endStationInfoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new StationMessageDialog(view.getContext(), route.getEndStation().getStationMessage())
                                .show();
                    }
                });
            }
        } else if(list.get(position) instanceof HistoryRoute){
            Log.e("onBindViewHolder", "Status history route");
            final HistoryRoute route = (HistoryRoute) list.get(position);
            holder.txtStart.setText(route.getStartStation().getSubwayStationName());
            holder.txtStartLine.setText(route.getStartStation().getSubwayLine().getSubwayLineName());
            holder.txtEnd.setText(route.getEndStation().getSubwayStationName());
            holder.txtEndLine.setText(route.getEndStation().getSubwayLine().getSubwayLineName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position, list.get(position), VIEW_TYPE_HISTORY_ROUTE);
                }
            });
            holder.preferButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position, list.get(position), STATUS_ADD_PREFER_ROUTE);
                }
            });
            if(!route.getStartStation().isAvailable()){
                holder.txtStartLine.setTextColor(holder.itemView.getResources().getColor(R.color.grey_400));
                holder.txtStart.setTextColor(holder.itemView.getResources().getColor(R.color.grey_400));
            }
            if(route.getStartStation().getStationMessage() != null){
                holder.startStationInfoButton.setVisibility(View.VISIBLE);
                holder.startStationInfoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new StationMessageDialog(view.getContext(), route.getStartStation().getStationMessage())
                                .show();
                    }
                });
            }
            if(!route.getEndStation().isAvailable()){
                holder.txtEndLine.setTextColor(holder.itemView.getResources().getColor(R.color.grey_400));
                holder.txtEnd.setTextColor(holder.itemView.getResources().getColor(R.color.grey_400));
            }
            if(route.getEndStation().getStationMessage() != null){
                holder.endStationInfoButton.setVisibility(View.VISIBLE);
                holder.endStationInfoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new StationMessageDialog(view.getContext(), route.getEndStation().getStationMessage())
                                .show();
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Object data, int mode);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView txtStart;
        private TextView txtStartLine;
        private TextView txtEnd;
        private TextView txtEndLine;
        private ImageButton startStationInfoButton;
        private ImageButton endStationInfoButton;

        private TextView txtName;
        private Button goToThere;
        private Button comeFromThere;
        private ImageButton preferButton;
        private ImageButton stationInfoButton;

        public ViewHolder(View itemView, int type) {
            super(itemView);
            this.itemView = (View) itemView;
            switch (type){
                case VIEW_TYPE_PREFER_ROUTE:
                case VIEW_TYPE_HISTORY_ROUTE:
                    txtStart = (TextView) itemView.findViewById(R.id.txtNameStart);
                    txtEnd = (TextView) itemView.findViewById(R.id.txtNameEnd);
                    txtStartLine = (TextView) itemView.findViewById(R.id.txtNameStartLine);
                    txtEndLine = (TextView) itemView.findViewById(R.id.txtNameEndLine);
                    startStationInfoButton = (ImageButton) itemView.findViewById(R.id.btn_start_station_info);
                    endStationInfoButton = (ImageButton) itemView.findViewById(R.id.btn_end_station_info);
                    break;
                case VIEW_TYPE_STATIONS:
                    txtName = (TextView) itemView.findViewById(R.id.txtName);
                    goToThere = (Button) itemView.findViewById(R.id.goToThere);
                    comeFromThere = (Button) itemView.findViewById(R.id.comeFromThere);
                    preferButton = (ImageButton) itemView.findViewById(R.id.btn_remove_prefer_station);
                    stationInfoButton = (ImageButton) itemView.findViewById(R.id.btn_station_info);
            }
            if (type == VIEW_TYPE_PREFER_ROUTE) {
                preferButton = (ImageButton) itemView.findViewById(R.id.btn_remove_prefer_route);
            } else if (type == VIEW_TYPE_HISTORY_ROUTE) {
                preferButton = (ImageButton) itemView.findViewById(R.id.btn_add_prefer_route);
            }
        }
    }


}

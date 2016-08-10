package cn.crepusculo.subway_ticket_android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.content.Station;

public class StationAdapter extends BaseAdapter implements Filterable {

    public Context context;
    public ArrayList<Station> stationArrayList;
    public ArrayList<Station> orig;

    public StationAdapter(Context context, ArrayList<Station> stationArrayList) {
        super();
        this.context = context;
        this.stationArrayList = stationArrayList;
    }

    /**
     * @return query after filter
     */
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Station> results = new ArrayList<>();
                if (orig == null)
                    orig = stationArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Station g : orig) {
                            if (g.getName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                stationArrayList = (ArrayList<Station>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return stationArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return stationArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StationHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_row_station, parent, false);
            holder = new StationHolder();
            holder.name = (TextView) convertView.findViewById(R.id.txtName);
            holder.line = (TextView) convertView.findViewById(R.id.txtLine);
            convertView.setTag(holder);
        } else {
            holder = (StationHolder) convertView.getTag();
        }

        holder.name.setText(stationArrayList.get(position).getName());
        holder.line.setText(String.valueOf(stationArrayList.get(position).getLine()));

        return convertView;

    }

    public class StationHolder {
        TextView name;
        TextView line;
    }

}
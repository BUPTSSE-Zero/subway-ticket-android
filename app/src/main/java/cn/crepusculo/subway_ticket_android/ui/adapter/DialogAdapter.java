package cn.crepusculo.subway_ticket_android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.crepusculo.subway_ticket_android.R;

public class DialogAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;

    public DialogAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
                view = layoutInflater.inflate(R.layout.layout_dialog_bills, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.start = (TextView) view.findViewById(R.id.start);
            viewHolder.destination = (TextView) view.findViewById(R.id.destination);
            viewHolder.status = (TextView) view.findViewById(R.id.status);
            viewHolder.time = (TextView) view.findViewById(R.id.time);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Context context = parent.getContext();
        return view;
    }

    static class ViewHolder {
        TextView start;
        TextView destination;
        TextView status;
        TextView time;
    }
}


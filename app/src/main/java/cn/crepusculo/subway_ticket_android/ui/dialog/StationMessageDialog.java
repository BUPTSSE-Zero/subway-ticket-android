package cn.crepusculo.subway_ticket_android.ui.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.subwayticket.database.model.StationMessage;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.util.CalendarUtils;

/**
 * Created by zhou-shengyun on 8/12/16.
 */
public class StationMessageDialog {
    private AlertDialog dialog;

    public StationMessageDialog(Context context, StationMessage message){
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_station_msg, null, false);
        ((TextView)v.findViewById(R.id.msg_title)).setText(message.getTitle());
        ((TextView)v.findViewById(R.id.msg_publisher)).setText(message.getPublisher());
        ((TextView)v.findViewById(R.id.msg_content)).setText(Html.fromHtml(message.getContent()));
        ((TextView)v.findViewById(R.id.msg_time)).setText(CalendarUtils.formatTimeMills(message.getReleaseTime().getTime()));

        dialog = new AlertDialog.Builder(context).setView(v).
                setPositiveButton(R.string.close, null).create();
    }

    public void show(){
        dialog.show();
    }
}

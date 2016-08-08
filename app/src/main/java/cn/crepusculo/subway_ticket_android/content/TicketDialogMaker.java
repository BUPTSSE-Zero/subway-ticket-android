package cn.crepusculo.subway_ticket_android.content;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.utils.SubwayLineUtil;

/**
 * The class TicketDialogMaker
 * <p/>
 * Inflate a ticket message dialog
 */
public class TicketDialogMaker implements View.OnClickListener {
    public final static int HISTORY = 3;
    public final static int INVALID = 1;
    public final static int PREPARED = 2;
    Dialog dialog;
    private ExpandableLinearLayout subTitleLayout;
    private ExpandableRelativeLayout buttonLayout;
    private ExpandableLinearLayout qrLayout;


    public TicketDialogMaker(Activity mActivity, Context mContext, TicketOrder item) {
        Rect displayRectangle = new Rect();
        Window window = mActivity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

    /* inflate and adjust layout */
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_dialog_bills, null);
        layout.setMinimumWidth(displayRectangle.width());
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.7f));

        dialog = new Dialog(mContext, R.style.AppTheme_Dialog);
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int height = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.8);

        dialog.getWindow().setLayout(width, height);

        TextView start;
        TextView destination;
        TextView status;
        TextView date;
        TicketOrder bills;
        List<Button> buttons;

        dialog.setContentView(layout);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        start = (TextView) dialog.findViewById(R.id.start_dialog);
        destination = (TextView) dialog.findViewById(R.id.destination_dialog);
        status = (TextView) dialog.findViewById(R.id.status);
        date = (TextView) dialog.findViewById(R.id.date);

        this.subTitleLayout = (ExpandableLinearLayout) dialog.findViewById(R.id.expanded_sub_title);
        this.buttonLayout = (ExpandableRelativeLayout) dialog.findViewById(R.id.expanded_btn);
        this.qrLayout = (ExpandableLinearLayout) dialog.findViewById(R.id.expanded_qr);

        start.setText(item.getStartStation().getSubwayStationName());
        destination.setText(item.getEndStation().getSubwayStationName());
        date.setText(item.getEndStation().getSubwayStationName());
        status.setText(TicketOrder.translationCode(mContext, item.getStatus()));
        ImageView v_s = (ImageView) dialog.findViewById(R.id.come_dialog);
        ImageView v_d = (ImageView) dialog.findViewById(R.id.go_dialog);

        SubwayLineUtil.setColor(
                mContext,
                v_s,
                item.getStartStation().getSubwayLine().getSubwayLineId()
        );

        SubwayLineUtil.setColor(
                mContext,
                v_d,
                item.getEndStation().getSubwayLine().getSubwayLineId()
        );


        buttons = new ArrayList<>();

        buttons.add((Button) dialog.findViewById(R.id.dialog_btn_3));
        buttons.add((Button) dialog.findViewById(R.id.dialog_btn_1));
        buttons.add((Button) dialog.findViewById(R.id.dialog_btn_2));

        for (final Button button : buttons) {
            button.setOnClickListener(this);
        } // for

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_btn_3:
                dialog.dismiss();
                return;
            case R.id.dialog_btn_1:
                if (subTitleLayout.isExpanded()) {
                    subTitleLayout.collapse();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            qrLayout.expand();
                        }
                    }, 300);
                } else {
                    qrLayout.collapse();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            subTitleLayout.expand();
                        }
                    }, 400);
                }
                return;
            case R.id.dialog_btn_2:

                return;
            default:
        }
    }
}
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

import java.util.ArrayList;
import java.util.List;

import cn.crepusculo.subway_ticket_android.R;
import cn.crepusculo.subway_ticket_android.util.CalendarUtils;
import cn.crepusculo.subway_ticket_android.util.SubwayLineUtil;

/**
 * The class TicketDialogMaker
 * <p/>
 * Inflate a ticket message dialog
 */
public class TicketDialogMaker implements View.OnClickListener {
    public final static int HISTORY = 3;
    public final static int INVALID = 1;
    public final static int PREPARED = 2;
    /**
     * Mode of Dialog
     * <p/>
     * Depend On Fragment
     */
    public int mode = 0;
    /**
     * Compat containers
     */
    private Dialog dialog;
    private ExpandableLinearLayout subTitleLayout;
    private ExpandableLinearLayout qrLayout;
    /**
     * Context
     */
    private Activity mActivity;
    private Context mContext;

    public TicketDialogMaker(Activity activity, Context context, TicketOrder item) {
        this.mActivity = activity;
        this.mContext = context;
        initDialog();

        /**
         * Declare compat
         */
        TextView start;
        TextView destination;
        TextView status;
        TextView date;
        TicketOrder bills;
        List<Button> buttons;
        /**
         * Bind compat
         */
        start = (TextView) dialog.findViewById(R.id.start_dialog);
        destination = (TextView) dialog.findViewById(R.id.destination_dialog);
        status = (TextView) dialog.findViewById(R.id.status);
        date = (TextView) dialog.findViewById(R.id.date);

        this.subTitleLayout = (ExpandableLinearLayout) dialog.findViewById(R.id.expanded_sub_title);
        this.qrLayout = (ExpandableLinearLayout) dialog.findViewById(R.id.expanded_qr);

        ImageView v_s = (ImageView) dialog.findViewById(R.id.come_dialog);
        ImageView v_d = (ImageView) dialog.findViewById(R.id.go_dialog);

        /**
         * Full in Data
         */
        start.setText(item.getStartStation().getSubwayStationName());
        destination.setText(item.getEndStation().getSubwayStationName());
        date.setText(CalendarUtils.formatCurrentTimeMills(item.getTicketOrderTime().getTime()));
        status.setText(TicketOrder.translationCode(context, item.getStatus()));


        SubwayLineUtil.setColor(
                context,
                v_s,
                item.getStartStation().getSubwayLine().getSubwayLineId()
        );

        SubwayLineUtil.setColor(
                context,
                v_d,
                item.getEndStation().getSubwayLine().getSubwayLineId()
        );

        /**
         * Bind button
         */
        buttons = new ArrayList<>();

        buttons.add((Button) dialog.findViewById(R.id.dialog_btn_cancel));
        buttons.add((Button) dialog.findViewById(R.id.dialog_btn_display_qr));
        buttons.add((Button) dialog.findViewById(R.id.dialog_btn_refund_ticket));

        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setOnClickListener(this);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_btn_cancel:
                dialog.dismiss();
                return;
            case R.id.dialog_btn_display_qr:
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
            case R.id.dialog_btn_refund_ticket:

                return;
            default:
        }
    }

    private void initDialog() {
        View layout;
        Rect displayRectangle = new Rect();
        Window window = mActivity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        /* inflate and adjust layout */
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.layout_dialog_bills, null);
        layout.setMinimumWidth(displayRectangle.width());
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.7f));
        dialog = new Dialog(mContext, R.style.AppTheme_Dialog);
        /* set size */
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int height = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.8);
        dialog.getWindow().setLayout(width, height);

        dialog.setContentView(layout);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
    }
}
package cn.crepusculo.subway_ticket_android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import cn.crepusculo.subway_ticket_android.R;

public class SelectActivity extends BaseActivity {
    public static int ET_COME = 1;
    public static int ET_GO = -1;
    public static int EDIT_TEXT_REQUEST_CODE_COME = 1000;
    public static int EDIT_TEXT_REQUEST_CODE_GO = 1001;
    /* var */
    private int search_object;
    private Bundle result;
    private String result_come;
    private String result_go;
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_select;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getBundle();
        initString();
        search_object = bundle.getInt("TYPE", 0);

    }

    @Override
    public void onBackPressed() {
        setSearchResult();
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in_center, R.anim.fade_out_center);
    }

    private void initString(){
        result = new Bundle();
        result_come = "23333";
        result_go = "xinbaqi";
    }

    private void setSearchResult(){
        result.putString("result_come",result_come);
        result.putString("result_go",result_go);

        Intent intent = new Intent();
        intent.putExtras(result);
        if(search_object == ET_COME)
            setResult(EDIT_TEXT_REQUEST_CODE_COME,intent);
        else
            setResult(EDIT_TEXT_REQUEST_CODE_GO,intent);
    }
}

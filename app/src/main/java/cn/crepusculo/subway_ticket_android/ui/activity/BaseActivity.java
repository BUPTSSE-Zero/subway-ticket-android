package cn.crepusculo.subway_ticket_android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.crepusculo.subway_ticket_android.R;

/**
 * The BaseActivity
 *
 * @see AppCompatActivity
 * <p/>
 * Package jumpMethod
 */
public abstract class BaseActivity extends AppCompatActivity {
    abstract protected int getLayoutResource();

    abstract protected void initView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeAddContent();
        setContentView(getLayoutResource());
        initView();
    }

    public void beforeAddContent() {

    }

    /**
     * jump to activity
     */
    public void jumpToActivity(Class<?> c) {
        Intent intent = new Intent();
        intent.setClass(this, c);
        this.startActivity(intent);
    }

    public void jumpToActivity(Class<?> c, Bundle extras) {
        Intent intent = new Intent();
        intent.setClass(this, c);
        intent.putExtras(extras);
        this.startActivity(intent);
    }

    public void jumpToActivity(Class<?> c, int... flags) {
        Intent intent = new Intent();
        intent.setClass(this, c);

        for (int flag
                : flags) {
            intent.addFlags(flag);
        }

        this.startActivity(intent);
    }

    protected void jumpToActivity(Class<?> c, View sharedElement, String shareElementName) {
        Intent intent = new Intent();
        intent.setClass(this, c);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, sharedElement, shareElementName);
        ActivityCompat.startActivity(
                this, intent, optionsCompat.toBundle()
        );
        /**
         * 在目标Activity中执行此操作
         */
        // ViewCompat.setTransitionName(sharedElement, shareElementName);
    }

    protected void jumpToActivity(Class<?> c, Bundle extras, View sharedElement, String shareElementName) {
        Intent intent = new Intent();
        intent.setClass(this, c);
        intent.putExtras(extras);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, sharedElement, shareElementName);
        ActivityCompat.startActivity(
                this, intent, optionsCompat.toBundle());
    }

    public void backToPreviousActicity() {
        Intent intent = new Intent();
        this.setResult(RESULT_OK, intent);
        this.finish();
    }

    protected Bundle getBundle() {
        return this.getIntent().getExtras();
    }

    public void jumpToActivityWithResult(Class<?> c, Bundle extras, int REQUEST_CODE) {
        Intent intent = new Intent();
        intent.setClass(this, c);
        intent.putExtras(extras);
        this.startActivityForResult(intent, REQUEST_CODE);
        overridePendingTransition(R.anim.in_anim, R.anim.fade_out_center);
    }
}

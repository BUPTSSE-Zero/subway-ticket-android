package cn.crepusculo.subway_ticket_android.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wafer on 16/4/8.
 *
 */
public abstract class BaseFragment extends Fragment {

    protected View mRootView;
    protected Activity mActivity;
    protected Context mContext;
    protected ViewGroup mContainer;
    /**
     * Return the fragment layout resource
     * @return The fragment layout resource
     */
    protected abstract int getFragmentLayout();

    /**
     *
     *
     *
     * Init the view widget
     * Find view, set listener or something else
     */
    protected abstract void initView();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mRootView == null) {
            mRootView = inflater.inflate(getFragmentLayout(), container, false);
            mActivity = getActivity();
            mContext = getContext();
            mContainer = container;
            initView();
        }
        return mRootView;
    }

    protected void jumpToActivity(Class<?> c) {
        Intent intent = new Intent();
        intent.setClass(this.getActivity(), c);
        startActivity(intent);
    }

    protected void jumpToActivity(Class<?> c, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this.getActivity(), c);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

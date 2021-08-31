package com.tarun3k.movieapp.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    protected AppCompatActivity mActivity;
    protected BaseFragment mBaseFragment;
    protected LayoutInflater mInflater;
    protected ProgressDialog mProgressDialog;
    public abstract String getScreenName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mActivity = (AppCompatActivity) getActivity();
        mBaseFragment = this;
        mInflater = LayoutInflater.from(mContext);
    }

    protected FragmentManager getSupportFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }

    protected boolean isAlive() {
        return isAdded() && mActivity != null && !mActivity.isFinishing();
    }

    protected boolean isDead() {
        return !isAlive();
    }

    protected void showProgressDialog(int msgId) {
        showProgressDialog(getString(msgId));
    }

    protected void showProgressDialog(String msg) {

        if (isDead()) return;

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
        }

        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    public void dismissProgressDialog() {

        if (isDead()) return;

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}

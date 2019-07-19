package com.shx.base.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import com.alibaba.android.arouter.launcher.ARouter;
import com.shx.base.R;
import com.shx.base.bean.MessageEvent;
import com.shx.base.interfaces.IBaseWindow;
import com.shx.base.interfaces.OnFragmentListener;
import com.shx.base.utils.ShxActivityStack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

public abstract class ShxBaseFragment extends Fragment implements IBaseWindow {
    public Context mContext;
    public Activity mActivity;
    public View rootView;
    protected OnFragmentListener mTransferListener;
    /**
     * 加载进度框
     */
    public AlertDialog proDialog = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(mActivity).inflate(getLayoutId(), null);
        initViews(rootView);
        initParams(getArguments());
        initListeners();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * Fragment第一次附属于Activity时调用,在onCreate之前调用
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            mTransferListener = (OnFragmentListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    protected void init() {
        mContext = getContext();
        mActivity = getActivity();
    }


    /**
     * 初始化所有的views
     */
    protected void initViews(View parent) {
        ButterKnife.bind(this, parent);
        ARouter.getInstance().inject(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 初始化所有的变量
     */
    protected void initParams(Bundle bundle) {}

    protected void initListeners() {}

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEventBus(MessageEvent event) {
    }

    public void showProgress() {
        if (proDialog == null && !getActivity().isFinishing()) {
            proDialog = new AlertDialog.Builder(mContext, R.style.dialog_bg_transparent).create();
            proDialog.setCancelable(true);
            proDialog.setCanceledOnTouchOutside(false);
            proDialog.show();
            Window window = proDialog.getWindow();
            window.setContentView(R.layout.dialog_progress);
            window.setGravity(Gravity.CENTER);
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);

            proDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    if(mActivity!=null) {
                        ShxActivityStack.getInstance().popActivity(mActivity);
                    }
                }
            });
        } else if (proDialog != null && !getActivity().isFinishing() && !proDialog.isShowing()) {
            proDialog.show();
        }
    }

    public void hideProgress() {
        if (proDialog != null && mActivity != null) {
            proDialog.dismiss();
        }
    }
}

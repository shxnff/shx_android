package com.shx.base.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.barlibrary.ImmersionBar;
import com.shx.base.R;
import com.shx.base.bean.MessageEvent;
import com.shx.base.constant.ConstantBase;
import com.shx.base.interfaces.IBaseWindow;
import com.shx.base.okHttp.ShxRequest;
import com.shx.base.utils.ShxActivityStack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;


public abstract class ShxBaseActivity extends AppCompatActivity implements IBaseWindow {

    public Context mContext;
    public Activity mActivity;
    //设置状态栏，导航栏
    public ImmersionBar mBar;
    /**
     * 加载进度框
     */
    public AlertDialog proDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        //Activity管理器
        if (isMaxActivityEnable()) {
            ShxActivityStack.getInstance().pushActivity(this, getMaxActivityNumber());
        } else {
            ShxActivityStack.getInstance().pushActivity(this);
        }

        //所有子类都将继承这些相同的属性,请在设置界面之后设置
        mBar = ImmersionBar.with(this);

        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);

        if (this.getLayoutId() != 0) {
            setContentView(getLayoutId());
        }

        initViews();
        initListeners();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }

    protected void init() {
        this.mContext = this;
        this.mActivity = this;
        this.requestWindowFeature(1);
    }

    protected void initViews() {
        ButterKnife.bind(this);
        initBar();
    }


    //设置状态栏，导航栏
    protected void initBar() {
    }

    protected void initListeners() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //必须调用该方法，防止内存泄漏
        ImmersionBar.with(this).destroy();
        //取消本页面下的所有请求
        ShxRequest.cancelRequest(this);
        //Activity管理器
        ShxActivityStack.getInstance().popActivity(this);
        //注销EventBus
        EventBus.getDefault().unregister(this);
    }

    /**
     * 是否限制activity创建的次数,默认false
     */
    public boolean isMaxActivityEnable() {
        return false;
    }

    /**
     * 限制activity重复创建的个数，默认3个
     */
    public int getMaxActivityNumber() {
        return ShxActivityStack.MAX_LIMIT;
    }

    @Subscribe
    public void onEventBus(MessageEvent event) {
    }

    public void showProgress() {
        if (proDialog == null && !isFinishing()) {
            proDialog = new AlertDialog.Builder(mContext, R.style.dialog_bg_transparent).create();
            proDialog.setCancelable(true);
            proDialog.setCanceledOnTouchOutside(false);
            proDialog.show();
            Window window = proDialog.getWindow();
            window.setContentView(R.layout.dialog_progress);
            window.setGravity(Gravity.CENTER);
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

            proDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    if (mActivity != null) {
                        ShxActivityStack.getInstance().popActivity(mActivity);
                    }
                }
            });
        } else if (proDialog != null && !isFinishing() && !proDialog.isShowing()) {
            proDialog.show();
        }
    }

    public void hideProgress() {
        if (proDialog != null && mActivity != null) {
            proDialog.dismiss();
        }
    }
}

package com.shx.base.okHttp;

import android.graphics.Bitmap;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.model.Response;

/**
 * 从服务器获取bitmap
 */

public class RbBitmapCallback extends BitmapCallback {

    private RbEntity entity;
    private IRespondMsg mRespond;

    public RbBitmapCallback() {

    }

    public RbBitmapCallback(IRespondMsg respond, RbEntity entity) {
        this.mRespond = respond;
        this.entity = entity;
    }

    @Override
    public void onSuccess(Response<Bitmap> response) {
        if( response != null && response.body() != null) {
            entity.data = response.body();
            mRespond.onRequestSuccess(entity);
            return;
        }

        mRespond.onRequestFail(entity);
    }

}

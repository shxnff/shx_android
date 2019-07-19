package com.shx.base.imgFacebook;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * 图片异步加载回调
 */
public interface ImageLoaderListener extends Serializable {
    void onLoadFailed(Throwable throwable);

    void onLoadComplete(@NonNull Bitmap bitmap);
}

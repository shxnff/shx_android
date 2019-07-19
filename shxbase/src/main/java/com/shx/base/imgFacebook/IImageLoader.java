package com.shx.base.imgFacebook;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

/**
 * 开发者需实现的图片加载接口
 */
public interface IImageLoader {
    /**
     * 同步加载<br>
     * 一般是从缓存中加载图片
     * SDK在加载图片时会优先调用此接口，如果返回null，则会调用异步加载接口。
     *
     * @param uri 图片文件的uri。uri支持的格式由开发者自由定义。但必须要支持 file://， http:// 和 https:// 这3种
     * @param width 图片宽度
     * @param height 图片高度
     */
    @Nullable
    Bitmap loadImageSync(String uri, int width, int height);

    /**
     * 异步加载<br>
     * 当{@link IImageLoader#loadImageSync(String, int, int)}返回null，则调用此接口异步加载。<br>
     * <p>此接口也可能会独立调用，因此加载前也需要先检查缓存。</p>
     * @param uri 图片文件的uri。uri支持的格式由开发者自由定义。但必须要支持 file://， http:// 和 https:// 这3种
     * @param width 图片宽度
     * @param height 图片高度
     * @param listener 加载结束后的回调函数，需要在 UI 线程调用
     */
    void loadImage(String uri, int width, int height, ImageLoaderListener listener);
}

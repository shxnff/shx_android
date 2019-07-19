package com.shx.base.imgFacebook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.ByteConstants;
import com.facebook.common.util.UriUtil;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.shx.base.R;
import com.shx.base.utils.AppBaseUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 图片加载
 */
public class ShxImageUtils implements IImageLoader {

    public static final String BASEDIRPATH_FRESCO_DISKCACHE = "/sdcard/";
    public static final String BASEDIRNAME_FRESCO_DISKCACHE = "shx/img_cache";
    private ArrayList<MemoryTrimmable> sMemoryTrimmable;
    private static ShxImageUtils mInstance;

    public static ShxImageUtils getInstance() {
        if (mInstance == null) {
            mInstance = new ShxImageUtils();
        }
        return mInstance;
    }

    public void init() {
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(AppBaseUtils.getApplication())
                .setBaseDirectoryPath(new File(BASEDIRPATH_FRESCO_DISKCACHE))
                .setBaseDirectoryName(BASEDIRNAME_FRESCO_DISKCACHE)
                .setMaxCacheSize(100 * ByteConstants.MB)
                .setMaxCacheSizeOnLowDiskSpace(10 * ByteConstants.MB)
                .setMaxCacheSizeOnVeryLowDiskSpace(5 * ByteConstants.MB)
                .setVersion(1)
                .build();

        sMemoryTrimmable = new ArrayList<>();

        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(AppBaseUtils.getApplication())
                .setDownsampleEnabled(true)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setMemoryTrimmableRegistry(new MemoryTrimmableRegistry() {
                    @Override
                    public void registerMemoryTrimmable(MemoryTrimmable trimmable) {
                        sMemoryTrimmable.add(trimmable);
                    }

                    @Override
                    public void unregisterMemoryTrimmable(MemoryTrimmable trimmable) {
                        sMemoryTrimmable.remove(trimmable);
                    }
                })
                .build();
        Fresco.initialize(AppBaseUtils.getApplication(), imagePipelineConfig);
    }

    /**
     * 按照比例缩放view
     * @param bili 图片宽高比
     */
    public void load(SimpleDraweeView view, String url, int bili) {
        if (view != null) {
            view.setAspectRatio(bili);
            load(view, url);
        }
    }

    public void load(SimpleDraweeView view, String url) {
        if (view != null && url != null) {

            if (!Fresco.hasBeenInitialized()) {
                ShxImageUtils.getInstance().init();
            }

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                    .setLocalThumbnailPreviewsEnabled(true)
                        //根据View的尺寸放缩图片
//                    .setResizeOptions(new ResizeOptions(view.getWidth(), view.getHeight()))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setOldController(view.getController())
                    .setImageRequest(request)
                    .setAutoPlayAnimations(true)
                    .build();
            view.setTag(R.id.TAG_SHOW_URL, url);
            view.setController(controller);
        }
    }

    /**
     * 图片显示为黑白色（场景：商品失效时显示的主图）
     */
    public void loadHui(SimpleDraweeView view, String url) {
        if (view != null && url != null) {
            if (!Fresco.hasBeenInitialized()) {
                ShxImageUtils.getInstance().init();
            }

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                    .setPostprocessor(new BasePostprocessor() {
                        @Override
                        public String getName() {
                            return "redMeshPostprocessor";
                        }

                        @Override
                        public void process(Bitmap bitmap) {

                            if (bitmap != null) {

                                int width = bitmap.getWidth();
                                int height = bitmap.getHeight();
                                int[] pixels = new int[width * height];
                                bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                                int alpha = 0xFF << 24;
                                for (int i = 0; i < height; i++) {
                                    for (int j = 0; j < width; j++) {
                                        int grey = pixels[width * i + j];

                                        // 分离三原色
                                        int red = ((grey & 0x00FF0000) >> 16);
                                        int green = ((grey & 0x0000FF00) >> 8);
                                        int blue = (grey & 0x000000FF);

                                        // 转化成灰度像素
                                        grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                                        grey = alpha | (grey << 16) | (grey << 8) | grey;
                                        pixels[width * i + j] = grey;
                                    }
                                }

                                bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
                            }
                        }
                    })
                    .setLocalThumbnailPreviewsEnabled(true)
                    .build();

            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setOldController(view.getController())
                    .setImageRequest(request)
                    .setAutoPlayAnimations(true)
                    .build();
            view.setTag(R.id.TAG_SHOW_URL, url);
            view.setController(controller);

        }
    }


    public void load(SimpleDraweeView view, Uri uri) {
        if (view != null && uri != null) {

            if (!Fresco.hasBeenInitialized()) {
                ShxImageUtils.getInstance().init();
            }

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setLocalThumbnailPreviewsEnabled(true)
//                        //根据View的尺寸放缩图片
//                        .setResizeOptions(new ResizeOptions(view.getWidth(), view.getHeight()))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setOldController(view.getController())
                    .setImageRequest(request)
                    .setAutoPlayAnimations(true)
                    .build();
            view.setTag(R.id.TAG_SHOW_URL, uri.toString());
            view.setController(controller);
        }
    }

    public void loadListener(SimpleDraweeView view, String url, ControllerListener controllerListener) {
        if (view != null && url != null) {

            if (!Fresco.hasBeenInitialized()) {
                ShxImageUtils.getInstance().init();
            }

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                    .setLocalThumbnailPreviewsEnabled(true)
//                    //根据View的尺寸放缩图片
//                    .setResizeOptions(new ResizeOptions(view.getWidth(), view.getHeight()))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setOldController(view.getController())
                    .setControllerListener(controllerListener)
                    .setImageRequest(request)
                    .setAutoPlayAnimations(true)
                    .build();
            view.setTag(R.id.TAG_SHOW_URL, url);
            view.setController(controller);
        }
    }

    public void loadResize(SimpleDraweeView view, String url, int width, int height) {
        if (view != null && url != null) {

            if (!Fresco.hasBeenInitialized()) {
                ShxImageUtils.getInstance().init();
            }

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                    .setLocalThumbnailPreviewsEnabled(true)
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setOldController(view.getController())
                    .setImageRequest(request)
                    .setAutoPlayAnimations(true)
                    .build();
            view.setTag(R.id.TAG_SHOW_URL, url);
            view.setController(controller);

        }
    }

    public void loadImage(@DrawableRes int uriId, int width, int height, final ImageLoaderListener listener) {
        loadImage(UriUtil.getUriForResourceId(uriId), width, height, listener);
    }

    @Nullable
    @Override
    public Bitmap loadImageSync(String uri, int width, int height) {
        return null;
    }

    @Override
    public void loadImage(String uri, int width, int height, final ImageLoaderListener listener) {
        loadImage(Uri.parse(uri), width, height, listener);
    }

    public void loadImage(Uri uri, int width, int height, final ImageLoaderListener listener) {

        if (!Fresco.hasBeenInitialized()) {
            ShxImageUtils.getInstance().init();
        }

        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
        if (width > 0 && height > 0) {
            builder.setResizeOptions(new ResizeOptions(width, height));
        }
        ImageRequest imageRequest = builder.build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, AppBaseUtils.getApplication());

        dataSource.subscribe(new BaseBitmapDataSubscriber() {
                                 @Override
                                 public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                     if (listener != null && bitmap != null) {
                                         listener.onLoadComplete(bitmap.copy(Bitmap.Config.ARGB_8888, true));
                                     }
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                     if (listener != null) {
                                         listener.onLoadFailed(dataSource.getFailureCause());
                                     }
                                 }
                             },
                UiThreadImmediateExecutorService.getInstance());
    }

    public void loadImage(String uri, final ImageLoaderListener listener) {

        if(TextUtils.isEmpty(uri)){
            return;
        }

        if (!Fresco.hasBeenInitialized()) {
            ShxImageUtils.getInstance().init();
        }

        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri));
        ImageRequest imageRequest = builder.build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, AppBaseUtils.getApplication());

        dataSource.subscribe(new BaseBitmapDataSubscriber() {
                                 @Override
                                 public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                     if (listener != null && bitmap != null) {
                                         listener.onLoadComplete(bitmap.copy(Bitmap.Config.ARGB_8888, true));
                                     }
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                     if (listener != null) {
                                         listener.onLoadFailed(dataSource.getFailureCause());
                                     }
                                 }
                             }, UiThreadImmediateExecutorService.getInstance());
    }

    public void clearMemoryCache() {
        if (Fresco.hasBeenInitialized()) {
            /*编写释放资源代码*/
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            imagePipeline.clearMemoryCaches();
        }
    }

    /**
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 500, 500);
//        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    //计算图片的缩放值
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //把bitmap转换成String
    public String bitmapToString(String filePath) {
        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (bm == null)
            return null;

        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        Log.d("d", "压缩后的大小=" + b.length);
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String bitmapToBase64String(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     *将Bitmap转换成字符串
     */
    public String bitmapToString(Bitmap bitmap) {
        if (bitmap == null)
            return null;

        // 将Bitmap转换成字符串
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        float zoom = (float) Math.sqrt(200 * 1024 / (float) bStream
                .toByteArray().length);

        Matrix matrix = new Matrix();
        matrix.setScale(zoom, zoom);

        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        bStream.reset();
        result.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        while (bStream.toByteArray().length > 200 * 1024) {

            matrix.setScale(0.9f, 0.9f);
            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(),
                    result.getHeight(), matrix, true);
            bStream.reset();
            result.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        }

        byte[] bytes = bStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

}

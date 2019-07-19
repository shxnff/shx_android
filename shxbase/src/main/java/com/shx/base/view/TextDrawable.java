package com.shx.base.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.text.TextUtils;

import com.shx.base.utils.AppBaseUtils;
import com.shx.base.utils.ViewUtils;

/**
 * 功能介绍：显示文字的GradientDrawable, 显示背景，边框，圆角，文字
 */
public class TextDrawable extends GradientDrawable {

    private String text;
    private float textSize;
    private int textColor;
    private Paint paint;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int stokeWidth;

    public TextDrawable() {
        super();
        init();
    }

    public TextDrawable(Orientation orientation, int[] colors) {
        super(orientation, colors);
        init();
    }

    private void init() {
        this.paddingLeft = ViewUtils.dp2px(AppBaseUtils.getApplication(), 3);
        this.paddingRight = ViewUtils.dp2px(AppBaseUtils.getApplication(), 3);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        setTextSize(12);
    }


    private void setDrawableSize() {
        if (TextUtils.isEmpty(text)) {
            setSize(0, 0);
            setBounds(0, 0, 0, 0);
            return;
        }
        float textWidth = paint.measureText(text);
        //本身字体上下已经有一点空白了
        float textHeiht = paint.getFontMetricsInt().bottom - paint.getFontMetricsInt().top;
        int width = (int) (textWidth + paddingLeft + paddingRight + stokeWidth * 2);
        int height = (int) (textHeiht + paddingTop + paddingBottom + stokeWidth * 2);
        setSize(width, height);
        setBounds(0, 0, width, height);
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        super.draw(canvas);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (getIntrinsicHeight() - fontMetrics.ascent - fontMetrics.descent) / 2;
        canvas.drawText(text, 0, text.length(), getIntrinsicWidth() / 2, baseline, paint);
    }

    public TextDrawable setText(String text) {
        this.text = text;
        setDrawableSize();
        return this;
    }

    public TextDrawable setTextColor(int textColor) {
        this.textColor = textColor;
        paint.setColor(this.textColor);
        setDrawableSize();
        return this;
    }

    public TextDrawable setPadding(float paddingLeftDp, float paddingRightDp) {
        //本身字体上下已经有一点空白了,这里就不要设置了
        return setPadding(paddingLeftDp, 0, paddingRightDp, 0);
    }

    /**
     * @param paddingLeftDp
     * @param paddingTopDp    本身字体上下已经有一点空白了，注意
     * @param paddingRightDp
     * @param paddingBottomDp 本身字体上下已经有一点空白了，注意
     * @return
     */
    public TextDrawable setPadding(float paddingLeftDp, float paddingTopDp, float paddingRightDp, float paddingBottomDp) {
        this.paddingLeft = ViewUtils.dp2px(AppBaseUtils.getApplication(), paddingLeftDp);
        this.paddingTop = ViewUtils.dp2px(AppBaseUtils.getApplication(), paddingTopDp);
        this.paddingRight = ViewUtils.dp2px(AppBaseUtils.getApplication(), paddingRightDp);
        this.paddingBottom = ViewUtils.dp2px(AppBaseUtils.getApplication(), paddingBottomDp);
        setDrawableSize();
        return this;
    }

    public TextDrawable setTextSize(float textSizeDp) {
        textSize = ViewUtils.dp2px(AppBaseUtils.getApplication(), textSizeDp);
        paint.setTextSize(textSize);
        setDrawableSize();
        return this;
    }

    /**
     * 设置边框
     *
     * @param widthDp
     * @param color
     */
    public TextDrawable setStrokeNew(float widthDp, @ColorInt int color) {
        stokeWidth = ViewUtils.dp2px(AppBaseUtils.getApplication(), widthDp);
        setStroke(stokeWidth, color);
        return this;
    }

    public TextDrawable setColorNew(int argb) {
        super.setColor(argb);
        return this;
    }

    public TextDrawable setColorsNew(int[] colors) {
        super.setColors(colors);
        return this;
    }

    public TextDrawable setCornerRadiusNew(float radiusDp) {
        super.setCornerRadius(ViewUtils.dp2px(AppBaseUtils.getApplication(), radiusDp));
        return this;
    }

    /**
     * 设置8个方向的角标
     *
     * @param radiiDp
     * @return
     */
    public TextDrawable setCornerRadiuNew(float[] radiiDp) {
        //转成dp
        for (int i = 0; i < radiiDp.length; i++) {
            radiiDp[i] = ViewUtils.dp2px(AppBaseUtils.getApplication(), radiiDp[i]);
        }
        super.setCornerRadii(radiiDp);
        return this;
    }

}
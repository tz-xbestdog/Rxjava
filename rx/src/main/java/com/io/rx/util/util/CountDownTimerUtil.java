package com.io.rx.util.util;

import android.os.CountDownTimer;
import android.widget.TextView;


/**
 * Created by Administrator on 2017/4/17.
 * 验证码倒计时
 */

public class CountDownTimerUtil extends CountDownTimer {
    private TextView mTextView;
    private int resourceIdSelect = 0;
    private int resourceIdunSelect = 0;

    private int textColor = 0xFFFFFFFF;
    private String finalText = "获取验证码";

    public void setFinalText(String finalText) {
        this.finalText = finalText;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    /**
     * @param textView          The TextView
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receiver
     *                          {@link #onTick(long)} callbacks.
     */
    public CountDownTimerUtil(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
    }

    public void setBackGround(int resourceIdSelect, int resourceIdunSelect) {
        this.resourceIdSelect = resourceIdSelect;
        this.resourceIdunSelect = resourceIdunSelect;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false); //设置不可点击
        mTextView.setText(millisUntilFinished / 1000 + "秒");  //设置倒计时时间
//        mTextView.setText(millisUntilFinished / 1000 + "秒后可重新发送");  //设置倒计时时间
//        mTextView.setBackgroundResource(R.color.color_white_dddddd); //设置按钮为灰色，这时是不能点击的
        mTextView.setBackgroundResource(resourceIdSelect);
        mTextView.setTextColor(textColor);
        /**
         * 超链接 URLSpan
         * 文字背景颜色 BackgroundColorSpan
         * 文字颜色 ForegroundColorSpan
         * 字体大小 AbsoluteSizeSpan
         * 粗体、斜体 StyleSpan
         * 删除线 StrikethroughSpan
         * 下划线 UnderlineSpan
         * 图片 ImageSpan
         * http://blog.csdn.net/ah200614435/article/details/7914459
         */
//        SpannableString spannableString = new SpannableString(mTextView.getText().toString());  //获取按钮上的文字
//        ForegroundColorSpan span = new ForegroundColorSpan(0xFFFFFFFF);
//        /**
//         * public void setSpan(Object what, int start, int end, int flags) {
//         * 主要是start跟end，start是起始位置,无论中英文，都算一个。
//         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
//         */
//        spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为白色
//        mTextView.setText(spannableString);
    }

    @Override
    public void onFinish() {
        mTextView.setText(finalText);
        mTextView.setClickable(true);//重新获得点击
        mTextView.setBackgroundResource(resourceIdunSelect);
    }
}

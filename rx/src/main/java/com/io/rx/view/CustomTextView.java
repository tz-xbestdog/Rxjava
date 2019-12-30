package com.io.rx.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.Field;


@SuppressLint("AppCompatCustomView")
/**
 * 用于Adapter布局字体不变问题
 */
public class CustomTextView extends TextView {
    public static Context mContext;
    String fontPath = "pingfang.ttf";
    private Typeface typeface;


    public void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }

    public CustomTextView(Context context) {
        super(context);
        set();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        set();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        set();
    }

    private void replaceSystemDefaultFont(Context context, String fontPath) throws NoSuchFieldException, IllegalAccessException {
        typeface = Typeface.createFromAsset(context.getAssets(), fontPath);
        replaceTypefaceField("MONOSPACE", typeface);
    }

    //关键--》通过修改MONOSPACE字体为自定义的字体达到修改app默认字体的目的
    private void replaceTypefaceField(String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field defaultField = Typeface.class.getDeclaredField(fieldName);
        defaultField.setAccessible(true);
        defaultField.set(null, value);
    }


    private void set() {
        if (mContext == null)
            return;
        try {
            replaceSystemDefaultFont(mContext, fontPath);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        //设置字体
        setTypeface(typeface);
    }


}

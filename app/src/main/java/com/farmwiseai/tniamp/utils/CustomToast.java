package com.farmwiseai.tniamp.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import com.farmwiseai.tniamp.R;

public class CustomToast extends Toast {

    public static final int TYPE_INFO = 0;

    public static int LENGTH_LONG = Toast.LENGTH_LONG;
    public static int LENGTH_SHORT = Toast.LENGTH_SHORT;

    private Context mContext;
    private View mView;
    private int mType;


    public CustomToast(Context context)
    {
        super(context);
        mContext = context;
    }

    public static CustomToast makeText(Context context, String message) {
        return makeText(context, message, LENGTH_SHORT, TYPE_INFO);
    }

    public static CustomToast makeText(Context context, String message, int duration) {
        return makeText(context, message, duration, TYPE_INFO);
    }

    public static CustomToast makeText(Context context, String message, int duration, int type) {
//        if (context == null)
//        {
//
//        }
//        else
//        {
            CustomToast mdToast = new CustomToast(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.custom_toast_container, null);

            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            TextView text = (TextView) view.findViewById(R.id.mTxt_View);

            switch (type) {
                case TYPE_INFO:
                    //   icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_info_white_24dp));
                    view.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_toast_info_background));
                    break;
            }

            text.setText(message);
            mdToast.setDuration(duration);
            mdToast.setView(view);

            mdToast.mView = view;
            mdToast.mType = type;
            return mdToast;
//         }
//        return null;
    }

//    @Override
//    public void setText(@StringRes int resId) {
//        setText(mContext.getString(resId));
//    }
//
//    @Override
//    public void setText(CharSequence s) {
//        if (mView == null) {
//            throw new RuntimeException("This Toast was not created with Toast.makeText()");
//        }
//        TextView tv = (TextView) mView.findViewById(R.id.text);
//        if (tv == null) {
//            throw new RuntimeException("This Toast was not created with Toast.makeText()");
//        }
//        tv.setText(s);
//    }


    public void setIcon(@DrawableRes int iconId) {
        setIcon(ContextCompat.getDrawable(mContext, iconId));
    }


    public void setIcon(Drawable icon) {
        if (mView == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        ImageView iv = (ImageView) mView.findViewById(R.id.icon);
        if (iv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        iv.setImageDrawable(icon);
    }


    public void setType(int type) {
        mType = type;
    }


    public int getType() {
        return mType;
    }
}

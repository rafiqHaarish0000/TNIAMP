package com.farmwiseai.tniamp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.farmwiseai.tniamp.Ui.ActivityNoInternet;
import com.farmwiseai.tniamp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dmax.dialog.SpotsDialog;


public class CommonFunction {
    // Context con;
    private AlertDialog alertDialog;
    private Activity mActivity;
    protected Dialog progressView;
    //  public Typeface tfregular,tfmedium,tflight,tfbold;
    Dialog dialog;
    AlertDialog mDialog;

    public CommonFunction(Activity cont) {
        //  con = cont;
        mActivity = cont;

        //  tfregular = ResourcesCompat.getFont(mActivity, R.font.opensans_regular);


    }

    // Toast
//    public void ShowToast(String msg)
//    {
//        Toast toast = Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT);
//        LinearLayout toastLayout = (LinearLayout) toast.getView();
//        TextView toastTV = (TextView) toastLayout.getChildAt(0);
//       toastTV.setTypeface(tfregular);
//        toast.show();
//    }


    // checking internet connection

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        if (isConnected)
            return true;
        else {

            return false;
        }
    }


    public void navigationNoInternet() {
        Intent intent = new Intent(mActivity, ActivityNoInternet.class);
        mActivity.startActivity(intent);
    }


    // Navication between Classes
    public void navigation(Context currentactivityname, Class<?> nextactivityname, int size) {
        Intent i = new Intent(currentactivityname, nextactivityname);
        Bundle extras = new Bundle();
        //   extras.putString("otp", generateOTP.getResponseMessage().getOtpDataId().toString());
        extras.putString("count", String.valueOf(size));
        i.putExtras(extras);
        currentactivityname.startActivity(i);
    }

    public void navigation(Context currentactivityname, Class<?> nextactivityname) {
        Intent i = new Intent(currentactivityname, nextactivityname);
        currentactivityname.startActivity(i);
    }

    // Get Device id
    public String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }


    public void showProgress() {
        try {
            progressView = new Dialog(mActivity, R.style.AppTheme);
            View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_progress, null);
            progressView.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressView.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            progressView.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            progressView.setCancelable(false);
            progressView.setContentView(view);
            ImageView rotateImage = progressView.findViewById(R.id.rotate_image);
            startRotatingImage(rotateImage, mActivity);
            progressView.show();
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }

    private static void startRotatingImage(ImageView rotateImage, Context context) {
        Animation startRotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        rotateImage.startAnimation(startRotateAnimation);
    }

    public void mLoadCustomToast(Activity mcontaxt, String message) {
        CustomToast.makeText(mcontaxt, message, CustomToast.LENGTH_SHORT, 0).show();
    }


    public void hideProgress() {
        try {
            if (progressView != null)
                progressView.dismiss();
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }


    public void show(Activity mContext, String Title, String Msg) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = new SpotsDialog(mContext, Msg);
            mDialog.setTitle(Title);

            mDialog.setMessage(Msg);
            mDialog.show();

            mDialog.setCancelable(false);
        }

    }


    public void dismiss() {

        try {
            if ((mDialog != null) && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            mDialog = null;
        }
    }


    public static String removeFirstChar(String mGetValue, int mSubstringvalue) {
        return mGetValue.substring(mSubstringvalue).trim();
    }


    public String OnDateFormatChangeRegistration(String GetDate) {
        String mConvertDate = "";

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(GetDate);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            mConvertDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mConvertDate;
    }

    public String OnDateFormatChange(String GetDate) {
        String mConvertDate = "";

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(GetDate);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            mConvertDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mConvertDate;
    }


    public void showTextViewsAsMandatory(TextView... tvs) {
        for (TextView tv : tvs) {
            String text = tv.getText().toString();

            tv.setText(Html.fromHtml(text + "<font color=\"#ff0000\">" + " *" + "</font>"));
        }
    }


    public void showEditTextAsMandatory(EditText... tvs) {
        for (EditText tv : tvs) {
            String text = tv.getHint().toString();

            tv.setHint(Html.fromHtml(text + "<font color=\"#ff0000\">" + " *" + "</font>"));
        }
    }

    public static String formatDate(String dateToFormat, SimpleDateFormat inputFormat, SimpleDateFormat outputFormat) {
        try {

            Date date = inputFormat.parse(dateToFormat);
            String convertedDate = outputFormat.format(date);
            return convertedDate;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";

    }
}




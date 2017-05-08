package lilun.com.pension.widget;

import android.app.Activity;

import lilun.com.pension.R;

/**
 * @author yk
 *         create at 2017/3/1 9:10
 *         email : yk_developer@163.com
 */
public class NormalDialog {
    public void createNormal(Activity activity, String msg, OnPositiveListener listener) {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(activity)
                .content(msg)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive((dialog1, which) -> listener.onPositiveClick())
                .onNegative((dialog1, which) -> dialog1.dismiss())
                .show();
    }

    public void createNormal(Activity activity, int msg, OnPositiveListener listener) {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(activity)
                .content(msg)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive((dialog1, which) -> listener.onPositiveClick())
                .onNegative((dialog1, which) -> dialog1.dismiss())
                .show();
    }

    public void createShowMessage(Activity activity, String msg, OnPositiveListener listener) {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(activity)
                .content(msg)
                .positiveText(R.string.confirm)
                .onPositive((dialog1, which) -> listener.onPositiveClick())
                .show();
    }
    public void createShowMessage(Activity activity, String msg, OnPositiveListener listener, boolean cancelOnOutSide) {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(activity)
                .canceledOnTouchOutside(cancelOnOutSide)
                .content(msg)
                .positiveText(R.string.confirm)
                .onPositive((dialog1, which) -> listener.onPositiveClick())
                .show();
    }


    public interface OnPositiveListener {
        void onPositiveClick();
    }
}

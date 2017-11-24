package will.allezia.com.learn;

import will.allezia.com.learn.Helper.PreferenceHelper;
import will.allezia.com.learn.Helper.QuestionHelper;
import will.allezia.com.learn.Pages.QuestionPage;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * Created by abdullah on 20.10.2017.
 */

public class LockScreenReceiver extends BroadcastReceiver {
    private String action;
    private Intent mIntent;
    private Context mContext;
    private boolean quiteViewControlResult;
    private QuestionHelper helper;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        action = intent.getAction();
        helper = new QuestionHelper(mContext);
        quiteViewControlResult = helper.controlQuiteTime();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED) || action.equals(Intent.ACTION_SCREEN_ON)) {
            if(!quiteViewControlResult)
                showQuestionPage(context);
        }
    }

    private void showQuestionPage(Context context) {
        mIntent = new Intent(context, QuestionPage.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtra("receiver",true);
        context.startActivity(mIntent);
    }
    private final PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    new PreferenceHelper(mContext).setData("isRing",false);
                    break;

                case TelephonyManager.CALL_STATE_RINGING:
                    new PreferenceHelper(mContext).setData("isRing",false);
                    System.out.println("ring");
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    new PreferenceHelper(mContext).setData("isRing",false);
                    System.out.println("offhook");
                    break;
            }

            super.onCallStateChanged(state, incomingNumber);
        }
    };
}

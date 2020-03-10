package com.famtechnology.funticket.ui.activities;


import android.os.Handler;

import com.famtechnology.funticket.R;
import com.famtechnology.funticket.base.BaseActivity;
import com.famtechnology.funticket.data.rn.SplashRN;
import com.famtechnology.funticket.util.ContactId;

import butterknife.BindView;

public class SplashScreenActivity extends BaseActivity {

    /*****VARIABLES*****/
    private SplashRN  mSplashRN;

    /*****METHODS*****/

    @Override
    public int setView() throws Exception {
        setFullScreen();
        return R.layout.activity_splash_screen;
    }

    @Override
    public void setupData() throws Exception {
        mSplashRN = new SplashRN(this,this);
        mSplashRN.start();
    }

    private void animateAndFinish() {
        new Handler().postDelayed(() -> {
            startActivity(MainActivity.class);
            finish();
        },2000);
    }

    @Override
    protected void onReceiveData(Class fromClass, int id, boolean result, Object... objects) throws Exception {
        if (fromClass == SplashRN.class){
            if (id == ContactId.SPLASH_FINISHED){
                animateAndFinish();
            }
        }
    }
}

package com.famtechnology.funticket.util;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by Ailton on 22/06/2017 for artGS.<br>
 * Created to swipe pages automatically in a view pager. If user touch, the swipe will pause
 * util viewpager its released.
 */
public class PagerSwipe implements Runnable, ViewPager.OnPageChangeListener, View.OnTouchListener {

    /***** VARABLES *****/
    private ViewPager mViewPager;
    private Handler mHandler;
    private int mCount;
    private long mTimeToChange;
    private int mCurrentPage;
    private boolean mStop;

    /**
     * Create a swipe to a specific viewpager. <b>When activity or fragments its destroyed, stop() must be called!</b>
     * @param viewPager Viewpager to flip
     * @param count Item sound_count
     * @param timeToChange Time in milliseconds to change page
     */
    public PagerSwipe(ViewPager viewPager, int count, long timeToChange) {
        mViewPager = viewPager;
        mCount = count;
        mTimeToChange = timeToChange;
        mStop = false;

        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOnTouchListener(this);
    }

    /**
     * Starts Swiping
     * @throws Exception
     */
    public void start() {
        mCurrentPage = mViewPager.getCurrentItem();

        mHandler = new Handler();
        mHandler.postDelayed(this, mTimeToChange);
    }

    /**
     * Stop Swiping
     * @throws Exception
     */
    public void stop() {
        mStop = true;
    }

    @Override
    public void run() {
        try {
            //if was stoped or doesn't exists, returns
            if (mStop || mViewPager == null)
                return;

            //Get the current to move to next
            mCurrentPage = mViewPager.getCurrentItem();

            //Move to next page
            mCurrentPage++;
            if (mCurrentPage == mCount)
                mCurrentPage = 0;

            mViewPager.setCurrentItem(mCurrentPage);
        } catch (Exception error) {
            Log.e("Error", "Error at run() in " + getClass().getName() + ". " + error.getMessage());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {
            //If were touched, stops callbal
            if (event.getAction() == MotionEvent.ACTION_DOWN)
                mHandler.removeCallbacks(this);

            //If it is releasing
            else if (event.getAction() == MotionEvent.ACTION_UP)
                mHandler.postDelayed(this, mTimeToChange);
        } catch (Exception error) {
            Log.e("Error", "Error at run() in " + getClass().getName() + ". " + error.getMessage());
        }
        return false;
    }

    @Override
    public void onPageSelected(int position) {
        try {
            //Restarts automatic flip
            mHandler.removeCallbacks(this);
            mHandler.postDelayed(this, mTimeToChange);
        } catch (Exception error) {
            Log.e("Error", "Error at run() in " + getClass().getName() + ". " + error.getMessage());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}


    @Override
    public void onPageScrollStateChanged(int state) {}
}

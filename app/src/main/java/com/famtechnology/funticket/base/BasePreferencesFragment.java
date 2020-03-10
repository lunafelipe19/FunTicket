package com.famtechnology.funticket.base;

import android.os.Bundle;
import android.util.Log;

import com.famtechnology.funticket.base.interfaces.IComponentContact;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by Ailton on 12/12/2016.
 */
public abstract class BasePreferencesFragment extends PreferenceFragmentCompat implements IComponentContact
{

    @Override
    public void onCreatePreferences(Bundle bundle, String text) {
        try {
            //starts br.com.shopper.shopperapp.ui.data flow
            startDataFlow();

        } catch (Exception error) {

        }
    }

    /**
     * Call this method at the end of onCreateView() to initialize the whole
     * br.com.shopper.shopperapp.ui.data flow of the fragment.
     * @throws Exception
     */
    public void startDataFlow() throws Exception {
        //Set br.com.shopper.shopperapp.ui.data on views
        this.setupData();
        //get parameters on intent
        this.getParam();
        //get toolbar
        this.setupToolbar();
    }

    /**
     * Called at OnCreate() to define the br.com.shopper.shopperapp.ui.data of each view.
     * Put texts, images, colors or listeners in views just in this method.
     * If you have to implement a listener for a view, may you should create a new method and
     * call it here.
     * @throws NullPointerException
     */
    public abstract void setupData() throws Exception;

    /**
     * Configure the toolbar, if the activity has one.
     * @throws NullPointerException
     */
    public void setupToolbar() throws NullPointerException {}

    /**
     * Called at OnCreate() to get intent
     * @throws NullPointerException
     */
    private void getParam() throws NullPointerException {}

    /**
     * If the activity have shared elements, set up transition name here.
     * @throws Exception
     */
    private void setSharedElements() throws Exception {}

    /**
     * It will be called whenever a method contacts the activity by IActivityContact.<br>
     * Treat all events or messages from another component using a really clean and effective method.<br>
     * @param fromClass The class that have contacted this activity. Use if (fromClass == SomeClass.class){} to check what class it is.
     * @param id If have multiples callings from sender class, use ID to identify it.
     * @param result If have a result
     * @param objects if have extra br.com.shopper.shopperapp.ui.data to use
     * @throws Exception
     */
    protected void onReceiveData(Class fromClass, int id, boolean result, Object... objects) throws Exception {};

    //------------------------------------ FINAL METHODS ------------------------------------
    @Override
    public void contactComponent(Class fromClass, int id, boolean result, Object... objects) {

        try {
            //Call the event to receive br.com.shopper.shopperapp.ui.data
            this.onReceiveData(fromClass, id, result, objects);
        } catch (Exception error) {
            //Crashlytics.logException(error);
            Log.e("Error","exception: " + error.getMessage());
        }
    }
}

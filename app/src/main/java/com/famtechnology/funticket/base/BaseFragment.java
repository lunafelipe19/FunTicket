package com.famtechnology.funticket.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.crashlytics.android.Crashlytics;
import com.famtechnology.funticket.base.interfaces.IComponentContact;

import butterknife.ButterKnife;

/**
 * A fragment that contain a data flow called automatically
 * in its creation lifecycle.<br>
 *
 * You must return the layoutid at <b>setView</b> method and implements
 * your initiation logic at setupData.
 */
public abstract class BaseFragment extends Fragment implements IComponentContact {

    /**
     * Returns a Fragment of the specific class type.
     * @param fragment The Fragment.class
     * @return A instance of the fragment or null if the class doesn't extends from BaseFragment
     */
    public static BaseFragment getFragmentClass(Class fragment) throws Exception {
        if (!BaseFragment.class.isAssignableFrom(fragment)) {
            return null;
        }

        BaseFragment baseFragment = (BaseFragment) fragment.newInstance();
        return baseFragment;
    }

    /**
     * Returns a Fragment of the specific class type.
     * @param fragment The Fragment.class
     * @param bundle A bundle with parameters that will be associated to the fragment.
     * @return A instance of the fragment or null if the class doesn't extends from BaseFragment
     */
    public static BaseFragment getFragmentClass(Class fragment, Bundle bundle) throws Exception {
        BaseFragment baseFragment = getFragmentClass(fragment);
        if (baseFragment == null) {
            return null;
        }

        baseFragment.setArguments(bundle);
        return baseFragment;
    }

    /**
     * You must return the layout id that will be setted at OnCreateView or null
     * if you are using a non visual fragment.</b><br><br>
     * e.g:<br>
     * public int setView() throws Exception {<br>
     *      return R.layout.activity_main;<br>
     * }<br>
     *
     * <br><br>
     * @throws Exception
     */
    public abstract int setView() throws Exception;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;

        try {
            int layoutId = setView();
            if (layoutId == 0)
                return null;

            view = inflater.inflate(layoutId, container, false); //Inflates fragment layout
            ButterKnife.bind(this, view); //Set butterknife
            startDataFlow(view); //call superclass flow
        } catch (Exception error) {
           // Crashlytics.logException(error);
            error.printStackTrace();
            Log.e("Error", "Erro at onCreateView " + getClass().getName() + ". " + error.getMessage());
        }

        return view;
    }

    /**
     * You must call <b>setView</b> or call <b>manually at OnCreateView</b> to use this!<br>
     * Call this method at the end of onCreateView() to initialize the whole
     * data flow of the fragment.
     * @throws Exception
     */
    public void startDataFlow(View view) throws Exception {
        this.setStyleToViews();
        //Set br.com.shopper.shopperapp.ui.data on views
        this.setupData(view);
        //get parameters on intent
        this.getParam();
        //get toolbar
        this.setupToolbar(view);
        //if it's lolipop or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setSharedElements();
        }
    }

    protected void setStyleToViews() throws Exception { }

    /**
     * Called at OnCreate() to define the br.com.shopper.shopperapp.ui.data of each view.
     * Put texts, images, colors or listeners in views just in this method.
     * If you have to implement a listener for a view, may you should create a new method and
     * call it here.
     * @throws NullPointerException
     */
    public abstract void setupData(View view) throws Exception;

    /**
     * Configure the toolbar, if the activity has one.
     * @throws NullPointerException
     */
    public void setupToolbar(View view) throws NullPointerException {}

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
     * Send analysis to Google Analytics
     * @param screenName The screen you are
     * @param category The category of the event (Ex. Intro. - It Will be converted to Current Language: Intro)
     * @param action The action of the category (Ex. Animals)
     * @param label A label for the action (Ex. Premium or free)
     */
    public void sendAnalysis(String screenName, String category, String action, String label) throws Exception {
//        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
//        Tracker tracker = application.getDefaultTracker();
//        String lang = UserRN.currentLanguage(getContext());
//
//        tracker.setScreenName(screenName);
//        tracker.setLanguage(lang);
//        tracker.send(new HitBuilders.EventBuilder()
//                .setCategory(lang.toUpperCase() + ": " + category)
//                .setAction(action)
//                .setLabel(label)
//                .build());
    }

    public String getString(String refString) {
        String packageName = getContext().getPackageName();
        int resId = getResources().getIdentifier(refString, "string", packageName);
        return getString(resId);
    }

    public void startActivity(Class activityToStart) {
        Intent intent = new Intent(getContext(), activityToStart);
        startActivity(intent);
    }

    public void startActivityForResult(Class activityToStart, int request) {
        Intent intent = new Intent(getContext(), activityToStart);
        startActivityForResult(intent, request);
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public void contactActivity(int id, boolean result, Object...objects) {
        getBaseActivity().contactComponent(getClass(), id, result, objects);
    }

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
    public void contactComponent(final Class fromClass, final int id, final boolean result, final Object... objects)
    {
        try {
            //Se não estiver na thread principal, cria um runnable para rodar na principal
            if (Looper.myLooper() != Looper.getMainLooper()) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                onReceiveData(fromClass, id, result, objects);
                            } catch (Exception error) {
                                Crashlytics.logException(error);
                                Log.e("Error", "Erro ao receber componente em " + getClass().getName() + ". " + error.getMessage());
                            }
                        }
                    });
                    return;
                }
            }

            //Se ja está na principal, executa sem precisar criar um runnable
            this.onReceiveData(fromClass, id, result, objects);
        } catch (Exception error) {
            //Crashlytics.logException(error);
            Log.e("Error", "Erro ao receber componente em " + getClass().getName() + ". " + error.getMessage());
        }
    }
}

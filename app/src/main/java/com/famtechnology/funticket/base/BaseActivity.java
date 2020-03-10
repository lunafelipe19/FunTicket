package com.famtechnology.funticket.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.crashlytics.android.Crashlytics;
import com.famtechnology.funticket.base.interfaces.IComponentContact;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Ailton on 26/01/2016.<br><br>
 *
 * A classe base das Activities da aplicação.<br>
 * Utilize essa classe para prover um fluxo de dados automático para as activities criadas.
 * Ao extender desta classe, basta sobreescrever o método <b>setupData()</b> e chamar a super classe
 * do método onCreate <b>após</b> a chamada do método <b>setContentView()</b>.<br><br>
 *
 * Utilize o método onReceiveData para tratar chamadas de outros componentes. Nunca passe a instancia de
 * uma activity para um componente externo (tais como Tasks, services, Fragmentos e outros).<br>
 *
 * Ao criar um desses componentes internos, utilize o construtor para obter o IComponentContact da activity.
 * Ao criar o componente, utilize o <b>this</b> para entregar o IComponentContact para o componente. Sempre que
 * o componente externo chamar por contactComponent(), o método onReceivedata será chamado.
 */
public abstract class BaseActivity extends AppCompatActivity implements IComponentContact {

    private FirebaseAnalytics mFirebaseAnalytics;
    protected boolean mCanChangeLanguage = true;
    private boolean mThemeChanged = false;

    /**
     * You must return the layout id that will be setted at OnCreate.<br>
     * If you have a fullscreen activity, <b>Call setFullscreen before returns!</b><br><br>
     * e.g:<br>
     * public int setView() throws Exception {<br>
     *      return R.layout.activity_main;<br>
     * }<br>
     *
     * <br><br>
     * Or e.g with fullscreen:
     * <br>
     * public int setView() throws Exception {<br>
     *      setFullscreen();<br>
     *      return R.layout.activity_main;<br>
     * }<br>
     *
     */
    public abstract int setView() throws Exception;

    /**
     * Call this method if language should not be changed.
     */
    public void dontChangeLanguage() {
        mCanChangeLanguage = false;
    }

    /**
     * Keeps screen on
     */
    public void keepScreenOn() throws Exception {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * Hide the toolbar with an animation effect
     * @param toolbar The Activity's toolbar
     */
    public void hideToolbar(Toolbar toolbar) {
        toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
    }

    /**
     * Show the toolbar with an animation effect.
     * @param toolbar The Activity's toolbar
     */
    public void showToolbar(Toolbar toolbar) {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
    }

    public String[] getStringArray(int id) {
        return getResources().getStringArray(id);
    }

    /**
     * Define if the activity must has the language changed.
     * @return true or false if you set, true otherwise
     */
    private boolean canChangeLanguage() {
        return mCanChangeLanguage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            int layoutId = setView();
            super.onCreate(savedInstanceState);

            setContentView(layoutId);
            ButterKnife.bind(this);
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

            //get parameters on intent
            this.getParam();
            //if it's lolipop or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.setSharedElements();
            }
            this.setStyleToViews();
            this.setupData();
            //get toolbar
            this.setupToolbar();
        } catch (Exception error) {
            //Crashlytics.logException(error);
            Log.e("Error","Error at OnCreate() at BaseActivity. Origin: " + getClass().getName() + ". Happened: " + error.getMessage());
        }
    }

    /**
     * Called at OnCreate() to define br.com.shopper.shopperapp.ui.data for each view.
     * Put texts, images, colors or listeners in views just at this method.
     * If you have to implement a listener for a view, may you should create a new method and
     * call it here.
     */
    public abstract void setupData() throws Exception;

    /**
     * Configures toolbar here, if this activity has one.
     */
    public void setupToolbar() throws Exception {}

    /**
     * Called at OnCreate() to get intent
     */
    public void getParam() throws Exception {}

    /**
     * If the activity has a shared elements, set up transition name here.
     */
    protected void setSharedElements() throws Exception {}

    protected void setStyleToViews() throws Exception {

    }

    protected void contactAllFragments(int param, boolean result, Object...objects) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        if (fragments == null || fragments.isEmpty()) {
            return;
        }

        for (Fragment fragment : fragments) {
            if (fragment instanceof BaseFragment) {
                ((BaseFragment) fragment).contactComponent(getClass(), param, result, objects);
            }
        }
    }

    /**
     * Add a fragment in some container of this acativity
     * @param containerViewId R.id.container? Your container id
     * @param fragment A new instance of the fragment
     * @param fragmentTag A tag to identify the fragment
     */
    protected void addFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) throws Exception {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }

    /**
     * Change the current fragment for a new one.
     * @param containerViewId R.id.container? Your container id (Use one that already have a fragment)
     * @param fragment A new instance of the fragment
     * @param fragmentTag A tag to identify the new fragment
     * @param backStackStateName A identify for the stack
     */
    protected void replaceFragment(@IdRes int containerViewId,
                                   @NonNull Fragment fragment,
                                   @NonNull String fragmentTag,
                                   @Nullable String backStackStateName) throws Exception {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateName)
                .commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Checks the touched menu
        switch (item.getItemId()) {
            // If touched at back button
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Must be called at <b>setView before its returns</b> otherwise
     * it won't work.
     */
    public void setFullScreen() {
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (Build.VERSION.SDK_INT < 19) { //19 or above api
                View v = this.getWindow().getDecorView();
                v.setSystemUiVisibility(View.GONE);
            } else {
                //for lower api versions.
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                decorView.setSystemUiVisibility(uiOptions);
            }
        } catch (Exception error) {
           // Crashlytics.logException(error);
            Log.e("Error", "Error at setFullScreen in " + getClass().getName() + ". " + error.getMessage());
        }
    }

    public FirebaseAnalytics getFirebaseAnalysis() {
        return mFirebaseAnalytics;
    }

    /**
     * Send analysis to Google Analytics
     * @param screenName The screen you are
     * @param category The category of the event (Ex. Intro. - It Will be converted to Current Language: Intro)
     * @param action The action of the category (Ex. Animals)
     * @param label A label for the action (Ex. Premium or free)
     */
    public void sendAnalysis(String screenName, String category, String action, String label) throws Exception {
//        AnalyticsApplication application = (AnalyticsApplication) getApplication();
//        Tracker tracker = application.getDefaultTracker();
//        String lang = UserRN.currentLanguage(this);
//
//        tracker.setScreenName(screenName);
//        tracker.setLanguage(lang);
//        tracker.send(new HitBuilders.EventBuilder()
//                .setCategory(lang.toUpperCase() + ": " + category)
//                .setAction(action)
//                .setLabel(label)
//                .build());
    }

    /**
     * Open the popup menu for the option menu at the toolbar.<br><br>
     * @param viewId The mnuOption id, usually the item that users it clicking in.
     * @param mnuId The menu layout from menu resource.
     * @return The popupmenu to set listeners and show or null if mnuItem doesn't exists.
     */
    protected PopupMenu createPopupMenu(int viewId, int mnuId) throws Exception {
        View mnuItem = findViewById(viewId);

        //If mnuItem doesn't exists
        if (mnuItem == null)
            return null;

        //creates the popup menu
        PopupMenu popupMenu = new PopupMenu(this, mnuItem);
        popupMenu.inflate(mnuId);

        return popupMenu;
    }

    public String getString(String refString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(refString, "string", packageName);
        return getString(resId);
    }

    public void startActivity(Class activityToStart) {
        Intent intent = new Intent(this, activityToStart);
        startActivity(intent);
    }

    public void startActivityForResult(Class activityToStart, int request) {
        Intent intent = new Intent(this, activityToStart);
        startActivityForResult(intent, request);
    }

    /**
     * It will be called whenever a method contacts the activity by IComponentContact interface.<br>
     * Treat all events or messages from another component using a really clean and effective method.<br>
     * @param fromClass The class that have contacted this activity. Use if (fromClass == SomeClass.class){} to check what class it is.
     * @param id If have multiples callings from sender class, use ID to identify it.
     * @param result If have a result
     * @param objects if have extra br.com.shopper.shopperapp.ui.data to use
     */
    protected void onReceiveData(Class fromClass, int id, boolean result, Object... objects) throws Exception {};

    //------------------------------------ FINAL METHODS ------------------------------------
    @Override
    public void contactComponent(final Class fromClass, final int id, final boolean result, final Object... objects)
    {
        try {
        //Se não estiver na thread principal, cria um runnable para rodar na principal
        if (Looper.myLooper() != Looper.getMainLooper()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        onReceiveData(fromClass, id, result, objects);
                    } catch (Exception error) {
                        Crashlytics.logException(error);
                        Log.e("Error","Erro ao receber componente em " + getClass().getName() + ". " + error.getMessage());
                    }
                }
            });
            return;
        }

            //Se ja está na principal, executa sem precisar criar um runnable
            this.onReceiveData(fromClass, id, result, objects);
        } catch (Exception error) {
            //Crashlytics.logException(error);
            Log.e("Error","Erro ao receber componente em " + getClass().getName() + ". " + error.getMessage());
        }
    }
}

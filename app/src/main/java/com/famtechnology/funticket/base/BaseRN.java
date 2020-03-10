package com.famtechnology.funticket.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.famtechnology.funticket.base.interfaces.IComponentContact;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ailton on 20/05/2017.
 */

public class BaseRN implements IComponentContact {

    protected Context mContext; //contexto da aplicação
    protected IComponentContact mContact; //Interface para contatar a classe que chamou
    protected static List<Thread> mActiveThreads;

    /**
     * O construtor base da classe.
     * @param context O contexto da aplicação.
     * @param contact A interface de comunicação base das activities e fragmentos.
     */
    public BaseRN(Context context, IComponentContact contact) {
        mContext = context;
        mContact = contact;
        mActiveThreads = new ArrayList<>();
    }

    /**
     * Creates a new thread and adds into active threads list
     * @param runnable The runnable
     */
    public static void createThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        mActiveThreads.add(thread);

        thread.start();
    }

    public static void stopAllThreads() {
        boolean allStoped = false;

        for (int i = 0; i < mActiveThreads.size(); i++) {
            Thread thread = mActiveThreads.get(i);

            if (thread == null || !thread.isAlive()) {
                mActiveThreads.set(i, null);
                continue;
            }

            allStoped = true;
            thread.interrupt();
        }

        if (allStoped)
            mActiveThreads.clear();
    }

    /**
     * Send a message to its creator to inform some message. The message will be received on OnReceiveData at Activities and Fragments or will receive
     * at onComponentContact in classes that implements IComponentContact.
     * @param param The id - use Param.Contact.IDS.
     * @param result If your operations depends of a result, place it here.
     * @param objects If you have extra objects to send, place it here.
     */
    protected void sendContact(int param, boolean result, Object...objects) {
        mContact.contactComponent(getClass(), param, result, objects);
    }

    protected void showToast(final String text, long after) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
            }
        }, after);
    }

    /**
     * Copy of Context's getString().
     * @param stringId String id at string values
     * @return A string to use
     */
    public String getString(int stringId) {
        return mContext.getString(stringId);
    }

    /**
     * It will be called whenever a method contacts the activity by IComponentContact interface.<br>
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
    public void contactComponent(Class fromClass, int id, boolean result, Object... objects)
    {
        try {
            //Call the event to receive br.com.shopper.shopperapp.ui.data
            this.onReceiveData(fromClass, id, result, objects);
        } catch (Exception error) {
           // Crashlytics.logException(error);
            Log.e("Error","Erro ao receber componente em " + getClass().getName() + ". " + error.getMessage());
        }
    }
}

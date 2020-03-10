package com.famtechnology.funticket.data.rn;

import android.content.Context;

import com.famtechnology.funticket.base.BaseRN;
import com.famtechnology.funticket.base.interfaces.IComponentContact;
import com.famtechnology.funticket.util.ContactId;


public class SplashRN extends BaseRN {

    public SplashRN(Context context, IComponentContact contact) {
        super(context,contact);
    }

    public void start() {
        sendContact(ContactId.SPLASH_FINISHED,true);
    }
}

package com.gooner10.okcupid.EventBus;

import android.os.Bundle;

public class OnItemClickEvent {
    public final Bundle bundle;

    public OnItemClickEvent(Bundle bundle) {
        this.bundle = bundle;
    }


    public Bundle getData() {
        return bundle;
    }
}

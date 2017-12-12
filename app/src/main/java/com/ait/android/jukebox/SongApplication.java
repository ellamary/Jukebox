package com.ait.android.jukebox;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Mari Kamiya on 12/10/2017.
 */

public class SongApplication extends Application {

    private Realm realmItem;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

    public void openRealm() {
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realmItem = Realm.getInstance(config);
    }

    public void closeRealm() {
        realmItem.close();
    }

    public Realm getRealmItem() {
        return realmItem;
    }
}

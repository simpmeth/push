package com.google.firebase.quickstart.fcm;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;

import org.apache.http.params.HttpParams;

public class App extends Application {

    public static final String TAG = App.class.getSimpleName();

    private static App         mInstance;

    public static synchronized App getInstance() {
        return App.mInstance;
    }

    private RequestQueue mRequestQueue;

    public <T> void addToRequestQueue( final Request<T> req ) {
        req.setTag( App.TAG );
        this.getRequestQueue().add( req );
    }

    public <T> void addToRequestQueue(final Request<T> req, final String tag ) {
        req.setTag( TextUtils.isEmpty( tag ) ? App.TAG : tag );
        this.getRequestQueue().add( req );
    }

    public void cancelPendingRequests( final Object tag ) {
        if ( this.mRequestQueue != null ) {
            this.mRequestQueue.cancelAll( tag );
        }
    }

    public RequestQueue getRequestQueue() {

        if ( this.mRequestQueue == null ) {




            this.mRequestQueue = Volley.newRequestQueue( this.getApplicationContext() );
        }

        return this.mRequestQueue;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        App.mInstance = this;
    }
}
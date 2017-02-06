package com.ninja.ilovezappos.mvp.presenters;

import com.ninja.ilovezappos.mvp.views.base.View;

/**
 * Created by niranjanb on 05/02/17.
 */

public interface Presenter {
    void onStart();
    void onStop();
    void onCreate();
    void onPause();
    void attachView(View view);
}

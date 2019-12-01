package com.example.myapplication2.ui.login;

import com.here.android.mpa.routing.Router;

public class RouteUtil {
    static abstract class RouteListener<T, U extends Enum<?>> implements Router.Listener<T, U> {
        @Override
        public void onProgress(int i) {
            /* The calculation progress can be retrieved in this callback. */
        }
    }
}

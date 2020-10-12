package com.uber.rib.core.screenstack.transition;

import android.view.View;

public interface Transition {

    void animate(View src, View dest, Direction direction, Callback callback);

    interface Callback {
        void onAnimationEnd();
    }
}

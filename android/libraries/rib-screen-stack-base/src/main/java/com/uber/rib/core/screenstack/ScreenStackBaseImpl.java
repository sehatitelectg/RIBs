package com.uber.rib.core.screenstack;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.UiThread;

import com.uber.rib.core.screenstack.transition.Direction;

import java.util.ArrayDeque;
import java.util.Deque;

@UiThread
public class ScreenStackBaseImpl implements ScreenStackBase {

    private final Deque<StateFulViewProvider> backStack = new ArrayDeque<>();
    private final ViewGroup parentViewGroup;

    public ScreenStackBaseImpl(ViewGroup parentViewGroup) {
        this.parentViewGroup = parentViewGroup;
    }

    @Override
    public void pushScreen(ViewProvider viewProvider) {
        pushScreen(viewProvider, false);
    }

    @Override
    public void pushScreen(ViewProvider viewProvider, boolean shouldAnimate) {
        removeCurrentScreen();
        onCurrentViewHidden();
        backStack.push(new StateFulViewProvider(viewProvider));
        // order matters here
        showCurrentScreen();
        onCurrentViewAppeared();
    }

    @Override
    public void popScreen() {
        popScreen(false);
    }

    @Override
    public void popScreen(boolean shouldAnimate) {
        if (backStack.isEmpty()) {
            return;
        }

        removeCurrentScreen();
        onCurrentViewRemoved();
        backStack.pop();
        showCurrentScreen();
        onCurrentViewAppeared();
    }

    @Override
    public void popBackTo(int index, boolean shouldAnimate) {
        for (int size = backStack.size() - 1; size > index; size--) {
            popScreen();
        }
    }

    @Override
    public boolean handleBackPress() {
        return handleBackPress(false);
    }

    @Override
    public boolean handleBackPress(boolean shouldAnimate) {
        if (backStack.size() == 1) {
            return false;
        }
        popScreen();
        return true;
    }

    @Override
    public int size() {
        return backStack.size();
    }

    /**
     * Returns the index of the last item in the stack.
     *
     * @return -1 is return when the backstack is empty.
     */
    public int indexOfLastItem() {
        return size() - 1;
    }

    private void showCurrentScreen() {
        ViewProvider vp = currentViewProvider();
        if (vp != null) {
            parentViewGroup.addView(vp.buildView(parentViewGroup));
        }
    }

    private void removeCurrentScreen() {
        if (parentViewGroup.getChildCount() > 0) {
            parentViewGroup.removeViewAt(parentViewGroup.getChildCount() - 1);
        }
    }

    private void onCurrentViewAppeared() {
        ViewProvider viewProvider = currentViewProvider();
        if (viewProvider != null) {
            viewProvider.onViewAppeared();
        }
    }

    private void onCurrentViewRemoved() {
        ViewProvider viewProvider = currentViewProvider();
        if (viewProvider != null) {
            viewProvider.onViewRemoved();
        }
    }

    private void onCurrentViewHidden() {
        ViewProvider viewProvider = currentViewProvider();
        if (viewProvider != null) {
            viewProvider.onViewHidden();
        }
    }

    private StateFulViewProvider currentStateFulViewProvider() {
        if (!backStack.isEmpty()) {
            return backStack.peek();
        }
        return null;
    }

    private ViewProvider currentViewProvider() {
        StateFulViewProvider stateFulViewProvider = currentStateFulViewProvider();
        return stateFulViewProvider == null ? null : stateFulViewProvider.getViewProvider();
    }

    private void saveCurrentState(View currentView) {
        StateFulViewProvider stateFulViewProvider = currentStateFulViewProvider();
        if (stateFulViewProvider == null) {
            return;
        }

        currentView.saveHierarchyState(stateFulViewProvider.getParcelableSparseArray());
    }

    private void restoreCurrentState(View currentView) {
        StateFulViewProvider stateFulViewProvider = currentStateFulViewProvider();
        if (stateFulViewProvider == null) {
            return;
        }

        currentView.restoreHierarchyState(stateFulViewProvider.getParcelableSparseArray());
    }
}

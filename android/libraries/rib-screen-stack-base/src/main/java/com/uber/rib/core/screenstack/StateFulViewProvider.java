package com.uber.rib.core.screenstack;

import android.os.Parcelable;
import android.util.SparseArray;

public class StateFulViewProvider {
    private final ViewProvider viewProvider;
    private final SparseArray<Parcelable> parcelableSparseArray;

    public StateFulViewProvider(ViewProvider viewProvider) {
        this.viewProvider = viewProvider;
        this.parcelableSparseArray = new SparseArray<>();
    }

    public ViewProvider getViewProvider() {
        return viewProvider;
    }

    public SparseArray<Parcelable> getParcelableSparseArray() {
        return parcelableSparseArray;
    }
}

package com.fenghuang.baselib.base.recycler.multitype;

import androidx.annotation.NonNull;

public final class Preconditions {

    private Preconditions() {
    }

    @SuppressWarnings("ConstantConditions")
    public static @NonNull
    <T> T checkNotNull(@NonNull final T object) {
        if (object == null) {
            throw new NullPointerException();
        }
        return object;
    }
}
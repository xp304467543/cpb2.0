package com.fenghuang.baselib.base.recycler.multitype;

import androidx.annotation.NonNull;

final class DefaultLinker<T> implements Linker<T> {

    @Override
    public int index(int position, @NonNull T t) {
        return 0;
    }
}
package com.fenghuang.baselib.base.recycler.multitype;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public interface Linker<T> {
    @IntRange(from = 0)
    int index(int position, @NonNull T t);
}
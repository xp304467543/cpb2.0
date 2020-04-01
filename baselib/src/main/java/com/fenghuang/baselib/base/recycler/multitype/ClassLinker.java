package com.fenghuang.baselib.base.recycler.multitype;

import androidx.annotation.NonNull;

public interface ClassLinker<T> {
    @NonNull
    Class<? extends MultiTypeViewHolder<T, ?>> index(int position, @NonNull T t);
}

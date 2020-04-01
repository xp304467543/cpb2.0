package com.fenghuang.baselib.base.recycler.multitype;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

public interface OneToManyFlow<T> {

    @CheckResult
    @SuppressWarnings("unchecked")
    @NonNull
    OneToManyEndpoint<T> to(@NonNull MultiTypeViewHolder<T, ?>... binders);
}
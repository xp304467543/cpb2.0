package com.fenghuang.baselib.base.recycler.multitype;

import androidx.annotation.NonNull;

public interface TypePool {

    <T> void register(
            @NonNull Class<? extends T> clazz,
            @NonNull MultiTypeViewHolder<T, ?> binder,
            @NonNull Linker<T> linker);


    boolean unregister(@NonNull Class<?> clazz);

    int size();

    int firstIndexOf(@NonNull Class<?> clazz);

    @NonNull
    Class<?> getClass(int index);

    @NonNull
    MultiTypeViewHolder<?, ?> getItemViewBinder(int index);

    @NonNull
    Linker<?> getLinker(int index);
}

package com.fenghuang.baselib.base.recycler.multitype;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


public class MultiTypePool implements TypePool {

    private final @NonNull
    List<Class<?>> classes;
    private final @NonNull
    List<MultiTypeViewHolder<?, ?>> binders;
    private final @NonNull
    List<Linker<?>> linkers;


    /**
     * Constructs a MultiTypePool with default lists.
     */
    public MultiTypePool() {
        this.classes = new ArrayList<>();
        this.binders = new ArrayList<>();
        this.linkers = new ArrayList<>();
    }


    /**
     * Constructs a MultiTypePool with default lists and a specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     */
    public MultiTypePool(int initialCapacity) {
        this.classes = new ArrayList<>(initialCapacity);
        this.binders = new ArrayList<>(initialCapacity);
        this.linkers = new ArrayList<>(initialCapacity);
    }


    /**
     * Constructs a MultiTypePool with specified lists.
     *
     * @param classes the list for classes
     * @param binders the list for binders
     * @param linkers the list for linkers
     */
    public MultiTypePool(
            @NonNull List<Class<?>> classes,
            @NonNull List<MultiTypeViewHolder<?, ?>> binders,
            @NonNull List<Linker<?>> linkers) {
        Preconditions.checkNotNull(classes);
        Preconditions.checkNotNull(binders);
        Preconditions.checkNotNull(linkers);
        this.classes = classes;
        this.binders = binders;
        this.linkers = linkers;
    }


    @Override
    public <T> void register(
            @NonNull Class<? extends T> clazz,
            @NonNull MultiTypeViewHolder<T, ?> binder,
            @NonNull Linker<T> linker) {
        Preconditions.checkNotNull(clazz);
        Preconditions.checkNotNull(binder);
        Preconditions.checkNotNull(linker);
        classes.add(clazz);
        binders.add(binder);
        linkers.add(linker);
    }


    @Override
    public boolean unregister(@NonNull Class<?> clazz) {
        Preconditions.checkNotNull(clazz);
        boolean removed = false;
        while (true) {
            int index = classes.indexOf(clazz);
            if (index != -1) {
                classes.remove(index);
                binders.remove(index);
                linkers.remove(index);
                removed = true;
            } else {
                break;
            }
        }
        return removed;
    }


    @Override
    public int size() {
        return classes.size();
    }


    @Override
    public int firstIndexOf(@NonNull final Class<?> clazz) {
        Preconditions.checkNotNull(clazz);
        int index = classes.indexOf(clazz);
        if (index != -1) {
            return index;
        }
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).isAssignableFrom(clazz)) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public @NonNull
    Class<?> getClass(int index) {
        return classes.get(index);
    }


    @Override
    public @NonNull
    MultiTypeViewHolder<?, ?> getItemViewBinder(int index) {
        return binders.get(index);
    }


    @Override
    public @NonNull
    Linker<?> getLinker(int index) {
        return linkers.get(index);
    }
}
package com.fenghuang.baselib.base.recycler.multitype;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter;
import com.fenghuang.baselib.base.recycler.BaseViewHolder;

import java.util.Collections;
import java.util.List;


/**
 * 多种数据类型
 */
public class MultiTypeAdapter extends BaseRecyclerAdapter<Object> {

    private @NonNull
    TypePool typePool;

    public MultiTypeAdapter(Context context) {
        this(context, Collections.emptyList());
    }

    public MultiTypeAdapter(Context context, @NonNull List<?> items) {
        this(context, items, new MultiTypePool());
    }

    public MultiTypeAdapter(Context context, @NonNull List<?> items, @NonNull TypePool pool) {
        super(context);
        Preconditions.checkNotNull(items);
        Preconditions.checkNotNull(pool);
        addAll(items);
        this.typePool = pool;
    }

    public <T> void register(@NonNull Class<? extends T> clazz, @NonNull MultiTypeViewHolder<T, ?> binder) {
        Preconditions.checkNotNull(clazz);
        Preconditions.checkNotNull(binder);
        checkAndRemoveAllTypesIfNeeded(clazz);
        register(clazz, binder, new DefaultLinker<T>());
    }

    <T> void register(
            @NonNull Class<? extends T> clazz,
            @NonNull MultiTypeViewHolder<T, ?> binder,
            @NonNull Linker<T> linker) {
        typePool.register(clazz, binder, linker);
        binder.adapter = this;
    }

    @CheckResult
    public @NonNull
    <T> OneToManyFlow<T> register(@NonNull Class<? extends T> clazz) {
        Preconditions.checkNotNull(clazz);
        checkAndRemoveAllTypesIfNeeded(clazz);
        return new OneToManyBuilder<>(this, clazz);
    }

    public void registerAll(@NonNull final TypePool pool) {
        Preconditions.checkNotNull(pool);
        final int size = pool.size();
        for (int i = 0; i < size; i++) {
            registerWithoutChecking(
                    pool.getClass(i),
                    pool.getItemViewBinder(i),
                    pool.getLinker(i)
            );
        }
    }

    public void setAll(@NonNull List<?> items) {
        Preconditions.checkNotNull(items);
        addAll(items);
    }


    public void setItems(@NonNull Class<?> items) {
        Preconditions.checkNotNull(items);
        add(items);
    }

    public @NonNull
    TypePool getTypePool() {
        return typePool;
    }

    public void setTypePool(@NonNull TypePool typePool) {
        Preconditions.checkNotNull(typePool);
        this.typePool = typePool;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItemData(position);
        return indexInTypesOf(position, item);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final long getItemId(int position) {
        Object item = getItemData(position);
        int itemViewType = getItemViewType(position);
        MultiTypeViewHolder binder = typePool.getItemViewBinder(itemViewType);
        return binder.getItemId(item);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        if (holder.getItemViewType() < typePool.size())
            getRawBinderByViewHolder(holder).onViewRecycled(holder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean onFailedToRecycleView(@NonNull BaseViewHolder holder) {
        if (holder.getItemViewType() < typePool.size())
            return getRawBinderByViewHolder(holder).onFailedToRecycleView(holder);
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
        if (holder.getItemViewType() < typePool.size())
            getRawBinderByViewHolder(holder).onViewAttachedToWindow(holder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder holder) {
        if (holder.getItemViewType() < typePool.size())
            getRawBinderByViewHolder(holder).onViewDetachedFromWindow(holder);
    }

    private @NonNull
    MultiTypeViewHolder getRawBinderByViewHolder(@NonNull BaseViewHolder holder) {
        return typePool.getItemViewBinder(holder.getItemViewType());
    }


    private int indexInTypesOf(int position, @NonNull Object item) throws BinderNotFoundException {
        int index = typePool.firstIndexOf(item.getClass());
        if (index != -1) {
            @SuppressWarnings("unchecked")
            Linker<Object> linker = (Linker<Object>) typePool.getLinker(index);
            return index + linker.index(position, item);
        }
        throw new BinderNotFoundException(item.getClass());
    }


    private void checkAndRemoveAllTypesIfNeeded(@NonNull Class<?> clazz) {
        if (typePool.unregister(clazz)) {
        }
    }

    @SuppressWarnings("unchecked")
    private void registerWithoutChecking(@NonNull Class clazz, @NonNull MultiTypeViewHolder binder, @NonNull Linker linker) {
        checkAndRemoveAllTypesIfNeeded(clazz);
        register(clazz, binder, linker);
    }

    @Override
    @SuppressWarnings("unchecked")
    public BaseViewHolder onCreateHolder(@NonNull ViewGroup parent, int viewType) {
        MultiTypeViewHolder<?, ?> binder = typePool.getItemViewBinder(viewType);
        return binder.onCreateViewHolder(parent);
    }


}

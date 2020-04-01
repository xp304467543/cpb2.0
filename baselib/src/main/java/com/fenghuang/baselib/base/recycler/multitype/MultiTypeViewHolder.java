package com.fenghuang.baselib.base.recycler.multitype;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fenghuang.baselib.base.recycler.BaseViewHolder;


public abstract class MultiTypeViewHolder<T, VH extends BaseViewHolder> {

    MultiTypeAdapter adapter;

    protected abstract @NonNull
    VH onCreateViewHolder(@NonNull ViewGroup parent);

    protected final int getPosition(@NonNull final BaseViewHolder holder) {
        return holder.getAdapterPosition();
    }

    protected final @NonNull
    MultiTypeAdapter getAdapter() {
        if (adapter == null) {
            throw new IllegalStateException("MultiTypeViewHolder " + this + " not attached to MultiTypeAdapter. " +
                    "You should not call the method before registering the binder.");
        }
        return adapter;
    }

    protected Context getContext() {
        return getAdapter().getContext();
    }


    protected long getItemId(@NonNull T item) {
        return RecyclerView.NO_ID;
    }

    protected void onViewRecycled(@NonNull VH holder) {
    }

    protected boolean onFailedToRecycleView(@NonNull VH holder) {
        return false;
    }

    protected void onViewAttachedToWindow(@NonNull VH holder) {
    }

    protected void onViewDetachedFromWindow(@NonNull VH holder) {
    }
}
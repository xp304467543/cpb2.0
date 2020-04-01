package com.fenghuang.baselib.base.recycler

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * 基类适配器封装，将数据和UI抽离到具体的Holder中实现，适配器只关心数据状态
 */
abstract class BaseRecyclerAdapter<T> : RecyclerView.Adapter<BaseViewHolder<T>> {

    private lateinit var mDatas: ArrayList<T>
    private lateinit var mContext: Context
    private var mNotifyOnChange = true
    private val mLock = Any()

    private lateinit var mViewPool: RecyclerView.RecycledViewPool

    /**
     * Constructor
     *
     * @param context The current mContext.
     */
    constructor(context: Context) {
        init(context, ArrayList())
    }

    /**
     * Constructor
     *
     * @param context The current mContext.
     * @param datas The objects to represent in the ListView.
     */
    constructor(context: Context, datas: Array<T>) {
        init(context, datas.toList())
    }


    /**
     * Constructor
     *
     * @param context The current mContext.
     * @param datas The objects to represent in the ListView.
     */
    constructor(context: Context, datas: List<T>) {
        init(context, datas)
    }

    private fun init(context: Context, datas: List<T>) {
        mContext = context
        mDatas = ArrayList(datas)
        mViewPool = RecyclerView.RecycledViewPool()
    }

    fun getCount(): Int {
        return mDatas.size
    }

    /**
     * 获取最后一条的位置
     */
    fun getLastPosition(): Int {
        return itemCount - 1
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    /**
     * 获取缓冲池
     */
    fun getRecyclerViewPool(): RecyclerView.RecycledViewPool {
        return mViewPool
    }

    /**
     * 只在建立对象的时候会调用一次，后面只有视图没销毁掉就不会调用
     *
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val holder = onCreateHolder(parent, viewType)

        mOnItemClickListener?.let { listener ->
            holder.itemView.setOnClickListener {
                holder.getData()?.let { data -> listener.invoke(data, holder.getDataPosition()) }

            }
        }
        mOnItemLongClickListener?.let { listener ->
            holder.itemView.setOnLongClickListener {
                holder.getData()?.let { data -> listener.invoke(data, holder.getDataPosition()) }
                return@setOnLongClickListener true
            }
        }
        return holder
    }

    abstract fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T>

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.onBindData(getItemData(position))
    }

    fun getAllData(): List<T> {
        return ArrayList<T>(mDatas)
    }

    fun getItemData(position: Int): T {
        return mDatas[position]
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    fun getPosition(item: T): Int {
        return mDatas.indexOf(item)
    }

    /**
     * Returns the mContext associated with this array adapter. The mContext is used
     * to create views from the resource passed to the constructor.
     *
     * @return The Context associated with this adapter.
     */
    fun getContext(): Context {
        return mContext
    }

    /**
     * Control whether methods that change the list ([.add],
     * [.insert], [.remove], [.clear]) automatically call
     * [.notifyDataSetChanged].  If set to false, caller must
     * manually call notifyDataSetChanged() to have the changes
     * reflected in the attached view.
     *
     *
     * The default is true, and calling notifyDataSetChanged()
     * resets the flag to true.
     *
     * @param notifyOnChange if true, modifications to the list will
     * automatically call {@link #notifyDataSetChanged}
     */
    fun setNotifyOnChange(notifyOnChange: Boolean) {
        mNotifyOnChange = notifyOnChange
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param data The object to add at the end of the array.
     */
    fun add(data: T) {
        if (data != null) {
            synchronized(mLock) {
                mDatas.add(data)
            }
        }
        if (mNotifyOnChange) notifyItemInserted(getCount())
    }


    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     */
    fun addAll(collection: Collection<T>?) {
        if (collection != null && collection.isNotEmpty()) {
            synchronized(mLock) {
                mDatas.addAll(collection)
            }
        }
        val dataCount = collection?.size ?: 0
        //mObserver.onItemRangeInserted(getCount() - dataCount, dataCount)
        if (mNotifyOnChange) notifyItemRangeInserted(getCount() - dataCount, dataCount)
    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param datas The items to add at the end of the array.
     */
    fun addAll(datas: Array<T>?) {
        if (datas != null && datas.isNotEmpty()) {
            synchronized(mLock) {
                mDatas.addAll(datas.toList())
            }
        }
        val dataCount = datas?.size ?: 0
        //mObserver.onItemRangeInserted(getCount() - dataCount, dataCount)
        if (mNotifyOnChange) notifyItemRangeInserted(getCount() - dataCount, dataCount)
    }

    /**
     * set the specified Collection at the data.
     *
     * @param collection The Collection to add data.
     */
    fun setData(collection: Collection<T>?) {
        if (collection != null && collection.isNotEmpty()) {
            synchronized(mLock) {
                clear()
                mDatas.addAll(collection)
            }
        }
        val dataCount = collection?.size ?: 0
        if (mNotifyOnChange) notifyItemRangeInserted(0, dataCount)
    }


    /**
     * @param datas The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     */
    fun insert(datas: T, index: Int) {
        synchronized(mLock) {
            mDatas.add(index, datas)
        }
        if (mNotifyOnChange) notifyItemInserted(index)
    }

    /**
     * @param datas The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     */
    fun insertAll(datas: Array<T>?, index: Int) {
        synchronized(mLock) {
            mDatas.addAll(index, datas?.toList()!!)
        }
        val dataCount = datas?.size ?: 0
        if (mNotifyOnChange) notifyItemRangeInserted(index, dataCount)
    }


    fun insertAll(datas: Collection<T>?, index: Int) {
        synchronized(mLock) {
            mDatas.addAll(index, datas!!)
        }
        val dataCount = datas?.size ?: 0
        if (mNotifyOnChange) notifyItemRangeInserted(index, dataCount)
    }

    fun update(datas: T, pos: Int) {
        synchronized(mLock) {
            mDatas.set(pos, datas)
        }
        if (mNotifyOnChange) notifyItemChanged(pos)
    }

    /**
     * @param datas The object to remove.
     */
    fun remove(datas: T) {
        val position = mDatas.indexOf(datas)
        synchronized(mLock) {
            if (mDatas.remove(datas)) {
                if (mNotifyOnChange) notifyItemRemoved(position)
            }
        }
    }

    /**
     * @param position The position of the object to remove.
     */
    fun remove(position: Int) {
        synchronized(mLock) {
            mDatas.removeAt(position)
        }
        if (mNotifyOnChange) notifyItemRemoved(position)
    }

    /**
     * 触发清空
     * 与[.clear]的不同仅在于这个使用notifyItemRangeRemoved.
     * 猜测这个方法与add伪并发执行的时候会造成"Scrapped or attached views may not be recycled"的Crash.
     * 所以建议使用[.clear]
     */
    fun removeAll() {
        val count = mDatas.size
        synchronized(mLock) {
            mDatas.clear()
        }
        if (mNotifyOnChange) notifyItemRangeRemoved(0, count)
    }


    fun clear() {
        synchronized(mLock) {
            mDatas.clear()
        }
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained
     * in this adapter.
     */
    fun sort(comparator: Comparator<in T>) {
        synchronized(mLock) {
            Collections.sort(mDatas, comparator)
        }
        if (mNotifyOnChange) notifyDataSetChanged()
    }

    private var mOnItemClickListener: ((data: T, position: Int) -> Unit)? = null
    private var mOnItemLongClickListener: ((data: T, position: Int) -> Unit)? = null


    /**
     * 新增设置条目点击
     */
    fun setOnItemClickListener(listener: (data: T, position: Int) -> Unit) {
        this.mOnItemClickListener = listener
    }

    /**
     * 设置条目长按
     */
    fun setOnItemLongClickListener(listener: (data: T, position: Int) -> Unit) {
        this.mOnItemLongClickListener = listener
    }

}
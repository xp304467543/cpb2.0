package com.fenghuang.caipiaobao.widget.pagegridview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DimenRes;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fenghuang.baselib.utils.ViewUtils;
import com.fenghuang.caipiaobao.R;
import com.fenghuang.caipiaobao.manager.ImageManager;
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveGiftList;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Author  QinTian
 * @ Date  2020-02-26
 * @ Describe ViewPager+GridView实现左右滑动查看更多分类的控件
 */
public class BottomGiftPageGridView extends FrameLayout {
    public final static int DEFAULT_PAGE_Size = 8;
    public final static int DEFAULT_NUM_COUNT = 8;
    public final static boolean DEFAULT_IS_ShOW_INDICATOR = true;
    public final static int DEFAULT_SELECTED_INDICTOR = R.drawable.shape_dot_selected;
    public final static int DEFAULT_UN_SELECTED_INDICTOR = R.drawable.shape_dot_normal;
    public final static int DEFAULT_VP_BACKGROUND = android.R.color.transparent;
    public final static int DEFAULT_ITEM_VIEW = R.layout.item_view;
    public final static int DEFAULT_INDICATOR_GRAVITY = 1;
    public final static int DEFAULT_INDICATOR_PADDING = 0;
    public final static int DEFAULT_INDICATOR_BACKGROUND = Color.WHITE;
    public final static int DEFAULT_VP_PADDING = 0;
    private Context mContext;
    private LayoutInflater mInflater;
    private View mContentView;
    private RollViewPager mViewPager;
    private LinearLayout mLlDot;
    private List<HomeLiveGiftList> mDatas;
    private List mPagerList;
    private GridViewAdapter gridViewAdapter;
    /**
     * 每一页显示的个数
     */
    private int pageSize;
    /**
     * 总的页数
     */
    private int pageCount;

    /**
     * 当前显示的是第几页
     */
    private int curIndex = 0;

    /**
     * 列数
     */
    private int numColumns = 0;
    /**
     * 指示器位置
     */
    private int indicatorGravity;
    /**
     * 是否显示指示器
     */
    private boolean isShowIndicator;
    /**
     * 选中指示器资源ID
     */
    private int selectedIndicator;
    /**
     * 未选中指示器资源ID
     */
    private int unSelectedIndicator;
    /**
     * Item布局
     */
    private int mItemView;
    /**
     * 指示器内边距
     */
    private int indicatorPaddingLeft;
    private int indicatorPaddingRight;
    private int indicatorPaddingTop;
    private int indicatorPaddingBottom;
    private int indicatorPadding;
    /**
     * 指示器背景颜色
     */
    private int indicatorBackground;


    /**
     * ViewPager背景
     */
    private int vpBackground;

    private int vpPadding;

    private int selectedPosition = 0;

    public BottomGiftPageGridView(Context context) {
        this(context, null, 0);
    }

    public BottomGiftPageGridView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public BottomGiftPageGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initViews(context);
    }


    private void initAttrs(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.PageGridView);
        pageSize = typedArray.getInteger(R.styleable.PageGridView_pageSize, DEFAULT_PAGE_Size);
        numColumns = typedArray.getInteger(R.styleable.PageGridView_numColumns, DEFAULT_NUM_COUNT);
        isShowIndicator = typedArray.getBoolean(R.styleable.PageGridView_isShowIndicator, DEFAULT_IS_ShOW_INDICATOR);
        selectedIndicator = typedArray.getResourceId(R.styleable.PageGridView_selectedIndicator, DEFAULT_SELECTED_INDICTOR);
        unSelectedIndicator = typedArray.getResourceId(R.styleable.PageGridView_unSelectedIndicator, DEFAULT_UN_SELECTED_INDICTOR);
        mItemView = typedArray.getResourceId(R.styleable.PageGridView_itemView, DEFAULT_ITEM_VIEW);
        indicatorGravity = typedArray.getInt(R.styleable.PageGridView_indicatorGravity, DEFAULT_INDICATOR_GRAVITY);
        indicatorPaddingLeft = typedArray.getDimensionPixelOffset(R.styleable.PageGridView_indicatorPaddingLeft, DEFAULT_INDICATOR_PADDING);
        indicatorPaddingRight = typedArray.getDimensionPixelOffset(R.styleable.PageGridView_indicatorPaddingRight, DEFAULT_INDICATOR_PADDING);
        indicatorPaddingTop = typedArray.getDimensionPixelOffset(R.styleable.PageGridView_indicatorPaddingTop, DEFAULT_INDICATOR_PADDING);
        indicatorPaddingBottom = typedArray.getDimensionPixelOffset(R.styleable.PageGridView_indicatorPaddingBottom, DEFAULT_INDICATOR_PADDING);
        indicatorPadding = typedArray.getDimensionPixelOffset(R.styleable.PageGridView_indicatorPadding, -1);
        indicatorBackground = typedArray.getColor(R.styleable.PageGridView_indicatorBackground, DEFAULT_INDICATOR_BACKGROUND);
        vpBackground = typedArray.getResourceId(R.styleable.PageGridView_vpBackground, DEFAULT_VP_BACKGROUND);
        vpPadding = typedArray.getDimensionPixelOffset(R.styleable.PageGridView_vpPadding, DEFAULT_VP_PADDING);
        typedArray.recycle();
    }

    private void initViews(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mContentView = mInflater.inflate(R.layout.vp_gridview, this, true);
        mViewPager = mContentView.findViewById(R.id.view_pager);
        mViewPager.setBackgroundResource(vpBackground);
        mViewPager.setPadding(vpPadding, vpPadding, vpPadding, vpPadding);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout
                .LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //动态设置ViewPager
        float rate = (float) pageSize / (float) numColumns;
        int rows = (int) Math.ceil(rate);
        View itemView = mInflater.inflate(mItemView, this, false);

        ViewGroup.LayoutParams itemLayoutParams = itemView.getLayoutParams();
        int itemHeight = itemLayoutParams.height;
        layoutParams.height = rows * itemHeight;
        layoutParams.height += vpPadding * 2;

        mViewPager.setLayoutParams(layoutParams);
        mLlDot = mContentView.findViewById(R.id.ll_dot);
        if (indicatorGravity == 0) {
            mLlDot.setGravity(Gravity.LEFT);
        } else if (indicatorGravity == 1) {
            mLlDot.setGravity(Gravity.CENTER);
        } else if (indicatorGravity == 2) {
            mLlDot.setGravity(Gravity.RIGHT);
        }
        if (indicatorPadding != -1) {
            mLlDot.setPadding(indicatorPadding, indicatorPadding, indicatorPadding, indicatorPadding);
        } else {
            mLlDot.setPadding(indicatorPaddingLeft, indicatorPaddingTop, indicatorPaddingRight, indicatorPaddingBottom);
        }
        mLlDot.setBackgroundColor(indicatorBackground);

    }


    public void setData(List<HomeLiveGiftList> data) {
        this.mDatas = data;
        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
        mPagerList = new ArrayList<View>();
        curIndex = 0;
        for (int i = 0; i < pageCount; i++) {
            // 每个页面都是inflate出一个新实例
            GridView gridView = new GridView(mContext);
            gridView.setNumColumns(numColumns);
            gridView.setOverScrollMode(OVER_SCROLL_NEVER);
            gridViewAdapter = new GridViewAdapter(mContext, mDatas, i, pageSize);
            gridView.setAdapter(gridViewAdapter);

            mPagerList.add(gridView);
            gridViewAdapter.setOnItemClickListener((position, name) -> {
                int pos = position + curIndex * pageSize;
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClick(pos, gridViewAdapter.mDatas.get(position));
                }
            });
        }
        //设置适配器
        mViewPager.setAdapter(new ViewPagerAdapter(mPagerList));
        setCurrentItem(selectedPosition);


    }

    /**
     * 设置圆点
     */
    public void setOvalLayout() {
        if (mLlDot.getChildCount() > 0) mLlDot.removeAllViews();
        for (int i = 0; i < pageCount; i++) {
            mLlDot.addView(mInflater.inflate(R.layout.dot, null));
            ImageView imageView = mLlDot.getChildAt(i).findViewById(R.id.v_dot);
            imageView.setImageResource(unSelectedIndicator);
        }
        // 默认显示
        ImageView imageView = mLlDot.getChildAt(selectedPosition).findViewById(R.id.v_dot);
        imageView.setImageResource(selectedIndicator);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageSelected(int position) {
                // 取消圆点选中
                ImageView lastImageView = mLlDot.getChildAt(curIndex)
                        .findViewById(R.id.v_dot);
                lastImageView.setImageResource(unSelectedIndicator);
                // 圆点选中
                ImageView nextImageView = mLlDot.getChildAt(position)
                        .findViewById(R.id.v_dot);
                nextImageView.setImageResource(selectedIndicator);
                curIndex = position;
            }
        });
    }

    class ViewPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public ViewPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return (mViewList.get(position));
        }

        @Override
        public int getCount() {
            if (mViewList == null)
                return 0;
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


    class GridViewAdapter extends BaseAdapter {
        private List<HomeLiveGiftList> mDatas;
        private LayoutInflater inflater;

        private OnItemClickListener mOnItemClickListener;

        /**
         * 页数下标,从0开始(当前是第几页)
         */
        private int curIndex;
        /**
         * 每一页显示的个数
         */
        private int pageSize;

        public GridViewAdapter(Context context, List<HomeLiveGiftList> mDatas, int curIndex, int pageSize) {
            inflater = LayoutInflater.from(context);
            this.mDatas = mDatas;
            this.curIndex = curIndex;
            this.pageSize = pageSize;
        }

        public void setData(List<HomeLiveGiftList> data) {
            this.mDatas = data;
            notifyDataSetChanged();
        }

        /**
         * 先判断数据集的大小是否足够显示满本页,如果够，则直接返回每一页显示的最大条目个数pageSize,如果不够，则有几项就返回几,(也就是最后一页的时候就显示剩余item)
         */
        @Override
        public int getCount() {
            return mDatas.size() > (curIndex + 1) * pageSize ? pageSize : (mDatas.size() - curIndex * pageSize);

        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position + curIndex * pageSize);
        }

        @Override
        public long getItemId(int position) {
            return position + curIndex * pageSize;
        }


        @SuppressLint("SetTextI18n")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;

            if (convertView == null) {
                convertView = inflater.inflate(mItemView, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.itemView = convertView;
                viewHolder.iv = convertView.findViewById(R.id.im_item_icon);
                viewHolder.tv = convertView.findViewById(R.id.tv_item_name);
                viewHolder.tvPrise= convertView.findViewById(R.id.tvGiftPrise);
                viewHolder.pagerGridContainer = convertView.findViewById(R.id.pagerGridContainer);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            /**
             * 在给View绑定显示的数据时，计算正确的position = position + curIndex * pageSize
             */
            int pos = position + curIndex * pageSize;
            if (null != viewHolder.tv) {
                viewHolder.tv.setTextColor(ViewUtils.INSTANCE.getColor(R.color.white));
                viewHolder.tv.setText(mDatas.get(pos).getName());
            }
            if (null != viewHolder.iv) {
                if (isFirst) {
                    ImageManager.INSTANCE.loadImg(mDatas.get(pos).getIcon(), viewHolder.iv);
                }

            }
            if (null != viewHolder.tvPrise) {
                viewHolder.tvPrise.setText(mDatas.get(pos).getAmount()+" 钻石");
            }
            if (null != viewHolder.pagerGridContainer) {
                if (clickPosition.equals(mDatas.get(pos).getName())) {
                    viewHolder.pagerGridContainer.setBackground(ViewUtils.INSTANCE.getDrawable(R.drawable.shape_home_live_chat_gif_selected_bg));
                } else viewHolder.pagerGridContainer.setBackground(null);
            }
            viewHolder.itemView.setOnClickListener(view -> {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClick(position, mDatas.get(pos));
                }
            });
            return convertView;
        }


        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        class ViewHolder {
            public View itemView;
            public TextView tv,tvPrise;
            public SimpleDraweeView iv;
            public LinearLayout pagerGridContainer;
        }
    }


    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, HomeLiveGiftList homeLiveGiftList);
    }


    /**
     * dp转px
     */
    private int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, mContext.getResources().getDisplayMetrics());
    }

    public int getDimensionPixelOffset(@DimenRes int resId) {
        return mContext.getResources().getDimensionPixelOffset(resId);
    }

    class OnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void setCurrentItem(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        mViewPager.setCurrentItem(selectedPosition);
        //设置圆点
        if (isShowIndicator && pageCount > 1) {
            setOvalLayout();
        } else {
            if (mLlDot.getChildCount() > 0) mLlDot.removeAllViews();
            mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
                public void onPageSelected(int position) {
                    curIndex = position;
                }
            });
        }


    }

    public String clickPosition = "";

    public Boolean isFirst = true;


    public List<HomeLiveGiftList> getDatas() {
        return mDatas;
    }

    public HomeLiveGiftList getItem(int position) {
        return mDatas.get(position);
    }

    public void notifyAllData(String name) {
        this.isFirst = false;
        this.clickPosition = name;
        if (gridViewAdapter == null) return;
        gridViewAdapter.notifyDataSetChanged();
    }
}
package com.example.vitamio.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.Filter;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: william-work from mobicloud.com.cn
 * Date: 12-7-12
 * Time: 下午4:27
 */
public abstract class FilterArrayAdapter<T> extends ArrayListAdapter<T> {

    private String mFilterString;
    private Object mLock = new Object();
    private ListFilter mFilter;
    private ArrayList<T> mOriginalValues;
    protected final ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);

    public FilterArrayAdapter(final Context context, final ArrayList<T> ts) {
        super(context, ts);
    }

    /**
     * 获得字符拦截器
     *
     * @return
     */
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ListFilter();
        }
        return mFilter;
    }

    /**
     * 获得搜索字符串
     *
     * @return
     */
    public String getFilterString() {
        return mFilterString;
    }

    /**
     *  高亮文本
     * @param text
     * @return
     */
    public CharSequence highLightText(String text) {
        CharSequence result;
        final String key = mFilterString;
        int index = TextUtils.isEmpty(key) ? -1 : text.toLowerCase().indexOf(key.toLowerCase());
        if (index > -1) {
            SpannableStringBuilder nameStyle = new SpannableStringBuilder(text);
            nameStyle.setSpan(redSpan, index, index + key.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            result = nameStyle;
        } else {
            result = text;
        }
        return result;
    }

    /**
     * 过滤
     * @param values
     * @param preFixString
     * @return
     */
    public abstract ArrayList<T> filter(final ArrayList<T> values, CharSequence preFixString);

    private class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (mOriginalValues == null){
                synchronized (mLock){
                    mOriginalValues = new ArrayList<T>(mObjects);
                }
            }
            if (prefix == null || prefix.length() == 0){
                mFilterString = "";
                synchronized (mLock){
                    ArrayList<T> list = new ArrayList<T>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            }  else {
                mFilterString = prefix.toString();
                final ArrayList<T> newValues = FilterArrayAdapter.this.filter(mOriginalValues, prefix);
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mObjects = (ArrayList<T>)results.values;
            if (results.count >0){
                notifyDataSetChanged();
            }else {
                notifyDataSetInvalidated();
            }
        }
    }
}

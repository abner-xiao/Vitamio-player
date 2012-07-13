package com.example.vitamio.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: william-work from mobicloud.com.cn
 * Date: 12-7-12
 * Time: 下午3:13
 */
public abstract class ArrayListAdapter<T> extends BaseAdapter {
    //数据
    protected ArrayList<T> mObjects;
    protected LayoutInflater mInflater;

    public ArrayListAdapter(final Context context, final ArrayList<T> list){
        mObjects = list == null ? new ArrayList<T>() : list;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount(){
        return mObjects.size();
    }

    public T getItem(int position){
        return mObjects.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public void add(T item){
        mObjects.add(item);
    }

    /**
     *  Adds the specified items at the end of the array
     *
     * @param items
     */
    public void addAll(T... items){
        ArrayList<T> values = this.mObjects;
        for (T item:items){
            values.add(item);
        }
        this.mObjects = values;
    }

    /**
     *
     * @param collection
     */
    public void addAll(Collection<? extends T> collection){
        mObjects.addAll(collection);
    }

    public void clear(){
        mObjects.clear();
    }

    /**
     *
     * @return
     */
    public final ArrayList<T> getAll(){
       return mObjects;
    }

}

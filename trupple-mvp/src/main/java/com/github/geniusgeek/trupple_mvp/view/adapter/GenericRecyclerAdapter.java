package com.github.geniusgeek.trupple_mvp.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by root on 11/13/16.
 */

public abstract class GenericRecyclerAdapter<T extends Object, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private List<T> data = new ArrayList<>();
    private Context mContext;

    protected GenericRecyclerAdapter(Context context, Collection<T> data) {
        mContext = context;
        this.data = (data == null || data.isEmpty()) ? new ArrayList<T>() : new ArrayList<>(data);
    }

    /**
     * add an item to the list
     *
     * @param model
     */
    public final void addItem(T model) {
        data.add(model);
        notifyItemInserted(data.size() - 1);
        // notifyItemChanged(data.size() - 1);//better than @RecyclerView.Adapter.notifyDataSetChanged in performance

    }

    /**
     * update an item
     *
     * @param model
     */
    public final void updateItem(T model) {
        if (!data.contains(model))
            return;
        int pos = data.indexOf(model);
        replaceAnItem(pos, model);
    }

    /**
     * add an item at a particular position to the list
     *
     * @param position
     * @param model
     */
    public final void replaceAnItem(int position, T model) {
        data.set(position, model);
        notifyItemChanged(position);//although RecyclerView.Adapter.notifyItemInserted(pos) will be preferred because its less expensive
    }

    /**
     * add an item at a particular position to the list
     *
     * @param position
     * @param model
     */
    public final void addItem(int position, T model) {
        data.add(position, model);
        notifyItemChanged(position);//although RecyclerView.Adapter.notifyItemInserted(pos) will be preferred because its less expensive
    }

    /**
     * add collection of data to
     *
     * @param model
     */
    public final void addAllItems(Collection<T> model) {
        data.clear();//clear all that exist there
        data.addAll(model);
        notifyDataSetChanged();
    }

    public final T removeItem(int position) {
        final T model = data.remove(position);
        notifyItemChanged(position);
        return model;
    }

    public final T getItem(int position) {
        return data.get(position);
    }


    public final void applyAndAnimateRemovals(List<T> newModels) {
        for (int i = data.size() - 1; i >= 0; i--) {
            final T model = data.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }


    public final void applyAndAnimateAdditions(List<T> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final T model = newModels.get(i);
            if (!data.contains(model)) {
                addItem(i, model);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= data.size())
            throw new IndexOutOfBoundsException("invalid index");
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /*@Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(getViewHolderLayout(), parent, false);
        ParameterizedType type = (ParameterizedType) (getClass ( ).getGenericSuperclass ( ));

        try {
            Class<VH> vhClass= (Class<VH>)type.getActualTypeArguments()[0];
            Constructor<?> constructor = vhClass.getConstructor(View.class);
            VH holder = (VH) constructor.newInstance(new Object[] { itemView });
            return holder;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    protected abstract
    @LayoutRes
    int getViewHolderLayout();

    public Context getContext() {
        return mContext;
    }

    /**
     * get the data
     *
     * @return
     */
    public List<T> getData() {
        return data;
    }
}

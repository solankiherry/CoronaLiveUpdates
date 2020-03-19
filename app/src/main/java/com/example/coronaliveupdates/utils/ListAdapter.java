package com.example.coronaliveupdates.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coronaliveupdates.R;
import com.example.coronaliveupdates.databinding.ItemListBinding;
import com.example.coronaliveupdates.model.MapDataModel;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {
    private ArrayList<MapDataModel> list;
    private Context context;

    public ListAdapter(Context context, ArrayList<MapDataModel> list) {
        this.list = list;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemListBinding binding;

        MyViewHolder(ItemListBinding layoutBinding) {
            super(layoutBinding.getRoot());
            this.binding = layoutBinding;
        }

        public void bind(MapDataModel obj) {
            binding.setVariable(BR.model, obj);
            binding.executePendingBindings();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemListBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_list, parent, false);
        return new MyViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        MapDataModel model = list.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
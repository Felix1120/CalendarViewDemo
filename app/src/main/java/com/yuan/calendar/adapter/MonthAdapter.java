package com.yuan.calendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yuan.calendar.R;
import com.yuan.calendar.bean.DateBean;
import com.yuan.calendar.callback.OnClickListener;

import java.util.List;

/**
 * @author XueChaoFei
 * @date :2021/5/22
 */
public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthHolder> {

    private List<DateBean> list;
    private OnClickListener listener;
    private Context context;

    public MonthAdapter(List<DateBean> list) {
        this.list = list;
    }

    public void updateList(List<DateBean> newList) {
        if (newList == null) {
            return ;
        }
        if (list == null) {
            list = newList;
        } else {
            list.clear();
            list.addAll(newList);
        }
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MonthHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_adapter_item, parent, false);
        return new MonthHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthHolder holder, int position) {
        holder.tvMonth.setText(String.valueOf(list.get(position).getMonth()).concat("月"));
        if (list.get(position).isSelected()) {
            holder.tvMonth.setTextColor(context.getResources().getColor(R.color.color_ff9405));
        } else {
            holder.tvMonth.setTextColor(context.getResources().getColor(R.color.day_color));
        }
        if (listener != null) {
            holder.tvMonth.setOnClickListener((view)->{
                listener.clickItem(position, list.get(position).getYear(), list.get(position).getMonth());
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class MonthHolder extends RecyclerView.ViewHolder {
        TextView tvMonth;
        public MonthHolder(@NonNull View itemView) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tv_month_adapter_item);
        }
    }
}

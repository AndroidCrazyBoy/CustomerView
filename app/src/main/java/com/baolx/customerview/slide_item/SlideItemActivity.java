package com.baolx.customerview.slide_item;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baolx.customerview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BaoLongxiang on 2016/7/14.
 */
public class SlideItemActivity extends AppCompatActivity {

    private ListView list_view;

    private List<String> datas;

    private MyAdapter adapter;

    private SlideItemView mItemView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        list_view = (ListView) findViewById(R.id.list_view);
        initData();
        adapter = new MyAdapter(this);
        list_view.setAdapter(adapter);
    }

    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            datas.add("SlideItem " + i);
        }
    }


    class MyAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private Context context;

        public MyAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.slid_item_view, null);
                holder.contentView = (TextView) convertView.findViewById(R.id.slid_item);
                holder.deleteBtn = (TextView) convertView.findViewById(R.id.delete_btn);
                holder.menuView = (SlideItemView) convertView.findViewById(R.id.item_view);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            holder.contentView.setText(datas.get(position));
            final ViewHolder finalHolder = holder;
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Delete" + position, Toast.LENGTH_SHORT).show();
                    datas.remove(position);
                    finalHolder.menuView.closeSlid2();
                    notifyDataSetChanged();
                }
            });

            holder.contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "SlidItem" + position, Toast.LENGTH_SHORT).show();
                    finalHolder.menuView.closeSlid2();
                }
            });

            /**
             * 监听开关的状态, 确定只能有一个 是开启的状态
             */
            holder.menuView.setChangeStateListener(new SlideItemView.ISlideItemCallBack() {
                @Override
                public void slidOpen(SlideItemView itemView) {
                    mItemView = itemView;
                }

                @Override
                public void slidClose(SlideItemView itemView) {
                    if(mItemView == itemView){
                        mItemView = null;
                    }
                }

                @Override
                public void onTouch(SlideItemView itemView) {
                    if(mItemView != null && mItemView != itemView){//
                        mItemView.closeSlid2();
                    }
                }
            });

            return convertView;
        }
    }

    class ViewHolder{
        private TextView contentView;
        private TextView deleteBtn;
        private SlideItemView menuView;
    }
}

package com.wind.windlinkrecycleview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wind.windlinkrecycleview.R;
import com.wind.windlinkrecycleview.listener.ItemClickListener;
import com.wind.windlinkrecycleview.model.Province;

import java.util.List;

/**
 * Created by zhangcong on 2017/7/24.
 */

public class ProvinceRvAdapter extends RvAdapter<Province> {
    private int clickPositon;
    public ProvinceRvAdapter(Context context, List list,ItemClickListener listener) {
        super(context, list,listener);
    }
    public ProvinceRvAdapter()
    {
        super();
    }
     public  void setClickPositon(int position)
    {
        clickPositon=position;
        Log.i("SIMON","clickposition"+clickPositon);
        notifyDataSetChanged();//更新view，否则点击背景不换
    }
    @Override
    protected RvHolder getHolder(View view, int viewType) {
        return new ProvinceHolder(view,viewType,listener);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_province;
    }
    private class ProvinceHolder extends RvHolder<Province>
    {
        private TextView textView;
        private View view;
        public ProvinceHolder(View itemView, int type,ItemClickListener listener) {
            super(itemView,type, listener);
             view=itemView;
            textView= (TextView) view.findViewById(R.id.tv_province);
        }

        @Override
        public void bindHolder(Province s, int position) {

            Log.i(">>>>>>","click"+clickPositon);
            if (position==clickPositon)
            {
                view.setBackgroundColor(Color.parseColor("#9EABF4"));
                textView.setTextColor(Color.parseColor("#ffffff"));
            }
            else {
                view.setBackgroundColor(Color.parseColor("#00FFFFFF"));//设置为透明的，因为白色会覆盖分割线
                textView.setTextColor(Color.parseColor("#1e1d1d"));
            }
            textView.setText(s.getName());
        }
    }
}

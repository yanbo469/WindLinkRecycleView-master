package com.wind.windlinkrecycleview.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wind.windlinkrecycleview.R;
import com.wind.windlinkrecycleview.listener.ItemClickListener;
import com.wind.windlinkrecycleview.model.CityBean;

import java.util.List;

/**
 * Created by zhangcong on 2017/7/25.
 */

public class CityRvAdapter extends RvAdapter<CityBean>{

    public CityRvAdapter(Context context, List<CityBean> list, ItemClickListener listener) {
        super(context, list, listener);
    }

    @Override
    protected RvHolder getHolder(View view, int viewType) {
        return new CityHolder(view,viewType,listener);
    }

    @Override
    public int getItemViewType(int position) {
        return  list.get(position).isTitle();
    }

    @Override
    protected int getLayoutId(int viewType) {
        int layouid=0;
        switch (viewType){
            case 0:
                layouid=R.layout.item_title;
                break;
            case 1:
                layouid=R.layout.item_city;
                break;
            case 2:
                layouid=R.layout.item_jjj;
                break;
        }

        return layouid;
    }
    private class CityHolder extends RvHolder<CityBean>
    {
        private TextView title;
        private TextView city;
        private TextView cityj;
        public CityHolder(View itemView, int type,ItemClickListener listener) {
            super(itemView,type, listener);
            switch (type)
            {
                case 0:
                    title= (TextView) itemView.findViewById(R.id.tv_title);
                    break;
                case 1:
                    city= (TextView) itemView.findViewById(R.id.tv_city);
                    break;
                case 2:
                    cityj= (TextView) itemView.findViewById(R.id.tv_city_j);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void bindHolder(CityBean cityBean, int position) {
            int itemViewTtpe=CityRvAdapter.this.getItemViewType(position);
            switch (itemViewTtpe)
            {
                case 0:
                    title.setText(list.get(position).getProvince());
                    break;
                case 1:
                    city.setText(list.get(position).getCity());
                    break;
                case 2:

                    break;
            }
        }
    }

}

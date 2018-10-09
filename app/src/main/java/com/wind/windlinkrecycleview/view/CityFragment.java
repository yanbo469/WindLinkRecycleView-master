package com.wind.windlinkrecycleview.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wind.windlinkrecycleview.CheckListener;
import com.wind.windlinkrecycleview.CityContract;
import com.wind.windlinkrecycleview.ItemHeaderDecoration;
import com.wind.windlinkrecycleview.R;
import com.wind.windlinkrecycleview.Utils;
import com.wind.windlinkrecycleview.adapter.CityRvAdapter;
import com.wind.windlinkrecycleview.listener.ItemClickListener;
import com.wind.windlinkrecycleview.model.CityBean;
import com.wind.windlinkrecycleview.model.Province;
import com.wind.windlinkrecycleview.model.StringList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangcong on 2017/7/24.
 */

public class CityFragment extends Fragment implements CityContract.View, CheckListener {
    private CityContract.Presenter mpresenter;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CityRvAdapter adapter;
    private List<CityBean> list = new ArrayList<>();
    private int moveCounts;
    public List<StringList> citylist;
    public boolean move = false;
    private CheckListener checkListener;
    private LinearLayoutManager manager;
    private List<Province> presenters;
    public boolean isLoading = false;
    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_city);
        recyclerView.addOnScrollListener(new RecyclerViewListener());
        mpresenter.start();
        Log.i("simonsimon", "resume");
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void setPresenter(CityContract.Presenter presenter) {
        mpresenter = presenter;
    }

    public void setListProvince(List<Province> presenters) {
        this.presenters = presenters;
    }

    @Override
    public void showSnackBar() {
        //  Utils.showSnackBar(view,);
    }

    @Override
    public void showCity() {
        Log.i("simonsimon", "showcity");
        Context context = getActivity();
        initData();
        manager = new LinearLayoutManager(getContext());

//        gridLayoutManager=new GridLayoutManager(context,1);
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return list.get(position).isTitle ? 3 : 1 ;
//            }
//        });
        recyclerView.setLayoutManager(manager);
        adapter = new CityRvAdapter(context, list, new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.root:
                        Utils.showSnackBar(recyclerView, list.get(position).getProvince());//需要传入的是父view
                        break;
                    case R.id.ll_city:
                        Utils.showSnackBar(recyclerView, list.get(position).getCity());
                        break;
                    case R.id.ll_city_j:

                        List<CityBean> lists=new ArrayList<>();
                        for (int j = 0; j < 5; j++) {
                            CityBean cityBean = new CityBean();
                            cityBean.setCity("点击加载的数据"+j);
                            cityBean.setTitle(1);//设置为title
                            cityBean.setTag(list.get(position).getTag());//设置成和省份一样的tag，将省份与城市绑定。
                            citylist.get(Integer.valueOf(list.get(position).getTag())).getMane().add("深圳"+j);
                            lists.add(cityBean);
                        }
                        list.addAll(position,lists);
                        notifyAdapter();
                        break;
                }

            }
        });
        ItemHeaderDecoration decoration = new ItemHeaderDecoration(context, list);
        decoration.setData(list);
        recyclerView.addItemDecoration(decoration);
        decoration.setCheckListener(checkListener);
        recyclerView.setAdapter(adapter);
    }


    private void notifyAdapter() {

        ItemHeaderDecoration decoration = new ItemHeaderDecoration(getActivity(), list);
        decoration.setData(list);
        recyclerView.addItemDecoration(decoration);
        decoration.setCheckListener(checkListener);
        adapter.setList(list);
    }

    /*
     获取左边点击，右边滑动的距离
     */
    public void setCounts(int counts) {
        moveCounts = counts;
        moveToPosition(moveCounts);
        Log.i("<<<<<<", "count:" + moveCounts);
    }

    /*
    移动到指定位置
     */
    private void moveToPosition(int moveCounts) {
        int firstItem = manager.findFirstVisibleItemPosition();//获取屏幕可见的第一个item的position
        int lastItem = manager.findLastVisibleItemPosition();//获取屏幕可见的最后一个item的position
        if (moveCounts < firstItem) {
            recyclerView.scrollToPosition(moveCounts);
        } else if (moveCounts < lastItem) {
            View aimsView = recyclerView.getChildAt(moveCounts - firstItem);
            int top = aimsView.getTop();
            recyclerView.scrollBy(0, top);
        } else {
            /*
            当往下滑动的position大于可见的最后一个item的时候，调用 recyclerView.scrollToPosition(moveCounts);
            只能讲item滑动到屏幕的底部。
             */
            /*
            第一种方案：先将item移动到底部，然后在调用scrollBy移动到顶部。不可行，不能讲item滑动到顶部，
            离上面还有一小段距离；
             recyclerView.scrollToPosition(moveCounts);
            int top=recyclerView.getHeight();
            recyclerView.scrollBy(0,top);

            第二种方案：直接计算要滑动的距离。程序崩溃，报空指针。看系统源码可知，当
            滑动的距离大于ChildCount（可见的item数目），将返回空。
            int top=recyclerView.getChildAt(moveCounts-firstItem).getTop();
            recyclerView.scrollBy(0,top);

            第三种解决方案：先将目标item滑动到底部，然后进行异步处理。调用滚动监听方法RecyclerViewListener。
            滑动到顶部

             */

//            int top=recyclerView.getHeight();
//            recyclerView.scrollBy(0,top);
//            int childcount=recyclerView.getChildCount();
//            Log.i("<<<<<<<<<<","childcount"+childcount);
//            int top=recyclerView.getChildAt(moveCounts-firstItem).getTop();
//            recyclerView.scrollBy(0,top);

            recyclerView.scrollToPosition(moveCounts);
            move = true;
        }
    }

    private void getData() {
        if (citylist != null) {
            return;
        }
        citylist = new ArrayList<>();
        StringList stringList1 = new StringList();
        stringList1.setMane(new ArrayList<String>(Arrays.asList(new String[]{"深圳", "东莞", "广州", "韶关", "汕头", "肇庆", "惠州"})));
        citylist.add(stringList1);
        StringList stringList2 = new StringList();
        stringList2.setMane(new ArrayList<String>(Arrays.asList(new String[]{"哈尔滨", "尚志", "五常", "海伦"})));
        citylist.add(stringList2);
        StringList stringList3 = new StringList();
        stringList3.setMane(new ArrayList<String>(Arrays.asList(new String[]{"南昌", "赣州", "宜春", "吉安"})));
        citylist.add(stringList3);
        StringList stringList4 = new StringList();
        stringList4.setMane(new ArrayList<String>(Arrays.asList(new String[]{"沈阳", "大连", "鞍山", "丹东"})));
        citylist.add(stringList4);
        StringList stringList5 = new StringList();
        stringList5.setMane(new ArrayList<String>(Arrays.asList(new String[]{"呼和浩特", "包头", "赤峰", "鄂尔多斯"})));
        citylist.add(stringList5);
        StringList stringList6 = new StringList();
        stringList6.setMane(new ArrayList<String>(Arrays.asList(new String[]{"银川", "石嘴山", "吴忠", "中卫"})));
        citylist.add(stringList6);
        StringList stringList7 = new StringList();
        stringList7.setMane(new ArrayList<String>(Arrays.asList(new String[]{"太原", "大同", "运城", "临汾"})));
        citylist.add(stringList7);
        StringList stringList8 = new StringList();
        stringList8.setMane(new ArrayList<String>(Arrays.asList(new String[]{"西安", "宝鸡", "咸阳", "延安"})));
        citylist.add(stringList8);
        StringList stringList9 = new StringList();
        stringList9.setMane(new ArrayList<String>(Arrays.asList(new String[]{"拉萨", "日喀则", "那曲", "巴青"})));
        citylist.add(stringList9);
        StringList stringList10 = new StringList();
        stringList10.setMane(new ArrayList<String>(Arrays.asList(new String[]{"昆明", "大理", "丽江", "普洱"})));
        citylist.add(stringList10);
        StringList stringList11 = new StringList();
        stringList11.setMane(new ArrayList<String>(Arrays.asList(new String[]{"兰州", "天水", "白银", "平凉"})));
        citylist.add(stringList11);
        StringList stringList12 = new StringList();
        stringList12.setMane(new ArrayList<String>(Arrays.asList(new String[]{"南宁", "柳州", "桂林", "钦州"})));
        citylist.add(stringList12);
        StringList stringList13 = new StringList();
        stringList13.setMane(new ArrayList<String>(Arrays.asList(new String[]{"海口", "三亚", "三沙", "琼海"})));
        citylist.add(stringList13);
    }

    private void initData() {
        getData();
        for (int i = 0; i < presenters.size(); i++) {
            CityBean titleBean = new CityBean();
            titleBean.setProvince(presenters.get(i).getName());
            titleBean.setCity("111");
            titleBean.setTitle(0);//设置为title
            titleBean.setTag(String.valueOf(i));//设置tag，方便获取position
            list.add(titleBean);

            for (int j = 0; j < citylist.get(i).getMane().size(); j++) {
                CityBean cityBean = new CityBean();
                cityBean.setCity(citylist.get(i).getMane().get(j));
                cityBean.setTitle(1);//设置为title
                cityBean.setTag(String.valueOf(i));//设置成和省份一样的tag，将省份与城市绑定。
                list.add(cityBean);
            }
            CityBean cityBeanj = new CityBean();
            cityBeanj.setTitle(2);//设置为title
            cityBeanj.setCity("111");
            cityBeanj.setTag(String.valueOf(i));//设置成和省份一样的tag，将省份与城市绑定。
            list.add(cityBeanj);
        }
        Log.i(">>>>>>", list.get(2).city.toString());
        Log.i(">>>>>>", citylist.size() + "");
        Log.i(">>>>>>", list.size() + "size");
    }

    @Override
    public void check(int position, boolean isScroll) {
        checkListener.check(position, isScroll);
    }

    public void setCheck(CheckListener listener) {
        this.checkListener = listener;
    }

    class RecyclerViewListener extends RecyclerView.OnScrollListener {
        /*
        监听回调，滑动结束回调。
         */
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //在这里进行第二次滚动（最后的100米！）
            if (move) {
                move = false;
                //获取要置顶的项在当前屏幕的位置，moveCount是记录的要置顶项在RecyclerView中的位置
                int n = moveCounts - manager.findFirstVisibleItemPosition();
                if (0 <= n && n < recyclerView.getChildCount()) {
                    //获取要置顶的项顶部离RecyclerView顶部的距离
                    int top = recyclerView.getChildAt(n).getTop();
                    //最后的移动
                    recyclerView.scrollBy(0, top);
                }
            }
            final int enditem=manager.findLastVisibleItemPosition();
            final CityBean cityBeans = list.get(enditem);
            final int positions = Integer.valueOf(cityBeans.getTag());
            if (isLoading){
                return;
            }
            if (presenters.get(positions).isAll()) {
                if (citylist.size() > 0) {
                    if (cityBeans.getCity().equals(citylist.get(positions).getMane().get(citylist.get(positions).getMane().size() - 2))) {
                                  isLoading=true;//延迟操作结束后 false；

                                presenters.get(positions).setAll(false);
                                List<CityBean> lists=new ArrayList<>();
                                for (int j = 0; j < 5; j++) {
                                    CityBean cityBean = new CityBean();
                                    cityBean.setCity("深圳"+j);
                                    cityBean.setTitle(1);//设置为title
                                    cityBean.setTag(cityBeans.getTag());//设置成和省份一样的tag，将省份与城市绑定。
                                    citylist.get(positions).getMane().add("深圳"+j);
                                    lists.add(cityBean);
                                }
                                list.addAll(enditem+2,lists);
                                notifyAdapter();

                    }
                }
            }
        }

        /*
        监听回调，滑动状态改变回调
         */
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                move = false;
                int n = moveCounts - manager.findFirstVisibleItemPosition();
                if (0 <= n && n < recyclerView.getChildCount()) {
                    int top = recyclerView.getChildAt(n).getTop();
                    recyclerView.scrollBy(0, top);
                }
            }

        }
    }
}

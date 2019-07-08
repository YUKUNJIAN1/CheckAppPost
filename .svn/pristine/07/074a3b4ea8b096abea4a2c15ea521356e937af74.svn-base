package com.frxs.check;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ewu.core.utils.SystemUtils;
import com.ewu.core.utils.ToastUtils;
import com.frxs.check.application.FrxsApplication;
import com.frxs.check.fragment.GoodsDetailFragment;
import com.frxs.check.model.GetDeliverProductInfo;
import com.frxs.check.model.ProductData;
import com.frxs.check.model.UserInfo;
import com.frxs.check.rest.model.AjaxParams;
import com.frxs.check.rest.model.ApiResponse;
import com.frxs.check.rest.service.SimpleCallback;
import com.frxs.check.widget.EmptyView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


/**
 * 商品清单 by Tipier
 */
public class OrderDetailsActivity extends FrxsActivity {

    private GetDeliverProductInfo productInfo;

    private String strOrderID;//订单ID

    private TextView tvTitle;

    private EmptyView emptyView;

    private TabLayout mTabLayout;

    private ViewPager mShelfPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_details;
    }

    @Override
    protected void initViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        emptyView = (EmptyView) findViewById(R.id.emptyview);
        mTabLayout = (TabLayout) findViewById(R.id.tl_tablayout);
        mTabLayout.setTabTextColors(Color.BLACK,Color.RED);
        mShelfPager = (ViewPager) findViewById(R.id.vp_viewpager);
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void initData() {
        tvTitle.setText("商品详情");
        emptyView.setVisibility(View.GONE);

        Intent intent = getIntent();
        if (intent != null) {
            strOrderID = intent.getStringExtra("ORDERID");
        }
        requestCommodityList();
    }

    public void setTabFragmentPager(List<ProductData> productDataList) {
        final List<Fragment> listFragments = new ArrayList<>();
        for (int i = 0; i < productDataList.size(); i++) {
            GoodsDetailFragment goodsDetailFragment = new GoodsDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("product_list", (Serializable) productDataList.get(i));
            bundle.putString("ORDERID", strOrderID);
            goodsDetailFragment.setArguments(bundle);
            listFragments.add(goodsDetailFragment);
        }

        SimpleFragmentPagerAdapter simplePagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), listFragments, productDataList);
        mShelfPager.setAdapter(simplePagerAdapter);

        int lastPosition = mTabLayout.getSelectedTabPosition();
        mTabLayout.setupWithViewPager(mShelfPager);
        mShelfPager.setCurrentItem(lastPosition);
    }

    /**
     * Fragment适配器
     */
    class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> listFragments;
        private List<ProductData> productDataList;
        public SimpleFragmentPagerAdapter(FragmentManager fm, List<Fragment> listFragments, List<ProductData> productDataList) {
            super(fm);
            this.listFragments = listFragments;
            this.productDataList = productDataList;
        }

        @Override
        public Fragment getItem(int position) {
            return listFragments.get(position);
        }

        @Override
        public int getCount() {
            return listFragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (productDataList != null && position < productDataList.size()) {
                return productDataList.get(position).getShelfAreaType();
            } else {
                return "";
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }

    }

    /**
     * 商品清单数据请求
     */
    public void requestCommodityList() {
        progressDialog.show();
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        AjaxParams params = new AjaxParams();
        params.put("WID", String.valueOf(userInfo.getWID()));
        if (!TextUtils.isEmpty(strOrderID)) {
            params.put("OrderId", strOrderID);
        }
        getService().PostDeliverProductInfo(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<GetDeliverProductInfo>>() {
            @Override
            public void onResponse(ApiResponse<GetDeliverProductInfo> result, int code, String msg) {
                if (result.getFlag().equals("0")) {
                    productInfo = result.getData();
                    if (productInfo != null) {

                        List<ProductData> productDataList = productInfo.getProductData();
                        if (null != productDataList && productDataList.size() > 0) {
                            setTabFragmentPager(productDataList);
                        }
                    }
                } else {
                    ToastUtils.show(OrderDetailsActivity.this, result.getInfo());
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setImageResource(R.mipmap.icon_visit_fail);
                    emptyView.setMode(EmptyView.MODE_NODATA);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponse<GetDeliverProductInfo>> call, Throwable t) {
                super.onFailure(call, t);
                progressDialog.dismiss();
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setImageResource(R.mipmap.icon_visit_fail);
                emptyView.setText("没有数据，请点击页面重试");
                emptyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemUtils.isNetworkAvailable(OrderDetailsActivity.this)) {
                            requestCommodityList();
                        }else
                        {
                            ToastUtils.show(OrderDetailsActivity.this,"网络异常，请检查网络是否连接");
                        }
                    }
                });
            }
        });
    }

}

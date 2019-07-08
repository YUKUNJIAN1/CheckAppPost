package com.frxs.check;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ewu.core.utils.SystemUtils;
import com.ewu.core.utils.ToastUtils;
import com.frxs.check.application.FrxsApplication;
import com.frxs.check.fragment.BigGoodsFragment;
import com.frxs.check.greendao.DBHelper;
import com.frxs.check.greendao.entity.ProductEntity;
import com.frxs.check.greendao.gen.ProductEntityDao;
import com.frxs.check.model.GetDeliverProductInfo;
import com.frxs.check.model.ProductData;
import com.frxs.check.model.UserInfo;
import com.frxs.check.rest.model.AjaxParams;
import com.frxs.check.rest.model.ApiResponse;
import com.frxs.check.rest.service.SimpleCallback;
import com.frxs.check.widget.EmptyView;
import com.lid.lib.LabelTextView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;


/**
 * 商品列表 by Tipier
 */
public class CheckGoodsListActivity extends FrxsActivity {

    private GetDeliverProductInfo productInfo;

    private String strOrderID;//订单ID

    private TextView tvTitle;

    private EmptyView emptyView;

    private TabLayout mTabLayout;

    private ViewPager mShelfPager;

    private TextView tvStationNum;// 待装区编号

    private boolean tabSort = true;// ture为升序

    private HashMap<String, Boolean> shelfAreaSortStateMap = new HashMap<String, Boolean>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_goods_new;
    }

    @Override
    protected void initViews() {
        /**
         * 实例化控件
         */
        tvTitle = (TextView) findViewById(R.id.tv_title);
        emptyView = (EmptyView) findViewById(R.id.emptyview);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        mTabLayout = (TabLayout) findViewById(R.id.tl_tablayout);
        mShelfPager = (ViewPager) findViewById(R.id.vp_viewpager);
        //mTabLayout.setTabTextColors(Color.BLACK, Color.RED);

        tvStationNum = (TextView) findViewById(R.id.tv_station_num);

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        tvTitle.setText("商品列表");
        emptyView.setVisibility(View.GONE);
        /**
         * 获取订单ID
         */
        Intent intent = getIntent();
        if (intent != null) {
            strOrderID = intent.getStringExtra("ORDERID");
            String stationnumber = intent.getStringExtra("STATIONNUMBER");
            tvStationNum.setText("待装区编号：" + (TextUtils.isEmpty(stationnumber) ? "" : stationnumber));
        }

        if (SystemUtils.isNetworkAvailable(CheckGoodsListActivity.this)) {
            requestCommodityList(strOrderID);
        } else {
            ToastUtils.show(CheckGoodsListActivity.this, "网络异常，请检查网络是否连接");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    /**
     * 赋值Fragment
     * @param productDataList
     * @param orderId
     */
    public void setTabFragmentData(List<ProductData> productDataList, String orderId) {
        final List<Fragment> listFragments = new ArrayList<>();
        for (int i = 0; i < productDataList.size(); i++) {
            List<ProductEntity> productList = productDataList.get(i).getProdcutDetailList();
            if (productList == null || productList.size() <= 0) {
                ToastUtils.show(this, "接口返回数据错误：ProdcutDetailList为空");
                return;
            }

            for (ProductEntity item : productList) {
                item.setOrderId(orderId);
                item.setPickedQty(item.getSaleQty());
                if (!TextUtils.isEmpty(item.getAreaCheckTime())) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
            }

            List<ProductEntity> productEntityList = DBHelper.getProductEntityService().queryBuilder().where(ProductEntityDao.Properties.OrderId.eq(orderId), ProductEntityDao.Properties.ShelfAreaCategoryName.eq(productDataList.get(i).getShelfAreaType())).list();
            if (null != productEntityList && productEntityList.size() > 0 && productList.size() == productEntityList.size()) {
                compareProductList(productList, productEntityList);
                productDataList.get(i).setProdcutDetailList(productEntityList);
            } else {
                if (null != productEntityList) {
                    DBHelper.getProductEntityService().delete(productEntityList);
                }

                DBHelper.getProductEntityService().saveOrUpdate(productList);
            }

            if (!shelfAreaSortStateMap.containsKey(productDataList.get(i).getShelfAreaType())) {
                shelfAreaSortStateMap.put(productDataList.get(i).getShelfAreaType(), tabSort);
            }

            boolean sortType = shelfAreaSortStateMap.get(productDataList.get(i).getShelfAreaType());
            BigGoodsFragment bigf = new BigGoodsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("product_list", (Serializable) productDataList.get(i));
            bundle.putString("ORDERID", TextUtils.isEmpty(orderId) ? strOrderID : orderId);
            bundle.putBoolean("SORTTYPE", sortType);
            bigf.setArguments(bundle);
            listFragments.add(bigf);
        }

        SimpleFragmentPagerAdapter simplePagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), listFragments, productDataList);
        mShelfPager.setAdapter(simplePagerAdapter);

        int lastPosition = mTabLayout.getSelectedTabPosition();
        mTabLayout.setupWithViewPager(mShelfPager);
        mShelfPager.setCurrentItem(lastPosition);

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(simplePagerAdapter.getTabView(i));
            }
        }
    }

    /**
     * 比较服务端返回货区的商品列表和本地数据库缓存的货区商品列表，如果不一致，则修改本地数据库商品为未对货状态，并返回需要重新对货的标识
     * @param productList 服务端返回的数据
     * @param productEntityList 本地数据库缓存的数据
     * @return 是否需要重新对货
     */
    private boolean compareProductList(List<ProductEntity> productList, List<ProductEntity> productEntityList) {
        boolean needCheck = false;

        HashMap<String, ProductEntity> productsHashMap = new HashMap<String, ProductEntity>();
        for (ProductEntity item : productEntityList) {
            String key = item.getProductId() + item.getIsGift();
            productsHashMap.put(key, item);
            if (item.isChecked()) {
                needCheck = true;
            }
        }

        for (ProductEntity item : productList) {
            ProductEntity localProduct = productsHashMap.get(item.getProductId() + item.getIsGift());
            boolean bChangedUnit = !localProduct.getSaleUnit().equals(item.getSaleUnit()); //是否修改了单位
            if (null != localProduct) {
                if (TextUtils.isEmpty(item.getPickEndTime())) {
                    localProduct.setSaleUnit(item.getSaleUnit());
                    localProduct.setPreQty(item.getPreQty());
                    localProduct.setPickedQty(item.getSaleQty());
                    localProduct.setSaleQty(item.getSaleQty());
                    needCheck = true;
                    localProduct.setChecked(true);
                    DBHelper.getProductEntityService().update(localProduct);
                } else {
                    if (TextUtils.isEmpty(localProduct.getPickEndTime()) || bChangedUnit || localProduct.getSaleQty() != item.getSaleQty()) {
                        needCheck = true;
                        if (TextUtils.isEmpty(localProduct.getPickEndTime())) {
                            localProduct.setSaleQty(item.getSaleQty());
                        }
                        localProduct.setChecked(true);
                        localProduct.setSaleUnit(item.getSaleUnit());
                        localProduct.setPreQty(item.getPreQty());
                        localProduct.setPickedQty(item.getSaleQty());
                        localProduct.setPickEndTime(item.getPickEndTime());
                        localProduct.setAreaCheckTime(item.getAreaCheckTime());
                    }
                    DBHelper.getProductEntityService().update(localProduct);
                }
            }
        }

        return needCheck;
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
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment)super.instantiateItem(container, position);

            Fragment newFragment = listFragments.get(position);
            if (!fragment.equals(newFragment)) {
                String fragmentTag = fragment.getTag();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                //移除旧的fragment
                ft.remove(fragment);
                //换成新的fragment
                fragment = listFragments.get(position);
                //添加新fragment时必须用前面获得的tag
                ft.add(container.getId(), fragment, fragmentTag);
                ft.attach(fragment);
                ft.commit();
            }

            return fragment;
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
            return POSITION_NONE;
        }

        private void initTabSortView(TextView sortTextView, boolean sortType) {
            if (sortType) {
                sortTextView.setText("升序");
                sortTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_sort_up), null);
                sortTextView.setCompoundDrawablePadding(5);
            } else {
                sortTextView.setText("降序");
                sortTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_sort_down), null);
                sortTextView.setCompoundDrawablePadding(5);
            }
        }

        public View getTabView(final int position) {
            View v = LayoutInflater.from(CheckGoodsListActivity.this).inflate(R.layout.tab_custom, null);
            TextView tvAreaType = (TextView) v.findViewById(R.id.tv_tab_text);
            tvAreaType.setText(productDataList.get(position).getShelfAreaType());
            boolean sortType = shelfAreaSortStateMap.get(productDataList.get(position).getShelfAreaType());
            final TextView tvTabSort = (TextView) v.findViewById(R.id.tv_tab_sort);
            initTabSortView(tvTabSort, sortType);
            tvTabSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedPosition = mTabLayout.getSelectedTabPosition();
                    if (selectedPosition == position){
                        BigGoodsFragment currentFrg = (BigGoodsFragment)((SimpleFragmentPagerAdapter)mShelfPager.getAdapter()).getItem(position);
                        if (null != currentFrg) {
                            tabSort = !tabSort;
                            shelfAreaSortStateMap.put(productDataList.get(position).getShelfAreaType(), tabSort);
                            initTabSortView(tvTabSort, tabSort);
                            currentFrg.resortGoodsView(tabSort);
                        }
                    } else {
                        mShelfPager.setCurrentItem(position);
                    }

                }
            });
            LabelTextView tvShelfareaName = (LabelTextView) v.findViewById(R.id.tv_shelfarea_name);
            tvShelfareaName.setLabelTextColor(getResources().getColor(R.color.white));
            tvShelfareaName.setLabelBackgroundColor(getResources().getColor(R.color.label_red));
            tvShelfareaName.setLabelText("已对货");

            if (!TextUtils.isEmpty(productDataList.get(position).getProdcutDetailList().get(0).getAreaCheckTime())) {
                tvShelfareaName.setLabelEnable(true);
            } else {
                tvShelfareaName.setLabelEnable(false);
            }
            return v;
        }
    }

    public void refreshFragment(String orderID) {
        requestCommodityList(orderID);
    }

    /**
     * 商品清单数据请求
     */
    public void requestCommodityList(final String strOrderID) {
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
                        String orderId = productInfo.getOrderId();
                        List<ProductData> productDatas = productInfo.getProductData();
                        if (null != productDatas && productDatas.size() > 0) {
                           // boolean updateFrag = needUpdateFrag;
                            setTabFragmentData(productDatas, orderId);
                        }
                    }
                } else {
                    ToastUtils.show(CheckGoodsListActivity.this, result.getInfo());
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
                        if (SystemUtils.isNetworkAvailable(CheckGoodsListActivity.this)) {
                            requestCommodityList(strOrderID);
                        } else {
                            ToastUtils.show(CheckGoodsListActivity.this, "网络异常，请检查网络是否连接");
                        }
                    }
                });
            }
        });
    }
}

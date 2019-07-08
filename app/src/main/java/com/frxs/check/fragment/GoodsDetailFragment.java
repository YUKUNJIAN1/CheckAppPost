package com.frxs.check.fragment;

import android.view.View;
import android.widget.ListView;

import com.ewu.core.utils.DisplayUtil;
import com.ewu.core.utils.MathUtils;
import com.frxs.check.R;
import com.frxs.check.greendao.entity.ProductEntity;
import com.frxs.check.model.ProductData;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.List;

/**
 * Created by Endoon on 2016/4/7.
 */
public class GoodsDetailFragment extends MaterialStyleFragment {

    private ListView GoodsLv;

    private QuickAdapter<ProductEntity> productAdapter;

    private ProductData mProductData;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods_detail;
    }

    @Override
    protected void initViews(View view) {
        GoodsLv = (ListView) view.findViewById(R.id.lv_goods);
    }

    @Override
    protected void initData() {
        mProductData = (ProductData) getArguments().getSerializable("product_list");

        if (mProductData != null) {
            List<ProductEntity> detailLists = mProductData.getProdcutDetailList();
            if (detailLists != null && detailLists.size() > 0) {
                productAdapter = new QuickAdapter<ProductEntity>(mActivity, R.layout.item_commodity_list, detailLists) {
                    @Override
                    protected void convert(BaseAdapterHelper helper, ProductEntity item) {
                        String productName = item.getProductName();
                        if (item.getIsGift() == 1) { //赠品
                            productName = "           " + item.getProductName();
                            helper.setText(R.id.tv_goods_gift, getResources().getString(R.string.product_gift));
                            helper.setVisible(R.id.tv_goods_gift, true);
                        } else if (item.getIsGift() == 2) { //搭售
                            productName = "           " + item.getProductName();
                            helper.setText(R.id.tv_goods_gift, getResources().getString(R.string.product_reduce));
                            helper.setVisible(R.id.tv_goods_gift, true);
                        } else {
                            helper.setVisible(R.id.tv_goods_gift, false);
                        }
                        helper.setText(R.id.tv_goods_name, productName);
                        helper.setText(R.id.tv_goods_count, DisplayUtil.subZeroAndDot(MathUtils.twolittercountString(item.getSaleQty())));
                        helper.setText(R.id.tv_goods_remark, "备注：" + item.getRemark());
                        helper.setText(R.id.tv_goods_unit, item.getSaleUnit());
                    }
                };
                GoodsLv.setAdapter(productAdapter);
            }
        }

    }


    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {

    }
}

package com.frxs.check.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import com.ewu.core.utils.DisplayUtil;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.SortListObjectUtil;
import com.ewu.core.utils.ToastUtils;
import com.frxs.check.HomeActivity;
import com.frxs.check.R;
import com.frxs.check.application.FrxsApplication;
import com.frxs.check.greendao.DBHelper;
import com.frxs.check.greendao.entity.ProductEntity;
import com.frxs.check.model.GoodsInfo;
import com.frxs.check.model.ProductData;
import com.frxs.check.model.SubmitCheckInfo;
import com.frxs.check.model.UserInfo;
import com.frxs.check.model.Warehouse;
import com.frxs.check.rest.model.AjaxParams;
import com.frxs.check.rest.model.ApiResponse;
import com.frxs.check.rest.service.SimpleCallback;
import com.frxs.check.widget.CountEditText;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.kyleduo.switchbutton.SwitchButton;
import com.lid.lib.LabelTextView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;

/**
 * Created by Endoon on 2016/4/7.
 */
public class BigGoodsFragment extends MaterialStyleFragment {
    private ListView lvBigGoods;

    private QuickAdapter<ProductEntity> productAdapter;

    private TextView btnChecked;

    private List<ProductEntity> wProductExts = new ArrayList<>();

    private String strOrderID;//订单ID

    private TextView tvGoodsCount;//商品数量

    private TextView tvGoodsLine;//商品行数

    private TextView tvBarCode;

    private String strShelfAreaId;//获取ID

    private List<GoodsInfo> goodsInfoList = new ArrayList<>();

    private String strTell;

    private ProductData mProductData;

    private boolean tabSort = true;//货区商品默认为升序排序

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_big_goods;
    }

    @Override
    protected void initViews(View view) {
        lvBigGoods = (ListView) view.findViewById(R.id.lv_goods);
        btnChecked = (TextView) view.findViewById(R.id.btn_checked);
        tvGoodsCount = (TextView) view.findViewById(R.id.tv_goods_count);
        tvGoodsLine = (TextView) view.findViewById(R.id.tv_goods_line);
        tvBarCode = (TextView) view.findViewById(R.id.tv_barcode);
    }

    @Override
    protected void initData() {
        mProductData = (ProductData) getArguments().getSerializable("product_list");
        strOrderID = getArguments().getString("ORDERID");
        tabSort = getArguments().getBoolean("SORTTYPE", true);

        filledViewWithData();
        addFooterView();
    }

    private void filledViewWithData() {
        if (mProductData != null) {
            List<ProductEntity> detailLists = mProductData.getProdcutDetailList();
            if (detailLists != null && detailLists.size() > 0) {
                wProductExts.clear();
                wProductExts.addAll(detailLists);
                if (tabSort) {
                    // 按货位号升序排列
                    SortListObjectUtil.sort(wProductExts, "getShelfCode", SortListObjectUtil.ORDER_BY_ASC);
                } else {
                    // 按货位号降序排列
                    SortListObjectUtil.sort(wProductExts, "getShelfCode", SortListObjectUtil.ORDER_BY_DESC);
                }
                refreshProductView();
            }
        }

        if (wProductExts != null && wProductExts.size() > 0) {
            productAdapter = new QuickAdapter<ProductEntity>(mActivity, R.layout.item_check_goods, wProductExts) {
                @Override
                protected void convert(final BaseAdapterHelper helper, final ProductEntity item) {
                    // 商品有备注才显示
                    if (TextUtils.isEmpty(item.getRemark())) {
                        helper.setVisible(R.id.remark_ll, false);
                    } else {
                        helper.setVisible(R.id.remark_ll, true);
                        helper.setText(R.id.remark_tv, "备注：" + item.getRemark());
                    }
                    helper.setText(R.id.tv_barcode, "商品编码：" + item.getSKU());// 商品编码
                    SwitchButton switchButton = (SwitchButton) helper.getView().findViewById(R.id.sb_text);//对货开关按钮
                    final TextView tvQty = (TextView) helper.getView().findViewById(R.id.tv_sale_qty);//商品数量
                    tvQty.setText(DisplayUtil.subZeroAndDot(MathUtils.twolittercountString(item.getSaleQty())) + item.getSaleUnit());
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

                    LabelTextView labelTextView = helper.getView(R.id.lab_textview);
                    labelTextView.setLabelTextColor(getResources().getColor(R.color.white));
                    labelTextView.setLabelBackgroundColor(getResources().getColor(R.color.dialog_font));
                    labelTextView.setLabelText("已拣");

                    helper.setText(R.id.tv_product_name, productName);
                    helper.setText(R.id.package_num_tv, String.format(getResources().getString(R.string.package_num), item.getSalePackingQty()));
                    helper.setText(R.id.unit_price_tv, String.format(getResources().getString(R.string.unit_price), item.getSalePrice()));
                    helper.setText(R.id.bar_code_tv, String.format(getResources().getString(R.string.bar_code), item.getBarCode().split(",")[0]));
                    if (!TextUtils.isEmpty(item.getPickEndTime())) {
                        labelTextView.setLabelEnable(true);
                    } else {
                        labelTextView.setLabelEnable(false);
                    }

                    if (isCheckRight(item) == 0) {
                        if (item.isChecked()) {
                            switchButton.setCheckedNoEvent(true);
                            tvQty.setTextColor(getResources().getColor(R.color.white));
                            tvQty.setBackgroundResource(R.drawable.shape_red_solid);
                        } else {
                            switchButton.setCheckedNoEvent(false);
                            tvQty.setTextColor(getResources().getColor(R.color.white));
                            tvQty.setBackgroundResource(R.drawable.shape_gray_solid);
                        }
                    } else {
                        if (item.isChecked()) {
                            switchButton.setCheckedNoEvent(true);
                            tvQty.setTextColor(getResources().getColor(R.color.red));
                            tvQty.setBackgroundResource(R.drawable.shape_red);
                        } else {
                            switchButton.setCheckedNoEvent(false);
                            tvQty.setTextColor(getResources().getColor(R.color.gray));
                            tvQty.setBackgroundResource(R.drawable.shape_green);
                        }
                    }

                    switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                            if (TextUtils.isEmpty(mProductData.getAreaCheckTime()))
                            {
                                if (checked) {// 未对货
                                    if (isCheckRight(item) == 0) {
                                        tvQty.setTextColor(getResources().getColor(R.color.white));
                                        tvQty.setBackgroundResource(R.drawable.shape_red_solid);
                                    } else {
                                        tvQty.setTextColor(getResources().getColor(R.color.red));
                                        tvQty.setBackgroundResource(R.drawable.shape_red);
                                    }
                                    item.setChecked(true);
                                    btnChecked.setBackgroundResource(R.color.frxs_red);
                                } else {// 已对货
                                    if (isCheckRight(item) == 0) {
                                        tvQty.setTextColor(getResources().getColor(R.color.white));
                                        tvQty.setBackgroundResource(R.drawable.shape_gray_solid);
                                    } else {
                                        tvQty.setTextColor(getResources().getColor(R.color.gray));
                                        tvQty.setBackgroundResource(R.drawable.shape_green);
                                    }
                                    item.setChecked(false);
                                }

                                DBHelper.getProductEntityService().update(item);
                            }
                        }
                    });

                    helper.setOnClickListener(R.id.tv_sale_qty, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showCheckDialog(item);
                        }
                    });
                }
            };
            lvBigGoods.setAdapter(productAdapter);
        }

        if (TextUtils.isEmpty(mProductData.getProdcutDetailList().get(0).getAreaCheckTime())) {
            btnChecked.setBackgroundResource(R.color.frxs_red);
        } else {
            //当前货区已经对过货时，判断本地缓存的商品数据是否有未对货的状态，当前货区为已对货时默认值为false；
            boolean needCheck = false;
            for (ProductEntity productEntity : wProductExts){
                if (productEntity.isChecked()){
                    needCheck = true;
                    break;
                }
            }

            if (needCheck){
                btnChecked.setBackgroundResource(R.color.frxs_red);
            } else {
                btnChecked.setBackgroundResource(R.color.gray);
            }

        }
    }

    /**
     * 拣货信息对话框
     */
    private void showCheckDialog(final ProductEntity item) {
        final Dialog dialog = new Dialog(mActivity, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_check);
        dialog.setCancelable(false);

        TextView btnConfirm = (TextView) dialog.findViewById(R.id.btn_confirm);// 确定+
        TextView btnCancel = (TextView) dialog.findViewById(R.id.btn_cancel);// 取消
        TextView tvCheckPersonnel = (TextView) dialog.findViewById(R.id.tv_check_personnel);//拣货员
        TextView tvCount = (TextView) dialog.findViewById(R.id.tv_check_count);//拣货数量
        TextView tvActual = (TextView) dialog.findViewById(R.id.tv_check_actual);//实发数量标题
        TextView tvPreCount = (TextView) dialog.findViewById(R.id.tv_pre_count);//订货数量
        final CountEditText countEditText = (CountEditText) dialog.findViewById(R.id.cet_count);//实发数量

        tvCheckPersonnel.setText("拣货员：" + item.getPickUserName());
        tvCount.setText("拣货数量：" + DisplayUtil.subZeroAndDot(MathUtils.twolittercountString(item.getPickedQty())) + item.getSaleUnit());
        tvPreCount.setText("订货数量：" +  DisplayUtil.subZeroAndDot(MathUtils.twolittercountString(item.getPreQty())) + item.getSaleUnit());
        if (item.getIsGift() == 1) {
            countEditText.setMaxCount(item.getPickedQty());
        } else {
            countEditText.setMaxCount(99999999);
        }
        countEditText.setCount(item.getSaleQty());

        if (item.isChecked()) {
            countEditText.setVisibility(View.VISIBLE);
        } else {
            countEditText.setVisibility(View.GONE);
            tvActual.setText("实发数量：" + DisplayUtil.subZeroAndDot(MathUtils.twolittercountString(item.getSaleQty())) + item.getSaleUnit());
        }

        //拨打电话
        tvCheckPersonnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqGetPickInfo(item.getPickUserId());
            }
        });

        //修改数量
        countEditText.setOnCountChangeListener(new CountEditText.onCountChangeListener() {
            @Override
            public void onCountAdd(double count) {

            }

            @Override
            public void onCountSub(double count) {

            }

            @Override
            public void onCountEdit(double count) {

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // 获取装箱数量
                double modifyCount = countEditText.getCount();
                // 获取规定倍数
                int multiple = FrxsApplication.getInstance().getUserInfo().getMultiple();
                // 是否为赠品并已修改数量
                if (item.getIsGift() == 1 && modifyCount > item.getPickedQty()) {
                    dialog.dismiss();
                    ToastUtils.show(mActivity, "赠品不能修改数量");
                    return;
                }
                // 装箱数量是否超过订货数量的指定倍数
                if (modifyCount <= item.getPreQty() * multiple) {
                    setPackQty(modifyCount, dialog, item);
                } else {
                    ToastUtils.show(mActivity, String.format(getString(R.string.cheked_multiple), String.valueOf(multiple)));
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * 拣货继续对话框
     */
    private void showAskDialog() {
        final Dialog dialog = new Dialog(mActivity, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_ask);
        dialog.setCancelable(false);
        TextView btnConfirm = (TextView) dialog.findViewById(R.id.btn_confirm);// 确定
        TextView btnCancel = (TextView) dialog.findViewById(R.id.btn_cancel);// 取消
        TextView tvAskText = (TextView) dialog.findViewById(R.id.tv_sak_text);

        tvAskText.setText("是否完成对货?");

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                reqCheckPickUpdate();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void initEvent() {
        btnChecked.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_checked: {
//                List<ProductEntity> prodcutDetailList = mProductData.getProdcutDetailList();
//                for (ProductEntity productEntity : prodcutDetailList) {
//                    if (TextUtils.isEmpty(productEntity.getPickEndTime())) {
//                        ToastUtils.show(mActivity, "该货区未拣货完成，无法对货");
//                        return;
//                    }
//                }
//                showAskDialog();

                List<ProductEntity> prodcutDetailList = mProductData.getProdcutDetailList();
                for (ProductEntity productEntity : prodcutDetailList) {
                    if (TextUtils.isEmpty(productEntity.getPickEndTime())) {
                        ToastUtils.show(mActivity, "该货区未拣货完成，无法对货");
                        return;
                    }
                }
                showAskDialog();
//                if (!TextUtils.isEmpty(mProductData.getPickEndTime())) {
//                    // 不用判断是否可以重复对货（现有需求就是需要重复对货）
//                    showAskDialog();
////                    if (TextUtils.isEmpty(mProductData.getAreaCheckTime())) {
////                        showAskDialog();
////                    } else {
////                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
////                        builder.setMessage("该货区已对货，请不要重复对货");
////                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////                                dialog.dismiss();
////                            }
////                        });
////                        builder.create().show();
////                    }
//                } else {
//                    ToastUtils.show(mActivity, "该货区未拣货完成，无法对货");
//                }
                break;
            }
        }
    }

    /**
     * 检测拣货数量是否跟对货的实发数一致，如果一致则返回1，不一致为0
     */
    private int isCheckRight(ProductEntity item) {
        return item.getPickedQty() == item.getSaleQty() ? 1 : 0;
    }

    /**
     * 确定对货
     */
    private void reqCheckPickUpdate() {
        mActivity.showProgressDialog();
        UserInfo userInfo = null;
        if (FrxsApplication.getInstance().getUserInfo() == null) {
            return;
        } else {
            userInfo = FrxsApplication.getInstance().getUserInfo();
        }
        SubmitCheckInfo checkInfo = new SubmitCheckInfo();
        checkInfo.setWID(userInfo.getWID());
        checkInfo.setOrderId(strOrderID);
        checkInfo.setShelfAreaId("1");
        checkInfo.setUserID(userInfo.getEmpID());
        checkInfo.setUserName(userInfo.getEmpName());
        checkInfo.setGoodsInfo(goodsInfoList);
        getService().PostCheckedGoods(checkInfo).enqueue(new SimpleCallback<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                mActivity.dismissProgressDialog();
                if (result != null) {
                    if (result.getFlag().equals("0")) {
                        ToastUtils.show(mActivity, "完成对货");
                        mProductData.getProdcutDetailList().get(0).setAreaCheckTime("Checked");
                        for (ProductEntity item : wProductExts) {
                            item.setIsChecked(false);
                        }
                        DBHelper.getProductEntityService().update(wProductExts);
                        productAdapter.notifyDataSetChanged();
                        btnChecked.setBackgroundResource(R.color.gray);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setMessage(result.getInfo() + "，无法完成对货。");
                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(mActivity, HomeActivity.class);
                                startActivity(intent);
                                mActivity.finish();
                            }
                        });
                        if (!mActivity.isFinishing()) {
                            builder.create().show();
                        } else {
                            Intent intent = new Intent(mActivity, HomeActivity.class);
                            startActivity(intent);
                            mActivity.finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                super.onFailure(call, t);
                mActivity.dismissProgressDialog();
            }
        });
    }

    /**
     * 获取拣货员信息
     */
    private void reqGetPickInfo(int pickUserId) {
        AjaxParams params = new AjaxParams();
        params.put("EmpID", pickUserId);
        getService().PostPickInfo(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<Warehouse>>() {
            @Override
            public void onResponse(ApiResponse<Warehouse> result, int code, String msg) {
                if (result.getFlag().equals("0")) {
                    Warehouse respData = result.getData();
                    if (null != respData) {
                        strTell = respData.getUserMobile();
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strTell));
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mActivity.startActivity(intent);
                    }
                } else {
                    ToastUtils.show(mActivity, result.getInfo());
                }
            }
        });
    }

    private void refreshProductView() {
        double count = 0.0;
        goodsInfoList.clear();

        for (int i = 0; i < wProductExts.size(); i++) {
            GoodsInfo goodsInfo = new GoodsInfo();
            count += wProductExts.get(i).getSaleQty();
            strShelfAreaId = wProductExts.get(i).getShelfAreaID();
            goodsInfo.setProductId(wProductExts.get(i).getProductId());
            goodsInfo.setNumber(wProductExts.get(i).getSaleQty());
            goodsInfo.setIsCheckRight(isCheckRight(wProductExts.get(i)));
            goodsInfo.setID(wProductExts.get(i).getID());
            goodsInfo.setShelfAreaId(wProductExts.get(i).getShelfAreaID());
            goodsInfoList.add(goodsInfo);
        }
        tvGoodsCount.setText("商品总数量:" + DisplayUtil.subZeroAndDot(MathUtils.twolittercountString(count)));
        tvGoodsLine.setText("(共计" + wProductExts.size() + "行)");//商品行数
    }

    // resort the product list
    public void resortGoodsView(boolean isAsc) {
        tabSort = isAsc;
        filledViewWithData();
    }

    /**
     * 添加脚布局
     */
    private void addFooterView() {
        TextView footerView = new TextView(getActivity());
        footerView.setText("亲，已经到底了~");
        footerView.setPadding(10, 10, 10, 10);
        footerView.setGravity(Gravity.CENTER);
        lvBigGoods.addFooterView(footerView);

    }

    /**
     * 修改装箱数量并保存
     *
     * @param modifyCount
     * @param dialog
     * @param item
     */
    private void setPackQty(double modifyCount, Dialog dialog, ProductEntity item) {
        dialog.dismiss();
        item.setSaleQty(modifyCount);
        DBHelper.getProductEntityService().update(item);
        refreshProductView();
        productAdapter.notifyDataSetChanged();
    }
}

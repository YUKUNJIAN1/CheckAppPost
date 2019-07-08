package com.frxs.check;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ewu.core.utils.SystemUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.MaterialDialog;
import com.ewu.core.widget.NoScrollListView;
import com.frxs.check.adapter.GiftsListAdapter;
import com.frxs.check.application.FrxsApplication;
import com.frxs.check.greendao.DBHelper;
import com.frxs.check.greendao.entity.ProductEntity;
import com.frxs.check.greendao.gen.ProductEntityDao;
import com.frxs.check.model.GiftsCheck;
import com.frxs.check.model.GiftsCheckItem;
import com.frxs.check.model.OrderInfo;
import com.frxs.check.model.SectionListItem;
import com.frxs.check.model.UserInfo;
import com.frxs.check.rest.model.AjaxParams;
import com.frxs.check.rest.model.ApiResponse;
import com.frxs.check.rest.service.SimpleCallback;
import com.frxs.check.utils.DateUtil;
import com.frxs.check.widget.CountEditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

/**
 * 装箱的页面
 * Created by Endoon on 2016/4/7.
 */
public class PackCaseActivity extends FrxsActivity {
    private CountEditText cetWeekCase;// 周转箱

    private CountEditText cetCaseCount;// 纸箱数

    private CountEditText cetFragile;// 易碎件

    private TextView tvFinishPackCase;// 完成装箱

    private TextView tvTitle;// 标题

    //private LinearLayout llOrderDetails;// 订单详情

    private OrderInfo mOrderInfo;

    private TextView tvOrderId;

    private TextView tvOrderTime;

    private TextView tvShopName;

    private TextView tvStationNum;

    private EditText etRemark;

    // 装载SectionListItem的集合
    private List<SectionListItem> mGiftsItems = new ArrayList<SectionListItem>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pack_case;
    }

    @Override
    protected void initViews() {
        cetWeekCase = (CountEditText) findViewById(R.id.cet_week_case);
        cetCaseCount = (CountEditText) findViewById(R.id.cet_case_count);
        cetFragile = (CountEditText) findViewById(R.id.cet_fragile);
        tvFinishPackCase = (TextView) findViewById(R.id.tv_finish_packcase);
        //llOrderDetails = (LinearLayout) findViewById(R.id.ll_order_details);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvOrderId = (TextView) findViewById(R.id.tv_order_id);
        tvOrderTime = (TextView) findViewById(R.id.tv_shop_time);
        tvShopName = (TextView) findViewById(R.id.tv_shop_name);
        tvStationNum = (TextView) findViewById(R.id.tv_station_num);
        etRemark = (EditText) findViewById(R.id.et_remark);
    }

    @Override
    protected void initData() {
        tvTitle.setText("快速装箱");
        Intent intent = getIntent();
        if (intent != null) {
            mOrderInfo = (OrderInfo) intent.getSerializableExtra("ORDER");
        }

        if (mOrderInfo != null) {
            tvOrderId.setText("单号：" + mOrderInfo.getOrderId());
            String orderTime = "";
            if (!TextUtils.isEmpty(mOrderInfo.getOrderDate())) {
                Date dateFormat = DateUtil.string2Date(mOrderInfo.getOrderDate(), "yyyy-MM-dd");
                orderTime = DateUtil.format(dateFormat, "yyyy-MM-dd");
            }
            tvOrderTime.setText("下单日期：" + orderTime);
            tvShopName.setText("门店：" + mOrderInfo.getShopName());
            tvStationNum.setText(mOrderInfo.getStationNumber() + "");
        }
    }

    @Override
    protected void initEvent() {
        cetWeekCase.setMaxCount(99999999);
        cetWeekCase.setOnCountChangeListener(new CountEditText.onCountChangeListener() {
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

        cetCaseCount.setMaxCount(99999999);
        cetCaseCount.setOnCountChangeListener(new CountEditText.onCountChangeListener() {
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

        cetFragile.setMaxCount(99999999);
        cetFragile.setOnCountChangeListener(new CountEditText.onCountChangeListener() {
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

        tvFinishPackCase.setOnClickListener(this);

        //llOrderDetails.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_finish_packcase: {
                if (mOrderInfo != null) {
                    reqBuyGiftsCheck(mOrderInfo.getOrderId());
                }
                break;
            }

            // 隐藏跳转订单详情
            /*case R.id.ll_order_details: {
                if (mOrderInfo != null) {
                    Intent intent = new Intent(PackCaseActivity.this, OrderDetailsActivity.class);
                    intent.putExtra("ORDERID", mOrderInfo.getOrderId());
                    startActivity(intent);
                }
                break;
            }*/
        }
    }

    private void finishPack() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_ask);
        dialog.setCancelable(false);
        TextView btnConfirm = (TextView) dialog.findViewById(R.id.btn_confirm);// 确定
        TextView btnCancel = (TextView) dialog.findViewById(R.id.btn_cancel);// 取消
        TextView tvAskText = (TextView) dialog.findViewById(R.id.tv_sak_text);
        tvAskText.setText("是否完成装箱？");
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (SystemUtils.isNetworkAvailable(PackCaseActivity.this)) {
                    reqFinishPackCase();
                } else {
                    ToastUtils.show(PackCaseActivity.this, "网络异常，请检查网络是否连接");
                }
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

    private void reqFinishPackCase() {
        progressDialog.show();
        UserInfo info = FrxsApplication.getInstance().getUserInfo();
        int weekCaseNum = (int) cetWeekCase.getCount();
        int caseCount = (int) cetCaseCount.getCount();
        int fragileNum = (int) cetFragile.getCount();
        AjaxParams params = new AjaxParams();
        params.put("WID", info.getWID());
        params.put("ShopID", mOrderInfo.getShopID());
        params.put("OrderId", mOrderInfo.getOrderId());
        params.put("Package1Qty", String.valueOf(weekCaseNum));
        params.put("Package2Qty", String.valueOf(caseCount));
        params.put("Package3Qty", String.valueOf(fragileNum));
        params.put("UserId", info.getEmpID());
        params.put("UserName", info.getEmpName());
        params.put("Remark", etRemark.getText().toString().trim());
        getService().PostPackCaseFinish(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                if (result.getFlag().equals("0")) {// 装箱完成
                    List<ProductEntity> productEntityList = DBHelper.getProductEntityService().queryBuilder().where(ProductEntityDao.Properties.OrderId.eq(mOrderInfo.getOrderId())).list();
                    DBHelper.getProductEntityService().delete(productEntityList);
                    ToastUtils.show(PackCaseActivity.this, "装箱完成");
                    finish();
                } else {// 装箱失败
                    AlertDialog.Builder builder = new AlertDialog.Builder(PackCaseActivity.this);
                    builder.setMessage(result.getInfo() + "，无法完成装箱。");
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(PackCaseActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    if (!isFinishing()) {
                        builder.create().show();
                    } else {
                        Intent intent = new Intent(PackCaseActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                super.onFailure(call, t);
                progressDialog.dismiss();
            }
        });
    }

    private void showGiftsDialog() {
        final MaterialDialog giftsDialog = new MaterialDialog(this);
        View giftsView = LayoutInflater.from(this).inflate(R.layout.dialog_no_conditions_gifts, null);
        giftsDialog.setContentView(giftsView);
        TextView titleTv = (TextView) giftsView.findViewById(R.id.title_tv);
        NoScrollListView lvGiftsList = (NoScrollListView) giftsView.findViewById(R.id.lv_gifts_list);
        GiftsListAdapter giftsListAdapter = new GiftsListAdapter(this, mGiftsItems);
        lvGiftsList.setAdapter(giftsListAdapter);
        titleTv.setText(Html.fromHtml("<font color='red'>促销商品应发数量不满足活动规则：</font>"));
//        titleTv.setText(Html.fromHtml("<font color='red'>促销商品应发数量不满足活动规则,请按应发数量将多出的促销商品取出</font>"));

        giftsDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PackCaseActivity.this, CheckGoodsListActivity.class);
                intent.putExtra("ORDERID", mOrderInfo.getOrderId());
                intent.putExtra("IS_BACK_PACK", false);
                intent.putExtra("STATIONNUMBER", mOrderInfo.getStationNumber());
                startActivity(intent);
                giftsDialog.dismiss();
            }
        });

        giftsDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giftsDialog.dismiss();
            }
        });
        giftsDialog.show();
    }

    /**
     * 处理货区和商品的显示
     *
     * @param giftsChecks
     */
    private void processGiftsData(List<GiftsCheck> giftsChecks) {
        mGiftsItems.clear();
        for (int i = 0; i < giftsChecks.size(); i++) {
            GiftsCheck currentGifts = giftsChecks.get(i);
            String curShelfArea = currentGifts.getShelfAreaName();
            String lastShelfArea = (i - 1) > 0 ? (giftsChecks.get(i - 1).getShelfAreaName()) : "";

            if (!curShelfArea.equals(lastShelfArea)) {
                SectionListItem title = new SectionListItem(null, SectionListItem.TITLE, curShelfArea);
                mGiftsItems.add(title);
            }

            List<GiftsCheckItem> giftsItems = currentGifts.getItems();
            for (GiftsCheckItem giftsCheckItem : giftsItems) {
                SectionListItem item = new SectionListItem(giftsCheckItem, SectionListItem.ITEM, null);
                mGiftsItems.add(item);
            }
        }
    }

    /**
     * 检查是否符合促销活动规则
     */
    public void reqBuyGiftsCheck(final String strOrderID) {
        progressDialog.show();
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        AjaxParams params = new AjaxParams();
        params.put("OrderId", strOrderID);
        params.put("WarehouseId", String.valueOf(userInfo.getWID()));
        params.put("UserId", userInfo.getEmpID());
        params.put("UserName", userInfo.getEmpName());
        getService().PostWPromotionBuyGiftsCheck(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<List<GiftsCheck>>>() {
            @Override
            public void onResponse(ApiResponse<List<GiftsCheck>> result, int code, String msg) {
                progressDialog.dismiss();
                if (result != null) {
                    if (result.getFlag().equals("1")) {
                        List<GiftsCheck> giftsCheckList = result.getData();
                        if (giftsCheckList != null && giftsCheckList.size() > 0) {
                            processGiftsData(giftsCheckList);
                            showGiftsDialog();
                        }
                    } else {
                        if (!PackCaseActivity.this.isFinishing()) {
                            finishPack();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<GiftsCheck>>> call, Throwable t) {
                super.onFailure(call, t);
                progressDialog.dismiss();
            }
        });
    }
}

package com.frxs.check;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ewu.core.utils.SystemUtils;
import com.ewu.core.utils.ToastUtils;
import com.frxs.check.application.FrxsApplication;
import com.frxs.check.model.Order;
import com.frxs.check.model.OrderInfo;
import com.frxs.check.rest.model.AjaxParams;
import com.frxs.check.rest.model.ApiResponse;
import com.frxs.check.rest.service.SimpleCallback;
import com.frxs.check.widget.ClearEditText;
import com.frxs.check.widget.EmptyView;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class HomeActivity extends FrxsActivity {
    /**
     * 控件实例
     */
    private TextView tvTitle;

    private TextView tvTitleLeft;

    private TextView tvTitleRight;

    private TextView tvOrderNum;

    private ListView lvEncasementList;

    private LinearLayout llOrderDetails;

    private EmptyView emptyView;

    private  TextView footerView;
    /**
     * 数据属性
     */
    private QuickAdapter<OrderInfo> mProductAdapter;
    private ClearEditText searchOrderEt;
    private List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
    private Spinner searchTypeSp;
    private String[] searchTypeArray;
    private String searchContent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitleLeft = (TextView) findViewById(R.id.tv_title_left);
        tvTitleRight = (TextView) findViewById(R.id.tv_title_right);
        tvOrderNum = (TextView) findViewById(R.id.tv_order_number);
        lvEncasementList = (ListView) findViewById(R.id.lv_encasement);
        llOrderDetails = (LinearLayout) findViewById(R.id.ll_order_details);
        emptyView = (EmptyView) findViewById(R.id.emptyview);
        searchOrderEt = (ClearEditText) findViewById(R.id.et_search_order);
        searchTypeSp = (Spinner) findViewById(R.id.search_type_sp);

        footerView = new TextView(this);
        footerView.setText("亲，已经到底了");
        footerView.setPadding(10,10,10,10);
        footerView.setTextSize(18);
        footerView.setGravity(Gravity.CENTER);
    }


    @Override
    protected void initData() {
        tvTitle.setText("等待装箱");
        emptyView.setVisibility(View.GONE);
//        FrxsApplication.getInstance().prepare4Update(this, false);
        FrxsApplication.getInstance().setHomeActivity(this);

        mProductAdapter = new QuickAdapter<OrderInfo>(HomeActivity.this, R.layout.item_wait_packcase) {
            @Override
            protected void convert(BaseAdapterHelper helper, final OrderInfo item) {
                helper.setText(R.id.tv_order_id, "单号：" + item.getOrderId());
                helper.setText(R.id.tv_shop_name, "门店：" + item.getShopName());
                helper.setText(R.id.tv_shop_man, "店主：" + item.getRevLinkMan());
                helper.setText(R.id.tv_pick_up, ((TextUtils.isEmpty(item.getDoneCheckShelfAreaName())) ? "" : "已对："+ item.getDoneCheckShelfAreaName() + "              ")
                        + ((TextUtils.isEmpty(item.getNoCheckShelfAreaName())) ? "" : "未对：" + item.getNoCheckShelfAreaName()));
                TextView tvPhone = helper.getView(R.id.tv_shop_phone);
                tvPhone.setText(item.getRevTelephone());
                tvPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线

                String time = item.getPickingEndDate();
                if (!TextUtils.isEmpty(time)) {
//                    Date date = DateUtil.string2Date(time,"HH:mm");
//                    String pickedTime = DateUtil.format(date,"HH:mm");
                    helper.setText(R.id.tv_pick_time, "拣货：" + time);
                }

                helper.setText(R.id.tv_station_num, item.getStationNumber());

                // 状态为4表示已经拣货完成，PickingEndDate拣货完成时间
                if (item.getStatus() == 4) {
                    helper.setVisible(R.id.tv_start_packcase, true);
                } else {
                    helper.setVisible(R.id.tv_start_packcase, false);
                }

                // 未对货货区为空时 对货按纽显示对货完成并设背景为灰色
                if (TextUtils.isEmpty(item.getNoCheckShelfAreaName())){
                    helper.setBackgroundRes(R.id.tv_check_goods, R.drawable.shape_gray_rounded_rectangle);
                    helper.setText(R.id.tv_check_goods, "对货完成");
                }else{
                    helper.setBackgroundRes(R.id.tv_check_goods, R.drawable.shape_green_rounded_rectangle);
                    helper.setText(R.id.tv_check_goods, "对货");
                }

                helper.setOnClickListener(R.id.tv_start_packcase, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent intent = new Intent(HomeActivity.this, PackCaseActivity.class);
                        intent.putExtra("ORDER", item);
                        // 判断当前是否有已对货区
                        if (TextUtils.isEmpty(item.getDoneCheckShelfAreaName())){
                            // 已对货为空 弹窗提示“该订单还未对货，是否开始装箱？”
                            final Dialog dialog = new Dialog(HomeActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                            dialog.setContentView(R.layout.dialog_ask);
                            dialog.setCancelable(false);
                            TextView btnCancel = (TextView) dialog.findViewById(R.id.btn_cancel);// 取消
                            TextView btnConfirm = (TextView) dialog.findViewById(R.id.btn_confirm);// 确定
                            TextView tvAskText = (TextView) dialog.findViewById(R.id.tv_sak_text);// 提示内容
                            tvAskText.setText("该订单还未对货，是否开始装箱？");
                            // 确定跳转到装箱页面
                            btnConfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    startActivity(intent);
                                }
                            });
                            // 取消留在当前页面
                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }else{
                            // 有对货跳转到装箱页面
                            startActivity(intent);
                        }
                    }
                });

                helper.setOnClickListener(R.id.tv_check_goods, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, CheckGoodsListActivity.class);
                        intent.putExtra("ORDERID", item.getOrderId());
                        intent.putExtra("STATIONNUMBER", item.getStationNumber());
                        startActivity(intent);
                    }
                });
            }
        };
        lvEncasementList.setAdapter(mProductAdapter);
        lvEncasementList.addFooterView(footerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SystemUtils.isNetworkAvailable(HomeActivity.this)) {
            reqGetWaitPackList();
        } else {
            ToastUtils.show(HomeActivity.this, "网络异常，请检查网络是否连接");
        }
    }

    @Override
    protected void initEvent() {

        tvTitleLeft.setOnClickListener(this);

        tvTitleRight.setOnClickListener(this);

        // 隐藏点击条目查看订单详情
        /*lvEncasementList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeActivity.this, OrderDetailsActivity.class);
                intent.putExtra("ORDERID", mProductAdapter.getItem(position).getOrderId());
                startActivity(intent);
            }
        });*/

        searchTypeArray = getResources().getStringArray(R.array.search_type);
        searchTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getAdapter().getItem(position);
                if (!TextUtils.isEmpty(item) && null != searchTypeArray && position < searchTypeArray.length) {
                    if (item.equals(searchTypeArray[0])) {
                        searchOrderEt.setHint(R.string.search_station_num);
                    } else {
                        searchOrderEt.setHint(R.string.search_shop_code);
                    }

                    renderListView();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchOrderEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchContent = s.toString();
                renderListView();
            }
        });
    }

    private void renderListView() {
        if (!TextUtils.isEmpty(searchContent)) {
            List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
            int selectedPosition = searchTypeSp.getSelectedItemPosition();
            if (0 == selectedPosition) {
                for (int i = 0; i < this.orderInfoList.size(); i++) {
                    String stationNumber = this.orderInfoList.get(i).getStationNumber();
                    if (!TextUtils.isEmpty(stationNumber) && stationNumber.contains(searchContent)) {
                        orderInfoList.add(this.orderInfoList.get(i));
                    }
                }
            } else {
                for (int i = 0; i < this.orderInfoList.size(); i++) {
                    String shopCode = this.orderInfoList.get(i).getShopCode();
                    if (!TextUtils.isEmpty(shopCode) && shopCode.contains(searchContent)) {
                        orderInfoList.add(this.orderInfoList.get(i));
                    }
                }
            }
            mProductAdapter.replaceAll(orderInfoList);
            if (orderInfoList.size() > 0) {
                lvEncasementList.setSelection(0);
                footerView.setText("亲，已经到底了");
            } else {
                footerView.setText("亲，没有搜索到任何订单");
            }
        } else {
            if (null != orderInfoList) {
                mProductAdapter.replaceAll(orderInfoList);
            }

            if (orderInfoList.size() > 0) {
                footerView.setText("亲，已经到底了");
            } else {
                footerView.setText("亲，暂无订单");
            }
        }
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_title_left: {
                Intent intent = new Intent(this, MineActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.tv_title_right: {
                if (SystemUtils.isNetworkAvailable(HomeActivity.this)) {
                    reqGetWaitPackList();
                } else {
                    ToastUtils.show(HomeActivity.this, "网络异常，请检查网络是否连接");
                }
                break;
            }
        }
    }

    private void reqGetWaitPackList() {
        progressDialog.show();

        AjaxParams params = new AjaxParams();
        params.put("WID", FrxsApplication.getInstance().getUserInfo().getWID());
        getService().PostPackingList(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<Order>>() {
            @Override
            public void onResponse(ApiResponse<Order> result, int code, String msg) {
                if (result != null) {
                    if (result.getFlag().equals("0")) {
                        emptyView.setVisibility(View.GONE);
                        orderInfoList.clear();
                        Order mOrder = result.getData();
                        List<OrderInfo> orderInfos = mOrder.getOrderData();
                        if (orderInfos != null) {
                            orderInfoList.addAll(orderInfos);
                            tvOrderNum.setText(String.valueOf(orderInfoList.size()));
                            renderListView();
                        }
                    } else {
                        ToastUtils.show(HomeActivity.this, result.getInfo());
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setImageResource(R.mipmap.icon_visit_fail);
                        emptyView.setText("没有数据，请点击页面重试");
                        emptyView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (SystemUtils.isNetworkAvailable(HomeActivity.this)) {
                                    reqGetWaitPackList();
                                } else {
                                    ToastUtils.show(HomeActivity.this, "网络异常，请检查网络是否连接");
                                }
                            }
                        });
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponse<Order>> call, Throwable t) {
                super.onFailure(call, t);
                progressDialog.dismiss();
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setImageResource(R.mipmap.icon_visit_fail);
                emptyView.setText("没有数据，请点击页面重试");
                emptyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemUtils.isNetworkAvailable(HomeActivity.this)) {
                            reqGetWaitPackList();
                        } else {
                            ToastUtils.show(HomeActivity.this, "网络异常，请检查网络是否连接");
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            loginOut();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void loginOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否退出装箱员？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { //设置取消按钮
                dialog.dismiss();
                finish();
                FrxsApplication.getInstance().exitApp(0);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


}

package com.frxs.check.rest.service;


import com.frxs.check.model.AppVersionGetRespData;
import com.frxs.check.model.CheckGoods;
import com.frxs.check.model.GetDeliverProductInfo;
import com.frxs.check.model.GiftsCheck;
import com.frxs.check.model.Order;
import com.frxs.check.model.SubmitCheckInfo;
import com.frxs.check.model.UserInfo;
import com.frxs.check.model.Warehouse;
import com.frxs.check.rest.model.ApiResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService<T> {
    /**************************************** GET请求开始 *****************************************/

    @GET("Api?ActionName=PickingLogin&format=JSON")
    Call<ApiResponse<UserInfo>> GetLogin(@Query("Data") String jsonData);

    @GET("Api?ActionName=PackingSaleSettle.Save&format=JSON")
    Call<ApiResponse<String>> GetPackCaseFinish(@Query("Data") String jsonData);

    @GET("Api?ActionName=GetPackingList&format=JSON")
    Call<ApiResponse<Order>> GetPackingList(@Query("Data") String jsonData);

    /**
     * 修改密码
     *
     * @param jsonData
     * @return
     */
    @GET("Api?ActionName=PickingUpdatePwd&format=JSON")
    Call<ApiResponse<String>> GetUpdatePwd(@Query("Data") String jsonData);

    /**
     * 商品清单
     *
     * @param jsonData
     * @return
     */
    @GET("Api?ActionName=GetDeliverProductInfo&format=JSON")
    Call<ApiResponse<GetDeliverProductInfo>> GetDeliverProductInfo(@Query("Data") String jsonData);

    @GET("Api?ActionName=AppVersion.UpdateGet&format=JSON")
    Call<ApiResponse<AppVersionGetRespData>> GetAppVersion(@Query("Data") String jsonData);

    @GET("Api?ActionName=GetModelByOrderId&format=JSON")
    Call<ApiResponse<List<CheckGoods>>> GetCheckGoodsList(@Query("Data") String jsonData);

    @GET("Api?ActionName=CheckPickUpdate&format=JSON")
    Call<ApiResponse<String>> GetCheckPickUpdate(@Query("Data") String jsonData);

    /**
     * 完成对货
     */
    @FormUrlEncoded
    @POST("Api?ActionName=CheckedGoods&format=JSON")
    Call<ApiResponse<String>> GetCheckedGoods(@Field("Data") String jsonData);

    /**
     * 获取拣货员信息
     */
    @GET("Api?ActionName=WarehouseEmp.Get&format=JSON")
    Call<ApiResponse<Warehouse>> GetPickInfo(@Query("Data") String jsonData);

    /******************************************* GET请求结束 *************************************/

    /******************************************* POST请求开始 *************************************/
    /**
     * 登录接口
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("Picking/PickingLogin")
    Call<ApiResponse<UserInfo>> PostLogin(@FieldMap Map<String,Object> params);

    /**
     * 修改密码
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("Picking/PickingUpdatePwd")
    Call<ApiResponse<String>> PostUpdatePwd(@FieldMap Map<String,Object> params);

    /**
     * 完成装箱
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("Packing/SavePackingSaleSettle")
    Call<ApiResponse<String>> PostPackCaseFinish(@FieldMap Map<String,Object> params);

    /**
     * 获取待装箱订单列表
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("Packing/GetPackingList")
    Call<ApiResponse<Order>> PostPackingList(@FieldMap Map<String,Object> params);

    /**
     * 商品清单
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("Deliver/GetDeliverProductInfo")
    Call<ApiResponse<GetDeliverProductInfo>> PostDeliverProductInfo(@FieldMap Map<String,Object> params);

    /**
     * 完成对货
     */
    @POST("Packing/CheckedGoods")
    Call<ApiResponse<String>> PostCheckedGoods(@Body SubmitCheckInfo info);


    /**
     * 获取拣货员信息
     */
    @FormUrlEncoded
    @POST("Deliver/GetWarehouseEmp")
    Call<ApiResponse<Warehouse>> PostPickInfo(@FieldMap Map<String,Object> params);

    /**
     * 版本更新
     * @param params
     * @return
     */
    //版本更新
    @FormUrlEncoded
    @POST("AppVersion/AppVersionUpdateGet")
    Call<ApiResponse<AppVersionGetRespData>> PostAppVersion(@FieldMap Map<String, Object> params);

    /**
     * 判断促销活动规则
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("Packing/WPromotionBuyGiftsCheck")
    Call<ApiResponse<List<GiftsCheck>>> PostWPromotionBuyGiftsCheck(@FieldMap Map<String, Object> params);

    /******************************************* POST请求结束 *************************************/
}

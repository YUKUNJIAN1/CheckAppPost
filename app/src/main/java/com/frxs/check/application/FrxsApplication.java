package com.frxs.check.application;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;
import com.allenliu.versionchecklib.core.http.HttpParams;
import com.allenliu.versionchecklib.core.http.HttpRequestMethod;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;
import com.ewu.core.utils.SerializableUtil;
import com.ewu.core.utils.SharedPreferencesHelper;
import com.ewu.core.utils.SystemUtils;
import com.ewu.core.utils.ToastUtils;
import com.frxs.check.FrxsActivity;
import com.frxs.check.HomeActivity;
import com.frxs.check.R;
import com.frxs.check.comms.Config;
import com.frxs.check.comms.GlobelDefines;
import com.frxs.check.greendao.utils.DbCore;
import com.frxs.check.model.AppVersionGetRespData;
import com.frxs.check.model.UserInfo;
import com.frxs.check.rest.RestClient;
import com.frxs.check.rest.model.ApiResponse;
import com.frxs.check.utils.DateUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by ewu on 2016/2/18.
 */
public class FrxsApplication extends Application {
    private static FrxsApplication mInstance;
    private UserInfo mUserInfo;// 用户信息

    private HomeActivity homeActivity;

    private boolean needCheckUpgrade = true; // 是否需要检测更新

    private static SparseArray<RestClient> restClientSparseArray = new SparseArray<RestClient>();

    private Activity mActivity;

    private DownloadBuilder builder;

    public static FrxsApplication getInstance()
    {
        if (mInstance == null)
        {
            throw new IllegalStateException("Not yet initialized");
        }

        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (mInstance != null)
        {
            throw new IllegalStateException("Not a singleton");
        }

        mInstance = this;

        DbCore.init(this);

        initData();

        initRestClient();
    }

    private void initRestClient() {
        restClientSparseArray.put(Config.TYPE_BASE, new RestClient(Config.getBaseUrl(Config.TYPE_BASE, getEnvironment())));
        restClientSparseArray.put(Config.TYPE_UPDATE, new RestClient(Config.getBaseUrl(Config.TYPE_UPDATE, getEnvironment())));
    }

    public static RestClient getRestClient(int clientType) {
        return restClientSparseArray.get(clientType);
    }

    private void initData()
    {
        // Get the user Info
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, Config.PREFS_NAME);
        String userStr = helper.getString(Config.KEY_USER, "");
        if (!TextUtils.isEmpty(userStr))
        {
            Object object = null;
            try
            {
                object = SerializableUtil.str2Obj(userStr);
                if (null != object)
                {
                    mUserInfo = (UserInfo) object;
                }

            } catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }

    public void setUserInfo(UserInfo userInfo)
    {
        this.mUserInfo = userInfo;

        String userStr = "";
        try
        {
            userStr = SerializableUtil.obj2Str(userInfo);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, Config.PREFS_NAME);
        helper.putValue(Config.KEY_USER, userStr);
    }

    public UserInfo getUserInfo()
    {
        if (null == mUserInfo)
        {
            initData();
        }

        return mUserInfo;
    }

    public int getEnvironment() {
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, GlobelDefines.PREFS_NAME);
        return helper.getInt(GlobelDefines.KEY_ENVIRONMENT, Config.networkEnv);
    }

    public void setEnvironment(int environmentId) {
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, GlobelDefines.PREFS_NAME);
        helper.putValue(GlobelDefines.KEY_ENVIRONMENT, environmentId);

        restClientSparseArray.clear();
        restClientSparseArray.put(Config.TYPE_BASE, new RestClient(Config.getBaseUrl(Config.TYPE_BASE, environmentId)));
        restClientSparseArray.put(Config.TYPE_UPDATE, new RestClient(Config.getBaseUrl(Config.TYPE_UPDATE, environmentId)));
    }

    public String getUserAccount() {
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, GlobelDefines.PREFS_NAME);
        return helper.getString(GlobelDefines.KEY_FIRST_ENTER, "");
    }

    public void setUserAccount(String userAccount) {
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, GlobelDefines.PREFS_NAME);
        helper.putValue(GlobelDefines.KEY_FIRST_ENTER, userAccount);
    }

    /**
     * 更新版本的网路请求
     * @param activity
     * @param isShow 是否需要提示用户
     */
    public void prepare4Update(final Activity activity, final boolean isShow) {
        if (!SystemUtils.checkNet(this) || !SystemUtils.isNetworkAvailable(this)) {
            ToastUtils.show(this, "网络不可用");
            return;
        }
        String strYMD = DateUtil.Date2StringYMD(new Date());
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, Config.PREFS_NAME);
        String localYMD = helper.getString(Config.KEY_CHECK_DATE, "");
        //当且仅当打开APP或者APP运行时每日的首次请求时需要检测版本号
        if (!strYMD.equals(localYMD)) {
            helper.putValue(Config.KEY_CHECK_DATE, strYMD);
            needCheckUpgrade = true;
        }

        if (needCheckUpgrade) {
            needCheckUpgrade = false;
        } else {
            return;
        }

        mActivity = activity;
        ((FrxsActivity) mActivity).showProgressDialog();
        String url = Config.getBaseUrl(Config.TYPE_UPDATE, getEnvironment()) + "AppVersion/AppVersionUpdateGet";
        HttpParams httpParams = new HttpParams();
        httpParams.put("SysType", 0); // 0:android;1:ios
        httpParams.put("AppType", 3); // 软件类型(0:兴盛店订货平台, 1:拣货APP. 2:兴盛店配送APP,3:装箱APP, 4:采购APP, 5:网络店订货平台,6：网络店配送APP,9退货库app)
        builder = AllenVersionChecker
                .getInstance()
                .requestVersion()
                .setRequestUrl(url)
                .setRequestMethod(HttpRequestMethod.POSTJSON)
                .setRequestParams(httpParams)
                .request(new RequestVersionListener() {
                    @Nullable
                    @Override
                    public UIData onRequestVersionSuccess(String result) {
                        ((FrxsActivity) mActivity).dismissProgressDialog();
                        Type type = new TypeToken<ApiResponse<AppVersionGetRespData>>() {
                        }.getType();
                        ApiResponse<AppVersionGetRespData> respData = new Gson().fromJson(result, type);
                        int versionCode = Integer.valueOf(SystemUtils.getVersionCode(getApplicationContext()));
                        if (respData.getData() == null) {
                            ToastUtils.show(activity, "更新接口无数据");
                            return null;
                        }
                        if (versionCode >= respData.getData().getCurCode()) {
                            ToastUtils.show(activity, "已是最新版本");
                            return null;
                        }
                        if (respData.getData().getUpdateFlag() == 0) {
                            return null;
                        }
                        if (respData.getData().getUpdateFlag() == 2) {
                            builder.setForceUpdateListener(new ForceUpdateListener() {
                                @Override
                                public void onShouldForceUpdate() {
                                    forceUpdate();
                                }
                            });
                        }
                        return crateUIData(respData.getData().getDownUrl(), respData.getData().getUpdateRemark());
                    }

                    @Override
                    public void onRequestVersionFailure(String message) {
                        ((FrxsActivity) mActivity).dismissProgressDialog();
                        ToastUtils.show(activity, "request failed");

                    }
                });
        builder.setShowNotification(true);
        builder.setShowDownloadingDialog(true);
        builder.setShowDownloadFailDialog(true);
        builder.setForceRedownload(true);
        builder.excuteMission(activity);
    }

    /**
     * @return
     * @important 使用请求版本功能，可以在这里设置downloadUrl
     * 这里可以构造UI需要显示的数据
     * UIData 内部是一个Bundle
     */
    private UIData crateUIData(String downloadUrl, String updateRemark) {
        UIData uiData = UIData.create();
        uiData.setTitle(getString(R.string.update_title));
        uiData.setDownloadUrl(downloadUrl);
        uiData.setContent(updateRemark);
        return uiData;
    }

    /**
     * 强制更新操作
     */
    private void forceUpdate() {
        mActivity.finish();
    }

    public void exitApp(int code)
    {
        System.exit(code);
    }

     public boolean isNeedCheckUpgrade() {
        return needCheckUpgrade;
    }

    public void setEnv(int environmentId)
    {
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, GlobelDefines.PREFS_NAME);
        helper.putValue(GlobelDefines.KEY_ENVIRONMENT, environmentId);
    }

    public int getEnv()
    {
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(this, GlobelDefines.PREFS_NAME);
        return helper.getInt(GlobelDefines.KEY_ENVIRONMENT, Config.networkEnv);
    }

    public HomeActivity getHomeActivity() {
        return homeActivity;
    }

    public void setHomeActivity(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    /**
     * 退出登录
     * 在这里做，如：清空用户信息，禁止接收消息之类的操作
     */
    public void logout() {
        // 清空用户信息
        setUserInfo(null);
    }
}

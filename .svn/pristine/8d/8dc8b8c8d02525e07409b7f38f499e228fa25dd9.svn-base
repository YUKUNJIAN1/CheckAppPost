package com.frxs.check.fragment;

import android.Manifest;
import android.view.View;
import com.ewu.core.base.BaseFragment;
import com.ewu.core.utils.EasyPermissionsEx;
import com.ewu.core.utils.SharedPreferencesHelper;
import com.frxs.check.comms.Config;
import com.frxs.check.rest.service.ApiService;
import com.frxs.check.application.FrxsApplication;
import com.frxs.check.utils.DateUtil;
import java.util.Date;

public abstract class FrxsFragment extends BaseFragment
{
	protected ApiService mService;

	private static final int MY_PERMISSIONS_REQUEST_WES = 4;// 请求文件存储权限的标识码

	public ApiService getService()
	{
		if (mService == null)
		{
			mService = FrxsApplication.getRestClient(Config.TYPE_BASE).getApiService();
		}

		//每次请求前需要检测版本号
		checkVersion();

		return mService;
	}

	private void checkVersion() {
		//当且仅当每日的首次请求时需要检测版本号
		if (EasyPermissionsEx.hasPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
			FrxsApplication.getInstance().prepare4Update(mActivity, false);
		} else {
			// 不允许 - 弹窗提示用户是否允许放开权限
			EasyPermissionsEx.executePermissionsRequest(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WES);
		}
	}

	protected abstract int getLayoutId();
	protected abstract void initViews(View view);
	protected abstract void initEvent();
	protected abstract void initData();

}

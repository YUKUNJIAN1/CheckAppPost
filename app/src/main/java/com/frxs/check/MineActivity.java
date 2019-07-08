package com.frxs.check;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ewu.core.utils.SystemUtils;
import com.frxs.check.application.FrxsApplication;
import com.frxs.check.greendao.DBHelper;

/**
 * 个人中心页面
 * Created by Endoon on 2016/3/26.
 */
public class MineActivity extends FrxsActivity {
    /* 控件 */
    private TextView tvUpdatePsw;

    private TextView tvMyVersion;

    private TextView tvExit;

    private TextView tvUserName;

    private TextView tvTitle;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine;
    }

    @Override
    protected void initViews() {
        tvUpdatePsw = (TextView) findViewById(R.id.tv_update_psw);
        tvMyVersion = (TextView) findViewById(R.id.tv_myversion);
        tvExit = (TextView) findViewById(R.id.tv_exit);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvTitle = (TextView) findViewById(R.id.tv_title);
    }

    @Override
    protected void initData() {
        tvTitle.setText("关于我");
        String userAccount = FrxsApplication.getInstance().getUserAccount();
        if (!TextUtils.isEmpty(userAccount)) {
            tvUserName.setText(userAccount+"");
        }
        tvMyVersion.setText("v"+SystemUtils.getAppVersion(MineActivity.this));
    }

    @Override
    protected void initEvent() {
        tvUpdatePsw.setOnClickListener(this);
        tvExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.tv_update_psw:// 跳转修改密码页面
            {
                Intent intent = new Intent(MineActivity.this,UpdatePswActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.tv_exit:// 退出账号
            {
                loginOut();
                break;
            }
        }
    }

    private void loginOut()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否退出装箱员？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FrxsApplication.getInstance().logout();
                DBHelper.getProductEntityService().deleteAll();
                HomeActivity homeActivity = FrxsApplication.getInstance().getHomeActivity();
                if (null != homeActivity)
                {
                    homeActivity.finish();
                }

                Intent intent = new Intent(MineActivity.this,LoginActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }
}

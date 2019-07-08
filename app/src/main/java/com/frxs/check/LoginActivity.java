package com.frxs.check;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ewu.core.utils.DisplayUtil;
import com.ewu.core.utils.InputUtils;
import com.ewu.core.utils.SystemUtils;
import com.ewu.core.utils.ToastUtils;
import com.frxs.check.application.FrxsApplication;
import com.frxs.check.comms.Config;
import com.frxs.check.greendao.DBHelper;
import com.frxs.check.model.UserInfo;
import com.frxs.check.rest.model.AjaxParams;
import com.frxs.check.rest.model.ApiResponse;
import com.frxs.check.rest.service.SimpleCallback;
import com.frxs.check.widget.ClearEditText;
import retrofit2.Call;

/**
 * Created by ewu on 2016/3/23.
 */
public class LoginActivity extends FrxsActivity {

    private Button loginBtn;

    private ClearEditText edtAccount;

    private ClearEditText edtPassword;

    private TextView tvTitle;

    private TextView tvLeft;

    private String strUserName;// 账号

    private String strPassWord;// 密码

    private long exitTime;// 退出时间

    private View envHiddenBtn;// 选择环境的暗门

    private String[] environments;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        loginBtn = (Button) findViewById(R.id.login_commit_btn);
        edtAccount = (ClearEditText) findViewById(R.id.login_account_edit);// 用户名编辑框
        edtPassword = (ClearEditText) findViewById(R.id.login_password_edit);// 密码编辑框
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_title_left);
        envHiddenBtn = findViewById(R.id.select_environment);// 环境选择按钮
    }

    @Override
    protected void initData() {
        tvTitle.setText("装箱APP登录");
        tvLeft.setVisibility(View.GONE);
        tvLeft.setEnabled(false);
        if (!TextUtils.isEmpty(FrxsApplication.getInstance().getUserAccount())) {
            edtAccount.setText(FrxsApplication.getInstance().getUserAccount());
        }
        // 设置光标永远在文字长度之后
        edtAccount.setSelection(edtAccount.getText().length());

        initEnvironment();

//        FrxsApplication.getInstance().prepare4Update(this,false);
    }

    private void initEnvironment() {
        environments = getResources().getStringArray(R.array.run_environments);
        for (int i = 0; i < environments.length; i++) {
            environments[i] = String.format(environments[i], Config.getBaseUrl(Config.TYPE_BASE, i));
        }
    }

    @Override
    protected void initEvent() {
        loginBtn.setOnClickListener(this);

        envHiddenBtn.setOnClickListener(new View.OnClickListener() {
            int keyDownNum = 0;

            @Override
            public void onClick(View view) {
                keyDownNum++;
                if (keyDownNum == 10) {
                    ToastUtils.show(LoginActivity.this, "已进入环境选择模式");
                    AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);

                    final int spEnv = FrxsApplication.getInstance().getEnvironment();
                    String env = spEnv < environments.length ? environments[spEnv] : "";
                    dialog.setTitle(getString(R.string.str_dialog_title_head) + env + getString(R.string.str_dialog_title_foot));
                    dialog.setCancelable(false);
                    dialog.setItems(environments, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, final int which) {
                            if (spEnv == which) {
                                return;
                            }
                            if (which != 0) {
                                final AlertDialog verifyMasterDialog = new AlertDialog.Builder(LoginActivity.this).create();
                                View contentView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_evironments, null);
                                final EditText pswEt = (EditText) contentView.findViewById(R.id.password_et);
                                contentView.findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (TextUtils.isEmpty(pswEt.getText().toString().trim())) {
                                            ToastUtils.show(LoginActivity.this, "密码不能为空！");
                                            return;
                                        }

                                        if (!pswEt.getText().toString().trim().equals(getString(R.string.str_psw))) {
                                            ToastUtils.show(LoginActivity.this, "密码错误！");
                                            return;
                                        }
                                        DBHelper.getProductEntityService().deleteAll();
                                        FrxsApplication.getInstance().setEnvironment(which);
                                        verifyMasterDialog.dismiss();
                                    }
                                });

                                contentView.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        verifyMasterDialog.dismiss();
                                    }
                                });
                                verifyMasterDialog.setView(contentView);
                                verifyMasterDialog.show();
                            } else {
                                DBHelper.getProductEntityService().deleteAll();
                                FrxsApplication.getInstance().setEnvironment(which);
                            }
                        }
                    });
                    dialog.setNegativeButton(getString(R.string.str_dialog_cancle),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    dialog.show();
                    keyDownNum = 0;
                }
            }
        });
    }

    private void requestLogin() {
        showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("UserAccount",strUserName);
        params.put("UserPwd",strPassWord);
        params.put("UserType","3");// 用户类型：1.拣货员 2.配送员 3.装箱员 4.采购员
        getService().PostLogin(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<UserInfo>>() {
            @Override
            public void onResponse(ApiResponse<UserInfo> result, int code, String msg) {
                if (result.getFlag().equals("0")) {
                    ToastUtils.show(LoginActivity.this,"登录成功");
                    FrxsApplication application = FrxsApplication.getInstance();
                    application.setUserAccount(strUserName);

                    UserInfo userInfo = result.getData();
                    if (null != userInfo) {
                        userInfo.setMultiple(userInfo.getMultiple() == 0 ? 2 : userInfo.getMultiple());
                        application.setUserInfo(userInfo);

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                }else
                {
                    ToastUtils.show(LoginActivity.this,result.getInfo());
                }

                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<ApiResponse<UserInfo>> call, Throwable t) {
                dismissProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId())
        {
            case R.id.login_commit_btn: {
                if (TextUtils.isEmpty(edtAccount.getText().toString().trim()))
                {
                    ToastUtils.show(this, R.string.tips_null_account);// 账号不能为空
                    shakeView(edtAccount);
                } else if (TextUtils.isEmpty(edtPassword.getText().toString().trim()))
                {
                    ToastUtils.show(LoginActivity.this, R.string.tips_null_password);// 密码不能为空
                    shakeView(edtPassword);
                }
                else {
                    strUserName = edtAccount.getText().toString().trim();
                    strPassWord = edtPassword.getText().toString().trim();
                    if (InputUtils.isNumericOrLetter(strPassWord))
                    {
                        if (SystemUtils.isNetworkAvailable(LoginActivity.this)) {
                            requestLogin();
                        }else
                        {
                            ToastUtils.show(LoginActivity.this,"网络异常，请检查网络是否连接");
                        }
                    } else
                    {
                        ToastUtils.show(LoginActivity.this, getString(R.string.tips_input_limit));// 密码只能由数字、字母组成
                        shakeView(edtPassword);
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    /**
     * 窗口抖动
     */
    private void shakeView(EditText edit)
    {
        DisplayUtil.shakeView(this, edit);
        edit.requestFocus();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 退出程序
     */
    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            Intent intent = new Intent();
            intent.setAction(FrxsActivity.SYSTEM_EXIT);
            sendBroadcast(intent);
            FrxsApplication.getInstance().exitApp(0);
        }
    }
}

package com.xhj.learningaccumulation;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.xhj.learningaccumulation.Custom.DownloadTask;
import com.xhj.learningaccumulation.Custom.Toast;
import com.xhj.learningaccumulation.Utils.AppUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * created by xuhaijia 2017/9/21
 */

public abstract class BaseActivity extends AppCompatActivity {


    private static final int REQUEST_CODE_CALL = 100;
    private static final int REQUEST_CODE_LOCATION = 101;
    private static final int REQUEST_CODE_CONTACTS = 102;
    private static final int REQUEST_CODE_CAMERA_PHOTO = 103;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        initView();
        initData();
    }

    protected abstract void initView();


    protected abstract void initData();

    /**
     * 下载apk
     *
     * @param url
     * @param path
     */
    private void doDownloadApk(String url, final String path) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return;
        }

        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }

        // 可自定义样式
        final ProgressDialog pgdialog = new ProgressDialog(BaseActivity.this);
        pgdialog.setMessage("正在下载安装包...");
        pgdialog.setIndeterminate(true);
        pgdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pgdialog.setCancelable(false);
        pgdialog.setMax(100);

        new DownloadTask(path, new DownloadTask.DownloadCallback() {
            @Override
            public void onStart() {
                pgdialog.show();
            }

            @Override
            public void onProgress(Integer... progress) {
                pgdialog.setIndeterminate(false);
                pgdialog.setProgress(progress[0]);

            }

            @Override
            public void onFinished(String result) {

                if (result != null) {
                    Toast.makeText(BaseActivity.this, "下载异常: " + result, Toast.LENGTH_LONG).show();
                } else {
                    //自动安装
                    AppUtils.installNormal(BaseActivity.this, path);
                    pgdialog.dismiss();
                    finish();
                }
                pgdialog.dismiss();
            }

            @Override
            public void onCancelled() {
            }
        }).execute(url);
    }

    public Activity getActivity() {
        return this;
    }

    public void callTelphone(String tel) {
        if (AndPermission.hasPermission(getActivity(), Permission.PHONE)) {
            if (TextUtils.isEmpty(tel)) {
                Toast.makeText(getActivity(), "电话号码有错", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
            startActivity(intent);
        } else {
            AndPermission.with(getActivity())
                    .requestCode(REQUEST_CODE_CALL)
                    .permission(Permission.PHONE)
                    .callback(getActivity())
                    // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                    // 这样避免用户勾选不再提示，导致以后无法申请权限。
                    // 你也可以不设置。
                    .rationale(new RationaleListener() {
                        @Override
                        public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                            // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                            AndPermission.rationaleDialog(getActivity(), rationale).show();
                        }
                    })
                    .start();
        }

    }

    public boolean getLocation() {
        if (AndPermission.hasPermission(getActivity(), Permission.LOCATION)) {
            return true;
        } else {
            AndPermission.with(getActivity())
                    .requestCode(REQUEST_CODE_LOCATION)
                    .permission(Permission.LOCATION)
                    .callback(getActivity())
                    // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                    // 这样避免用户勾选不再提示，导致以后无法申请权限。
                    // 你也可以不设置。
                    .rationale(new RationaleListener() {
                        @Override
                        public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                            // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                            AndPermission.rationaleDialog(getActivity(), rationale).show();

                        }
                    })
                    .start();
            return false;
        }
    }
    public boolean getContacts() {
        if (AndPermission.hasPermission(getActivity(), Permission.CONTACTS)) {
            return true;
        } else {
            AndPermission.with(getActivity())
                    .requestCode(REQUEST_CODE_CONTACTS)
                    .permission(Permission.CONTACTS)
                    .callback(getActivity())
                    // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                    // 这样避免用户勾选不再提示，导致以后无法申请权限。
                    // 你也可以不设置。
                    .rationale(new RationaleListener() {
                        @Override
                        public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                            // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                            AndPermission.rationaleDialog(getActivity(), rationale).show();

                        }
                    })
                    .start();
            return false;
        }
    }

    public boolean getCameraAndPhoto() {

        if (AndPermission.hasPermission(getActivity(), Permission.CAMERA) && AndPermission.hasPermission(getActivity(), Permission.STORAGE)) {
            return true;
        } else {
            AndPermission.with(getActivity())
                    .requestCode(REQUEST_CODE_CAMERA_PHOTO)
                    .permission(Permission.CAMERA, Permission.STORAGE)
                    .callback(getActivity())
                    // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                    // 这样避免用户勾选不再提示，导致以后无法申请权限。
                    // 你也可以不设置。
                    .rationale(new RationaleListener() {
                        @Override
                        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
                            // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("确认")
                                    .setMessage("确定吗？")
                                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            rationale.resume();
                                        }
                                    })
                                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            rationale.cancel();
                                        }
                                    })
                                    .show();


                        }
                    })
                    .start();
            return false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CALL:
                break;
            case REQUEST_CODE_LOCATION:
                break;
            case REQUEST_CODE_CAMERA_PHOTO:
                break;
            case REQUEST_CODE_CONTACTS:
                break;
        }

    }
}

package com.xhj.learningaccumulation;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yanzhenjie.permission.Permission;

public class MainActivity extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getLocation();
                getCameraAndPhoto();
            }
        });
    }

    @Override
    protected void initData() {
        PermissionRequest permissionRequest = new PermissionRequest(this, new PermissionRequest.PermissionCallback() {
            @Override
            public void onSuccessful() {

            }

            @Override
            public void onFailure() {

            }
        });
        permissionRequest.request(Permission.STORAGE , Permission.CALENDAR);

    }
}

package com.xuhaijia.learnkotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // java 8 下不支持 onClick 等方法
//        verticalLayout {
//            val name = editText()
//            button("Say Hello") {
//                setOnClickListener(View.OnClickListener {  toast("Hello, ${name.text}!")}) }
//
//            }
        setContentView(R.layout.activity_main)
        login.setOnClickListener({
            if ((account.text.toString().equals("1")) and (password.text.toString() == "2")) {
                toast("登录成功")
            } else {
                toast("登录失败" + account.text + "  : " + password.text)
            }
        })


    }
}

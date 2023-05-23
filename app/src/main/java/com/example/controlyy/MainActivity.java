package com.example.controlyy;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Context context = MainActivity.this;
//        SharedPreferences sharedPref = context.getSharedPreferences(
//                getString(R.string.xuexi_name), Context.MODE_PRIVATE);

        Log.d("packageName", getString(R.string.com_example_controlyy_gg));

//
//        SharedPreferences sharedPref =MainActivity.this.getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(getString(R.string.com_example_controlyy_gg), "5555");
////        editor.putInt(getString(R.string.saved_high_score_key), newHighScore);
//
//        editor.commit();
//        Log.d("packageName", getString(R.string.com_example_controlyy_gg));

//        SharedPreferences sharedPreferences= getSharedPreferences("data",Context.MODE_PRIVATE);
//        //步骤2： 实例化SharedPreferences.Editor对象
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        //步骤3：将获取过来的值放入文件
//        editor.putString("name", "oooo");
//        editor.putInt("age", 28);
//        editor.putBoolean("marrid",false);
//        //步骤4：提交
//        editor.commit();

        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }
}
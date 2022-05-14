package cn.leancloud.chatkit.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cn.leancloud.chatkit.R;

public class ChatSettingActivity extends AppCompatActivity {
    protected ChatSettingFragment chatSettingFragment;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_setting_activity_layout);
    }
}

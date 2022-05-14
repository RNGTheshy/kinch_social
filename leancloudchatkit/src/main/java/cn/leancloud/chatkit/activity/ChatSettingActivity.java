package cn.leancloud.chatkit.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.R;

public class ChatSettingActivity extends AppCompatActivity {
    public static final String CONVERSATION_ID = "conversation_id";
    protected ChatSettingFragment chatSettingFragment;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_setting_activity_layout);
        chatSettingFragment = (ChatSettingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat_setting);
        String convId = getIntent().getStringExtra(CONVERSATION_ID);
        chatSettingFragment.setImConversation(LCChatKit.getInstance().getClient().getConversation(convId));
    }
}

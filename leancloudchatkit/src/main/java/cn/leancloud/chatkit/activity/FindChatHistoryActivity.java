package cn.leancloud.chatkit.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.R;

public class FindChatHistoryActivity extends AppCompatActivity {
    public static final String CONVERSATION_ID = "conversation_id";
    protected FindChatHistoryFragment findChatHistoryFragment;
    protected void onCreate(Bundle savedInstanceState) {
        //填充布局
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_chat_history_activity_layout);
        findChatHistoryFragment = (FindChatHistoryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_find_chat_history);
        //获取会话id并设置对话
        String convId = getIntent().getStringExtra(CONVERSATION_ID);
        findChatHistoryFragment.setImConversation(LCChatKit.getInstance().getClient().getConversation(convId));
    }
}

package cn.leancloud.chatkit.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import cn.leancloud.chatkit.R;

public class ConversationListActivity extends AppCompatActivity {
    public static void goToConList(Context context){
        context.startActivity(new Intent(context,ConversationListActivity.class));
    }
    protected void onCreate(Bundle savedInstanceState) {
        //填充布局
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_list_activity_layout);
//        chatSettingFragment = (ChatSettingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat_setting);
        //获取会话id并设置对话

    }
}

package cn.leancloud.chatkit.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import cn.leancloud.chatkit.R;

public class ConversationListActivity extends AppCompatActivity {
    //跳转到会话列表接口
    public static void goToConList(Context context){
        context.startActivity(new Intent(context,ConversationListActivity.class));
    }

    /**
     * 初始化界面
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        //填充布局
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_list_activity_layout);
        //获取组件
        ImageView close = (ImageView)findViewById(R.id.close_con_list);
        //设置点击事件
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

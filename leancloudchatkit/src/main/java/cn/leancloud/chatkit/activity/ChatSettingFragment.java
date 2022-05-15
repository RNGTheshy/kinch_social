package cn.leancloud.chatkit.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.R;
import cn.leancloud.im.DatabaseDelegate;
import cn.leancloud.im.DatabaseDelegateFactory;
import cn.leancloud.im.InternalConfiguration;
import cn.leancloud.im.v2.LCIMConversation;
import cn.leancloud.im.v2.LCIMMessageStorage;

public class ChatSettingFragment extends Fragment {

    protected ImageView closeSetting;
    protected TextView findChatHistory;
    protected TextView clearChatHistory;
    protected LCIMConversation imConversation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //初始化界面
        View view = inflater.inflate(R.layout.chat_setting_fragment_layout, container, false);
        closeSetting = (ImageView) view.findViewById(R.id.close_setting);
        findChatHistory = (TextView) view.findViewById(R.id.find_chat_history);
        clearChatHistory = (TextView) view.findViewById(R.id.clear_chat_history);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        //点击时间
        //返回时间
        closeSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        //查询聊天记录
        findChatHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO 查找聊天记录
               //跳转聊天设置页面
                Intent intent = new Intent(getContext(),FindChatHistoryActivity.class);
                intent.putExtra(FindChatHistoryActivity.CONVERSATION_ID,imConversation.getConversationId());
                startActivity(intent);
            }
        });
        //清空聊天记录
        clearChatHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO 清空聊天记录
                LCIMMessageStorage lcimMessageStorage =LCChatKit.getInstance().getCurrentMessageStorage();
                //删除确定窗口
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setTitle("清空聊天记录").setMessage("确定要清空聊天记录吗?")
                        .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("确实",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                lcimMessageStorage.deleteConversationData(imConversation.getConversationId());
                                dialog.dismiss();
                                Toast.makeText(getContext(),"该聊天记录本地缓存已经清理完成",Toast.LENGTH_SHORT).show();
                            }
                        }).create();
                alertDialog.show();

            }
        });
    }
    public void setImConversation(LCIMConversation imConversation){
        this.imConversation = imConversation;
    }
}

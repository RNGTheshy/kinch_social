package cn.leancloud.chatkit.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.leancloud.chatkit.R;

public class ChatSettingFragment extends Fragment {

    protected ImageView closeSetting;
    protected TextView findChatHistory;
    protected TextView clearChatHistory;
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
        //查找聊天记录
        findChatHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO 查找聊天记录
            }
        });
        //清空聊天记录
        clearChatHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO 清空聊天记录
            }
        });
    }
}

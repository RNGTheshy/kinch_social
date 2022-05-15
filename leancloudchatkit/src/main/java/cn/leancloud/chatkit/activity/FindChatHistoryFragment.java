package cn.leancloud.chatkit.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.R;
import cn.leancloud.im.v2.LCIMConversation;
import cn.leancloud.im.v2.LCIMMessageStorage;

public class FindChatHistoryFragment extends Fragment {
    protected LCIMConversation imConversation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //初始化界面
        View view = inflater.inflate(R.layout.find_chat_history_fragment_layout, container, false);

        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){


    }
    public void setImConversation(LCIMConversation imConversation){
        this.imConversation = imConversation;
    }
}

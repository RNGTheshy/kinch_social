package cn.leancloud.chatkit.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.adapter.ChatHistoryAdapter;
import cn.leancloud.im.v2.LCIMConversation;
import cn.leancloud.im.v2.LCIMException;
import cn.leancloud.im.v2.LCIMMessage;
import cn.leancloud.im.v2.LCIMMessageStorage;
import cn.leancloud.im.v2.LCIMReservedMessageType;
import cn.leancloud.im.v2.LCIMTypedMessage;
import cn.leancloud.im.v2.callback.LCIMMessagesQueryCallback;
import cn.leancloud.im.v2.messages.LCIMTextMessage;

public class FindChatHistoryFragment extends Fragment {
    protected LCIMConversation imConversation;
    protected RecyclerView recyclerView;
    protected ChatHistoryAdapter adapter;
    protected EditText findHistory;
    protected TextView findNoting;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //初始化界面
        View view = inflater.inflate(R.layout.find_chat_history_fragment_layout, container, false);
        findHistory = (EditText)view.findViewById(R.id.find_history);
        findNoting = (TextView)view.findViewById(R.id.find_noting);
        recyclerView = (RecyclerView) view.findViewById(R.id.chat_history_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 25);
        adapter = new ChatHistoryAdapter();
        recyclerView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        //查找记录
        findHistory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                long count = 100;
                String content = s.toString();
                LCIMMessagesQueryCallback callback = new LCIMMessagesQueryCallback() {
                    @Override
                    public void done(List<LCIMMessage> messages, LCIMException e) {
                        if (null == e) {
                            if (messages != null && messages.size() != 0) {
                                //有缓存
                                //将含有相关内容的信息放入链表
                                List<LCIMMessage> messageList = new ArrayList<>();
                                if (!TextUtils.isEmpty(content)) {
                                    for (LCIMMessage message : messages) {
                                        if (message instanceof LCIMTextMessage){
                                            LCIMTextMessage textMessage = (LCIMTextMessage) message;
                                            if (textMessage.getText().contains(content)) {
                                                messageList.add(message);
                                            }
                                        }
                                    }
                                }
                                //查询结果为0
                                if (messageList.size() == 0) {
                                    recyclerView.setVisibility(View.GONE);
                                    findNoting.setVisibility(View.VISIBLE);
                                } else {
                                    //查询结果刷新
                                    adapter.setMessageList(messageList);
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setVisibility(View.VISIBLE);
                                    findNoting.setVisibility(View.GONE);
                                }
                            } else {
                                //没有缓存数据 判定为查询不到
                                recyclerView.setVisibility(View.GONE);
                                findNoting.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                };
                //文本有内容  查询缓存
                if (!TextUtils.isEmpty(content)){
                    imConversation.queryMessagesFromCache(100,callback);
                }else {
                    //文本为空  没必要查询
                    //初始化状态
                    adapter.setMessageList(null);
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    findNoting.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 设置对话对象
     * @param imConversation
     */
    public void setImConversation(LCIMConversation imConversation){
        this.imConversation = imConversation;
    }
}

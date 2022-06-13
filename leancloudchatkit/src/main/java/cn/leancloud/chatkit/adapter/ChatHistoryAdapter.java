package cn.leancloud.chatkit.adapter;

import static cn.leancloud.LeanCloud.getContext;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chaoshan.data_center.togetname.Headport;
import com.chaoshan.data_center.togetname.center_getname;
import com.chaoshan.data_center.togetname.getPersonal_data;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.leancloud.LCException;
import cn.leancloud.callback.LCCallback;
import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.cache.LCIMProfileCache;
import cn.leancloud.chatkit.utils.LCIMLogUtils;
import cn.leancloud.chatkit.viewholder.ChatHistoryHolder;
import cn.leancloud.im.v2.LCIMMessage;
import cn.leancloud.im.v2.messages.LCIMTextMessage;

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryHolder>{
    protected List<LCIMMessage> messageList = new ArrayList<LCIMMessage>();

    public ChatHistoryAdapter() {
        super();
    }

    public void setMessageList(List<LCIMMessage> messages) {
        messageList.clear();
        if (null != messages) {
            for (LCIMMessage msg : messages) {
                messageList.add(msg);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    @NonNull
    @Override
    public ChatHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_history_item_layout,parent,false);
        return new ChatHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHistoryHolder holder, int position) {
        LCIMMessage message = messageList.get(position);
        //设置用户名 内容
        if (message instanceof LCIMTextMessage) {
            LCIMTextMessage textMessage = (LCIMTextMessage) message;
            holder.content.setText(textMessage.getText());
        }
        holder.time.setText(millisecsToDateString(message.getTimestamp()));

//        getPersonal_data.center_getname(message.getFrom(), new center_getname() {
//            @Override
//            public void getname(String name) {
//                holder.name.setText(name);
//            }
//        });
//        //TODO 头像更新
//        Headport headPort = new Headport();
//        headPort.setImage(message.getFrom(),holder.avatar);

        LCIMProfileCache.getInstance().getCachedUser(message.getFrom(), new LCCallback<LCChatKitUser>() {
            @Override
            protected void internalDone0(LCChatKitUser userProfile, LCException e) {
                if (null != e) {
                    LCIMLogUtils.logException(e);
                } else if (null != userProfile) {
                    holder.name.setText(userProfile.getName());
                    final String avatarUrl = userProfile.getAvatarUrl();
                    if (!TextUtils.isEmpty(avatarUrl)) {
                        Picasso.with(getContext()).load(avatarUrl)
                                .placeholder(R.drawable.lcim_default_avatar_icon).into(holder.avatar);
                    }
                }
            }
        });

    }
    private static String millisecsToDateString(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("M-dd HH:mm");
        return format.format(new Date(timestamp));
    }
}

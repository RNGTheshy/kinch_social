package cn.leancloud.chatkit.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cn.leancloud.chatkit.R;

public class ChatHistoryHolder extends RecyclerView.ViewHolder{
    public TextView time;
    public TextView name;
    public ImageView avatar;
    public TextView content;
    public ChatHistoryHolder(@NonNull View itemView) {
        super(itemView);
        time = (TextView) itemView.findViewById(R.id.chat_history_time);
        name = (TextView) itemView.findViewById(R.id.chat_history_name);
        avatar = (ImageView) itemView.findViewById(R.id.chat_history_avatar);
        content = (TextView) itemView.findViewById(R.id.chat_history_content);
    }
}

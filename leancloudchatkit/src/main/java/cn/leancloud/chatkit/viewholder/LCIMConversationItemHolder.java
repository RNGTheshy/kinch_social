package cn.leancloud.chatkit.viewholder;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.leancloud.callback.LCCallback;
import cn.leancloud.LCException;
import cn.leancloud.im.v2.LCIMChatRoom;
import cn.leancloud.im.v2.LCIMConversation;
import cn.leancloud.im.v2.LCIMException;
import cn.leancloud.im.v2.LCIMMessage;
import cn.leancloud.im.v2.LCIMReservedMessageType;
import cn.leancloud.im.v2.LCIMServiceConversation;
import cn.leancloud.im.v2.LCIMTemporaryConversation;
import cn.leancloud.im.v2.LCIMTypedMessage;
import cn.leancloud.im.v2.callback.LCIMConversationCallback;
import cn.leancloud.im.v2.messages.LCIMTextMessage;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.leancloud.chatkit.LCChatMessageInterface;
import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.event.LCIMConversationItemLongClickEvent;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.utils.LCIMConversationUtils;
import cn.leancloud.chatkit.utils.LCIMLogUtils;
import de.greenrobot.event.EventBus;

/**
 * 会话 item 对应的 holder
 */
public class LCIMConversationItemHolder extends LCIMCommonViewHolder {

  ImageView avatarView;
  TextView unreadView;
  TextView typeView;
  TextView messageView;
  TextView timeView;
  TextView nameView;
//  RelativeLayout avatarLayout;
//  LinearLayout contentLayout;

  /**
   * 绑定布局
   * @param root
   */
  public LCIMConversationItemHolder(ViewGroup root) {
    super(root.getContext(), root, R.layout.lcim_conversation_item);
    initView();
  }

  /**
   * 获取组件
   */
  public void initView() {
    avatarView = (ImageView) itemView.findViewById(R.id.conversation_item_avatar);
    nameView = (TextView) itemView.findViewById(R.id.conversation_item_tv_name);
    timeView = (TextView) itemView.findViewById(R.id.conversation_item_tv_time);
    unreadView = (TextView) itemView.findViewById(R.id.conversation_item_tv_unread);
//    typeView = (TextView) itemView.findViewById(R.id.conversation_item_tv_type);
    messageView = (TextView) itemView.findViewById(R.id.conversation_item_tv_message);
//    avatarLayout = (RelativeLayout) itemView.findViewById(R.id.conversation_item_layout_avatar);
//    contentLayout = (LinearLayout) itemView.findViewById(R.id.conversation_item_layout_content);
  }

  /**
   * 将传入的数据绑定到组件上
   * @param o
   */
  @Override
  public void bindData(Object o) {
    //清空数据
    reset();
    final LCIMConversation conversation = (LCIMConversation) o;
    if (null != conversation) {
      if (null == conversation.getCreatedAt()) {
        conversation.fetchInfoInBackground(new LCIMConversationCallback() {
          @Override
          public void done(LCIMException e) {
            if (e != null) {
              LCIMLogUtils.logException(e);
            } else {
              updateName(conversation);
              updateIcon(conversation);
            }
          }
        });
      } else {
        updateName(conversation);
        updateIcon(conversation);
      }

//      if (conversation instanceof LCIMServiceConversation) {
//        typeView.setText("S");
//      } else if (conversation instanceof LCIMTemporaryConversation) {
//        typeView.setText("T");
//      } else if (conversation instanceof LCIMChatRoom) {
//        typeView.setText("R");
//      } else {
//        typeView.setText("C");
//      }

      //刷新未读数据量
      updateUnreadCount(conversation);
      //更新最后的消息
      updateLastMessage(conversation.getLastMessage());
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //响应点击事件 跳转到聊天
          onConversationItemClick(conversation);
        }
      });
    //删除聊天项item事件
      itemView.setOnLongClickListener(new View.OnLongClickListener() {
        //长按
        @Override
        public boolean onLongClick(View v) {
          AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
          builder.setItems(new String[]{"删除该聊天"}, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              EventBus.getDefault().post(new LCIMConversationItemLongClickEvent(conversation));
            }
          });
          AlertDialog dialog = builder.create();
          dialog.show();
          return false;
        }
      });
    }
  }

  /**
   * 一开始的时候全部置为空，避免因为异步请求造成的刷新不及时而导致的展示原有的缓存数据
   */
  private void reset() {
    avatarView.setImageResource(0);
    nameView.setText("");
    timeView.setText("");
    messageView.setText("");
//    typeView.setText("");
    unreadView.setVisibility(View.GONE);
  }

  /**
   * 更新 name，单聊的话展示对方姓名
   *
   * @param conversation
   */
  private void updateName(final LCIMConversation conversation) {
    //从缓存中取出数据
    LCIMConversationUtils.getConversationName(conversation, new LCCallback<String>() {
      @Override
      protected void internalDone0(String s, LCException e) {
        if (null != e) {
          LCIMLogUtils.logException(e);
        } else {
          //设置聊天名
          nameView.setText(s);
        }
      }
    });
  }

  /**
   * 更新 item icon，目前的逻辑为：
   * 单聊：展示对方的头像
   * @param conversation
   */
  private void updateIcon(LCIMConversation conversation) {
    if (null != conversation) {
      if (conversation.isTransient() || conversation.getMembers().size() > 2) {
        avatarView.setImageResource(R.drawable.lcim_group_icon);
      } else {
        //TODO 更新头像
        //更新头像
        LCIMConversationUtils.getConversationPeerIcon(conversation, new LCCallback<String>() {
          @Override
          protected void internalDone0(String s, LCException e) {
            if (null != e) {
              LCIMLogUtils.logException(e);
            }
            if (!TextUtils.isEmpty(s)) {
              Picasso.with(getContext()).load(s)
                .placeholder(R.drawable.lcim_default_avatar_icon).into(avatarView);
            } else {
              avatarView.setImageResource(R.drawable.lcim_default_avatar_icon);
            }
          }
        });
      }
    }
  }

  /**
   * 更新未读消息数量
   *
   * @param conversation
   */
  private void updateUnreadCount(LCIMConversation conversation) {
    int num = conversation.getUnreadMessagesCount();
    unreadView.setText(num + "");
    unreadView.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
  }

  /**
   * 更新 item 的展示内容，及最后一条消息的内容
   *
   * @param message
   */
  private void updateLastMessage(LCIMMessage message) {
    if (null != message) {
      Date date = new Date(message.getTimestamp());
      SimpleDateFormat format = new SimpleDateFormat("M-d HH:mm");
      timeView.setText(format.format(date));
      messageView.setText(getMessageeShorthand(getContext(), message));
    }
  }

  /**
   * 响应聊天项点击事件，跳转到聊天页面
   * @param conversation
   */
  private void onConversationItemClick(LCIMConversation conversation) {
    try {
      //跳转 传入对话id
      Intent intent = new Intent();
      intent.setPackage(getContext().getPackageName());
      intent.setAction(LCIMConstants.CONVERSATION_ITEM_CLICK_ACTION);
      intent.addCategory(Intent.CATEGORY_DEFAULT);
      intent.putExtra(LCIMConstants.CONVERSATION_ID, conversation.getConversationId());
      getContext().startActivity(intent);
    } catch (ActivityNotFoundException exception) {
      Log.i(LCIMConstants.LCIM_LOG_TAG, exception.toString());
    }
  }

  /**
   * 通过此接口实例化该类
   */
  public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<LCIMConversationItemHolder>() {
    @Override
    public LCIMConversationItemHolder createByViewGroupAndType(ViewGroup parent, int viewType, int attachData) {
      return new LCIMConversationItemHolder(parent);
    }
  };

  /**
   * 获取消息的简讯
   * @param context
   * @param message
   * @return 消息的简讯CharSequence类型,语音返回[语音],文字返回文本内容
   */
  private static CharSequence getMessageeShorthand(Context context, LCIMMessage message) {
    if (message instanceof LCIMTypedMessage) {
      LCIMReservedMessageType type = LCIMReservedMessageType.getLCIMReservedMessageType(
        ((LCIMTypedMessage) message).getMessageType());
      switch (type) {
        case TextMessageType:
          return ((LCIMTextMessage) message).getText();
        case ImageMessageType:
          return context.getString(R.string.lcim_message_shorthand_image);
        case LocationMessageType:
          return context.getString(R.string.lcim_message_shorthand_location);
        case AudioMessageType:
          return context.getString(R.string.lcim_message_shorthand_audio);
        default:
          CharSequence shortHand = "";
          if (message instanceof LCChatMessageInterface) {
            LCChatMessageInterface messageInterface = (LCChatMessageInterface) message;
            shortHand = messageInterface.getShorthand();
          }
          if (TextUtils.isEmpty(shortHand)) {
            shortHand = context.getString(R.string.lcim_message_shorthand_unknown);
          }
          return shortHand;
      }
    } else {
      return message.getContent();
    }
  }
}

package cn.leancloud.chatkit.viewholder;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.leancloud.callback.LCCallback;
import cn.leancloud.LCException;
import cn.leancloud.chatkit.activity.LCIMConversationSettingActivity;
import cn.leancloud.im.v2.LCIMMessage;

import com.chaoshan.data_center.togetname.Headport;
import com.chaoshan.data_center.togetname.center_getname;
import com.chaoshan.data_center.togetname.getPersonal_data;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.cache.LCIMProfileCache;
import cn.leancloud.chatkit.event.LCIMMessageResendEvent;
import cn.leancloud.chatkit.event.LCIMMessageUpdateEvent;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.utils.LCIMLogUtils;
import de.greenrobot.event.EventBus;

/**
 * Created by wli on 15/9/17.
 * 聊天 item 的 holder
 */
public class LCIMChatItemHolder extends LCIMCommonViewHolder {

  protected boolean isLeft;

  protected LCIMMessage message;
  protected ImageView avatarView;
  protected TextView timeView;
  protected TextView nameView;
  protected LinearLayout conventLayout;
  protected FrameLayout statusLayout;
  protected ProgressBar progressBar;
  protected TextView statusView;
  protected ImageView errorView;

  public LCIMChatItemHolder(Context context, ViewGroup root, boolean isLeft) {
    super(context, root, isLeft ? R.layout.lcim_chat_item_left_layout : R.layout.lcim_chat_item_right_layout);
    this.isLeft = isLeft;
    initView();
  }

  public void initView() {
    if (isLeft) {
      avatarView = (ImageView) itemView.findViewById(R.id.chat_left_iv_avatar);
      timeView = (TextView) itemView.findViewById(R.id.chat_left_tv_time);
      nameView = (TextView) itemView.findViewById(R.id.chat_left_tv_name);
      conventLayout = (LinearLayout) itemView.findViewById(R.id.chat_left_layout_content);
      statusLayout = (FrameLayout) itemView.findViewById(R.id.chat_left_layout_status);
      statusView = (TextView) itemView.findViewById(R.id.chat_left_tv_status);
      progressBar = (ProgressBar) itemView.findViewById(R.id.chat_left_progressbar);
      errorView = (ImageView) itemView.findViewById(R.id.chat_left_tv_error);
    } else {
      avatarView = (ImageView) itemView.findViewById(R.id.chat_right_iv_avatar);
      timeView = (TextView) itemView.findViewById(R.id.chat_right_tv_time);
      nameView = (TextView) itemView.findViewById(R.id.chat_right_tv_name);
      nameView.setVisibility(View.GONE);
      conventLayout = (LinearLayout) itemView.findViewById(R.id.chat_right_layout_content);
      statusLayout = (FrameLayout) itemView.findViewById(R.id.chat_right_layout_status);
      progressBar = (ProgressBar) itemView.findViewById(R.id.chat_right_progressbar);
      errorView = (ImageView) itemView.findViewById(R.id.chat_right_tv_error);
      statusView = (TextView) itemView.findViewById(R.id.chat_right_tv_status);
    }

    setAvatarClickEvent();
    setResendClickEvent();
    setUpdateMessageEvent();
  }

  @Override
  public void bindData(Object o) {
    message = (LCIMMessage) o;
    timeView.setText(millisecsToDateString(message.getTimestamp()));
    nameView.setText("");
    avatarView.setImageResource(R.drawable.lcim_default_avatar_icon);
    //TODO 头像设置
//    Headport  headPort = new Headport();
//    headPort.setImage(message.getFrom(),avatarView);
//    getPersonal_data.center_getname(message.getFrom(), new center_getname() {
//      @Override
//      public void getname(String name) {
//        nameView.setText(name);
//      }
//    });

    LCIMProfileCache.getInstance().getCachedUser(message.getFrom(), new LCCallback<LCChatKitUser>() {
      @Override
      protected void internalDone0(LCChatKitUser userProfile, LCException e) {
        if (null != e) {
          LCIMLogUtils.logException(e);
        } else if (null != userProfile) {
          nameView.setText(userProfile.getName());
          final String avatarUrl = userProfile.getAvatarUrl();
          if (!TextUtils.isEmpty(avatarUrl)) {
            Picasso.with(getContext()).load(avatarUrl)
              .placeholder(R.drawable.lcim_default_avatar_icon).into(avatarView);
          }
        }
      }
    });

    switch (message.getMessageStatus()) {
      case StatusFailed:
        statusLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        statusView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        break;
      case StatusSent:
        statusLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        statusView.setVisibility(View.VISIBLE);
        statusView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        break;
      case StatusSending:
        statusLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        statusView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        break;
      case StatusNone:
      case StatusReceipt:
        statusLayout.setVisibility(View.GONE);
        break;
    }
  }

  public void setHolderOption(LCIMChatHolderOption option) {
    if (null != option && !isLeft &&
      (LCIMMessage.MessageStatus.StatusSent == message.getMessageStatus() ||
      LCIMMessage.MessageStatus.StatusReceipt == message.getMessageStatus())) {
      timeView.setVisibility(option.isShowTime() ? View.VISIBLE : View.GONE);
//      nameView.setVisibility(option.isShowName() ? View.VISIBLE : View.GONE);
      nameView.setVisibility(isLeft? View.VISIBLE : View.GONE);
      statusView.setVisibility(option.isShowDelivered() || option.isShowRead() ? View.VISIBLE : View.GONE);
      statusLayout.setVisibility(option.isShowDelivered() || option.isShowRead() ? View.VISIBLE : View.GONE);
      progressBar.setVisibility(View.GONE);
      errorView.setVisibility(View.GONE);
//      if (option.isShowRead()) {
//        statusView.setText("已读");
//      } else if (option.isShowDelivered()) {
//        statusView.setText("已收到");
//      }
    }
  }

  /**
   * 设置头像点击按钮的事件
   */
  private void setAvatarClickEvent() {
    avatarView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          //TODO 跳转个人中心
//          Intent intent = new Intent(getContext(), LCIMConversationSettingActivity.class);
//          getContext().startActivity(intent);
        } catch (ActivityNotFoundException exception) {
          Log.i(LCIMConstants.LCIM_LOG_TAG, exception.toString());
        }
      }
    });
  }

  /**
   * 设置发送失败的叹号按钮的事件
   */
  private void setResendClickEvent() {
    errorView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        LCIMMessageResendEvent event = new LCIMMessageResendEvent();
        event.message = message;
        EventBus.getDefault().post(event);
      }
    });
  }

  private void setUpdateMessageEvent() {
//    conventLayout.setOnLongClickListener(new View.OnLongClickListener() {
//      @Override
//      public boolean onLongClick(View v) {
//        LCIMMessageUpdateEvent event = new LCIMMessageUpdateEvent();
//        event.message = message;
//        EventBus.getDefault().post(event);
//        return false;
//      }
//    });
  }

  //TODO 展示更人性一点
  private static String millisecsToDateString(long timestamp) {
    SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
    return format.format(new Date(timestamp));
  }
}


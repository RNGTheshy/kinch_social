package cn.leancloud.chatkit.viewholder;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.leancloud.im.v2.LCIMMessage;
import cn.leancloud.im.v2.messages.LCIMImageMessage;
import com.squareup.picasso.Picasso;

import java.io.File;

import cn.leancloud.chatkit.activity.LCIMImageActivity;
import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.utils.LCIMConstants;

/**
 * 聊天页面中的图片 item 对应的 holder
 */
public class LCIMChatItemImageHolder extends LCIMChatItemHolder {

  protected ImageView contentView;
  private static final int MAX_DEFAULT_HEIGHT = 400;
  private static final int MAX_DEFAULT_WIDTH = 300;

  public LCIMChatItemImageHolder(Context context, ViewGroup root, boolean isLeft) {
    super(context, root, isLeft);
  }

  /**
   * 初始化组件
   */
  @Override
  public void initView() {
    super.initView();
    conventLayout.addView(View.inflate(getContext(), R.layout.lcim_chat_item_image_layout, null));
    contentView = (ImageView) itemView.findViewById(R.id.chat_item_image_view);
    //设置消息背景框
    if (isLeft) {
      contentView.setBackgroundResource(R.drawable.lcim_chat_item_left_bg);
    } else {
      contentView.setBackgroundResource(R.drawable.lcim_chat_item_right_bg);
    }
    //图片点击事件
    contentView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          //跳转到详情页面
          Intent intent = new Intent(getContext(), LCIMImageActivity.class);
          intent.setPackage(getContext().getPackageName());
          intent.putExtra(LCIMConstants.IMAGE_LOCAL_PATH, ((LCIMImageMessage) message).getLocalFilePath());
          intent.putExtra(LCIMConstants.IMAGE_URL, ((LCIMImageMessage) message).getFileUrl());
          getContext().startActivity(intent);
        } catch (ActivityNotFoundException exception) {
          Log.i(LCIMConstants.LCIM_LOG_TAG, exception.toString());
        }
      }
    });
  }

  /**
   * 绑定数据到组件
   * @param o
   */
  @Override
  public void bindData(Object o) {
    super.bindData(o);
    contentView.setImageResource(0);
    LCIMMessage message = (LCIMMessage) o;
    if (message instanceof LCIMImageMessage) {
      LCIMImageMessage imageMsg = (LCIMImageMessage) message;
      String localFilePath = imageMsg.getLocalFilePath();

      // 图片的真实高度与宽度
      double actualHight = imageMsg.getHeight();
      double actualWidth = imageMsg.getWidth();
      //图片最大尺寸
      double viewHeight = MAX_DEFAULT_HEIGHT;
      double viewWidth = MAX_DEFAULT_WIDTH;

      if (0 != actualHight && 0 != actualWidth) {
        // 要保证图片的长宽比不变
        double ratio = actualHight / actualWidth;
        // 图片不能超过最大尺寸
        if (ratio > viewHeight / viewWidth) {
          viewHeight = (actualHight > viewHeight ? viewHeight : actualHight);
          viewWidth = viewHeight / ratio;
        } else {
          viewWidth = (actualWidth > viewWidth ? viewWidth : actualWidth);
          viewHeight = viewWidth * ratio;
        }
      }
      //设置尺寸
      contentView.getLayoutParams().height = (int) viewHeight;
      contentView.getLayoutParams().width = (int) viewWidth;
      //加载图片
      if (!TextUtils.isEmpty(localFilePath)) {
        Picasso.with(getContext().getApplicationContext()).load(new File(localFilePath)).
          resize((int) viewWidth, (int) viewHeight).centerCrop().into(contentView);
      } else if (!TextUtils.isEmpty(imageMsg.getFileUrl())) {
        Picasso.with(getContext().getApplicationContext()).load(imageMsg.getFileUrl()).
          resize((int) viewWidth, (int) viewHeight).centerCrop().into(contentView);
      } else {
        contentView.setImageResource(0);
      }
    }
  }
}
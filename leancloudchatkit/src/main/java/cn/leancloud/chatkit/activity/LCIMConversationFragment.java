package cn.leancloud.chatkit.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.EditText;

import cn.leancloud.LCException;
import cn.leancloud.core.AppConfiguration;
import cn.leancloud.im.v2.LCIMConversation;
import cn.leancloud.im.v2.LCIMException;
import cn.leancloud.im.v2.LCIMMessage;
import cn.leancloud.im.v2.LCIMMessageOption;
import cn.leancloud.im.v2.callback.LCIMConversationCallback;
import cn.leancloud.im.v2.callback.LCIMMessagesQueryCallback;
import cn.leancloud.im.v2.callback.LCIMMessageRecalledCallback;
import cn.leancloud.im.v2.callback.LCIMMessageUpdatedCallback;
import cn.leancloud.im.v2.messages.LCIMAudioMessage;
import cn.leancloud.im.v2.messages.LCIMImageMessage;
import cn.leancloud.im.v2.messages.LCIMTextMessage;
import cn.leancloud.im.v2.messages.LCIMRecalledMessage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.adapter.LCIMChatAdapter;
import cn.leancloud.chatkit.event.LCIMConversationReadStatusEvent;
import cn.leancloud.chatkit.event.LCIMIMTypeMessageEvent;
import cn.leancloud.chatkit.event.LCIMInputBottomBarEvent;
import cn.leancloud.chatkit.event.LCIMInputBottomBarRecordEvent;
import cn.leancloud.chatkit.event.LCIMInputBottomBarTextEvent;
import cn.leancloud.chatkit.event.LCIMMessageResendEvent;
import cn.leancloud.chatkit.event.LCIMMessageUpdateEvent;
import cn.leancloud.chatkit.event.LCIMMessageUpdatedEvent;
import cn.leancloud.chatkit.event.LCIMOfflineMessageCountChangeEvent;
import cn.leancloud.chatkit.utils.LCIMAudioHelper;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.utils.LCIMLogUtils;
import cn.leancloud.chatkit.utils.LCIMNotificationUtils;
import cn.leancloud.chatkit.utils.LCIMPathUtils;
import cn.leancloud.chatkit.view.LCIMInputBottomBar;
import de.greenrobot.event.EventBus;

/**
 * ?????????????????????????????? Fragment ???????????????????????? setConversation ?????? Conversation ??????
 */
public class LCIMConversationFragment extends Fragment {

  private static final int REQUEST_IMAGE_CAPTURE = 1;
  private static final int REQUEST_IMAGE_PICK = 2;
  private static final int REQUEST_CLEANING_CHAT = 3;
  protected LCIMConversation imConversation;

  /**
   * recyclerView ????????? Adapter
   */
  protected LCIMChatAdapter itemAdapter;

  protected RecyclerView recyclerView;
  protected LinearLayoutManager layoutManager;

  /**
   * ????????????????????? SwipeRefreshLayout
   */
  protected SwipeRefreshLayout refreshLayout;

  /**
   * ??????????????????
   */
  protected LCIMInputBottomBar inputBottomBar;

  // ??????????????????????????????
  protected String localCameraPath;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.lcim_conversation_fragment, container, false);

    recyclerView = (RecyclerView) view.findViewById(R.id.fragment_chat_rv_chat);
    refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_chat_srl_pullrefresh);
    refreshLayout.setEnabled(false);
    inputBottomBar = (LCIMInputBottomBar) view.findViewById(R.id.fragment_chat_inputbottombar);
    layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);

    itemAdapter = getAdpter();
    itemAdapter.resetRecycledViewPoolSize(recyclerView);
    recyclerView.setAdapter(itemAdapter);

    EventBus.getDefault().register(this);
    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        LCIMMessage message = itemAdapter.getFirstMessage();
        if (null == message) {
          refreshLayout.setRefreshing(false);
        } else {
          imConversation.queryMessages(message.getMessageId(), message.getTimestamp(), 20, new LCIMMessagesQueryCallback() {
            @Override
            public void done(List<LCIMMessage> list, LCIMException e) {
              refreshLayout.setRefreshing(false);
              if (filterException(e)) {
                if (null != list && list.size() > 0) {
                  itemAdapter.addMessageList(list);
                  itemAdapter.setDeliveredAndReadMark(imConversation.getLastDeliveredAt(),
                    imConversation.getLastReadAt());
                  itemAdapter.notifyDataSetChanged();
                  layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                }
              }
            }
          });
        }
      }
    });
    ImageView closeBtn = (ImageView)view.findViewById(R.id.close_chat);
    closeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getActivity().finish();
      }
    });
    ImageView chatSetting = (ImageView)view.findViewById(R.id.chat_setting);
    chatSetting.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //TODO ????????????????????????
        //????????????????????????
        Intent intent = new Intent(getContext(),ChatSettingActivity.class);
        intent.putExtra(ChatSettingActivity.CONVERSATION_ID,imConversation.getConversationId());
        startActivityForResult(intent,REQUEST_CLEANING_CHAT);
      }
    });
  }

  protected LCIMChatAdapter getAdpter() {
    return new LCIMChatAdapter();
  }

  @Override
  public void onResume() {
    super.onResume();
    if (null != imConversation) {
      LCIMNotificationUtils.addTag(imConversation.getConversationId());
      //??????????????????
//      LCIMMessagesQueryCallback callback = new LCIMMessagesQueryCallback() {
//        @Override
//        public void done(List<LCIMMessage> messageList, LCIMException e) {
//          if (filterException(e)) {
//            itemAdapter.setMessageList(messageList);
//            recyclerView.setAdapter(itemAdapter);
//            itemAdapter.setDeliveredAndReadMark(imConversation.getLastDeliveredAt(),
//                    imConversation.getLastReadAt());
//            itemAdapter.notifyDataSetChanged();
//            scrollToBottom();
//            clearUnreadConut();
//          }
//        }
//      };
//      imConversation.queryMessagesFromCache( 20, callback);
    }
  }


  @Override
  public void onPause() {
    super.onPause();
    LCIMAudioHelper.getInstance().stopPlayer();
    if (null != imConversation) {
      LCIMNotificationUtils.removeTag(imConversation.getConversationId());
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    EventBus.getDefault().unregister(this);
  }

//  @Override
//  public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
//    inflater.inflate(R.menu.conv_menu, menu);
//  }

//  @Override
//  public boolean onOptionsItemSelected (MenuItem item) {
//    if (item.getItemId() == R.id.menu_conv_setting) {
//      Intent intent = new Intent(getActivity(), LCIMConversationDetailActivity.class);
//      intent.putExtra(LCIMConstants.CONVERSATION_ID, imConversation.getConversationId());
//      getActivity().startActivity(intent);
//      return true;
//    }
//    return super.onOptionsItemSelected(item);
//  }

  public void setConversation(final LCIMConversation conversation) {
    imConversation = conversation;
    refreshLayout.setEnabled(true);
    inputBottomBar.setTag(imConversation.getConversationId());
    fetchMessages();
    imConversation.read();
    LCIMNotificationUtils.addTag(conversation.getConversationId());
    if (!conversation.isTransient()) {
      if (conversation.getMembers().size() == 0) {
        conversation.fetchInfoInBackground(new LCIMConversationCallback() {
          @Override
          public void done(LCIMException e) {
            if (null != e) {
              LCIMLogUtils.logException(e);
              Toast.makeText(getContext(), "encounter network error, please try later.", Toast.LENGTH_SHORT);
            }
            itemAdapter.showUserName(conversation.getMembers().size() > 2);
          }
        });
      } else {
        itemAdapter.showUserName(conversation.getMembers().size() > 2);
      }
    } else {
      itemAdapter.showUserName(true);
    }
  }

  /**
   * ??????????????????????????? conversation ?????????????????????
   */
  private void fetchMessages() {
//    LCIMMessagesQueryCallback callback = new LCIMMessagesQueryCallback() {
//      @Override
//      public void done(List<LCIMMessage> messageList, LCIMException e) {
//        if (filterException(e)) {
//          itemAdapter.setMessageList(messageList);
//          recyclerView.setAdapter(itemAdapter);
//          itemAdapter.setDeliveredAndReadMark(imConversation.getLastDeliveredAt(),
//                  imConversation.getLastReadAt());
//          itemAdapter.notifyDataSetChanged();
//          scrollToBottom();
//          clearUnreadConut();
//        }
//      }
//    };
//    if (!AppConfiguration.getGlobalNetworkingDetector().isConnected()) {
//      imConversation.queryMessagesFromCache( 20, callback);
//    }else{
//      imConversation.queryMessagesFromServer(20,callback);
//    }
    imConversation.queryMessagesFromCache(10,new LCIMMessagesQueryCallback() {
      @Override
      public void done(List<LCIMMessage> messageList, LCIMException e) {
        if (filterException(e)) {
          itemAdapter.setMessageList(messageList);
          recyclerView.setAdapter(itemAdapter);
          itemAdapter.setDeliveredAndReadMark(imConversation.getLastDeliveredAt(),
            imConversation.getLastReadAt());
          itemAdapter.notifyDataSetChanged();
          scrollToBottom();
          clearUnreadConut();
        }
      }
    });
  }

  /**
   * ??????????????????????????????????????? LCIMTextMessage ????????????
   * ???????????????????????????????????????????????????????????????????????????????????????????????? tag ??????
   */
  public void onEvent(LCIMInputBottomBarTextEvent textEvent) {
    if (null != imConversation && null != textEvent) {
      if (!TextUtils.isEmpty(textEvent.sendContent) && imConversation.getConversationId().equals(textEvent.tag)) {
        sendText(textEvent.sendContent);
      }
    }
  }

  /**
   * ???????????????????????????
   * ?????????????????????????????????????????? conversation id ??????
   */
  public void onEvent(LCIMIMTypeMessageEvent messageEvent) {
    if (null != imConversation && null != messageEvent &&
      imConversation.getConversationId().equals(messageEvent.conversation.getConversationId())) {
      System.out.println("currentConv unreadCount=" + imConversation.getUnreadMessagesCount());
      if (imConversation.getUnreadMessagesCount() > 0) {
//        Log.e("testPost",String.valueOf(itemAdapter.getItemCount()));
        paddingNewMessage(imConversation);
      } else {
        itemAdapter.addMessage(messageEvent.message);
        itemAdapter.notifyDataSetChanged();
        scrollToBottom();
      }
    }
  }

  /**
   * ???????????????????????????????????????
   */

  public void onEvent(LCIMMessageResendEvent resendEvent) {
    if (null != imConversation && null != resendEvent &&
      null != resendEvent.message && imConversation.getConversationId().equals(resendEvent.message.getConversationId())) {
      if (LCIMMessage.MessageStatus.StatusFailed == resendEvent.message.getMessageStatus()
        && imConversation.getConversationId().equals(resendEvent.message.getConversationId())) {
        sendMessage(resendEvent.message, false);
      }
    }
  }

  /**
   * ????????????????????????????????????
   *
   * @param event
   */
  public void onEvent(LCIMInputBottomBarEvent event) {
    if (null != imConversation && null != event && imConversation.getConversationId().equals(event.tag)) {
      switch (event.eventAction) {
        case LCIMInputBottomBarEvent.INPUTBOTTOMBAR_IMAGE_ACTION:
          dispatchPickPictureIntent();
          break;
        case LCIMInputBottomBarEvent.INPUTBOTTOMBAR_CAMERA_ACTION:
          dispatchTakePictureIntent();
          break;
        default:
          break;
      }
    }
  }

  /**
   * ??????????????????
   *
   * @param recordEvent
   */
  public void onEvent(LCIMInputBottomBarRecordEvent recordEvent) {
    if (null != imConversation && null != recordEvent &&
      !TextUtils.isEmpty(recordEvent.audioPath) &&
      imConversation.getConversationId().equals(recordEvent.tag)) {
      if (recordEvent.audioDuration > 0)
        sendAudio(recordEvent.audioPath);
    }
  }

  /**
   * ?????????????????????????????????
   * @param readEvent
   */
  public void onEvent(LCIMConversationReadStatusEvent readEvent) {
    if (null != imConversation && null != readEvent &&
      imConversation.getConversationId().equals(readEvent.conversationId)) {
      itemAdapter.setDeliveredAndReadMark(imConversation.getLastDeliveredAt(),
        imConversation.getLastReadAt());
      itemAdapter.notifyDataSetChanged();
    }
  }

  public void onEvent(final LCIMMessageUpdateEvent event) {
    if (null != imConversation && null != event &&
      null != event.message && imConversation.getConversationId().equals(event.message.getConversationId())) {
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder.setTitle("??????").setItems(new String[]{"??????", "??????????????????"}, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          if (0 == which) {
            recallMessage(event.message);
          } else if (1 == which) {
            showUpdateMessageDialog(event.message);
          }
        }
      });
      builder.create().show();
    }
  }

  public void onEvent(final LCIMMessageUpdatedEvent event) {
    if (null != imConversation && null != event &&
      null != event.message && imConversation.getConversationId().equals(event.message.getConversationId())) {
      itemAdapter.updateMessage(event.message);
    }
  }

  public void onEvent(final LCIMOfflineMessageCountChangeEvent event) {
    if (null == event || null == event.conversation || null == event.conversation) {
      return;
    }
    if (!imConversation.getConversationId().equals(event.conversation.getConversationId())) {
      return;
    }
    if (event.conversation.getUnreadMessagesCount() < 1) {
      return;
    }
//    Log.e("testOff",String.valueOf(itemAdapter.getItemCount()));
//    paddingNewMessage(event.conversation);
  }

  private void paddingNewMessage(LCIMConversation currentConversation) {
//    Log.e("testRead",String.valueOf(itemAdapter.getItemCount()));
    if (null == currentConversation || currentConversation.getUnreadMessagesCount() < 1) {
      return;
    }
    final int queryLimit = currentConversation.getUnreadMessagesCount() > 100 ? 100 : currentConversation.getUnreadMessagesCount();
    currentConversation.queryMessages(queryLimit, new LCIMMessagesQueryCallback() {
      @Override
      public void done(List<LCIMMessage> list, LCIMException e) {
        if (null != e) {
          return;
        }
        for (LCIMMessage m: list) {
          itemAdapter.addMessage(m);
        }
        itemAdapter.notifyDataSetChanged();
        scrollToBottom();
//        Log.e("testMess",String.valueOf(itemAdapter.getItemCount()));
        clearUnreadConut();
      }
    });
  }

  private void showUpdateMessageDialog(final LCIMMessage message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    final EditText editText = new EditText(getActivity());
    builder.setView(editText);
    builder.setTitle("??????????????????");
    builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        String content = editText.getText().toString();
        updateMessage(message, content);
      }
    });
    builder.show();
  }

  private void recallMessage(LCIMMessage message) {
    imConversation.recallMessage(message, new LCIMMessageRecalledCallback() {
      @Override
      public void done(LCIMRecalledMessage recalledMessage, LCException e) {
        if (null == e) {
          itemAdapter.updateMessage(recalledMessage);
        } else {
          Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  private void updateMessage(LCIMMessage message, String newContent) {
    LCIMTextMessage textMessage = new LCIMTextMessage();
    textMessage.setText(newContent);
    imConversation.updateMessage(message, textMessage, new LCIMMessageUpdatedCallback() {
        @Override
        public void done(LCIMMessage message, LCException e) {
          if (null == e) {
            itemAdapter.updateMessage(message);
          } else {
            Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
          }
        }
      });
  }

  /**
   * ?????? Intent ???????????????????????????
   */
  private void dispatchTakePictureIntent() {
//    if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED) {

      Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        localCameraPath = LCIMPathUtils.getPicturePathByCurrentTime(getContext());
        Uri imageUri = Uri.fromFile(new File(localCameraPath));
        takePictureIntent.putExtra("return-data", false);
        takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
      } else {
        localCameraPath = Environment.getExternalStorageDirectory() + "/images/" + System.currentTimeMillis() + ".jpg";
        File photoFile = new File(localCameraPath);
        if(!photoFile.exists()){
          photoFile.getParentFile().mkdirs();
        }
        Uri photoURI = FileProvider.getUriForFile(this.getContext(),
                this.getContext().getPackageName() + ".provider", photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                photoURI);
      }
      if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
      }
//    }else {
//      this.requestPermissions(new String[]{Manifest.permission.WRITE_SETTINGS},1);
//    }
  }

  /**
   *  ??????????????????????????????
   * @param requestCode ?????????
   * @param permissions ??????
   * @param grantResults ????????????
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    switch (requestCode){
      //??????????????????
      case 1 :
        //??????
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
          //??????
          dispatchTakePictureIntent();
        }else {
          //????????????
          Toast.makeText(getContext(),"?????????????????????",Toast.LENGTH_SHORT).show();
        }
        break;
      default:
        break;
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  /**
   * ?????? Intent ???????????????????????????
   */
  private void dispatchPickPictureIntent() {
    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, null);
    photoPickerIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    startActivityForResult(photoPickerIntent, REQUEST_IMAGE_PICK);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (Activity.RESULT_OK == resultCode) {
      switch (requestCode) {
        //??????????????????
        case REQUEST_IMAGE_CAPTURE:
          //??????????????????
          sendImage(localCameraPath);
          break;
        //??????????????????
        case REQUEST_IMAGE_PICK:
          //??????????????????
          sendImage(getRealPathFromURI(getActivity(), data.getData()));
          break;
        //????????????????????????
        case REQUEST_CLEANING_CHAT:
          //????????????????????????
          fetchMessages();
          break;
        default:
          break;
      }
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  /**
   * ?????? recyclerView ?????????
   */
  private void scrollToBottom() {
    layoutManager.scrollToPositionWithOffset(itemAdapter.getItemCount() - 1, 0);
  }

  /**
   * ?????? Uri ???????????????????????????
   *
   * @param context
   * @param contentUri
   * @return
   */
  private String getRealPathFromURI(Context context, Uri contentUri) {
    if (contentUri.getScheme().equals("file")) {
      return contentUri.getEncodedPath();
    } else {
      Cursor cursor = null;
      try {
        String[] proj = {MediaStore.Images.Media.DATA};
        cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor) {
          int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
          cursor.moveToFirst();
          return cursor.getString(column_index);
        } else {
          return "";
        }
      } finally {
        if (cursor != null) {
          cursor.close();
        }
      }
    }
  }

  /**
   * ??????????????????
   *
   * @param content
   */
  protected void sendText(String content) {

    LCIMTextMessage message = new LCIMTextMessage();
    message.setText(content);
    sendMessage(message);
  }

  /**
   * ??????????????????
   * TODO ????????????????????????????????????
   *
   * @param imagePath
   */
  protected void sendImage(String imagePath) {
    try {
      sendMessage(new LCIMImageMessage(imagePath));
    } catch (IOException e) {
      LCIMLogUtils.logException(e);
    }
  }

  /**
   * ??????????????????
   *
   * @param audioPath
   */
  protected void sendAudio(String audioPath) {
    try {
      LCIMAudioMessage audioMessage = new LCIMAudioMessage(audioPath);
      sendMessage(audioMessage);
    } catch (IOException e) {
      LCIMLogUtils.logException(e);
    }
  }

  public void sendMessage(LCIMMessage message) {
    sendMessage(message, true);
  }

  /**
   * ????????????
   *
   * @param message
   */
  public void sendMessage(LCIMMessage message, boolean addToList) {
    if (addToList) {
      itemAdapter.addMessage(message);
    }
    itemAdapter.notifyDataSetChanged();
    scrollToBottom();
    LCIMMessageOption option = new LCIMMessageOption();
    if (message instanceof LCIMTextMessage) {
      LCIMTextMessage textMessage = (LCIMTextMessage) message;
      if (textMessage.getText().startsWith("tr:")) {
        option.setTransient(true);
      } else {
        option.setReceipt(true);
      }
    } else {
      option.setReceipt(true);
    }
    imConversation.sendMessage(message, option, new LCIMConversationCallback() {
      @Override
      public void done(LCIMException e) {
        itemAdapter.notifyDataSetChanged();
        if (null != e) {
          LCIMLogUtils.logException(e);
        }
      }
    });
  }

  /**
   * ????????????
   * @param e
   * @return
   */
  private boolean filterException(Exception e) {
    if (null != e) {
      LCIMLogUtils.logException(e);
      Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    return (null == e);
  }

  /**
   * ??????????????????
   */
  private void clearUnreadConut() {
    if (imConversation.getUnreadMessagesCount() > 0) {
      imConversation.read();
    }
  }
}

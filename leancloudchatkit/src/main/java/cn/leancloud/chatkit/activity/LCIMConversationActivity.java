package cn.leancloud.chatkit.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cn.leancloud.callback.LCCallback;
import cn.leancloud.LCException;
import cn.leancloud.im.v2.LCIMConversation;
import cn.leancloud.im.v2.LCIMException;
import cn.leancloud.im.v2.LCIMTemporaryConversation;
import cn.leancloud.im.v2.callback.LCIMConversationCreatedCallback;

import java.util.Arrays;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.utils.LCIMConversationUtils;
import cn.leancloud.chatkit.utils.LCIMLogUtils;

/**
 * 会话详情页
 * 包含会话的创建以及拉取，具体的 UI 细节在 LCIMConversationFragment 中
 */
public class LCIMConversationActivity extends AppCompatActivity {

  protected LCIMConversationFragment conversationFragment;
  public static void startChatWithFriend(Context context, String friendObjId){
    Intent intent = new Intent(context, LCIMConversationActivity.class);
    intent.putExtra(LCIMConstants.PEER_ID, friendObjId);
    context.startActivity(intent);
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lcim_conversation_activity);
    conversationFragment = (LCIMConversationFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
//    conversationFragment.setHasOptionsMenu(true);
    initByIntent(getIntent());
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    initByIntent(intent);
  }

  private void initByIntent(Intent intent) {
    if (null == LCChatKit.getInstance().getClient()) {
      showToast("please login first!");
      finish();
      return;
    }

    Bundle extras = intent.getExtras();
    if (null != extras) {
      if (extras.containsKey(LCIMConstants.PEER_ID)) {
        getConversation(extras.getString(LCIMConstants.PEER_ID));
      } else if (extras.containsKey(LCIMConstants.CONVERSATION_ID)) {
        String conversationId = extras.getString(LCIMConstants.CONVERSATION_ID);
        updateConversation(LCChatKit.getInstance().getClient().getConversation(conversationId));
      } else {
        showToast("memberId or conversationId is needed");
        finish();
      }
    }
  }

  /**
   * 设置 actionBar title 以及 up 按钮事件
   *
   * @param title
   */
//  protected void initActionBar(String title) {
//    ActionBar actionBar = getSupportActionBar();
//    if (null != actionBar) {
//      if (null != title) {
//        actionBar.setTitle(title);
//      }
//      actionBar.setDisplayUseLogoEnabled(false);
//      actionBar.setDisplayHomeAsUpEnabled(true);
//      finishActivity(RESULT_OK);
//    }
//  }

//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//    getMenuInflater().inflate(R.menu.menu_setting, menu);
//    return true;
//  }

//  @Override
//  public boolean onOptionsItemSelected(MenuItem item) {
//    if (android.R.id.home == item.getItemId()) {
//      onBackPressed();
//      return true;
//    }
//    if (item.getItemId() == R.id.menu_conv_setting) {
//      Intent intent = new Intent(this, LCIMConversationSettingActivity.class);
//      startActivity(intent);
//      return true;
//    }
//    return super.onOptionsItemSelected(item);
//  }

  /**
   * 主动刷新 UI
   *
   * @param conversation
   */
  protected void updateConversation(LCIMConversation conversation) {
    if (null != conversation) {
      if (conversation instanceof LCIMTemporaryConversation) {
        System.out.println("Conversation expired flag: " + ((LCIMTemporaryConversation)conversation).isExpired());
      }
      conversationFragment.setConversation(conversation);
      LCIMConversationItemCache.getInstance().insertConversation(conversation.getConversationId());
      LCIMConversationUtils.getConversationName(conversation, new LCCallback<String>() {
        @Override
        protected void internalDone0(String s, LCException e) {
          if (null != e) {
            LCIMLogUtils.logException(e);
          }
//          else {
//            initActionBar(s);
//          }
        }
      });
    }
  }

  /**
   * 获取 conversation
   * 为了避免重复的创建，createConversation 参数 isUnique 设为 true·
   */
  protected void getConversation(final String memberId) {
    LCChatKit.getInstance().getClient().createConversation(
        Arrays.asList(memberId), "", null, false, true, new LCIMConversationCreatedCallback() {
          @Override
          public void done(LCIMConversation avimConversation, LCIMException e) {
            if (null != e) {
              showToast(e.getMessage());
            } else {
              updateConversation(avimConversation);
            }
          }
        });
  }

  /**
   * 弹出 toast
   *
   * @param content
   */
  private void showToast(String content) {
    Toast.makeText(LCIMConversationActivity.this, content, Toast.LENGTH_SHORT).show();
  }

//  @Override
//  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    List<Fragment> fragments = getSupportFragmentManager().getFragments();
//    if (fragments == null)
//    {
//      return;
//    }
//    // 查找在Fragment中onRequestPermissionsResult方法并调用
//    for (Fragment fragment : fragments)
//    {
//      if (fragment != null)
//      {
//        // 这里就会调用我们Fragment中的onRequestPermissionsResult方法
//        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
//      }
//    }
//  }
}
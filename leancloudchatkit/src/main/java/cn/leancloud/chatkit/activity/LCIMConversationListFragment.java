package cn.leancloud.chatkit.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.leancloud.im.v2.LCIMConversation;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.adapter.LCIMCommonListAdapter;
import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import cn.leancloud.chatkit.event.LCIMConversationItemLongClickEvent;
import cn.leancloud.chatkit.event.LCIMIMTypeMessageEvent;
import cn.leancloud.chatkit.event.LCIMOfflineMessageCountChangeEvent;
import cn.leancloud.chatkit.view.LCIMDividerItemDecoration;
import cn.leancloud.chatkit.viewholder.LCIMConversationItemHolder;
import de.greenrobot.event.EventBus;

/**
 * 会话列表页
 */
public class LCIMConversationListFragment extends Fragment {
  protected SwipeRefreshLayout refreshLayout;
  protected RecyclerView recyclerView;

  protected LCIMCommonListAdapter<LCIMConversation> itemAdapter;
  protected LinearLayoutManager layoutManager;

  /**
   * 初始化界面和事件
   * @param inflater
   * @param container
   * @param savedInstanceState
   * @return
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.lcim_conversation_list_fragment, container, false);
    //获取组件
    refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_conversation_srl_pullrefresh);
    recyclerView = (RecyclerView) view.findViewById(R.id.fragment_conversation_srl_view);
    //初始化组件
//    refreshLayout.setEnabled(false);
    layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.addItemDecoration(new LCIMDividerItemDecoration(getActivity()));
    itemAdapter = new LCIMCommonListAdapter<LCIMConversation>(LCIMConversationItemHolder.class);
    recyclerView.setAdapter(itemAdapter);

    //注册事件
    EventBus.getDefault().register(this);
    return view;
  }

//  @Override
//  public void onActivityCreated(Bundle savedInstanceState) {
//    super.onActivityCreated(savedInstanceState);
//    updateConversationList();
//  }

  /**
   * 初始化页面,刷新列表内容
   * @param view
   * @param savedInstanceState
   */
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //刷新页面内容
    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        updateConversationList();
        refreshLayout.setRefreshing(false);
      }
    });
    updateConversationList();
  }

  /**
   * 返回时刷新
   */
  @Override
  public void onResume() {
    super.onResume();
    updateConversationList();
  }

  /**
   * 销毁页面 注销响应事件
   */
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    EventBus.getDefault().unregister(this);
  }

  /**
   * 收到对方消息时响应此事件
   *
   * @param event
   */
  public void onEvent(LCIMIMTypeMessageEvent event) {
    updateConversationList();
  }

  /**
   * 删除会话列表中的某个 item
   * @param event
   */
  public void onEvent(LCIMConversationItemLongClickEvent event) {
    if (null != event.conversation) {
      String conversationId = event.conversation.getConversationId();
      LCIMConversationItemCache.getInstance().deleteConversation(conversationId);
      updateConversationList();
    }
  }

  /**
   * 刷新页面
   */
  private void updateConversationList() {
    //获取数据
    List<String> convIdList = LCIMConversationItemCache.getInstance().getSortedConversationList();
    List<LCIMConversation> conversationList = new ArrayList<>();
    for (String convId : convIdList) {
      conversationList.add(LCChatKit.getInstance().getClient().getConversation(convId));
    }
    //设置数据集
    itemAdapter.setDataList(conversationList);
    itemAdapter.notifyDataSetChanged();
  }

  /**
   * 离线消息数量发生变化是响应此事件
   * 避免登陆后先进入此页面，然后才收到离线消息数量的通知导致的页面不刷新的问题
   * @param updateEvent
   */
  public void onEvent(LCIMOfflineMessageCountChangeEvent updateEvent) {
    updateConversationList();
  }
}

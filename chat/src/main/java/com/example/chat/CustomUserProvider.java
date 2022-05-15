package com.example.chat;


import java.util.ArrayList;
import java.util.List;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by wli on 15/12/4.
 * 实现自定义用户体系
 */
public class CustomUserProvider implements LCChatProfileProvider {

  private static CustomUserProvider customUserProvider;

  public synchronized static CustomUserProvider getInstance() {
    if (null == customUserProvider) {
      customUserProvider = new CustomUserProvider();
    }
    return customUserProvider;
  }

  private CustomUserProvider() {
  }

  private static List<LCChatKitUser> partUsers = new ArrayList<LCChatKitUser>();

  // 此数据均为 fake，仅供参考
  static {
//    partUsers.add(new LCChatKitUser("Tom", "Tom", "http://www.avatarsdb.com/avatars/tom_and_jerry2.jpg"));
//    partUsers.add(new LCChatKitUser("Jerry", "Jerry", "http://www.avatarsdb.com/avatars/jerry.jpg"));
//    partUsers.add(new LCChatKitUser("Harry", "Harry", "http://www.avatarsdb.com/avatars/young_harry.jpg"));
//    partUsers.add(new LCChatKitUser("William", "William", "http://www.avatarsdb.com/avatars/william_shakespeare.jpg"));
//    partUsers.add(new LCChatKitUser("Bob", "Bob", "http://www.avatarsdb.com/avatars/bath_bob.jpg"));
//    partUsers.add(new LCChatKitUser("626684f51c11246b6f372cd2", "369", "http://www.avatarsdb.com/avatars/bath_bob.jpg"));
//    partUsers.add(new LCChatKitUser("626684df1c11246b6f372cc8", "258", "http://www.avatarsdb.com/avatars/bath_bob.jpg"));
//    partUsers.add(new LCChatKitUser("626684d61c11246b6f372cc4", "147", "http://www.avatarsdb.com/avatars/bath_bob.jpg"));
//    partUsers.add(new LCChatKitUser("626684ceadc5786698ac1f09", "789", "http://www.avatarsdb.com/avatars/bath_bob.jpg"));
//    partUsers.add(new LCChatKitUser("626684c2adc5786698ac1efa", "456", "http://www.avatarsdb.com/avatars/bath_bob.jpg"));
//    partUsers.add(new LCChatKitUser("6265738aadc5786698ab7876", "123", "http://www.avatarsdb.com/avatars/bath_bob.jpg"));

    LCQuery<LCObject> query = new LCQuery<>("_User");
    query.whereNotEqualTo("objectId", "");
    query.findInBackground().subscribe(new Observer<List<LCObject>>() {
      public void onSubscribe(Disposable disposable) {}
      public void onNext(List<LCObject> users) {
        for (LCObject user : users){
          partUsers.add(new LCChatKitUser(user.getObjectId(),user.getString("username"),"http://www.avatarsdb.com/avatars/bath_bob.jpg"));
        }
      }
      public void onError(Throwable throwable) {}
      public void onComplete() {}
    });
  }

  @Override
  public void fetchProfiles(List<String> list, LCChatProfilesCallBack callBack) {
    List<LCChatKitUser> userList = new ArrayList<LCChatKitUser>();
    for (String userId : list) {
      for (LCChatKitUser user : partUsers) {
        if (user.getUserId().equals(userId)) {
          userList.add(user);
          break;
        }
      }
    }
    callBack.done(userList, null);
  }

  @Override
  public List<LCChatKitUser> getAllUsers() {
    return partUsers;
  }
}

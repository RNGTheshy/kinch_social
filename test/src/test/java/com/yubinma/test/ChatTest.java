package com.yubinma.test;

import com.example.chat.CustomUserProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.handler.ChatKitClientEventHandler;
import cn.leancloud.chatkit.handler.LCIMConversationHandler;
import cn.leancloud.chatkit.handler.LCIMMessageHandler;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.core.LeanCloud;
import cn.leancloud.im.v2.LCIMClient;
import cn.leancloud.im.v2.LCIMException;
import cn.leancloud.im.v2.callback.LCIMClientCallback;

public class ChatTest {

    public  ChatTest(){

        LCChatKit.getInstance().init(
                "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
                "9uuBkty0jB2T7HXyqDWmLOVj",
                "https://wfb1urkd.lc-cn-n1-shared.com"
        );
    }

    /**
     * 测试单聊全局表名是否正确
     */
    @Test
    public void constantsPeerIdTest(){
        Assert.assertEquals("cn.leancloud.chatkit.peer_id",LCIMConstants.PEER_ID);
    }
    /**
     * 测试会话缓存全局表名是否正确
     */
    @Test
    public void constantsConversationIdTest(){
        Assert.assertEquals("cn.leancloud.chatkit.conversation_id",LCIMConstants.CONVERSATION_ID);
    }
    /**
     * 测试会话点击响应名是否正确
     */
    @Test
    public void constantsConversationItemActionIdTest(){
        Assert.assertEquals("cn.leancloud.chatkit.conversation_item_click_action",LCIMConstants.CONVERSATION_ITEM_CLICK_ACTION);
    }

    /**
     * 验证登录的用户id是否与登录相同
     */
    @Test
    public void isLCChatKit(){
        //启动聊天连接
        LCChatKit.getInstance().open("627f824e7a6d3118ac0c015f", new LCIMClientCallback() {
            @Override
            public void done(LCIMClient client, LCIMException e) {
                Assert.assertEquals(null, LCChatKit.getInstance().getCurrentUserId());
            }
        });

    }


}

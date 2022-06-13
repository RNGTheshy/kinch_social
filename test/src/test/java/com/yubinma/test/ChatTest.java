package com.yubinma.test;

import com.example.chat.CustomUserProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cn.leancloud.LCException;
import cn.leancloud.callback.LCCallback;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.cache.LCIMProfileCache;
import cn.leancloud.chatkit.handler.ChatKitClientEventHandler;
import cn.leancloud.chatkit.handler.LCIMConversationHandler;
import cn.leancloud.chatkit.handler.LCIMMessageHandler;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.utils.LCIMAudioHelper;
import cn.leancloud.chatkit.utils.LCIMConversationUtils;
import cn.leancloud.core.LeanCloud;
import cn.leancloud.im.LCIMOptions;
import cn.leancloud.im.v2.LCIMClient;
import cn.leancloud.im.v2.LCIMConversation;
import cn.leancloud.im.v2.LCIMException;
import cn.leancloud.im.v2.callback.LCIMClientCallback;
import cn.leancloud.session.LCConnectionManager;

public class ChatTest {
    /**
     * 初始化类
     * 初始化网络请求
     */
    public  ChatTest(){

//        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
//        LCIMOptions.getGlobalOptions().setDisableAutoLogin4Push(true);

//        LeanCloud.initialize("WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz", "9uuBkty0jB2T7HXyqDWmLOVj", "https://please-replace-with-your-customized.domain.com");
//        LCChatKit.getInstance().init(
//                "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
//                "9uuBkty0jB2T7HXyqDWmLOVj",
//                "https://wfb1urkd.lc-cn-n1-shared.com"
//        );
//        LCConnectionManager.getInstance().startConnection(new LCCallback() {
//            @Override
//            protected void internalDone0(Object o, LCException e) {
//                Assert.assertEquals(null,e);
//                if (e == null) {
//                    System.out.println("成功建立 WebSocket 链接");
//                } else {
//                    System.out.println("建立 WebSocket 链接失败：" + e.getMessage());
//                }
//            }
//        });
    }

    /**
     * 测试单聊全局表名是否正确
     */
    @Test
    public void constantsPeerIdTest(){
        //判断表名是否正确
        Assert.assertEquals("cn.leancloud.chatkit.peer_id",LCIMConstants.PEER_ID);
    }
    /**
     * 测试会话缓存全局表名是否正确
     */
    @Test
    public void constantsConversationIdTest(){
        //判断表名是否正确
        Assert.assertEquals("cn.leancloud.chatkit.conversation_id",LCIMConstants.CONVERSATION_ID);
    }
    /**
     * 测试会话点击响应名是否正确
     */
    @Test
    public void constantsConversationItemActionIdTest(){
        //判断表名是否正确
        Assert.assertEquals("cn.leancloud.chatkit.conversation_item_click_action",LCIMConstants.CONVERSATION_ITEM_CLICK_ACTION);
    }

    /**
     * 验证登录的用户id是否与登录相同
     */
    @Test
    public void isRightClientId(){
        //启动聊天连接
        LCChatKit.getInstance().init(
                "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
                "9uuBkty0jB2T7HXyqDWmLOVj",
                "https://wfb1urkd.lc-cn-n1-shared.com"
        );
        //连接
        LCConnectionManager.getInstance().startConnection(new LCCallback() {
            @Override
            protected void internalDone0(Object o, LCException e) {
                Assert.assertEquals(null,e);
                LCChatKit.getInstance().open("627f824e7a6d3118ac0c015f", new LCIMClientCallback() {
                    @Override
                    public void done(LCIMClient client, LCIMException e) {
                        //是否有错
                        Assert.assertEquals(null,e);
                        Assert.assertEquals("627f824e7a6d3118ac0c015f",LCChatKit.getInstance().getClient().getClientId());
                    }
                });
            }
        });

    }

    //new

    /**
     * 客户端连接是否正常关闭
     */
    @Test
    public void isClientClose(){
        //登录用户
        LCChatKit.getInstance().init(
                "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
                "9uuBkty0jB2T7HXyqDWmLOVj",
                "https://wfb1urkd.lc-cn-n1-shared.com"
        );
        //建立连接
        LCConnectionManager.getInstance().startConnection(new LCCallback() {
            @Override
            protected void internalDone0(Object o, LCException e) {
                //是否有异常
                Assert.assertEquals(null,e);
                LCChatKit.getInstance().open("627f824e7a6d3118ac0c015f", new LCIMClientCallback() {
                    @Override
                    public void done(LCIMClient client, LCIMException e) {
                        //是否有异常
                        Assert.assertEquals(null,e);
                        LCChatKit.getInstance().close(new LCIMClientCallback() {
                            @Override
                            public void done(LCIMClient client, LCIMException e) {
                                //client是否注销
                                Assert.assertNotEquals("627f824e7a6d3118ac0c015f",LCChatKit.getInstance().getClient().getClientId());
                            }
                        });

                    }
                });
            }
        });


    }
    /**
     * 通过id能否正确获取client
     */
    @Test
    public void isClientNull(){
        //登录服务器
        LCChatKit.getInstance().init(
                "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
                "9uuBkty0jB2T7HXyqDWmLOVj",
                "https://wfb1urkd.lc-cn-n1-shared.com"
        );
        //获取数据
        //建立连接
        LCConnectionManager.getInstance().startConnection(new LCCallback() {
            @Override
            protected void internalDone0(Object o, LCException e) {
                //是否有异常
                Assert.assertEquals(null,e);
                //登录用户
                LCChatKit.getInstance().open("627f824e7a6d3118ac0c015f", new LCIMClientCallback() {
                    @Override
                    public void done(LCIMClient client, LCIMException e) {
                        //是否有异常
                        Assert.assertEquals(null,e);
                        //client是否正常
                        Assert.assertNotEquals("627f824e7a6d3118ac0c015f",LCChatKit.getInstance().getClient());
                    }
                });
            }
        });


    }

    /**
     * 对话名称是否正确
     */
    @Test
    public void isNameRightFromConversation(){
        //登录服务器
        LCChatKit.getInstance().init(
                "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
                "9uuBkty0jB2T7HXyqDWmLOVj",
                "https://wfb1urkd.lc-cn-n1-shared.com"
        );
        //获取数据
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        //建立连接
        LCConnectionManager.getInstance().startConnection(new LCCallback() {
            @Override
            protected void internalDone0(Object o, LCException e) {
                //是否有异常
                Assert.assertEquals(null,e);
                //登录用户
                LCChatKit.getInstance().open("627f824e7a6d3118ac0c015f", new LCIMClientCallback() {
                    @Override
                    public void done(LCIMClient client, LCIMException e) {
                        //是否有异常
                        Assert.assertEquals(null,e);
                        //从id获取对话
                        LCIMConversationUtils.getConversationName(
                                LCChatKit.getInstance().getClient().getConversation("6283047db1f03ecd927e4573")
                                , new LCCallback<String>() {
                                    @Override
                                    protected void internalDone0(String s, LCException LCException) {
                                        //id对应对话名称是否正确
                                        Assert.assertEquals("nihaoao", s);
                                    }
                                });
                    }
                });
            }
        });
    }

    /**
     * 对话头像的url是否正确
     */
    @Test
    public void isIconRightFromConversation(){
        //登录服务器
        LCChatKit.getInstance().init(
                "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
                "9uuBkty0jB2T7HXyqDWmLOVj",
                "https://wfb1urkd.lc-cn-n1-shared.com"
        );
        //获取数据
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        //建立连接
        LCConnectionManager.getInstance().startConnection(new LCCallback() {
            @Override
            protected void internalDone0(Object o, LCException e) {
                //是否有异常
                Assert.assertEquals(null,e);
                //登录用户
                LCChatKit.getInstance().open("627f824e7a6d3118ac0c015f", new LCIMClientCallback() {
                    @Override
                    public void done(LCIMClient client, LCIMException e) {
                        //是否有异常
                        Assert.assertEquals(null,e);
                        //从id获取对话
                        LCIMConversationUtils.getConversationPeerIcon(
                                LCChatKit.getInstance().getClient().getConversation("6283047db1f03ecd927e4573")
                                , new LCCallback<String>() {
                                    @Override
                                    protected void internalDone0(String s, LCException LCException) {
                                        //id对应对话icon的url是否正确
                                        Assert.assertEquals("http://lc-WFB1URKd.cn-n1.lcfile.com/lL8lt7tkKOLue2vhRytGSxL5NWnhrmhk/test",
                                                s);
                                    }
                                });
                    }
                });
            }
        });
    }

    /**
     * 对话人数是否正常
     */
    @Test
    public void isMemberSizeRightFromConversation(){
        //登录服务器
        LCChatKit.getInstance().init(
                "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
                "9uuBkty0jB2T7HXyqDWmLOVj",
                "https://wfb1urkd.lc-cn-n1-shared.com"
        );
        //获取数据
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        //建立连接
        LCConnectionManager.getInstance().startConnection(new LCCallback() {
            @Override
            protected void internalDone0(Object o, LCException e) {
                //是否有异常
                Assert.assertEquals(null,e);
                //登录用户
                LCChatKit.getInstance().open("627f824e7a6d3118ac0c015f", new LCIMClientCallback() {
                    @Override
                    public void done(LCIMClient client, LCIMException e) {
                        //是否有异常
                        Assert.assertEquals(null,e);
                        //从id获取对话
                        //聊天人数是否为2
                        Assert.assertEquals(2,
                        LCChatKit.getInstance().getClient().getConversation("6283047db1f03ecd927e4573").getMembers().size());

                    }
                });
            }
        });
    }
}

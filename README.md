潮汕文化主题的app

包：
app： 主程序
app_debug ：开发测试用 进入activity或者fragment等快速调试启动入口
fishprawncrab：鱼虾蟹功能
blindbox ：盲盒
login:登录模块
socialforum：论坛朋友圈模块
ui：试图UI模块

技术说明：

1、build.gradle中
'''buildFeatures {
        viewBinding true
        mlModelBinding true
}'''
采用viewBinding 替换 findId

package com.yubinma.test;

import android.util.Log;

import com.chaoshan.data_center.friend.GetAllDataListener;
import com.chaoshan.data_center.togetname.Personal_data2;
import com.yubinma.person_center.Personal_data;
import junit.framework.TestCase;
import org.junit.Test;


import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.core.LeanCloud;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CenterTest {
    public CenterTest()
    {
        LeanCloud.initialize(
                "WFB1URKdIJqueBEjLd0P0xoy-gzGzoHsz",
                "9uuBkty0jB2T7HXyqDWmLOVj",
                "https://wfb1urkd.lc-cn-n1-shared.com"
        );
    }
    @Test
    //测试生日修改
    public void testsavebirth() throws InterruptedException {
        Personal_data2 test=new Personal_data2();
        final String userdata="userdata";
        final String objectid="629373a90534fd5ba0adbc78";
        test.saveBirth(userdata,objectid,"1999-1-1");
    }

    @Test
    //测试手机号码修改
    public void testsavetelephone()throws InterruptedException{
        Personal_data2 test= new Personal_data2();
        final String userdata="userdata";
        final String objectid="629373a90534fd5ba0adbc78";
        final String telephone="13926970513";
        test.saveTelephone(userdata,objectid,telephone);
    }

    @Test
    //测试id修改
    public void testsaveID() throws InterruptedException {
        Personal_data2 test= new Personal_data2();
        final String userdata="userdata";
        final String objectid="629373a90534fd5ba0adbc78";
        final String ID="test";
        test.saveId(userdata,objectid,ID);
    }

    @Test
    //测试性别修改
    public void testsaveGender() throws InterruptedException {
        Personal_data2 test= new Personal_data2();
        final String userdata="userdata";
        final String objectid="629373a90534fd5ba0adbc78";
        final String gender="男";
        test.saveGender(userdata,objectid,gender);
    }

    @Test
    //测试状态修改
    public void testsaveState() throws InterruptedException {
        Personal_data2 test= new Personal_data2();
        final String userdata="userdata";
        final String objectid="629373a90534fd5ba0adbc78";
        final String state="正在学习";
        test.saveState(userdata,objectid,state);
    }

    @Test
    //测试签名修改
    public void testsaveSign() throws InterruptedException {
        Personal_data2 test= new Personal_data2();
        String userdata="userdata";
        String objectid="629373a90534fd5ba0adbc78";
        String state="我爱学习";
        test.savesign(userdata,objectid,state);
    }

    @Test
    //测试经纬度修改
    public void testsavePlace() throws InterruptedException {
        Personal_data2 test= new Personal_data2();
        String objectid="629373a90534fd5ba0adbc78";
        double longitude=21;
        double latitude=21;
        test.saveplace(longitude,latitude,objectid);
    }

    @Test
    //测试浏览
    public void testsetbrowse() throws InterruptedException {
        Personal_data2 test= new Personal_data2();
        String objectid="629373a90534fd5ba0adbc78";
        test.setbrowse(objectid);
    }

    @Test
    //测试点赞修改
    public void testsetno() throws InterruptedException {
        Personal_data2 test= new Personal_data2();
        String objectid="629373a90534fd5ba0adbc78";
        String objectid1="test1";
        String objectid2="test2";
        test.setno(objectid1,objectid2,"yes");
    }

    @Test
    //测试取消点赞后总数修改
    public void testcancel() throws InterruptedException {
        Personal_data2 test= new Personal_data2();
        String objectid="629373a90534fd5ba0adbc78";
        test.cancel(objectid);
    }

    @Test
    //测试点赞后总数修改
    public void testthumbsup() throws InterruptedException {
        Personal_data2 test= new Personal_data2();
        String objectid="629373a90534fd5ba0adbc78";
        test.thumbsup(objectid);
    }


}

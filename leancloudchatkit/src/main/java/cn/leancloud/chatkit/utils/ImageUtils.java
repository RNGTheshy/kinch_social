package cn.leancloud.chatkit.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片工具类
 */
public class ImageUtils {

    /**
     * 将图片 bitmap保存到图库
     * @param activity 上下文
     * @param bitmap  图片
     */
    public static void saveBitmap(Context activity, Bitmap bitmap) {
        //因为xml用的是背景，所以这里也是获得背景
//获取参数Bitmap方式一： Bitmap bitmap=((BitmapDrawable)(imageView.getBackground())).getBitmap();
//获取参数Bitmap方式二： Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        //t设置图片名称，要保存png，这里后缀就是png，要保存jpg，后缀就用jpg
        String imageName = System.currentTimeMillis() + "code.png";

        //创建文件，安卓低版本的方式
        //  File file=new File(Environment.getExternalStorageDirectory() +"/test.png");

        //Android Q  10为每个应用程序提供了一个独立的在外部存储设备的存储沙箱，没有其他应用可以直接访问您应用的沙盒文件
        File f = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(f.getPath() + "/"+imageName);//创建文件
        //        file.getParentFile().mkdirs();
        try {
            //文件输出流
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            //压缩图片，如果要保存png，就用Bitmap.CompressFormat.PNG，要保存jpg就用Bitmap.CompressFormat.JPEG,质量是100%，表示不压缩
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            //写入，这里会卡顿，因为图片较大
            fileOutputStream.flush();
            //记得要关闭写入流
            fileOutputStream.close();
            //成功的提示，写入成功后，请在对应目录中找保存的图片
            Log.e("写入成功！位置目录", f.getPath() + "/"+imageName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //失败的提示，这里的Toast工具类，大家用自己项目中的即可，若不需要可以删除
            Toast.makeText(activity,"找不到文件",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            //失败的提示
            Toast.makeText(activity,"图片保存失败",Toast.LENGTH_SHORT).show();
        }
        // 下面的步骤必须有，不然在相册里找不到图片，若不需要让用户知道你保存了图片，可以不写下面的代码。
        // 把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                    file.getAbsolutePath(), imageName, null);
            Toast.makeText(activity,"成功保存到相册",Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            Toast.makeText(activity,"保存失败",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        // 最后通知图库更新
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(file.getPath()))));
    }
}

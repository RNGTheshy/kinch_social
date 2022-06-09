package cn.leancloud.chatkit.activity;



import static cn.leancloud.chatkit.utils.ImageUtils.saveBitmap;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.text.TextUtils;

import android.view.View;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.io.File;

import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.utils.LCIMConstants;


/**
 * 图片详情页，聊天时点击图片则会跳转到此页面
 */
public class LCIMImageActivity extends AppCompatActivity {

  private ImageView imageView;

  /**
   * 初始化组件和界面
   * @param savedInstanceState 保存的状态信息
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //初始化组件
    setContentView(R.layout.lcim_chat_image_brower_layout);
    imageView = (ImageView) findViewById(R.id.imageView);

    //获取图片地址数据
    Intent intent = getIntent();
    String path = intent.getStringExtra(LCIMConstants.IMAGE_LOCAL_PATH);
    String url = intent.getStringExtra(LCIMConstants.IMAGE_URL);

    //从网络URL或者本地路径加载图片
    if (TextUtils.isEmpty(path)) {
      Picasso.with(this).load(url).into(imageView);
    } else {
      Picasso.with(this).load(new File(path)).into(imageView);
    }

    //图片长按事件
    imageView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(imageView.getContext());

        builder.setItems(new String[]{"保存图片到相册"}, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            //取消选项框
            dialog.dismiss();
            //获取并保存到相册
            Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            saveBitmap(imageView.getContext(),image);
          }
        });

        //创建选项框并展示
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
      }
    });
  }

}

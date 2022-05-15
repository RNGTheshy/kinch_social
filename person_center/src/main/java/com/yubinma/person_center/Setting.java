package com.yubinma.person_center;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaoshan.data_center.dynamic.dynamic.BitmapUtils;

import java.nio.ByteBuffer;
import java.util.Calendar;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Setting extends AppCompatActivity {
    //生日部分
    LinearLayout tvDate;
    TextView etDate;
    String delete;
    String[] data;
    DatePickerDialog.OnDateSetListener setListener;
    String useremail = "";
    String telephonenumber = "";
    private ImageView img;
    ImageView iv_photo;
    final String classname = "userdata";
    String objid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_infor_layout);
        Toast.makeText(Setting.this,"修改成功",Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        final String objectid = intent.getStringExtra("objectid").toString();
        objid=objectid;
        TextView btcenter=findViewById(R.id.btcenter);
        btcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Setting.this,PersonCenter2Activity.class);
                startActivity(intent);
                finish();
            }
        });

        img = (ImageView) findViewById(R.id.usernam);


        Personal_data personal_data = new Personal_data();
        TextView username = findViewById(R.id.username);
        TextView usergender = findViewById(R.id.usergender);
        TextView showbirth = findViewById(R.id.showbirth);
        TextView userphone = findViewById(R.id.userphone);
        TextView userpassword1 = findViewById(R.id.userpassword);

        personal_data.getdata(classname, objectid, username, usergender, showbirth, userphone, userpassword1);

        telephonenumber = userphone.getText().toString();
        useremail = userpassword1.getText().toString();

        final LCQuery<LCObject> query;
        query = new LCQuery<>(classname);
        query.getInBackground(objectid).subscribe(new Observer<LCObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }
            @Override
            public void onNext(LCObject todo) {
                telephonenumber = todo.getString("phone_number");
                Toast.makeText(Setting.this, telephonenumber, Toast.LENGTH_SHORT).show();
                useremail = todo.getString("email");
            }
            @Override
            public void onError(Throwable throwable) {
            }
            @Override
            public void onComplete() {
            }
        });
        tvDate = findViewById(R.id.setbirth);
        etDate = findViewById(R.id.showbirth);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Setting.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month += 1;
                String date = year + "-" + month + "-" + day;
                Personal_data personal_data = new Personal_data();
                personal_data.saveBirth(classname, objectid, date);
                etDate.setText(date);
            }
        };


        LinearLayout rename = findViewById(R.id.rename);
        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent torename = new Intent(Setting.this, Rename.class);
                torename.putExtra("objectid", objectid);
                torename.putExtra("classname", classname);
                startActivity(torename);
            }
        });
        LinearLayout setsex = findViewById(R.id.setsex);
        setsex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tosetsex = new Intent(Setting.this, Setsex.class);
                tosetsex.putExtra("objectid", objectid);
                tosetsex.putExtra("classname", classname);
                startActivity(tosetsex);
                finish();
            }
        });
        LinearLayout cpassword = findViewById(R.id.cpassword);
        cpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tocpassword = new Intent(Setting.this, Cpassword.class);
                tocpassword.putExtra("email", useremail);
                tocpassword.putExtra("objectid", objectid);
                tocpassword.putExtra("classname", classname);
                startActivity(tocpassword);

            }
        });
        LinearLayout ctelephone = findViewById(R.id.ctelephone);
        ctelephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toctelephone = new Intent(Setting.this, Ctelephone1.class);
                toctelephone.putExtra("telephone", telephonenumber);
                toctelephone.putExtra("objectid", objectid);
                toctelephone.putExtra("classname", classname);
                startActivity(toctelephone);
            }
        });

        LinearLayout btn_1 = findViewById(R.id.cpicture);
        iv_photo = findViewById(R.id.usernam);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 意图
                Intent inten = new Intent(Intent.ACTION_PICK);
                // 类型
                inten.setType("image/*");
                // 跳转回传
                startActivityForResult(inten, 333);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 111:
                Bitmap bitmap = data.getParcelableExtra("data");
                img.setImageBitmap(bitmap);
                break;
            case 222:
                //路径
                Uri data1 = data.getData();
                img.setImageURI(data1);
                break;
            case 333:
                //得到路径
                Uri uri = data.getData();
                //裁剪
                Intent corp = corp(uri);
                //跳转到裁剪页面
                startActivityForResult(corp, 444);
                break;
            case 444:
                //位图
                Bitmap bitmap2 = (Bitmap) data.getExtras().get("data");
                //设置
//                saveBitmap(bitmap2);
                int bytes = bitmap2.getByteCount();
                ByteBuffer buf = ByteBuffer.allocate(bytes);
                bitmap2.copyPixelsToBuffer(buf);
                Headport headport=new Headport();


                headport.savepicture(objid, BitmapUtils.bmpToByteArray(bitmap2,false));



                img.setImageBitmap(bitmap2);
                break;
        }
    }


    private Intent corp(Uri uri) {
        // TODO Auto-generated method stub
        //裁剪的意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        //裁剪
        intent.putExtra("corp", true);
        //设置数据类型
        intent.setDataAndType(uri, "image/*");
        //设置裁剪框的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectX", 1);
        //设置裁剪后的比例
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        //设置裁剪后图片的格式
        intent.putExtra("outputFormat", "JPG");
        //返回数据
        intent.putExtra("return-data", true);
        return intent;
    }


}

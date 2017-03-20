package com.tx.lfclient.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imp.ToastShow;
import com.tx.lfclient.R;
import com.tx.lfclient.db.UserDbManager;
import com.tx.lfclient.entities.ReceiveTemplate;
import com.tx.lfclient.entities.User;
import com.tx.lfclient.event.MainEvent;
import com.tx.lfclient.url.UrlPath;
import com.tx.lfclient.utils.AlertUser;
import com.tx.lfclient.utils.DesHelper;
import com.tx.lfclient.utils.PictureNaming;
import com.tx.lfclient.utils.UIHandler;
import com.tx.lfclient.utils.inter.IAlertUser;
import com.tx.lfclient.utils.inter.IUIHandler;
import com.tx.lfclient.view.ActionSheetDialog;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/14
 */

@ContentView(R.layout.activity_account_settings)
public class AccountSettingActivity extends ImpActivity implements IUIHandler, IAlertUser {
    @ViewInject(R.id.account_setting_portrait)
    private ImageView portrait;
    @ViewInject(R.id.account_setting_nick)
    private TextView nick;
    @ViewInject(R.id.account_setting_phone)
    private TextView phone;
    @ViewInject(R.id.account_setting_location)
    private TextView location;

    private User user;

    private ImageOptions imageOptions;

    private ActionSheetDialog dialog;

    private ActionSheetDialog exit;

    private UIHandler<AccountSettingActivity> handler;

    private Bitmap bitmap;

    private UserDbManager userDbManager;


    private AlertUser<AccountSettingActivity> alertUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        init();
        initData();
        initView();
    }


    @Override
    void init() {
        imageOptions = myApplication.getImageOptions();
        handler = new UIHandler<>(this);
        userDbManager = UserDbManager.getInstance();
        alertUser = new AlertUser<>(this);
    }

    @Override
    void initData() {
        user = myApplication.getUser();
    }

    @Override
    void initView() {
        dialog = new ActionSheetDialog(this).builder()
                .setTitle("步入相册^o^")
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("从相册中选取", ActionSheetDialog.SheetItemColor.Red, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, 1);
                    }
                }).setAllItems();
        exit = new ActionSheetDialog(this).builder()
                .setTitle("注销后将退出登录")
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("注销", ActionSheetDialog.SheetItemColor.Red, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        myApplication.setLogin(false);
                        myApplication.finishActivity(AccountSettingActivity.class);
                    }
                }).setAllItems();
    }


    @Event(type = View.OnClickListener.class,
            value = {R.id.account_setting_portrait_title,
                    R.id.account_setting_nick_title,
                    R.id.account_setting_back,
                    R.id.account_setting_location_title,
                    R.id.account_setting_password_title,
                    R.id.account_setting_exit})
    private void onClick(View v) {

        switch (v.getId()) {
            case R.id.account_setting_portrait_title:
                dialog.show();
                break;
            case R.id.account_setting_nick_title:
                Intent intent = new Intent(this, AccountNickActivity.class);
                startActivity(intent);
                break;
            case R.id.account_setting_back:
                back();
                break;
            case R.id.account_setting_location_title:
                intent = new Intent(this, ProvinceActivity.class);
                String location = user.getLocation();
                if (location.contains("学")) {
                    intent.putExtra("kind", "school");
                } else {
                    intent.putExtra("kind", "society");
                }
                intent.putExtra("sortPage", "setting");
                startActivity(intent);
                break;
            case R.id.account_setting_password_title:
                intent = new Intent(this, AccountPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.account_setting_exit:
                exit.show();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            // 如果是调用相机拍照时
            case 2:

                File temp = new File(Environment.getExternalStorageDirectory() + "/icon_guest_count_small");
                if (data != null) {
                    startPhotoZoom(Uri.fromFile(temp));
                }
                break;
            // 取得裁剪后的图片
            case 3:
                if (data != null) {
                    setPicToView(data);
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }


    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        int left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            left = 0;
            top = 0;
            right = width;
            bottom = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            int clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect src = new Rect(left, top, right, bottom);
        Rect dst = new Rect(dst_left, dst_top, dst_right, dst_bottom);
        RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

        // 以下有两种方法画圆,drawRounRect和drawCircle
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        // canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output;
    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void setPicToView(Intent picData) {

        Bundle extras = picData.getExtras();

        if (extras != null) {

            Bitmap temp = extras.getParcelable("data");

            bitmap = toRoundBitmap(temp);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            // 将图片流以字符串形式存储下来
            byte[] portraitFile = stream.toByteArray();

            alertUser.post("1", "1", "1", portraitFile);

        }
    }


    @Override
    public void sendMessage(Message msg) {
        switch (msg.what) {
            case 0:
                ToastShow.showShort(this, "修改头像失败");
                break;
            case 1:
                ToastShow.showShort(this, "修改头像成功");
                portrait.setImageBitmap(bitmap);
                break;
            case 2:
                ToastShow.showShort(this, "请求失败");
                break;
            case 3:
                ToastShow.showShort(this, "账号异地登录，请重新登录");
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            back();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void back() {
        MainEvent event = new MainEvent();
        event.setSetting(true);
        event.setType("");
        event.setKind("");
        EventBus.getDefault().post(event);
        myApplication.finishActivity(getClass());
    }


    @Override
    public void onFailure(Call call, IOException e) {
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onResponse(Call call, Response response) throws Exception {
        String res = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();
        ReceiveTemplate template = objectMapper.readValue(res, ReceiveTemplate.class);
        String data = (String) template.getData();
        switch (data) {
            case "Incomplete":
                handler.sendEmptyMessage(2);
                break;
            case "Remote":
                handler.sendEmptyMessage(3);
                break;
            default:
                user.setPortrait(data);
                user.setToken(template.getToken());
                myApplication.setUser(user);
                userDbManager.saveOrUpdateUser(user);
                handler.sendEmptyMessage(1);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        x.image().bind(portrait, UrlPath.root + user.getPortrait(), imageOptions);
        nick.setText(user.getName());
        phone.setText(user.getPhoneNumber());
        location.setText(user.getLocation());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy(this);
    }
}

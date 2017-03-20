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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imp.ToastShow;
import com.tx.lfclient.R;
import com.tx.lfclient.entities.User;
import com.tx.lfclient.enumeration.DataType;
import com.tx.lfclient.url.UrlPath;
import com.tx.lfclient.utils.DesHelper;
import com.tx.lfclient.utils.EditTextWatcher;
import com.tx.lfclient.utils.PictureNaming;
import com.tx.lfclient.utils.UIHandler;
import com.tx.lfclient.utils.inter.IEditTextWatcher;
import com.tx.lfclient.utils.inter.IUIHandler;
import com.tx.lfclient.view.ActionSheetDialog;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/6
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends ImpActivity implements IUIHandler, IEditTextWatcher {

    @ViewInject(R.id.activity_register_portrait)
    private ImageView portrait;
    @ViewInject(R.id.activity_register_nickname)
    private EditText nick;
    @ViewInject(R.id.activity_register_PutInPassword)
    private EditText password;
    @ViewInject(R.id.activity_register_confirm)
    private EditText password_confirm;

    @ViewInject(R.id.activity_register_password_confirm)
    private ImageView register_password_confirm;


    private UIHandler<RegisterActivity> uiHandler;

    private EditTextWatcher<RegisterActivity> textWatcher;

    private String nickName;
    private String passwordText;
    private byte[] portraitFile;
    private String location;
    private String phoneNumber;

    private boolean isPortrait;//头像是否设置
    private boolean isPassword;//密码是否相同

    private ActionSheetDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        try {
            init();
            initData();
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    protected void init() {
        uiHandler = new UIHandler<>(this);
        textWatcher = new EditTextWatcher<>(this);
    }

    protected void initData() {
        User user = myApplication.getUser();
        location = user.getLocation();
        phoneNumber = user.getPhoneNumber();

    }

    protected void initView() {
        password_confirm.addTextChangedListener(textWatcher);
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
    }

    @Event(type = View.OnClickListener.class,
            value = {R.id.activity_register_back,
                    R.id.activity_register_portrait,
                    R.id.activity_register_submit})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_register_back:
                myApplication.finishActivity(getClass());
                break;
            case R.id.activity_register_portrait:
                dialog.show();
                break;
            case R.id.activity_register_submit:
                check();
                break;
        }
    }

    //检查输入的信息  头像，昵称，密码，等
    private void check() {
        nickName = nick.getText().toString().trim();
        passwordText = password.getText().toString().trim();
        switch (0) {
            case 0://头像
                if (!isPortrait) {
                    ToastShow.showShort(this, "请设置头像");
                    return;
                }
            case 1://昵称
                if (TextUtils.isEmpty(nickName)) {
                    ToastShow.showShort(this, "请设置昵称");
                    return;
                } else {
                    int len = nickName.length();
                    if (len > 10) {
                        ToastShow.showShort(this, "请设置昵称长度小于10！");
                        return;
                    }
                }

            case 2://密码
                if (TextUtils.isEmpty(passwordText)) {
                    ToastShow.showShort(this, "请设置密码");
                    return;
                } else {
                    int len = passwordText.length();
                    if (len < 7 || len > 17) {
                        ToastShow.showShort(this, "密码必须大于6位小于18位！");
                        return;
                    }
                }
            case 3://密码验证
                if (!isPassword) {
                    ToastShow.showShort(this, "两次密码输入不一致！");
                    return;
                }
            case 4:
                postRegister();

        }
    }


    @Override
    public void onTextChanged(CharSequence s) {

        String passwordConfirm = s.toString();
        if (passwordConfirm.equals(password.getText().toString().trim())) {
            register_password_confirm.setImageResource(R.mipmap.forget_password_true);
            isPassword = true;
        } else {
            register_password_confirm.setImageResource(R.mipmap.forget_password_confirm);
            isPassword = false;
        }
    }


    private void postRegister() {

        if (!myApplication.isNet()) {
            ToastShow.showShort(this, "无网络访问！！！");
            return;
        }
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("name", nickName)
                .addFormDataPart("location", location)
                .addFormDataPart("phoneNumber", DesHelper.getEncode(phoneNumber))
                .addFormDataPart("password", DesHelper.getEncode(passwordText))
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"portrait\";filename=\"" + PictureNaming.getNaming(DataType.PORTRAIT,1) + ".jpg\""),
                        RequestBody.create(MediaType.parse("image/png"), portraitFile));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Connection", "close")
                .url(UrlPath.registerPath)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                uiHandler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                User user = objectMapper.readValue(res, User.class);
                String password = user.getPassword();
                password = DesHelper.getDecode(password);
                password = DesHelper.getDecode(password);
                user.setPassword(password);
                myApplication.setUser(user);
                uiHandler.sendEmptyMessage(1);
            }
        });
    }

    @Override
    public void sendMessage(Message msg) {

        switch (msg.what) {
            case 0:
                ToastShow.showShort(this, "注册失败");
                break;
            case 1:
                ToastShow.showShort(this, "注册成功");
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                myApplication.finishActivity(LoginActivity.class
                        , RegisterLocationActivity.class
                        , ProvinceActivity.class
                        , LocationActivity.class
                        , SmsActivity.class
                        , IdentifyCodeActivity.class
                        , RegisterActivity.class
                );
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

    /**
     * 保存裁剪之后的图片数据
     */
    private void setPicToView(Intent picData) {

        Bundle extras = picData.getExtras();

        if (extras != null) {

            Bitmap bitmap = extras.getParcelable("data");

            Bitmap bitmap2 = toRoundBitmap(bitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream);
            // 将图片流以字符串形式存储下来
            portraitFile = stream.toByteArray();
            portrait.setImageBitmap(bitmap2);
            isPortrait = true;
        }
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy(this);
    }
}

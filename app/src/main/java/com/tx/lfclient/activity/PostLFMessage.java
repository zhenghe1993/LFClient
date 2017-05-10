package com.tx.lfclient.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.imp.ToastShow;
import com.tx.lfclient.R;
import com.tx.lfclient.adapter.NineGridAdapter;
import com.tx.lfclient.adapter.PostTypeAdapter;
import com.tx.lfclient.custom.NineBitMap;
import com.tx.lfclient.entities.LFMessage;
import com.tx.lfclient.entities.Pane;
import com.tx.lfclient.enumeration.DataType;
import com.tx.lfclient.enumeration.ValidatorType;
import com.tx.lfclient.event.LocationEvent;
import com.tx.lfclient.event.OnActivityResult;
import com.tx.lfclient.time.TimePicker;
import com.tx.lfclient.url.UrlPath;
import com.tx.lfclient.utils.DateFormatUtils;
import com.tx.lfclient.utils.DownLoadImageToLocal;
import com.tx.lfclient.utils.EditTextWatcher;
import com.tx.lfclient.utils.PictureNaming;
import com.tx.lfclient.utils.UIHandler;
import com.tx.lfclient.utils.ValidatorUtils;
import com.tx.lfclient.utils.inter.IEditTextWatcher;
import com.tx.lfclient.utils.inter.IUIHandler;
import com.tx.lfclient.view.ActionSheetDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 * Date is 2017/2/4
 * <p>
 * 失物招领页面   判断
 */
@ContentView(R.layout.activity_post_lfmessage)
public class PostLFMessage extends ImpActivity implements IUIHandler, IEditTextWatcher {


    @ViewInject(R.id.LFMessage_name)
    private EditText edit_name;
    @ViewInject(R.id.LFMessage_time)
    private EditText edit_time;
    @ViewInject(R.id.LFMessage_location)
    private EditText edit_location;
    @ViewInject(R.id.LFMessage_phone)
    private EditText edit_phone;
    @ViewInject(R.id.LFMessage_introduction)
    private EditText edit_introduction;
    @ViewInject(R.id.LFMessage_title)
    private TextView text_title;
    @ViewInject(R.id.LFMessage_time_title)
    private TextView text_time_title;
    @ViewInject(R.id.LFMessage_locate_title)
    private TextView text_location_title;
    @ViewInject(R.id.LFMessage_type_title)
    private TextView text_type_title;
    @ViewInject(R.id.LFMessage_introduction_title)
    private TextView text_introduction_title;
    @ViewInject(R.id.LFMessage_num)
    private TextView text_LFMessage_num;
    @ViewInject(R.id.LFMessage_grid_view)
    private GridView types;
    @ViewInject(R.id.LFMessage_pictures)
    private GridView pictures;

    private ActionSheetDialog dialog;

    private String type;

    private UIHandler<PostLFMessage> handler;

    private List<Pane> list;

    private PostTypeAdapter typeAdapter;

    private LFMessage message;

    private NineBitMap nineBitMap;

    private NineGridAdapter nineGridAdapter;

    private EditTextWatcher<PostLFMessage> textWatcher;

    private Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        EventBus.getDefault().register(this);
        try {
            init();
            initData();
            initView();
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }


    }


    @Override
    void init() {

        list = new ArrayList<>();
        nineBitMap = NineBitMap.getInstance(this);
        message = new LFMessage();
        typeAdapter = new PostTypeAdapter(list, this);
        nineGridAdapter = new NineGridAdapter(nineBitMap, this);
        types.setAdapter(typeAdapter);
        pictures.setAdapter(nineGridAdapter);
        textWatcher = new EditTextWatcher<>(this);
        edit_introduction.addTextChangedListener(textWatcher);
        dialog = new ActionSheetDialog(this).builder()
                .setTitle("步入相册^o^")
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {

                                try {

                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                    File file = DownLoadImageToLocal.getTempFile();

                                    uri = Uri.fromFile(file);

                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                                    startActivityForResult(intent, 2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        })
                .addSheetItem("从相册中选取", ActionSheetDialog.SheetItemColor.Red, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, 1);
                    }
                }).setAllItems();

    }

    @Override
    void initData() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        message.setType(type);
        TypedArray ar = getResources().obtainTypedArray(R.array.category_image_ch);
        String[] titles = getResources().getStringArray(R.array.category_title);
        int len = ar.length();
        int[] images = new int[len];
        for (int i = 0; i < len; i++) {
            images[i] = ar.getResourceId(i, 0);
        }
        ar.recycle();
        for (int i = 0; i < titles.length; i++) {
            if (titles[i].equals("找人") && type.equals("FOUND")) {
                continue;
            } else if (titles[i].equals("所有")) {
                continue;
            }
            list.add(new Pane(titles[i], images[i]));
        }
    }

    @Override
    void initView() {
        if (type.equals("LOST")) {
            text_title.setText("发布失物信息");
            text_time_title.setText(getString(R.string.LostTime));
            edit_time.setHint("请选择丢失时间...");
            text_location_title.setText(getString(R.string.LostPlace));
            edit_location.setHint("请标注丢失地点...");
            text_type_title.setText(getString(R.string.choose_lost_type));
            text_introduction_title.setText("失物简介：");
        } else {
            text_title.setText("发布招领信息");
            text_time_title.setText(getString(R.string.FoundTime));
            edit_time.setHint("请选择拾物时间...");
            text_location_title.setText(getString(R.string.FoundPlace));
            edit_location.setHint("请标注拾物地点...");
            text_type_title.setText(getString(R.string.choose_found_type));
            text_introduction_title.setText("拾物简介：");
        }

    }


    @Event(type = AdapterView.OnItemClickListener.class, value = {R.id.LFMessage_grid_view, R.id.LFMessage_pictures})
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.LFMessage_grid_view:
                LogUtil.i("LFMessage_grid_view");
                message.setKind(list.get(position).getTitle());
                typeAdapter.notifyDataChange(position);
                break;
            case R.id.LFMessage_pictures:
                LogUtil.i("LFMessage_pictures");

                /*
                 * click the adding picture ,touch the function which is add
                 * click the choose picture ,touch the function which is turn to the other view
                 */
                int count = nineBitMap.size();

                if (count != 4 && position + 1 == count) {
                    dialog.show();
                } else {
                    Intent intent = new Intent(this, DisplayPictureActivity.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }

                break;
        }

    }


    @Event(type = View.OnClickListener.class, value = {
            R.id.LFMessage_back,
            R.id.LFMessage_submit,
            R.id.LFMessage_top_submit,
            R.id.LFMessage_locate_Button,
            R.id.LFMessage_time_Button})
    private void onClick(View v) {


        switch (v.getId()) {
            case R.id.LFMessage_back:
                myApplication.finishActivity(PostLFMessage.class);
                break;
            case R.id.LFMessage_submit:
            case R.id.LFMessage_top_submit:

                //表单验证

                boolean flag = false;
                try {
                    flag = validator();


                if (flag) {
                    sendLFMessage();
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.LFMessage_locate_Button:
                Intent intent = new Intent(this, MapLocationActivity.class);
                startActivity(intent);
                break;
            case R.id.LFMessage_time_Button:
                final TimePicker picker = new TimePicker(this);
                String time = edit_time.getText().toString().trim();
                if (!TextUtils.isEmpty(time)) {
                    picker.setCurrentTime(time);
                }
                new AlertDialog.Builder(this)
                        .setTitle("选择时间")
                        .setIcon(R.drawable.time_choose_orange_gray)
                        .setView(picker.getView())
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        edit_time.setText(picker.getTime());
                                    }
                                })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();
                break;

        }


    }

    private boolean validator() throws Exception{
        String title = edit_name.getText().toString().trim();
        String thingTime = edit_time.getText().toString().trim();
        String phone = edit_phone.getText().toString().trim();
        String detail = edit_introduction.getText().toString().trim();
        String location=edit_location.getText().toString().trim();
        message.setTitle(title);
        message.setThingTime(new DateFormatUtils("yyyy-MM-dd HH:mm:ss").stringToDate(thingTime));
        message.setPhoneNumber(phone);
        message.setDetail(detail);

        switch (0) {
            case 0:
                ValidatorType type = ValidatorUtils.isValidator(title, null, 1, 40);
                if (type != ValidatorType.TRUE) {
                    ValidatorUtils.showToast(this, type, "标题");
                    return false;
                }
            case 1:
                type = ValidatorUtils.isValidator(thingTime, null, 1, 40);
                if (type != ValidatorType.TRUE) {
                    ValidatorUtils.showToast(this, type, "时间");
                    return false;
                }
            case 2:
                type = ValidatorUtils.isValidator(location, null, 1, Integer.MAX_VALUE);
                if (type != ValidatorType.TRUE) {
                    ValidatorUtils.showToast(this, type, "地址");
                    return false;
                }
            case 4:
                type = ValidatorUtils.isValidator(phone + "", "[0-9]{11}", 11, 11);
                if (type != ValidatorType.TRUE) {
                    ValidatorUtils.showToast(this, type, "手机号");
                    return false;
                }
            case 5:
                type = ValidatorUtils.isValidator(message.getKind() + "", null, 1, 11);
                if (type != ValidatorType.TRUE) {
                    ValidatorUtils.showToast(this, type, "实物类型");
                    return false;
                }
            case 6:
                type = ValidatorUtils.isValidator(detail, null, 1, 222);
                if (type != ValidatorType.TRUE) {
                    ValidatorUtils.showToast(this, type, "简介");
                    return false;
                }
        }


        return true;
    }


    @Override
    public void sendMessage(Message msg) {


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LocationEvent event) {
        edit_location.setText(event.getLocationName());
        message.setLatitude(event.getLatitude());
        message.setLongitude(event.getLongitude());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnActivityResult onActivityResult) {
        if (onActivityResult.isTrue()) {
            nineBitMap = NineBitMap.getInstance(this);
            nineGridAdapter.reFleshData(nineBitMap);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy(this);
        EventBus.getDefault().unregister(this);
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
                startPhotoZoom(uri);
                break;
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
        intent.putExtra("aspectX", 9998);
        intent.putExtra("aspectY", 9999);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);

    }

    private void setPicToView(Intent picData) {

        Bundle extras = picData.getExtras();
        LogUtil.w("extras ==" + extras);
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            // 将图片流以字符串形式存储下来
            LogUtil.w("bitmap ==" + bitmap);
            nineBitMap.addBitmap(bitmap);
            nineGridAdapter.reFleshData(nineBitMap);
        }
    }

    @Override
    public void onTextChanged(CharSequence s) {
        String temp = s.toString();

        int len = temp.length();

        if (len > 222) {
            temp = temp.substring(0, 221);
            edit_introduction.setText(temp);
        } else {
            text_LFMessage_num.setText(String.valueOf(len));
        }


    }

    private void sendLFMessage() {

        if (!myApplication.isNet()) {
            ToastShow.showShort(this, "无网络访问！！！");
            return;
        }
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("title", message.getTitle())
                .addFormDataPart("thingTime", new DateFormatUtils("yyyy-MM-dd HH:mm:ss").dateToString(message.getThingTime()))
                .addFormDataPart("createTime", new DateFormatUtils("yyyy-MM-dd HH:mm:ss").dateToString(new Date()))
                .addFormDataPart("latitude", message.getLatitude() + "")
                .addFormDataPart("longitude", message.getLongitude() + "")
                .addFormDataPart("phoneNumber", message.getPhoneNumber())
                .addFormDataPart("kind", message.getKind())
                .addFormDataPart("type", message.getType())
                .addFormDataPart("detail", message.getDetail())
                .addFormDataPart("shareCount","0")
                .addFormDataPart("commentsCount", "0")
                .addFormDataPart("attentionCount", "0");

        for (int i = 1; i <= nineBitMap.size() - 1; i++) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"image\";filename=\"" + PictureNaming.getNaming(DataType.PORTRAIT, i) + ".jpg\""),
                    RequestBody.create(MediaType.parse("image/png"), nineBitMap.getByteImage(i)));
        }

        builder.addFormDataPart("token", myApplication.getUser().getToken());
        builder.addFormDataPart("userId", myApplication.getUser().getId() + "");

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Connection", "close")
                .url(UrlPath.addLFMessagePath)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();




                handler.sendEmptyMessage(1);
            }
        });
    }
}

package com.tx.lfclient.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.tx.lfclient.R;
import com.tx.lfclient.adapter.NineGridAdapter;
import com.tx.lfclient.adapter.PostTypeAdapter;
import com.tx.lfclient.custom.NineBitMap;
import com.tx.lfclient.entities.LFMessage;
import com.tx.lfclient.entities.Pane;
import com.tx.lfclient.event.LocationEvent;
import com.tx.lfclient.event.MainEvent;
import com.tx.lfclient.event.OnActivityResult;
import com.tx.lfclient.time.TimePicker;
import com.tx.lfclient.utils.AlertUser;
import com.tx.lfclient.utils.DownLoadImageToLocal;
import com.tx.lfclient.utils.UIHandler;
import com.tx.lfclient.utils.inter.IAlertUser;
import com.tx.lfclient.utils.inter.IUIHandler;
import com.tx.lfclient.view.ActionSheetDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/4
 * <p>
 * 失物招领页面   判断
 */
@ContentView(R.layout.activity_post_lfmessage)
public class PostLFMessage extends ImpActivity implements IUIHandler {


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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        EventBus.getDefault().register(this);
        try {
            init();
            initData();
            initView();
        } catch (Exception e) {
            e.printStackTrace();
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
        dialog = new ActionSheetDialog(this).builder()
                .setTitle("步入相册^o^")
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {

                                try{
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    File file= DownLoadImageToLocal.getTempFile();
                                    Uri uri = Uri.fromFile(file);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                    startActivityForResult(intent, 2);
                                }catch (Exception e){
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
            R.id.LFMessage_back, R.id.LFMessage_submit, R.id.LFMessage_top_submit,
            R.id.LFMessage_locate_Button, R.id.LFMessage_time_Button})
    private void onClick(View v) {

        switch (v.getId()) {
            case R.id.LFMessage_back:
                myApplication.finishActivity(PostLFMessage.class);
                break;
            case R.id.LFMessage_submit:
            case R.id.LFMessage_top_submit:

                //表单验证


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


    @Override
    public void sendMessage(Message msg) {


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LocationEvent event) {
        edit_location.setText(event.getLocationName());
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
            case 3:
                if (data != null) {
                    try{
                        setPicToView(data);
                    }catch (Exception e){
                        e.printStackTrace();
                    }



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
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Intent picData) {

        Bundle extras = picData.getExtras();

        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            // 将图片流以字符串形式存储下来
            nineBitMap.addBitmap(bitmap);
            nineGridAdapter.reFleshData(nineBitMap);
        }
    }

    public static boolean hasSDcard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }


}

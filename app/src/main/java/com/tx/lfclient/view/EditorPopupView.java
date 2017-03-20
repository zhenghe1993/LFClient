package com.tx.lfclient.view;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;


import com.tx.lfclient.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class EditorPopupView extends PopupWindow {
    @ViewInject(R.id.lost_information)
    private Button postLostMessage;
    @ViewInject(R.id.found_information)
    private Button postFoundMessage;
    @ViewInject(R.id.discover_information)
    private Button postDiscoverMessage;

    private View mMenuView;

    public EditorPopupView(final Activity context, OnClickListener itemsOnClick){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         mMenuView = inflater.inflate(R.layout.editor_popup_view, null);
        // 设置SelectPicPopupWindow的View
        setContentView(mMenuView);
        x.view().inject(this,mMenuView);
        postLostMessage.setOnClickListener(itemsOnClick);
        postFoundMessage.setOnClickListener(itemsOnClick);
        postDiscoverMessage.setOnClickListener(itemsOnClick);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//		mMenuView.setOnTouchListener(new View.OnTouchListener() {
//
//			public boolean onTouch(View v, MotionEvent event) {
//                L.e(context,"-----------------");
//				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
//				int y = (int) event.getY();
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					if (y < height) {
//						dismiss();
//                        L.e(context,"===+++===");
//						Handler handler = new Handler();
//						handler.postDelayed(new Runnable() {
//							@Override
//							public void run() {
//                                L.e(context,"======");
//                                context.finish();
//							}
//						}, 50);
//
//					}
//				}
//				return true;
//			}
//		});

    }

}
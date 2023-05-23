package com.example.controlyy;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.graphics.Rect;
import android.icu.text.SimpleDateFormat;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.webkit.WebView;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class AccessibilityService extends android.accessibilityservice.AccessibilityService {
    private String xueXiName = "cn.xuexi.android";
    private String ContrlyyName = "com.example.controlyy";

    private int progress = 0; // 自动化进度

    private SharedPreferences sharedPref;
    private String todaydef = "2023-05-20";
    private String today;

    private int videoNumDef = -1;
    private int videoNum;


    private int articleNumDef = -1;

    private int articleNum;


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todaydate = dateFormat.format(date);

        sharedPref = MyApplication.getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        today = sharedPref.getString("today", todaydef);
        videoNum = sharedPref.getInt("videoNum", videoNumDef);
        articleNum = sharedPref.getInt("articleNum", articleNumDef);


        //当初次运行时，没有data.xml时，创建这个文件并写入对应的数值
        //或者data.xml里的数据不是当天数据时，重新写入默认的数据
        if (!(today.equals(todaydate))) {

            today = todaydate;
            videoNum = 0;
            articleNum = 0;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("today", today);
            editor.putInt("videoNum", videoNum);
            editor.putInt("articleNum", articleNum);
            editor.commit();
        }


    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = event.getPackageName() == null ? "" : event.getPackageName().toString();
        // 如果活动APP是目标APP(controyy)则自动打开xuexi
        Log.d("packageName", packageName);

        if (packageName.equals(ContrlyyName)) {
            Log.d("packageName", packageName);

            Intent xuexi = getPackageManager().getLaunchIntentForPackage(xueXiName);
            if (xuexi != null) {
                try {
                    xuexi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(xuexi);
                } catch (Exception e) {
                }
            }
        }

        if (!packageName.equals(xueXiName)) {
            return;
        }
        int eventType = event.getEventType();


        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:// 捕获窗口内容改变事件
                if (packageName.equals(xueXiName)) {
                    controlYY();
                }
                break;
            default:
                break;
        }
    }

    private void controlYY() {
        // progress自动化进度，防止顺序出错
        sleep(5, 3);
        if (progress == 0) {
            AccessibilityNodeInfo nodeOne = findViewByText("百灵");
            if (nodeOne != null) {
                performViewClick(nodeOne);
                sleep(1, 2);
                performViewClick(nodeOne);
                sleep(1, 2);
                progress++;
            }
        }

        // 步骤2：点击最上面的一个视频
        if (progress == 1) {
            // 用于获取控件的矩形信息：坐标位置和宽高
            Rect rect = new Rect();

            rect.left = 0;
            rect.right = 1080;
            rect.top = 400;
            rect.bottom = 1007;


            int moveToX = (rect.left + rect.right) / 2;
            int moveToY = (rect.top + rect.bottom) / 2;
            int lineToX = (rect.left + rect.right) / 2;
            int lineToY = (rect.top + rect.bottom) / 2;

            // 有些View是不能点击，这时候可以用手势来处理
            gesture(moveToX, moveToY, lineToX, lineToY, 100L, 400L);
            progress++;
            sleep(3, 3);
        }

        //step3:看10个视频
        if (progress == 2) {
            Log.d("packageName", String.valueOf(videoNum));

            if (videoNum < 7) {
                DownSnip(7 - videoNum);
            }
            progress++;
        }

        //step4:回到主页
        if (progress == 3) {
            gesture(800, 800, 1000, 800, 0, 500);
            sleep(1, 3);
            progress++;
        }

        // 步骤5：点击学习
        if (progress == 4) {
            // 有些view是没有text的，就可以通过ID、类名等属性来获取
            AccessibilityNodeInfo nodeAdd = findViewByID("cn.xuexi.android:id/home_bottom_tab_button_work");
//            nodeAdd.getText()
            if (nodeAdd != null) {
                performViewClick(nodeAdd);
                sleep(1, 3);
                performViewClick(nodeAdd);
                sleep(1, 3);
                progress++;

            }
        }

        if (progress == 5) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String sim = dateFormat.format(date);

            for (int i = 0; i <= 6; i++) {
                List<AccessibilityNodeInfo> nodeInfoList = findViewsByText(sim, true);
                if (nodeInfoList != null) {
                    if (i >= articleNum) {
                        clicknodeInfoList(nodeInfoList);
                        articleNum++;
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("articleNum", articleNum);
                        editor.commit();
                    }
                    sleep(1, 3);
                    gesture(800, 2000, 800, 500, 0, 500);
                    sleep(1, 3);
                } else {
                    gesture(800, 2000, 800, 500, 0, 500);
                    sleep(1, 3);
                }

            }

            progress++;
        }

        if (progress == 6) {
            AccessibilityNodeInfo nodeAdd = findViewByID("cn.xuexi.android:id/comm_head_xuexi_score");
            if (nodeAdd != null) {
                performViewClick(nodeAdd);
            }
            progress++;
        }

    }

    public List<AccessibilityNodeInfo> findViewsByText(String text, boolean clickable) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            return nodeInfoList;
        }
        return null;
    }

    public void clicknodeInfoList(List<AccessibilityNodeInfo> nodeInfoList) {
        for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
            performViewClick(nodeInfo);
            sleep(2, 3);
            for (int i = 1; i <= 3; i++) {
                sleep(6, 3);
                gesture(800, 800, 800, 700, 0, 500);
                sleep(6, 3);
                gesture(800, 800, 800, 900, 0, 500);
                sleep(7, 3);
            }
            gesture(800, 800, 1000, 800, 0, 500);
            sleep(1, 3);
        }
    }

    public void DownSnip(int n) {
        for (int i = 1; i <= n; i++) {
            int moveToX = 800;
            int moveToY = 800;
            int lineToX = 800;
            int lineToY = 600;

            // 有些View是不能点击，这时候可以用手势来处理
            gesture(moveToX, moveToY, lineToX, lineToY, 0, 500);
            sleep(60, 10);
            videoNum++;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("videoNum", videoNum);
            editor.commit();
        }

    }

    public AccessibilityNodeInfo findViewByID(String id) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }


    /**
     * 查找对应文本的View，无论该node能不能点击
     *
     * @param text text
     * @return View
     */
    public AccessibilityNodeInfo findViewByText(String text) {
        AccessibilityNodeInfo viewByText = findViewByText(text, true);
        if (viewByText == null) {
            viewByText = findViewByText(text, false);
        }
        return viewByText;
    }

    /**
     * 查找对应文本的View
     *
     * @param text      text
     * @param clickable 该View是否可以点击
     * @return View
     */
    public AccessibilityNodeInfo findViewByText(String text, boolean clickable) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return null;
        }

        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null && (nodeInfo.isClickable() == clickable)) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }


    /**
     * 模拟点击事件,如果该node不能点击，则点击父node，将点击事件一直向父级传递，直至到根node或者找到一个可以点击的node
     *
     * @param nodeInfo nodeInfo
     */
    public void performViewClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        while (nodeInfo != null) {
            if (nodeInfo.isClickable()) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
            nodeInfo = nodeInfo.getParent();
        }
    }

    protected void sleep(long s, int bound) {
        Random random = new Random();
        long rd = random.nextInt(bound);
        try {
            Thread.sleep((s + rd) * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 手势操作，因为path不能小于0，因此小于则直接返回，不操作，另外如果有需求，可以自行修改小于则设置为0或者屏幕的宽高
     *
     * @param moveToX
     * @param moveToY
     * @param lineToX
     * @param lineToY
     * @param startTime
     * @param duration
     */
    public void gesture(int moveToX, int moveToY, int lineToX, int lineToY, long startTime, long duration) {

        if (moveToX < 0 || moveToY < 0 || lineToX < 0 || lineToY < 0) {
            return;
        }

        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(moveToX, moveToY);
        path.lineTo(lineToX, lineToY);
        GestureDescription gestureDescription = builder
                .addStroke(new GestureDescription.StrokeDescription(path, startTime, duration, false))
                .build();
        dispatchGesture(gestureDescription, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
            }
        }, new Handler(Looper.getMainLooper()));
    }


    @Override
    public void onInterrupt() {
    }
}
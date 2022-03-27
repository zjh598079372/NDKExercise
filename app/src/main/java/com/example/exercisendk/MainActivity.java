package com.example.exercisendk;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.MessageQueue;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;



import com.huawei.hms.hmsscankit.RemoteView;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.huawei.hms.scankit.drawable.ScanDrawable;
import com.me.support.app.AppManager;
import com.me.support.datacache.AppDataCache;
import com.me.support.recyclerView.CommonRecyclerViewAdapter;
import com.me.support.recyclerView.MyViewHolder;
import com.me.support.utils.AppUtil;
import com.me.support.utils.DisplayUtil;
import com.me.support.utils.LogUtil;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.

    static {
        System.loadLibrary("native-lib");

    }

    private TextView show_text;
    private ImageView imageView;
    private final int SCAN_FRAME_SIZE = 300;
    private ScanDrawable mScanDrawable;
    private List<String> itemLists = new ArrayList<>();
    private static int number = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        try {
//            Resources superRes = getResources();
//            // 创建AssetManager，但是不能直接new所以只能通过反射
//            AssetManager assetManager = AssetManager.class.newInstance();
//            // 反射获取addAssetPath方法
//            Method addAssetPathMethod = AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
//            // 皮肤包的路径:  本地sdcard/plugin.skin
//            String skinPath = Environment.getExternalStorageDirectory().getAbsoluteFile()+ File.separator+"plugin.skin";
//            // 反射调用addAssetPath方法
//            addAssetPathMethod.invoke(assetManager, skinPath);
//            // 创建皮肤的Resources对象
//            Resources skinResources = new Resources(assetManager,superRes.getDisplayMetrics(),superRes.getConfiguration());
//            // 通过资源名称,类型，包名获取Id
//            int bgId = skinResources.getIdentifier("main_bg","drawable","com.hc.skin");
//            Drawable bgDrawable = skinResources.getDrawable(bgId);
//            // 设置背景
//            findViewById(R.id.activity_main).setBackgroundDrawable(bgDrawable);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        Resources resources = getResources();
//        getResources().getString(R.string.app_name);


//        UserBean userBean = new UserBean();
//        userBean.setName("张一");
//        userBean.setAge(22);
//        UserBean userBean1 = new UserBean();
//        userBean1.setName("张二");
//        userBean1.setAge(23);
//        UserBean userBean2 = new UserBean();
//        userBean2.setName("张三");
//        userBean2.setAge(24);
//        AppDataCache.withDb().removeAllInfo(UserBean.class);
//        AppDataCache.withDb().addDbInfo(userBean);
//        AppDataCache.withDb().addDbInfo(userBean1);
//        AppDataCache.withDb().addDbInfo(userBean2);
//        AppDataCache.withDb().updataInfo(userBean1,1);
//        AppDataCache.withDb().updataAllInfo(userBean1,"age > ?","22");
//        AppDataCache.withDb().updataAllInfo(userBean1,"age > ? and name = ?","22","张三");
////        String name = AppDataCache.withDb().getInfo(UserBean.class,1).getName();
//        AppDataCache.withDb().removeAllInfo(UserBean.class,"age > ?","22");
//        AppDataCache.withDb().removeAllInfo(UserBean.class,"age > ? and name = ?","22","张三");
//        List<UserBean> userBeans = AppDataCache.withDb().getAllInfo(UserBean.class);
//        UserBean userBean3 = AppDataCache.withDb().getFirstInfo(UserBean.class);
//
//        LogUtil.e("userBeans.size()-->"+userBeans.size());
//        for (int i = 0; i < userBeans.size(); i++) {
//            LogUtil.e("userBeans.name()-->"+userBeans.get(i).getName());
//        }




//        try {
//            AssetManager assetManager = AssetManager.class.newInstance();
//            Method addAssetPathMethod = AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
//            String skinPath = getExternalFilesDir("").getAbsolutePath()+File.separator+"skin_plugin.apk";
//            addAssetPathMethod.invoke(assetManager,skinPath);
//            Resources skinResources = new Resources(assetManager,resources.getDisplayMetrics(),resources.getConfiguration());
//            int bgId = skinResources.getIdentifier("launch_bg","drawable",getPackageName());
//            Drawable drawable = skinResources.getDrawable(bgId);
//            findViewById(R.id.activity_main).setBackgroundDrawable(drawable);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        TextView tv = findViewById(R.id.sample_text);
        show_text = findViewById(R.id.show_text);
        imageView = findViewById(R.id.imageView);
        tv.setText("stringFromJNI()");
        show_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,BitmapActivity.class));
            }
        });

        //1、
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int mScreenWidth = displayMetrics.widthPixels;
        int mScreenHeight = displayMetrics.heightPixels;
        float density = displayMetrics.density;

        int scanFrameSize = (int) (SCAN_FRAME_SIZE * density);

        final Rect rect = new Rect();

        rect.left = (mScreenWidth / 2 - scanFrameSize / 2);
        rect.right = (mScreenWidth / 2 + scanFrameSize / 2);
        rect.top = (mScreenHeight / 2 - scanFrameSize / 2);
        rect.bottom = (mScreenHeight / 2 + scanFrameSize / 2);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.CAMERA, new CheckRequestPermissionListener() {
//                    @Override
//                    public void onPermissionOk(Permission permission) {
////                        HmsScanAnalyzerOptions hmsScanAnalyzerOptions = new HmsScanAnalyzerOptions.Creator().setPhotoMode(true).setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create();
////                        ScanUtil.startScan(
////                                MainActivity.this, 200,
////                                hmsScanAnalyzerOptions);
//
//                        startActivityForResult(new Intent(MainActivity.this,ScanActivity.class),200);
////                        ////////////////////////////////////////////////////////////////////////////////
////                        mScanDrawable = new ScanDrawable(getResources());
////                        mScanDrawable.setBounds(rect);
////                        imageView.setBackground(mScanDrawable);
////                        mScanDrawable.start();
////                        stringFromJNI();
////
////                        BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
////                        Vector<Integer> vector = new Vector<>();
////                        ExecutorUtil.submitRunnable(new Runnable() {
////                            @Override
////                            public void run() {
////                                Log.e("TAG","执行完毕！");
////                            }
////                        });
//
//
////                        RemoteView b ;
////                        b.selectPictureFromLocalFile();
////                        Intent intent = new Intent(MainActivity.this, MyService.class);
////                        startService(intent);
//
////                        Type[] type = MyTest.class.getGenericInterfaces();
////
////                        if(type.length > 0){
////                            Log.e("type-->",type[0].toString());
////                        }else {
////                            Log.e("type-->","null");
////                        }
//
//                        MyTest<Worker> myTest = new MyTest<Worker>();
//                        Type clazz = myTest.getClass().getGenericSuperclass();
//                        Type[] types = ((ParameterizedType)clazz).getActualTypeArguments();
//
////                        if(types != null){
////                            Log.e("types-->",types[0].getTypeName());
////                        }else {
////                            Log.e("types-->","null");
////                        }
//
//
//                    }
//
//                    @Override
//                    public void onPermissionDenied(Permission permission) {
//
//                    }
//                });

//                stringFromJNI();
//                number++;
//                HashSet hashSet = new HashSet();
//                hashSet.add(12);
//                ArrayList<String> arrayList = new ArrayList<>();
//                arrayList.add("10");
//                arrayList.add("20");
//                arrayList.add("20");
//                arrayList.add("30");
//                Iterator iterator = arrayList.iterator();
//                while (iterator.hasNext()){
//                    String value = (String) iterator.next();
//                    LogUtil.e("number-->value=256="+value);
//                    if(value.equals("20")){
//                        iterator.remove();
//                        LogUtil.e("number-->value=="+value);
//                    }
//                }
//                LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<>();
//                linkedHashMap.put("1","10");
//                linkedHashMap.put("2","20");

//                LinkedList linkedList = new LinkedList();
//                linkedList.add(12);
//                linkedList.add(3,45);
//                CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
//                copyOnWriteArrayList.add(13);

//                LogUtil.e("number-->"+arrayList.size());
                startActivity(new Intent(MainActivity.this,SecondActivity.class));

            }
        });
//        Worker oldWorkerArr[] = new Worker[3];
//        oldWorkerArr[0] = new Worker("张三",20 ,20000);
//        oldWorkerArr[1] = new Worker("李四",30 ,30000);
//        oldWorkerArr[2] = new Worker("王五",40 ,40000);
//        Worker newWorkerArr[] = new Worker[3];
//        arraycopy(oldWorkerArr,0,newWorkerArr,0,3);
//        Log.e("Main-->",newWorkerArr[2].getName());
//        LinkedList<Integer> linkedList = new LinkedList();
//        linkedList.add(10);
//        linkedList.remove();
//        List<Integer> arryList = new ArrayList<Integer> ();
//        arryList.add(10);
//        arryList.add(20);
//        arryList.add(-10);
//        Integer a = new Integer(10);
////        Collections.sort(arryList);
//        arryList.sort(new MyComparator<Integer>());
//        Handler handler = new Handler();
////arryList
//        for (int i = 0; i<arryList.size();i++){
//            int value = (int) arryList.get(i);
//            Log.e("Main-->",""+value);
//        }



        findViewById(R.id.showNotification).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                startForegroundService(intent);


            }
        });

        findViewById(R.id.hideNotification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                stopService(intent);
            }
        });
        for (int i = 0; i< 60; i++){
            itemLists.add("条目"+i);
        }

//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
////        final String url = "https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%8F%AF%E4%B8%8B%E8%BD%BD%E7%9A%84%E5%9B%BE%E7%89%87&hs=0&pn=4&spn=0&di=202070&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&ie=utf-8&oe=utf-8&cl=2&lm=-1&cs=1481321554%2C1259560715&os=3893586909%2C3368816783&simid=3381782471%2C461498695&adpicid=0&lpn=0&ln=30&fr=ala&fm=&sme=&cg=&bdtype=0&oriquery=%E5%8F%AF%E4%B8%8B%E8%BD%BD%E7%9A%84%E5%9B%BE%E7%89%87&objurl=https%3A%2F%2Fgimg2.baidu.com%2Fimage_search%2Fsrc%3Dhttp%3A%2F%2Fimg.redocn.com%2Fsheying%2F20160919%2Fyangguangxiaxiangrikui_7049979.jpg%26refer%3Dhttp%3A%2F%2Fimg.redocn.com%26app%3D2002%26size%3Df9999%2C10000%26q%3Da80%26n%3D0%26g%3D0n%26fmt%3Djpeg%3Fsec%3D1635305458%26t%3Dc76fe14d719f49a474c9a04700723c92&fromurl=ippr_z2C%24qAzdH3FAzdH3Ff7vwt_z%26e3B6j15vg_z%26e3Bv54AzdH3F15g2o7zito7_0a9ll0l_z%26e3Bip4s&gsm=5&islist=&querylist=";
//        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
//        recyclerView.setAdapter(new CommonRecyclerViewAdapter<String>(itemLists,R.layout.recycerview_item) {
//            @Override
//            public void convert(MyViewHolder holder, String data, int position) {
//                holder.setImageRound(R.id.image,R.mipmap.ic_chuoli,R.mipmap.ic_chuoli,R.mipmap.ic_chuoli, DisplayUtil.dip2px(MainActivity.this,8)).setText(R.id.textView,data);
//            }
//        });

        findViewById(R.id.pictureBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,BitmapActivity.class));
            }
        });

        findViewById(R.id.videoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }



    class MyComparator<Integer> implements Comparator<java.lang.Integer> {
        @Override
        public int compare(java.lang.Integer value_1, java.lang.Integer value_2) {

           return value_1.compareTo(value_2);
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native void arraycopy(Object src,  int  srcPos,
                                        Object dest, int destPos,
                                        int length);


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 200 &&resultCode == RESULT_OK && data != null){
            HmsScan  hmsScan = data.getParcelableExtra(ScanUtil.RESULT);// 获取扫码结果 ScanUtil.RESULT
            if (hmsScan != null){
                show_text.setText(hmsScan.originalValue);
            }
        }
    }

    @Override
    protected void onDestroy() {
        ExecutorUtil.cancelRunnable();
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }
}

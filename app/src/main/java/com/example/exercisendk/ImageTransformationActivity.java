package com.example.exercisendk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.exercisendk.callNative.NativeBitmap;
import com.me.support.recyclerView.CommonRecyclerViewAdapter;
import com.me.support.recyclerView.MyViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ImageTransformationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView showIV;
    private List<String> dataList = new ArrayList<>();
    private Bitmap rotationBitamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_transformation);
        recyclerView = findViewById(R.id.recyclerView);
        showIV = findViewById(R.id.showIV);
        dataList.add("原图");
        dataList.add("灰度");
        dataList.add("高斯模糊");
        dataList.add("美容");
        dataList.add("逆世界");
        dataList.add("镜像");
        dataList.add("旋转");
        dataList.add("倒影");
        dataList.add("加水印");
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(new CommonRecyclerViewAdapter<String>(dataList, R.layout.item_image_transformation) {
            @Override
            public void convert(MyViewHolder holder, final String data, int position) {
                holder.setText(R.id.textTV,data);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inMutable = false;
                        if(data.equals("原图")){
                            showIV.setImageResource(R.drawable.dog);
                        }else if(data.equals("灰度")){
                            showIV.setImageBitmap(NativeBitmap.generateGrayBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.dog,options), NativeBitmap.TYPE_GRAY));
                        }else if(data.equals("高斯模糊")){
                            showIV.setImageBitmap(NativeBitmap.generateGrayBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.dog,options), NativeBitmap.TYPE_GUASI));
                        }else if(data.equals("美容")){
                            showIV.setImageBitmap(NativeBitmap.generateGrayBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.dog,options), NativeBitmap.TYPE_BILA));
                        } else if(data.equals("逆世界")){
                            showIV.setImageBitmap(NativeBitmap.againstWorld(BitmapFactory.decodeResource(getResources(),R.drawable.dog,options)));
                        }else if(data.equals("镜像")){
                            showIV.setImageBitmap(NativeBitmap.mirrorImage(BitmapFactory.decodeResource(getResources(),R.drawable.dog,options)));
                        }else if(data.equals("旋转")){
                            options.inMutable = true;
                            if(rotationBitamp == null){
                                rotationBitamp = BitmapFactory.decodeResource(getResources(),R.drawable.dog,options);
                            }
                            showIV.setImageBitmap(NativeBitmap.rotationImage(rotationBitamp));
                        }else if(data.equals("倒影")){
                            options.inMutable = true;
                            showIV.setImageBitmap(NativeBitmap.reflectionImage(BitmapFactory.decodeResource(getResources(),R.drawable.dog,options)));
                        }else if(data.equals("加水印")){
                            Bitmap dog = BitmapFactory.decodeResource(getResources(),R.drawable.zhuye,options);
                            Bitmap waterDog = BitmapUtils.addTimeWatermark(dog,100);
                            showIV.setImageBitmap(waterDog);
                        }

                    }
                });
            }
        });
    }
}
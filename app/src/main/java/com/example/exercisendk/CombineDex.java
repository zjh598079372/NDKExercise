package com.example.exercisendk;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

public class CombineDex {
    /**
     * 合并注入
     * @param context
     * @throws Exception
     */
    private static void injectDexElements(Context context) throws Exception {
        ClassLoader pathClassLoader = context.getClassLoader();

        File outDexFile = new File(context.getDir("odex", Context.MODE_PRIVATE).getAbsolutePath()
                + File.separator + "out_dex");

        if (!outDexFile.exists()) {
            outDexFile.mkdirs();
        }

        // 合并成一个数组
        Object applicationDexElement = getDexElementByClassLoader(pathClassLoader);

        File[] mFixDex = new File[]{};
        for (File dexFile : mFixDex) {
            ClassLoader classLoader = new DexClassLoader(dexFile.getAbsolutePath(),// dexPath
                    outDexFile.getAbsolutePath(),// optimizedDirectory
                    null,
                    pathClassLoader
            );
            // 获取这个classLoader中的Element
            Object classElement = getDexElementByClassLoader(classLoader);
            Log.e("TAG", classElement.toString());
            applicationDexElement = combineArray(classElement, applicationDexElement);
        }

        // 注入到pathClassLoader中
        injectDexElements(pathClassLoader, applicationDexElement);
    }

    /**
     * 把dexElement注入到已运行classLoader中
     * @param classLoader
     * @param dexElement
     * @throws Exception
     */
    private static void injectDexElements(ClassLoader classLoader, Object dexElement) throws Exception {
        Class<?> classLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
        Field pathListField = classLoaderClass.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);

        Class<?> pathListClass = pathList.getClass();
        Field dexElementsField = pathListClass.getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        dexElementsField.set(pathList, dexElement);
    }

    /**
     * 合并两个dexElements数组
     *
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

    /**
     * 获取classLoader中的DexElement
     * @param classLoader ClassLoader
     */
    public static Object getDexElementByClassLoader(ClassLoader classLoader) throws Exception {
        Class<?> classLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
        Field pathListField = classLoaderClass.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);

        Class<?> pathListClass = pathList.getClass();
        Field dexElementsField = pathListClass.getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        Object dexElements = dexElementsField.get(pathList);

        return dexElements;
    }
}

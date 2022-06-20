package com.example.exercisendk.fragment;

import android.util.Log;

import androidx.fragment.app.Fragment;

public abstract class LazyFragment extends Fragment {

    /**
     * 是否执行懒加载
     */
    private boolean isLoaded = false;

    /**
     * 当前Fragment是否对用户可见
     */
    private boolean isVisibleToUser = false;

    /**
     * 当使用ViewPager+Fragment形式会调用该方法时，setUserVisibleHint会优先Fragment生命周期函数调用，
     * 所以这个时候就,会导致在setUserVisibleHint方法执行时就执行了懒加载，
     * 而不是在onResume方法实际调用的时候执行懒加载。所以需要这个变量
     */
    private boolean isCallResume = false;

    /**
     * 是否调用了setUserVisibleHint方法。处理show+add+hide模式下，默认可见 Fragment 不调用
     * onHiddenChanged 方法，进而不执行懒加载方法的问题。
     */
    private boolean isCallUserVisibleHint = false;

    @Override
    public void onResume() {
        super.onResume();
        isCallResume = true;
        if (!isCallUserVisibleHint) isVisibleToUser = !isHidden();
        judgeLazyInit();
    }

    private void judgeLazyInit() {
        if (!isLoaded && isVisibleToUser && isCallResume) {
            lazyInit();
            Log.d("TAG", "lazyInit:!!!!!!!");
                    isLoaded = true;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isVisibleToUser = !hidden;
        judgeLazyInit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isLoaded = false;
        isVisibleToUser = false;
        isCallUserVisibleHint = false;
        isCallResume = false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        isCallUserVisibleHint = true;
        judgeLazyInit();
    }

    abstract void lazyInit();
}

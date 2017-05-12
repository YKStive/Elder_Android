package lilun.com.pension.widget.recycler_view;

import android.support.v7.widget.GridLayoutManager;

import lilun.com.pension.module.utils.LogUtils;

/**
 * recyclerView的layoutManager自动填充
 *
 * @author yk
 *         create at 2017/4/1 13:10
 *         email : yk_developer@163.com
 */
public class AutoExtendSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private int spanCount;
    private int dataSize;
    private int remainder;

    public AutoExtendSpanSizeLookup(int dataSize, int spanCount) {
        this.dataSize = dataSize;
        this.spanCount = spanCount;
        remainder = dataSize%spanCount;
    }



    @Override
    public int getSpanSize(int position) {
        LogUtils.d("余数 = "+remainder+"---"+"当前position = "+position);
        if (remainder!=0 && position>=dataSize-remainder){
            return spanCount/remainder;
        }
        return 1;
    }
}
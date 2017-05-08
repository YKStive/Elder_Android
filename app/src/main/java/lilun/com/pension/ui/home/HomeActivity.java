package lilun.com.pension.ui.home;

import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.module.adapter.PushInfoAdapter;
import lilun.com.pension.ui.activity.activity_detail.ActivityPartnersListFragment;

public class HomeActivity extends BaseActivity {

    private RecyclerView rvPushInfo;
    private PushInfoAdapter pushInfoAdapter;
    private ArrayList<String> data;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void initView() {
        replaceLoadRootFragment(R.id.fl_root_container, new HomeFragment(), false);

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getTopFragment() instanceof ActivityPartnersListFragment) {
            if (((ActivityPartnersListFragment) getTopFragment()).dealOnBack())
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i("尝试重链接mqtt");
        App.mqttConnectAndSub();
    }

}

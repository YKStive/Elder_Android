package lilun.com.pension.ui.welcome;

import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import java.util.ArrayList;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.module.adapter.PushInfoAdapter;
import lilun.com.pension.ui.activity.activity_detail.ActivityPartnersListFragment;
import lilun.com.pension.ui.home.HomeFragment;

public class ChangePasswordActivity extends BaseActivity {

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
        replaceLoadRootFragment(R.id.fl_root_container, new ChangePasswordFragment1(), false);

    }

    @Override
    protected void initEvent() {

    }



}
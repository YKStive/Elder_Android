package lilun.com.pensionlife.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.module.adapter.PushInfoAdapter;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.bean.OrganizationAid;
import lilun.com.pensionlife.module.callback.MyCallBack;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.SystemUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.module.utils.mqtt.MQTTManager;
import lilun.com.pensionlife.module.utils.mqtt.MqttTopic;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.ui.lbs.AnnounceInfoActivity;
import lilun.com.pensionlife.ui.lbs.UrgentAidInfoActivity;
import lilun.com.pensionlife.ui.welcome.LoginActivity;
import lilun.com.pensionlife.ui.welcome.WelcomeActivity;
import lilun.com.pensionlife.widget.progress.RxProgressDialog;
import me.yokeyword.fragmentation.SupportActivity;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yk on 2017/1/5.
 * 基类activity
 */
public abstract class BaseActivity<T extends IPresenter> extends SupportActivity {

    protected T mPresenter;
    private Subscription subscribe;
    private RecyclerView rvPushInfo;
    private FrameLayout mFrameLayout;
    private PushInfoAdapter pushInfoAdapter;
    private MyCallBack callback;
    protected CompositeSubscription subscription = new CompositeSubscription();
    private RxProgressDialog dialog;
    private int pushAidInfoCunt = 0;
    private int pushInfoCunt = 0;

    //用于监听弹出软键盘的Enter事件；
    public View.OnKeyListener editOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                editViewEnterButton();
            }
            return false;
        }
    };

    /**
     * 软键盘的Enter事件响应
     */
    public void editViewEnterButton() {

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);
        rvPushInfo = (RecyclerView) findViewById(R.id.rv_push_container);

        mFrameLayout = (FrameLayout) findViewById(R.id.fl_root_container);
        mFrameLayout.addView(LayoutInflater.from(this).inflate(getLayoutId(), null));


        getTransferData();

        initData();

        ButterKnife.bind(this);

        initPresenter();

        initView();


        initEvent();

        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void tokenFailure(Event.TokenFailure event) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Subscribe
    public void permissionDenied(Event.PermissionDenied event) {
//        Logger.d("prepare http me");
        subscribe = NetHelper.getApi().getMe().
                compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>() {
                    @Override
                    public void _next(Object o) {

                    }
                });

    }

//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void refreshPushMessage(Event.RefreshPushMessage event) {
//        showPushMessage(getPushMessageFromDatabase());
//    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPushMessage(Event.BoardMsg data) {
        showBoardMsg(data.topic, data.data);
    }


    /**
     * 登陆事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPushMessage(Event.OffLine offLine) {
        startActivity(new Intent(this, WelcomeActivity.class));
        ToastHelper.get().showShort("您的账号已在其他设备登陆");
        MQTTManager.getInstance().unSubscribe("user/" + User.getUserName() + "/.login", null, null);
        App.clear();
        finish();
    }


    //    ===============子类需要实现的方法=============================

    //获取传递过来的数据,可选,最先被调用
    protected void getTransferData() {

    }

    protected void initData() {
    }

    protected abstract int getLayoutId();

    //非Mvp就不用管
    protected abstract void initPresenter();

    //初始化布局
    protected abstract void initView();

    //初始化事件,listener...
    protected void initEvent() {
    }

    /**
     * 显示推送过来的消息
     */
//    private void showPushMessage(List<PushMessage> pushMessages) {
//        if (pushMessages == null || pushMessages.size() == 0) {
//            rvPushInfo.setVisibility(View.GONE);
//            return;
//        }
//
//        rvPushInfo.setVisibility(View.VISIBLE);
//        if (pushInfoAdapter == null) {
//            pushInfoAdapter = new PushInfoAdapter(rvPushInfo, pushMessages);
//            rvPushInfo.setAdapter(pushInfoAdapter);
//        } else {
//            pushInfoAdapter.replaceAll(pushMessages);
//        }

//    }

    /**
     * 初始化推送消息栏
     */
//    private void initRecyclerView() {
//        rvPushInfo.setLayoutManager(new OverLayCardLayoutManager());
//        CardConfig.initConfig(this);
//        CardConfig.MAX_SHOW_COUNT = 3;
//
//        callback = new MyCallBack(rvPushInfo);
//        callback.setOnItemSwipedListener(() -> {
//            if (pushInfoAdapter != null && pushInfoAdapter.getItemCount() != 0) {
//                PushMessage item = pushInfoAdapter.getItem(pushInfoAdapter.getItemCount() - 1);
//                pushInfoAdapter.remove(item);
//                if (pushInfoAdapter.getItemCount() == 0) {
//                    Logger.d("推送栏设置gone");
//                    rvPushInfo.setVisibility(View.GONE);
//                }
//
////                DataSupport.deleteAll(PushMessage.class, "king = ?", item.getKing());
//            }
//        });
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(rvPushInfo);
//
//        showPushMessage(getPushMessageFromDatabase());
//    }


    /**
     * 从数据库中取消息
     */
//    public List<PushMessage> getPushMessageFromDatabase() {
//        List<PushMessage> allMessage = DataSupport.findAll(PushMessage.class);
//        return allMessage;
//    }

    /**
     * 显示紧急求助弹窗
     */
    public void showBoardMsg(String topic, String data) {
        Gson gson = new Gson();
        MqttTopic mqttTopic = new MqttTopic();
        JSONObject jsonObject = JSON.parseObject(data);
        if (topic.equals(mqttTopic.urgent_help)) {
            OrganizationAid aid = new OrganizationAid();
            aid.setAddress( jsonObject.getString("address"));
            aid.setMobile( jsonObject.getString("mobile"));
            aid.setCreatedAt(jsonObject.getString("time"));
            aid.setCreatorName(jsonObject.getString("title"));
            aid.setMemo(jsonObject.getString("location"));
            if (pushAidInfoCunt == 0 && !SystemUtils.isTopActivity(UrgentAidInfoActivity.class.getName())) {
                pushAidInfoCunt++;
                Intent intent = new Intent(this, UrgentAidInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("aid", aid);
                intent.putExtras(bundle);
                startActivityForResult(intent, 123);
            }
            EventBus.getDefault().post(new Event.RefreshUrgentInfo());

        }


         if (topic.contains(mqttTopic.topic_information_suffix)) {
             String infoString = jsonObject.getString("data");
             Information information = JSON.parseObject(infoString, Information.class);
//             Information Information = gson.fromJson(pushMessage.getData(), Information.class);
            if (pushInfoCunt == 0 && !SystemUtils.isTopActivity(Information.class.getName())) {
                pushInfoCunt++;
                Intent intent = new Intent(this, AnnounceInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("organizationInfo", information);
                intent.putExtras(bundle);
                startActivityForResult(intent, 123);
            }
            EventBus.getDefault().post(new Event.RefreshUrgentInfo());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == 0) {
            pushAidInfoCunt = 0;
            pushInfoCunt = 0;
        }
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            pop();
        } else {
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (mPresenter != null) {
            mPresenter.unBindView();
            mPresenter = null;
        }

        if (subscribe != null && subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }

        EventBus.getDefault().unregister(this);
    }


    public void showDialog() {
        if (dialog == null) {
            dialog = new RxProgressDialog(this);
        }

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


}
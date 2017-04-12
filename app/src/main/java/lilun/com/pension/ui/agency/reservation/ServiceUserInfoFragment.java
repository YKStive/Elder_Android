package lilun.com.pension.ui.agency.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ServiceUserInfoAdapter;
import lilun.com.pension.module.bean.Contact;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.ui.residential.detail.OrderDetailActivity;
import lilun.com.pension.widget.NormalDialog;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 自己预约信息列表V
 *
 * @author yk
 *         create at 2017/3/29 18:47
 *         email : yk_developer@163.com
 */
public class ServiceUserInfoFragment extends BaseFragment {

    @Bind(R.id.rv_info)
    RecyclerView rvInfo;

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    private String productCategoryId;
    private String productId;

    @OnClick({R.id.btn_add_info})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_info:
                start(AddServiceInfoFragment.newInstance(productCategoryId, productId, null));
                break;
        }
    }

    @Subscribe
    public void refreshData(Event.RefreshContract event) {
        getContract();
    }

    public static ServiceUserInfoFragment newInstance(String productCategoryId, String productId) {
        ServiceUserInfoFragment fragment = new ServiceUserInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("productCategoryId", productCategoryId);
        bundle.putString("productId", productId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        productCategoryId = arguments.getString("productCategoryId");
        productId = arguments.getString("productId");
        Preconditions.checkNull(productCategoryId);
        Preconditions.checkNull(productId);
    }

    @Override
    protected void initPresenter() {
        //TODO 获取个人健康信息列表
        getContract();
    }

    private void getContract() {
        String filter = "{\"where\":{\"creatorId\":\"" + User.getUserId() + "\"}}";
        NetHelper.getApi().getContacts(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Contact>>(_mActivity) {
                    @Override
                    public void _next(List<Contact> contacts) {
                        showUserInfo(contacts);
                    }
                });
    }

    private void showUserInfo(List<Contact> contacts) {
        ServiceUserInfoAdapter adapter = new ServiceUserInfoAdapter(contacts);
        adapter.setOnRecyclerViewItemClickListener((view, i) -> {
            reservation(productId, contacts.get(i).getId(), null);
        });
        adapter.setOnItemClickListener(new ServiceUserInfoAdapter.OnItemClickListener() {
            @Override
            public void onDelete() {
                Logger.d("删除个人信息");
            }

            @Override
            public void onEdit(Contact contact) {
                start(AddServiceInfoFragment.newInstance(productCategoryId, productId, contact));
                Logger.d("编辑个人信息");
            }
        });
        rvInfo.setAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_info_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);

        rvInfo.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        rvInfo.addItemDecoration(new NormalItemDecoration(10));
    }


    /**
     * 预约服务
     */
    private void reservation(String productId, String contactId, String data) {
        new NormalDialog().createNormal(_mActivity, getString(R.string.reservation_desc), () -> {
            NetHelper.getApi()
                    .createOrder(productId, contactId, data)
                    .compose(RxUtils.handleResult())
                    .compose(RxUtils.applySchedule())
                    .subscribe(new RxSubscriber<ProductOrder>() {
                        @Override
                        public void _next(ProductOrder order) {
                            Intent intent = new Intent(_mActivity, OrderDetailActivity.class);
                            intent.putExtra("orderId", order.getId());
                            startActivity(intent);
                            setFragmentResult(0, null);
                            pop();
                        }
                    });
        });

    }

}
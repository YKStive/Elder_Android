package lilun.com.pension.ui.residential.list;

import java.util.List;

import lilun.com.pension.base.RxPresenter;
import lilun.com.pension.module.bean.OrganizationProduct;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;

/**
*居家服务列表P
*@author yk
*create at 2017/2/22 10:44
*email : yk_developer@163.com
*/
public class ResidentialListPresenter extends RxPresenter<ResidentialListContract.View> implements ResidentialListContract.Presenter {

    @Override
    public void getResidentialServices(String filter, int skip) {
        addSubscribe(NetHelper.getApi()
                .getProducts(StringUtils.addFilterWithDef(filter,skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationProduct>>() {
                    @Override
                    public void _next(List<OrganizationProduct> products) {
                        view.showResidentialServices(products, skip!=0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.completeRefresh();
                    }
                })
        );

    }
}

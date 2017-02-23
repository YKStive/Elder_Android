package lilun.com.pension.ui.residential.list;

import java.util.List;

import lilun.com.pension.base.IPresenter;
import lilun.com.pension.base.IView;
import lilun.com.pension.module.bean.OrganizationProduct;

/**
*居家服务列表C
*@author yk
*create at 2017/2/13 10:43
*email : yk_developer@163.com
*/
public interface ResidentialListContract {
    interface View extends IView<Presenter> {
        void showResidentialServices(List<OrganizationProduct> products, boolean isLoadMore);
        void completeRefresh();
    }

    interface Presenter extends IPresenter<View> {
        void getResidentialServices(String filter,int skip);
    }

}

package lilun.com.pensionlife.ui.education.course_list;

import java.util.List;

import lilun.com.pensionlife.base.IPresenter;
import lilun.com.pensionlife.base.IView;
import lilun.com.pensionlife.module.bean.ConditionOption;
import lilun.com.pensionlife.module.bean.OrganizationProduct;

/**
 * 老年教育契约类
 *
 * @author yk
 *         create at 2017/2/13 10:43
 *         email : yk_developer@163.com
 */
public interface CourseListContract {
    interface View extends IView<Presenter> {
        void showCollageCourseList(List<OrganizationProduct> orders, boolean isLoadMore);

        void completeRefresh();


    }

    interface Presenter extends IPresenter<View> {

        void getProducts(String filter, int skip);
        List<List<ConditionOption>> getConditionOptionsList();
    }
}

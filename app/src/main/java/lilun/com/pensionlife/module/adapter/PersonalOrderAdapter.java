package lilun.com.pensionlife.module.adapter;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 个人订单adapter
 *
 * @author yk
 *         create at 2017/3/6 11:10
 *         email : yk_developer@163.com
 */
public class PersonalOrderAdapter extends QuickAdapter<ProductOrder> {

    private OnItemClickListener listener;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public PersonalOrderAdapter(List<ProductOrder> data) {
        super(R.layout.item_personal_order, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ProductOrder order) {
        OrganizationProduct product = order.getProductBackup();
        if (product != null) {

            String url = StringUtils.getFirstIcon(product.getImage());
            ImageLoaderUtil.instance().loadImage(url, helper.getView(R.id.iv_product_icon));

            setOrderStatus(helper, order);
            // setNextOperate(helper, order);
            String agencyName = StringUtils.getOrganizationNameFromId(StringUtils.removeSpecialSuffix(product.getOrganizationId()));
//            helper.setVisible(R.id.tv_next_operate, order.getStatus().equals("reserved") || order.getStatus().equals("done"))
            helper.setVisible(R.id.tv_next_operate, false)
                    .setText(R.id.tv_provider_name, agencyName)
                    .setText(R.id.tv_product_title, product.getTitle())

                    .setText(R.id.tv_reservation_time, "服务时间:" + StringUtils.IOS2ToUTC(order.getRegisterDate(), format))
                    .setText(R.id.tv_product_price, Html.fromHtml("价格: <font color='#fe620f'>" + "￥" + new DecimalFormat("######0.00").format(product.getPrice()) + "</font>"))
                    .setOnClickListener(R.id.rl_item, v -> {
                        if (listener != null) {
                            listener.onItemClick(order);
                        }
                    })
                    .setOnClickListener(R.id.tv_provider_name, v -> {
                        if (listener != null) {
                            listener.nextOperate(order);
                        }
                    });


            String orgCategoryId = product.getOrgCategoryId();
            if (!TextUtils.isEmpty(orgCategoryId) && orgCategoryId.contains("/教育服务/其他教育服务/老年教育服务")) {
                //是课程订单,显示学期
                String semester = showSemester(product.getExtend());
                helper.setText(R.id.tv_product_area, semester);

            } else {
                helper.setText(R.id.tv_product_area, String.format("服务范围: %1$s", StringUtils.getProductArea(product.getAreaIds())));
            }

        }
    }

    private String showSemester(Map<String, Object> extend) {
        //显示学期
        String semester = "无";
        if (extend != null) {
            String termStartDate = (String) extend.get("termStartDate");
            String termEndDate = (String) extend.get("termEndDate");
            if (!TextUtils.isEmpty(termStartDate) && !TextUtils.isEmpty(termEndDate)) {
                semester = "学期时间：" + StringUtils.IOS2ToUTC(termStartDate, 5) + "--" + StringUtils.IOS2ToUTC(termEndDate, 5);
            }
        }
        return semester;
    }

    private void setOrderStatus(BaseViewHolder helper, ProductOrder order) {
        TextView tvOrderStatus = helper.getView(R.id.tv_order_status);
        String status = order.getStatus();
        if (status.equals("reserved")) {
            tvOrderStatus.setText("等待商家处理");
        } else if (status.equals("assigned")) {
            tvOrderStatus.setText("商家已经受理");
        } else if (status.equals("done")) {
            tvOrderStatus.setText("该订单已经完成");
        } else if (status.equals("cancel")) {
            tvOrderStatus.setText("该订单已经取消");
        } else if (status.equals("delay")) {
            tvOrderStatus.setText("延期中");
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ProductOrder order);

        //操作进入商家
        void nextOperate(ProductOrder order);
    }
}

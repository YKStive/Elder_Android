package lilun.com.pension.ui.activity.activity_add;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import cn.qqtheme.framework.picker.DateTimePicker;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.OrganizationChildrenConfig;
import lilun.com.pension.base.BaseTakePhotoFragment;
import lilun.com.pension.module.adapter.ActivityCategoryAdapter;
import lilun.com.pension.module.bean.ActivityCategory;
import lilun.com.pension.module.bean.OrganizationActivity;
import lilun.com.pension.module.bean.TakePhotoResult;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.widget.CommonButton;
import lilun.com.pension.widget.ElderModuleClassifyDecoration;
import lilun.com.pension.widget.InputView;
import lilun.com.pension.widget.NormalTitleBar;
import lilun.com.pension.widget.TakePhotoLayout;
import rx.Observable;

/**
 * 新建活动V
 *
 * @author yk
 *         create at 2017/3/13 11:18
 *         email : yk_developer@163.com
 */

public class AddActivityFragment extends BaseTakePhotoFragment<AddActivityConstract.Presenter> implements AddActivityConstract.View, View.OnClickListener {

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    @Bind(R.id.rv_activity_classify)
    RecyclerView rvActivityClassify;

    @Bind(R.id.input_title)
    InputView inputTitle;

    @Bind(R.id.input_address)
    InputView inputAddress;


    @Bind(R.id.input_require)
    InputView inputRequire;

    @Bind(R.id.input_maxPartner)
    InputView inputMaxPartner;

    @Bind(R.id.input_location)
    InputView inputLocation;

    @Bind(R.id.btn_add_activity)
    CommonButton btnAddActivity;

    @Bind(R.id.take_photo)

    TakePhotoLayout takePhotoLayout;
    @Bind(R.id.rg_repeat_type)
    RadioGroup rgRepeatType;

    @Bind(R.id.input_repeat_type)
    InputView inputRepeatType;

    @Bind(R.id.tv_end_time)
    TextView tvEndTime;

    @Bind(R.id.rl_end_time)
    RelativeLayout rlEndTime;

    @Bind(R.id.et_start_time)
    EditText etStartTime;

    @Bind(R.id.tv_start_time)
    TextView tvStartTime;

    private ArrayList<ActivityCategory> activityCategories;
    private String mCategoryId;
    private String[] repeatedTypeArray;
    private int chooseTime = 0;


    public static AddActivityFragment newInstance(ArrayList<ActivityCategory> activityCategories) {
        AddActivityFragment fragment = new AddActivityFragment();
        Bundle args = new Bundle();
        args.putSerializable("activityCategories", activityCategories);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        activityCategories = (ArrayList<ActivityCategory>) arguments.getSerializable("activityCategories");
        Preconditions.checkNull(activityCategories);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new AddActivityPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activity_add;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        //标题栏
        titleBar.setOnBackClickListener(this::pop);

        //分类选择栏
        showActivityCategories();

        //图片上传栏
        takePhotoLayout.setFragmentManager(_mActivity.getFragmentManager());
        takePhotoLayout.setOnResultListener(this);
        setTakePhotoLayout(takePhotoLayout);

        //输入类型
        inputMaxPartner.setInputType(InputType.TYPE_CLASS_NUMBER);


        //周期活动控制
        rgRepeatType.check(R.id.rbtn_repeat);
        rgRepeatType.setOnCheckedChangeListener((group, checkedId) -> {
            //选择周期活动
            rlEndTime.setVisibility(checkedId == R.id.rbtn_repeat ? View.GONE : View.VISIBLE);
            inputRepeatType.setVisibility(checkedId != R.id.rbtn_repeat ? View.GONE : View.VISIBLE);
            etStartTime.setVisibility(checkedId == R.id.rbtn_repeat ? View.VISIBLE : View.GONE);
            tvStartTime.setVisibility(checkedId != R.id.rbtn_repeat ? View.VISIBLE : View.GONE);
        });

        tvStartTime.setOnClickListener(this);
        btnAddActivity.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        inputRepeatType.setOnClickListener(this);
    }


    private void showActivityCategories() {
        rvActivityClassify.setLayoutManager(new GridLayoutManager(_mActivity, StringUtils.spanCountByData(activityCategories)));
        rvActivityClassify.addItemDecoration(new ElderModuleClassifyDecoration());
        ActivityCategoryAdapter adapter = new ActivityCategoryAdapter(this, activityCategories);
        adapter.setIsRadioModule(true);
        adapter.setOnItemClickListener((activityCategory -> {
            mCategoryId = activityCategory.getId();
        }));

        rvActivityClassify.setAdapter(adapter);
    }


    private void addActivity() {
        if (TextUtils.isEmpty(mCategoryId)) {
            ToastHelper.get().showWareShort("请选择活动类别");
            return;
        }

        int intDuration = 0;
        String title = inputTitle.getInput();

        String address = inputAddress.getInput();

        String time;
        if (etStartTime.getVisibility() == View.VISIBLE) {
            time = etStartTime.getText().toString();
        } else {
            time = tvStartTime.getText().toString();
        }

        String maxPartner = inputMaxPartner.getInput();

        String require = inputRequire.getInput();

        String location = inputLocation.getInput();

        String repeatedType = inputRepeatType.getInput();


//        TODO 输入检查
        if (TextUtils.isEmpty(title)) {
            showNotEmpty(R.string.activity_title);
            return;
        }

        if (TextUtils.isEmpty(address)) {
            showNotEmpty(R.string.activity_address);
            return;
        }

        if (TextUtils.isEmpty(time)) {
            showNotEmpty(R.string.activity_time);
            return;
        }

        if (TextUtils.isEmpty(time)) {
            showNotEmpty(R.string.activity_time);
            return;
        }

        String max = StringUtils.get_StringNum(maxPartner);
        Integer maxPart = null;
        if (!TextUtils.isEmpty(max)) {
            maxPart = Integer.parseInt(maxPartner);
            if (maxPart > 10) {
                ToastHelper.get().showWareShort("最大人数不能超过10个");
            }
        }

        OrganizationActivity activity = new OrganizationActivity();
        activity.setCategoryId(mCategoryId);
        activity.setTitle(title);
        activity.setAddress(address);
        activity.setStartTime(time);
        activity.setDruation(intDuration);
        activity.setRepeatedType(mPresenter.getRepeatType(repeatedTypeArray, repeatedType));
        activity.setOrganizationId(OrganizationChildrenConfig.activity());
        activity.setDescription(require);
        activity.setMaxPartner(maxPart);


        if (!TextUtils.isEmpty(max)) {
            activity.setMaxPartner(Integer.parseInt(maxPartner));
        }


        mPresenter.addActivity(activity, getPhotoData());


    }

    private void showNotEmpty(int string) {
        ToastHelper.get().showWareShort("请输入" + getString(string));
    }

    @Override
    public void addActivitySuccess() {
        EventBus.getDefault().post(new Event.RefreshActivityData());
        pop();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_activity:
                addActivity();
                break;

            case R.id.input_repeat_type:
                //定期类型
                chooseRepeatedType();
                break;

            case R.id.tv_start_time:
                // 选择活动开始时间
                chooseTime = 0;
                chooseTime();
                break;
            case R.id.tv_end_time:
                // 选择活动预期结束时间
                chooseTime = 1;
                chooseTime();
                break;

        }
    }

    private void chooseTime() {
        DateTimePicker picker = new DateTimePicker(_mActivity, DateTimePicker.MONTH_DAY, DateTimePicker.HOUR_24);
        picker.setOnDateTimePickListener((DateTimePicker.OnMonthDayTimePickListener) (month, day, hour, minute) -> {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            String time = year + "-" + month + "-" + day + " " + hour + ":" + minute;
            if (chooseTime == 0) {
                if (etStartTime.getVisibility() == View.VISIBLE) {
                    etStartTime.setText(time);
                } else {
                    tvStartTime.setText(time);
                }
            } else {
                tvEndTime.setText(time);
            }
            Logger.d(time);
        });
        picker.show();
    }


    /**
     * 选择活动持续时长
     */
//    private void chooseDuration() {
//        if (durationArray == null) {
//            durationArray = getResources().getStringArray(R.array.activity_duration);
//        }
//        MaterialDialog d = new MaterialDialog.Builder(_mActivity)
//                .items(durationArray)
//                .itemsCallbackSingleChoice(0, (dialog, view, which, text) -> {
//                    inputDuration.setInput(durationArray[which]);
//                    return true;
//                })
//                .positiveText(R.string.choose)
//                .show();
//    }


    /**
     * 选择求助信息的优先级
     */
    private void chooseRepeatedType() {
        if (repeatedTypeArray == null) {
            repeatedTypeArray = getResources().getStringArray(R.array.activity_repeatedType);
        }
        MaterialDialog d = new MaterialDialog.Builder(_mActivity)
                .items(repeatedTypeArray)
                .itemsCallbackSingleChoice(0, (dialog, view, which, text) -> {
                    inputRepeatType.setInput(repeatedTypeArray[which]);
                    return true;
                })
                .positiveText(R.string.choose)
                .show();

    }


    @Override
    protected void onTakePhotoSuccess(TResult tResult) {
        List<TakePhotoResult> results = new ArrayList<>();
        for (TImage tImage : tResult.getImages()) {
            TakePhotoResult result1 = TakePhotoResult.of(tImage.getOriginalPath(), tImage.getCompressPath(), tImage.getFromType(), TakePhotoResult.TYPE_PHOTO);
            results.add(result1);
        }
        Observable.just("")
                .compose(RxUtils.applySchedule())
                .subscribe(s -> {
                    takePhotoLayout.showPhotos(results);
                });
    }


}
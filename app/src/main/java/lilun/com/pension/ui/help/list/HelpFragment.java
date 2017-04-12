package lilun.com.pension.ui.help.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.OrganizationAidAdapter;
import lilun.com.pension.module.bean.ConditionOption;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.ui.help.help_detail.AskDetailFragment;
import lilun.com.pension.ui.help.help_detail.HelpDetailFragment;
import lilun.com.pension.widget.NormalItemDecoration;
import lilun.com.pension.widget.SearchTitleBar;
import lilun.com.pension.widget.filter_view.FilterView;

/**
 * 分类求助（问？帮？）V
 *
 * @author yk
 *         create at 2017/2/7 16:04
 *         email : yk_developer@163.com
 */
public class HelpFragment extends BaseFragment<HelpContract.Presenter> implements HelpContract.View {


    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    @Bind(R.id.searchBar)
    SearchTitleBar searchBar;
    @Bind(R.id.filter_view)
    FilterView filterView;


    private String condition_title = "title";
    private String condition_kind = "kind";
    private String condition_priority = "priority";
    private String condition_near = "near";
    private boolean isMain;

    private Map<String, String> whereConditionMap;
    private Map<String, Object> conditionMap;
    //    private String[] conditionKind;
//    private String[] conditionPriority;
//    private String[] conditionTitles = new String[]{"类别", "状态", "优先级"};
    private SearchTitleBar.LayoutType layoutType = SearchTitleBar.LayoutType.BIG;


    private OrganizationAidAdapter mAidAdapter;
    private List<OrganizationAid> helps;

    public static HelpFragment newInstance(boolean isMain) {
        HelpFragment fragment = new HelpFragment();
        Bundle args = new Bundle();
        args.putBoolean("isMain", isMain);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        isMain = arguments.getBoolean("isMain", false);
    }

    @Subscribe
    public void refreshData(Event.RefreshHelpData event) {
        getHelps(0);
    }


    @Override
    protected void initPresenter() {
        mPresenter = new HelpPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_help_list;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        initConditionOption();
        searchBar.setFragment(this);
        searchBar.setOnItemClickListener(new SearchTitleBar.OnItemClickListener() {
            @Override
            public void onBack() {
                pop();
            }

            @Override
            public void onChangeLayout(SearchTitleBar.LayoutType type) {
                layoutType = type;
                if (helps != null && helps.size() != 0) {
                    setRecyclerAdapter(helps);
                }
            }

            @Override
            public void onSearch(String searchStr) {
                whereConditionMap.put(condition_title, "{\"like\":\"" + searchStr + "\"}");
                getHelps(0);
            }
        });


        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NormalItemDecoration(6));
        //刷新
        mSwipeLayout.setOnRefreshListener(() -> {
                    if (mPresenter != null) {
                        getHelps(0);
                    }
                }
        );

        initConditionMap();

    }

    private void initConditionOption() {
        List<String> conditionTitles = new ArrayList<>();
        List<ConditionOption> conditionOptionsList = mPresenter.getConditionOptionsList();
        if (conditionOptionsList != null) {
            for (ConditionOption conditionOption : conditionOptionsList) {
                conditionTitles.add(conditionOption.getCondition());
            }
            filterView.setTitlesAndDatas(conditionTitles, conditionOptionsList, mSwipeLayout);
            filterView.setOnOptionClickListener((whereKey, whereValue) -> {
                whereConditionMap.put(whereKey, whereValue);
                getHelps(0);
            });
        }
    }

    private void initConditionMap() {
        conditionMap = new HashMap<>();
        whereConditionMap = new HashMap<>();
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getHelps(0);
        }
    }

    private void getHelps(int skip) {
        mSwipeLayout.setRefreshing(true);
        String filter = getFilterWithCondition();
        if (isMain) {
            mPresenter.getAboutMe(filter, skip);
        } else {
            mPresenter.getHelps(filter, skip);
        }
    }

    private String getFilterWithCondition() {
        conditionMap.put("where", whereConditionMap);
        Gson gson = new Gson();
        String filter = gson.toJson(conditionMap);
        Logger.d("filter = " + filter);
        return filter;
    }


    @Override
    public void showAboutMe(List<OrganizationAid> helps, boolean isLoadMore) {
        this.helps = helps;
        completeRefresh();
        if (helps != null) {
            for (OrganizationAid aid : helps) {
                aid.setItemType(aid.getKind());
            }
            if (mAidAdapter == null) {
                setRecyclerAdapter(helps);
            } else if (isLoadMore) {
                mAidAdapter.addAll(helps);
            } else {
                mAidAdapter.replaceAll(helps);
            }
        }
    }

    private void setRecyclerAdapter(List<OrganizationAid> helps) {
        mAidAdapter = getAdapterFromLayoutType(helps);
        if (mAidAdapter != null) {
            mAidAdapter.setOnItemClickListener((aid) -> {
                start(aid.getKind() == 0 ? AskDetailFragment.newInstance(aid.getId(), User.creatorIsOwn(aid.getCreatorId())) : HelpDetailFragment.newInstance(aid.getId()));
            });
            mAidAdapter.setEmptyView();
        }
        mRecyclerView.setAdapter(mAidAdapter);
    }

    private OrganizationAidAdapter getAdapterFromLayoutType(List<OrganizationAid> helps) {
        OrganizationAidAdapter adapter;
        int layoutId;
        if (layoutType == SearchTitleBar.LayoutType.BIG) {
            layoutId = R.layout.item_aid_big;
        } else if (layoutType == SearchTitleBar.LayoutType.SMALL) {
            layoutId = R.layout.item_aid_small;
        } else {
            layoutId = R.layout.item_aid_null;
        }
        adapter = new OrganizationAidAdapter(helps, layoutId, layoutType);
        return adapter;
    }

    @Override
    public void completeRefresh() {
        if (mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }


}
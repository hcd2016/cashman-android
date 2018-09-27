package com.credit.xiaowei.dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.credit.xiaowei.R;
import com.credit.xiaowei.ui.lend.adapter.CostDetailsAdapter;
import com.credit.xiaowei.ui.lend.bean.ExpenseDetailBean;
import com.credit.xiaowei.ui.lend.contract.ExpenseDetailContract;
import com.credit.xiaowei.ui.lend.presenter.ExpenseDetailPresenter;
import com.credit.xiaowei.widget.loading.LoadingLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by gyl on 2017/3/7 0007.
 */

public class CostDetailsDialog extends DialogFragment implements ExpenseDetailContract.View{
    public static final String TAG = "CostDetails";
    @BindView(R.id.load_view)
    LoadingLayout mLoadView;
    @BindView(R.id.rl_cost_details)
    RecyclerView mRlCostDetails;
    private String money;
    private String day;
    private ExpenseDetailPresenter presenter;
    private CostDetailsAdapter adapter;
    public static CostDetailsDialog newInstance(String money,String day) {
        CostDetailsDialog dialog = new CostDetailsDialog();
        Bundle bundle = new Bundle();
        bundle.putString("money", money);
        bundle.putString("day", day);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_cost_details, container, false);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }

    private void initDialog() {
        getDialog().getWindow().setGravity(Gravity.CENTER);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.8), getDialog().getWindow().getAttributes().height);
    }

    private void setData() {
        money = getArguments().getString("money");
        day = getArguments().getString("day");
        mRlCostDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CostDetailsAdapter();
        mRlCostDetails.setAdapter(adapter);
        presenter = new ExpenseDetailPresenter();
        presenter.init(this);
        presenter.loadExpenseDetail(money,day);
    }

    @Override
    public void onStart() {
        super.onStart();
        initDialog();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        //修改commit方法为commitAllowingStateLoss
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() != null) {
            setShowsDialog(false);
        }
    }


    @OnClick({R.id.rl_cost_details, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_cost_details:
                break;
            case R.id.tv_submit:
                dismiss();
                break;
        }
    }

    @Override
    public void showLoading(String content) {
        mLoadView.setStatus(LoadingLayout.Loading);
    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if ("网络不可用".equals(msg)){
            mLoadView.setStatus(LoadingLayout.No_Network);
        }else{
            mLoadView.setEmptyText(msg)
            .setStatus(LoadingLayout.Error);
        }
        mLoadView.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                presenter.loadExpenseDetail(money,day);
            }
        });

    }

    @Override
    public void loadExpenseDetailSuccess(List<ExpenseDetailBean> result) {
        mLoadView.setStatus(LoadingLayout.Success);
        adapter.clearData();
        adapter.addData(result);
    }
}

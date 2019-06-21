package com.hiscene.flytech.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.excel.ProcessExcel;

import org.apache.poi.ss.formula.functions.T;

import butterknife.BindView;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des
 */
@SuppressLint("ValidFragment")
public class ProcessExcelFragment extends BaseExcelFragment<ProcessModel> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.standard)
    TextView standard;

    ProcessModel data;

    public ProcessExcelFragment(ExcelFragmentManager excelFragmentManager) {
        super(excelFragmentManager);
    }

    public static ProcessExcelFragment newInstance(ExcelFragmentManager excelFragmentManager) {
        ProcessExcelFragment processExcelFragment = new ProcessExcelFragment(excelFragmentManager);
        return processExcelFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_process;
    }


    @Override
    protected void initView() {
        if (data != null) {//第一次初始化setData还没Attach Activity
            initData(data);
        }
    }

    @Override
    public void setData(ProcessModel data) {
        if (title != null) {
            initData(data);
        } else {
            this.data = data;
        }
    }

    private void initData(ProcessModel data) {
        title.setText(data.content);
        standard.setText(data.standard);
    }

    @Override
    protected void previousStep() {
        excelFragmentManager.previousStep();
    }

    @Override
    protected void nextStep() {
        excelFragmentManager.nextStep();
    }
}

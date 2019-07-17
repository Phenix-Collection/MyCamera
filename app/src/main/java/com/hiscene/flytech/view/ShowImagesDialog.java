package com.hiscene.flytech.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.weiss.core.utils.DisplayUtil;
import com.hiscene.flytech.R;
import com.hiscene.flytech.adapter.ShowImagesAdapter;
import com.hiscene.flytech.util.AnimUtils;

import org.apache.poi.ss.formula.functions.T;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static uk.co.senab.photoview.IPhotoView.DEFAULT_MAX_SCALE;
import static uk.co.senab.photoview.IPhotoView.DEFAULT_MIN_SCALE;

/**
 * Created by Administrator on 2017/5/3.
 * 嵌套了viewpager的图片浏览
 */

public class ShowImagesDialog extends Dialog{
    @BindView(R.id.vp_images)
    ShowImagesViewPager mViewPager;
    @BindView(R.id.pre_picture)
    TextView prePrcture;
    @BindView(R.id.next_picture)
    TextView nextPicture;
    @BindView(R.id.iv_cancel)
    TextView ivCancel;

    private View mView;
    private ShowImagesAdapter mAdapter;
    private Context mContext;
    private List<File> fileList;
    private List<String> mTitles;
    private List<View> mViews;

    public ShowImagesDialog(@NonNull Context context, List<File> fileList) {
        super(context, R.style.transparentBgDialog);//R.style.transparentBgDialog
        this.mContext = context;
        this.fileList = fileList;
        initView();
        initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
    }

    private void initView() {
        mView = View.inflate(mContext, R.layout.dialog_images_brower, null);
        ButterKnife.bind(this,mView);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        mTitles = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    private void initData() {
        //按钮动画
        AlphaAnimation alpha = (AlphaAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.alpha_0_to_1);
        ivCancel.startAnimation(alpha);
        //点击图片监听
        PhotoViewAttacher.OnPhotoTapListener listener = new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                dismiss();
            }
        };
        for (int i = 0; i < fileList.size(); i++) {
            final PhotoView photoView = new PhotoView(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            photoView.setLayoutParams(layoutParams);
            photoView.setOnPhotoTapListener(listener);
                    //点击图片外围（无图片处）监听
            /**
             photoView.setOnViewTapListener(new OnViewTapListener() {
            @Override public void onViewTap(View view, float x, float y){
            dismiss();
            }
            });
             **/
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher);
            Glide.with(mContext)
                    .load(fileList.get(i))
                    .apply(requestOptions)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            photoView.setImageDrawable(resource);
                        }
                    });
            mViews.add(photoView);
            mTitles.add(i + "");
        }

        mAdapter = new ShowImagesAdapter(mViews, mTitles);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("Dialog+onPageScrolled", position + "");
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("Dialog+onPageSelected", position + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }



    @OnClick(R.id.pre_picture)
    void prePicture() {
//        ((PhotoView)mViews.get(mViewPager.getCurrentItem())).setMinimumScale(DEFAULT_MIN_SCALE);
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
    }

    @OnClick(R.id.next_picture)
    void nextPicture() {

//        ((PhotoView)mViews.get(mViewPager.getCurrentItem())).setMinimumScale(DEFAULT_MAX_SCALE);
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
    }

    @OnClick(R.id.iv_cancel)
    void cancelDialog() {
        AlphaAnimation alpha = (AlphaAnimation) new AnimUtils().getAnimation(getContext(), R.anim.alpha_1_to_0);
        ivCancel.startAnimation(alpha);
        dismiss();
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        wl.width = (int) (DisplayUtil.getScreenWidth(mContext));
        wl.height = (int) (DisplayUtil.getScreenHeight(mContext));
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);
    }
}

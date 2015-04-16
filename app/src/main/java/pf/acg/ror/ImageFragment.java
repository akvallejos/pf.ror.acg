package pf.acg.ror;

import java.text.DecimalFormat;

import pf.acg.ror.TouchImageView.OnTouchImageViewListener;

import android.app.Dialog;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageFragment extends DialogFragment {
	public static final String EXTRA_IMAGE_PATH = "cpf.acg.ror.image_path";
    private static final String TAG = "imagefragment_tag";
	
	private TouchImageView mImageView;
	
	
	public static ImageFragment newInstace(String imagePath){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_IMAGE_PATH, imagePath);
		
		ImageFragment fragment = new ImageFragment();
		fragment.setArguments(args);
		fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		
		return fragment;
	}

    //override onStart in order to create a full screen dialog
    @Override
    public void onStart(){
        super.onStart();
        Dialog d = getDialog();
        if(d != null){
            int w = ViewGroup.LayoutParams.MATCH_PARENT;
            int h = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(w,h);
        }
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
		mImageView = new TouchImageView(getActivity());
		String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
		BitmapDrawable image = PictureUtils.getScaledDrawable(getActivity(), path);

		//mImageView.setImageDrawable(image);
        //ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(image.getIntrinsicWidth(),image.getIntrinsicHeight());
        //mImageView.setLayoutParams(params);
        //mImageView.setScaleType(ImageView.ScaleType.CENTER);

		mImageView.setOnTouchImageViewListener(new OnTouchImageViewListener() {
			
			@Override
			public void onMove() {
				
			}
		});
		
		return  mImageView;
	}

    @Override
    public void onStop(){
        super.onStop();
        PictureUtils.cleanImageView(mImageView);
    }

    @Override
    public void onResume(){
        super.onPause();
        String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
        BitmapDrawable image = PictureUtils.getScaledDrawable(getActivity(), path);

        mImageView.setImageDrawable(image);
    }

}

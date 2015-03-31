package pf.acg.ror;

import java.text.DecimalFormat;

import pf.acg.ror.TouchImageView.OnTouchImageViewListener;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageFragment extends DialogFragment {
	public static final String EXTRA_IMAGE_PATH = "cpf.acg.ror.image_path";
	
	private TouchImageView mImageView;
	
	
	public static ImageFragment newInstace(String imagePath){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_IMAGE_PATH, imagePath);
		
		ImageFragment fragment = new ImageFragment();
		fragment.setArguments(args);
		fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
		mImageView = new TouchImageView(getActivity());
		String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
		BitmapDrawable image = PictureUtils.getScaledDrawable(getActivity(), path);
		
		
		mImageView.setImageDrawable(image);
		
		mImageView.setOnTouchImageViewListener(new OnTouchImageViewListener() {
			
			@Override
			public void onMove() {
				
			}
		});
		
		return  mImageView;
	}

}

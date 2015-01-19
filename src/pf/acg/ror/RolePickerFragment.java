package pf.acg.ror;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class RolePickerFragment extends DialogFragment {
	public static final String EXTRA_ROLE_BONUS = "pf.acg.ror.role";
	
	private Integer mRoleBonus;
	
	public static RolePickerFragment newInstance(int role){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_ROLE_BONUS, role);
		
		RolePickerFragment fragment = new RolePickerFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		final CharSequence[] items = {"monk", "zen archer", "drunken master"};
		mRoleBonus = (Integer)getArguments().getSerializable(EXTRA_ROLE_BONUS);

		return new AlertDialog.Builder(getActivity())
			.setTitle("Select Role")
			.setSingleChoiceItems(items, mRoleBonus, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int item){
					mRoleBonus = item;
					
					getArguments().putSerializable(EXTRA_ROLE_BONUS, mRoleBonus);
				}
			})
			.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							sendResult(Activity.RESULT_OK);
						}
					})
			.create();
	}
	
	private void sendResult(int resultCode){
		if(getTargetFragment() == null)
			return;
		
		Intent i = new Intent();
		i.putExtra(EXTRA_ROLE_BONUS, mRoleBonus);
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}
}

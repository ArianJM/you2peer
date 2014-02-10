package com.u2p.ui.component;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.u2p.ui.R;

public class LoginDialogFragment extends DialogFragment {

	public interface LoginDialogListener{
		public void onLoginPositiveClick(DialogFragment dialog);
		public void onLoginNegativeClick(DialogFragment dialog);
	}
	
	LoginDialogListener mListener;
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			mListener=(LoginDialogListener)activity;
			
		}catch(ClassCastException e){
			Log.e("LoginDialog","ClassCastException"+e.toString());
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		Bundle args = getArguments();
		boolean first = args.getBoolean("FIRST_LOGIN");
		int layout;
		if(first)
			layout = R.layout.first_login_dialog;
		else{
			layout = R.layout.login_dialog;
		}
		builder.setTitle(R.string.loginDialogTitle);
		builder.setView(inflater.inflate(layout, null))
			.setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					mListener.onLoginPositiveClick(LoginDialogFragment.this);
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					mListener.onLoginNegativeClick(LoginDialogFragment.this);
				}
			});
		return builder.create();
	}

}

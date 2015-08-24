package com.washhous.comman;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class CommanMethods {

	private static ProgressDialog pDialog;

	public static void myCustomToast(Context applicationContext,
			String toastMessage) {

		Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_LONG)
				.show();

	}

	public static boolean isConnected(Context applicationContext) {
		ConnectivityManager conManager = (ConnectivityManager) applicationContext
				.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;

	}

	public static void startProgrssDialog(Context applicationContext,
			String processBarMessage) {
		pDialog = new ProgressDialog(applicationContext);
		pDialog.setMessage(processBarMessage); // typically you will define
												// such
		// strings in a remote file.
		pDialog.setCancelable(false);
		pDialog.setIndeterminate(false);
		pDialog.show();
	}

	public static void stopProcessDialog() {

		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
		}
	}

}

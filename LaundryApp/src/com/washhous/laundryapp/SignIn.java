package com.washhous.laundryapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.washhous.comman.CommanMethods;
import com.washhous.database.SessionManager;
import com.washhous.menudrawer.MainActivity;

public class SignIn extends Activity {

	private EditText edtEmailId;
	private EditText edtPassword;
	private Button btnSignIn;
	private TextView txtForgotPassword;
	protected String strUserName;
	protected String strPassword;
	private String strEmailId;

	ProgressDialog pDialog = null;
SessionManager sessionmanger;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_sign_in);
       sessionmanger=new SessionManager(SignIn.this);
		initialiseControl();

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater
				.inflate(R.layout.actionbar_wash_fold, null);
		ImageButton imgBtnBack = (ImageButton) mCustomView
				.findViewById(R.id.btn_img_back_wash_fold);
		imgBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SignIn.this, SignInOption.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

			}
		});
		TextView mTitleTextView = (TextView) mCustomView
				.findViewById(R.id.title_text);
		mTitleTextView.setText("Login");

		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);
	}

	private void initialiseControl() {

		edtEmailId = (EditText) findViewById(R.id.edt_emailid);
		edtPassword = (EditText) findViewById(R.id.edt_password);
		btnSignIn = (Button) findViewById(R.id.btn_signin);
		txtForgotPassword = (TextView) findViewById(R.id.txt_forgor_password);
		txtForgotPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SignIn.this, ForgotPassword.class);
				startActivity(intent);

			}
		});
		btnSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Retrieve the text entered from the EditText
				strEmailId = edtEmailId.getText().toString();
				strPassword = edtPassword.getText().toString();

				if (strEmailId.equals("")) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Enter Email");
					return;
				}

				if (strPassword.equals("")) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Enter Password");
					return;
				}

				if (!emailValidator(strEmailId)) {
					CommanMethods.myCustomToast(SignIn.this,
							"Enter Valid email");
					return;
				}
				if (!CommanMethods.isConnected(getApplicationContext())) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Please Check Internet Connection");
					return;

				} else {
					startProcessBar();
					// Send data to Parse.com for verification
					ParseUser.logInInBackground(strEmailId, strPassword,
							new LogInCallback() {
								public void done(ParseUser user,
										ParseException e) {
									stopProcessBar();
									if (user != null) {
										// If user exist and authenticated, send
										// user to Welcome.class
										sessionmanger.createlogin(strEmailId,strPassword);
										Intent intent = new Intent(SignIn.this,
												MainActivity.class);
										startActivity(intent);
										finish();
										((MyApplication) getApplicationContext())
												.saveLoginStatus(true);
										Toast.makeText(getApplicationContext(),
												"Successfully Logged in",
												Toast.LENGTH_LONG).show();
										SignIn.this.finish();
										SignInOption signInOption = new SignInOption();
										signInOption.finish();

									} else {
										Toast.makeText(
												getApplicationContext(),
												"No such user exist, please signup",
												Toast.LENGTH_LONG).show();
									}
								}
							});
				}

			}
		});

	}

	protected void startProcessBar() {

		pDialog = new ProgressDialog(SignIn.this);
		pDialog.setMessage("Logging In..."); // typically

		// will define
		// such
		// strings in a remote file.
		pDialog.setCancelable(false);
		pDialog.setIndeterminate(false);
		pDialog.show();

	}

	protected void stopProcessBar() {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
		}

	}

	public boolean emailValidator(String email) {
		Pattern pattern;
		Matcher matcher;
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[a-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}

	@Override
	public void onBackPressed() {
		Intent intentSignIN = new Intent(SignIn.this, SignInOption.class);
		startActivity(intentSignIN);
		finish();
	}
}

package com.washhous.laundryapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.washhous.comman.CommanMethods;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotPassword extends Activity {

	private EditText edtEmail;
	private Button btnSendMail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.actionbar_drawermenu,
				null);

		TextView mTitleTextView = (TextView) mCustomView
				.findViewById(R.id.title_text);
		mTitleTextView.setText("Forgot Password");
		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);
		initControls();
	}

	private void initControls() {

		edtEmail = (EditText) findViewById(R.id.edt_email_forgotpassword);
		btnSendMail = (Button) findViewById(R.id.btn_send_mail);
		btnSendMail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String strEmail = edtEmail.getText().toString().trim();

				if (strEmail.equals("")) {
					CommanMethods.myCustomToast(ForgotPassword.this,
							"Enter Email Id");
					return;
				}

				if (!emailValidator(strEmail)) {
					CommanMethods.myCustomToast(ForgotPassword.this,
							"Enter Valid email");
					return;
				}

				if (!CommanMethods.isConnected(getApplicationContext())) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Please Check Internet Connection");
					return;
				}

				CommanMethods.startProgrssDialog(ForgotPassword.this,
						"Please Wait...");

				ParseUser.requestPasswordResetInBackground(strEmail,
						new RequestPasswordResetCallback() {

							@Override
							public void done(ParseException e) {
								// TODO Auto-generated method stub

								CommanMethods.stopProcessDialog();
								if (e == null) {
									Toast.makeText(getApplicationContext(),
											"Password Sent Successfully",
											Toast.LENGTH_LONG).show();
								} else {
									String message = e.getMessage();
									Toast.makeText(getApplicationContext(),
											message, Toast.LENGTH_LONG).show();
								}
							}
						});

			}
		});

	}

	protected boolean emailValidator(String email) {
		Pattern pattern;
		Matcher matcher;
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[a-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}

}

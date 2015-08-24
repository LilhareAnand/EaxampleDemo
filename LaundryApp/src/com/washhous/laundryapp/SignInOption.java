package com.washhous.laundryapp;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.washhous.comman.CommanMethods;
import com.washhous.database.SessionManager;
import com.washhous.menudrawer.MainActivity;

public class SignInOption extends Activity {

	private LinearLayout btnSignInFacebook, btnSignIn, btnSignUp;
	private static SocialAuthAdapter adapter;

	private final Provider[] providers = new Provider[] { Provider.FACEBOOK };
	public String providerName;

	private String strEmail;
	private String strFirstName;
	private String strLastName;
	SessionManager sessionmanger;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 0);
		setContentView(R.layout.activity_sign_in_option);
		intialiseControl();
         sessionmanger=new SessionManager(SignInOption.this);
		adapter = new SocialAuthAdapter(new ResponseListener());
	}

	// To receive the response after authentication
	private final class ResponseListener implements DialogListener {

		@Override
		public void onBack() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(Bundle values) {
			// TODO Auto-generated method stub
			// Get the provider
			providerName = values.getString(SocialAuthAdapter.PROVIDER);
			Events(providerName);
		}

		@Override
		public void onError(SocialAuthError arg0) {
			// TODO Auto-generated method stub

		}

	}

	private void intialiseControl() {

		btnSignIn = (LinearLayout) findViewById(R.id.btn_signin_option);
		btnSignInFacebook = (LinearLayout) findViewById(R.id.btn_option_signin_facebook);
		btnSignUp = (LinearLayout) findViewById(R.id.btn_signup);

		btnSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentSignIN = new Intent(SignInOption.this,
						SignIn.class);
				startActivity(intentSignIN);
				finish();
			}
		});
		btnSignInFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (CommanMethods.isConnected(getApplicationContext())) {

					CommanMethods.myCustomToast(getApplicationContext(),
							"Logging in Please wait");

					loginToFB();

				} else {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Please Ckeck Internet Connection");
				}

			}
		});
		btnSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intentSignUp = new Intent(SignInOption.this,
						SignUp.class);
				startActivity(intentSignUp);
				finish();
			}
		});
	}

	protected void loginToFB() {

		if (CommanMethods.isConnected(getApplicationContext())) {
			adapter.authorize(SignInOption.this, providers[0]);
		}

	}

	public void Events(String providerName2) {

		adapter.getUserProfileAsync(new ProfileDataListener());

	}

	// To receive the profile response after authentication
	private final class ProfileDataListener implements
			SocialAuthListener<Profile> {

		@Override
		public void onExecute(String provider, Profile t) {

			Log.d("Custom-UI", "Receiving Data");

			Profile profileMap = t;
			getSocialProfileInfo(profileMap);

		}

		@Override
		public void onError(SocialAuthError e) {

		}
	}

	// Getting User Information from Token Provided by Facebook after
	// authanticate Login.
	public void getSocialProfileInfo(Profile profileMap) {

		try {
			if (profileMap != null) {
				strEmail = profileMap.getEmail();
				strFirstName = profileMap.getFirstName();
				strLastName = profileMap.getLastName();

				if (!strEmail.isEmpty()) {
                    sessionmanger.createlogin(strEmail, "xxxx");
					Intent intentSignIN = new Intent(SignInOption.this,
							MainActivity.class);
					startActivity(intentSignIN);
					((MyApplication) getApplicationContext())
							.saveLoginStatus(true);
					CommanMethods.myCustomToast(getApplicationContext(),
							"Login Successful");
					SignInOption.this.finish();

				}

			} else {
				Toast.makeText(getApplicationContext(),
						"Person information is null", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

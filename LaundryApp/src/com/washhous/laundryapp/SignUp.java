package com.washhous.laundryapp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.washhous.comman.CommanMethods;
import com.washhous.database.SessionManager;
import com.washhous.dataclasses.StateList;
import com.washhous.menudrawer.MainActivity;

public class SignUp extends Activity {

	private EditText edtFirstName;
	private EditText edtLastName;
	private EditText edtEmailId;
	private EditText edtPhoneNumber;
	private EditText edtPassword;
	private EditText edtConfirmPassword;
	private Spinner spnState;
	private Spinner spnCountry;
	private Button btnSubmit;
	private ImageView imgBack;
	private CheckBox chkTermsAndCondition;

	private String strFirstName = "";
	private String strLastName = "";
	private String strEmailId = "";
	private String strPhoneNumber = "";
	private String strPassword = "";
	private String strConfirmPassword = "";
	private String strCity = "";

	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

	ProgressDialog mProgressDialog;
	public ArrayAdapter<String> adapter;
    SessionManager sessionmanger;
	public List<String> stateList = new ArrayList<String>();
	List<String> countryList = new ArrayList<String>();

	public static List<String> usaStateList = new ArrayList<String>();
	public static List<String> canadaStateList = new ArrayList<String>();
	public static List<StateList> dataStateList = new ArrayList<StateList>();

	public List<ParseObject> countryObject;
	public List<ParseObject> stateObject;
	ProgressDialog pDialog = null;

	private EditText edtCity;
	private EditText edtAddress;
	protected String strAddress = "";
	private EditText edtPostalCode;
	protected String strPostalCode = "";
	protected String strSelectedCountry = "";
	protected String strSelectedState = "";
	private Button btnSignUpFacebook;
	private final Provider[] providers = new Provider[] { Provider.FACEBOOK };
	public String providerName;
	private String strEmail;
	private TextView txtTermAndCondition;
	private static SocialAuthAdapter adapter1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 0);

		setContentView(R.layout.activity_sign_up);
		initialiseControl();
		// getListForCountryAndState();
		adapter1 = new SocialAuthAdapter(new ResponseListener());
		dataStateList = MyApplication.dataStateList;
		countryList = MyApplication.countryList;

		if (dataStateList.size() < 1 || countryList.size() < 1) {
			if (CommanMethods.isConnected(getBaseContext())) {
				callMethodToGetData();
			}
		} else {
			setCountryAdapter();
			addStateForCountryIdOne();
			addStateForCountryIdTwo();
		}

	}

	private void callMethodToGetData() {

		if (CommanMethods.isConnected(getBaseContext())) {
			new RemoteDataTask().execute();
		}

	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {

			pDialog = new ProgressDialog(SignUp.this);
			pDialog.setMessage("Please Wait... Fetching List"); // typically

			// will define
			// such
			// strings in a remote file.
			pDialog.setCancelable(false);
			pDialog.setIndeterminate(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			// Locate the class table named "Country" in Parse.com
			ParseQuery<ParseObject> country = new ParseQuery<ParseObject>(
					"Countries");
			ParseQuery<ParseObject> states = new ParseQuery<ParseObject>(
					"States");

			country.orderByDescending("_created_at");
			states.orderByAscending("Name");
			try {
				countryObject = country.find();
				stateObject = states.find();

			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			stopProcessBar();

			dataStateList.clear();
			countryList.clear();
			// Retrieve object "name" from Parse.com database
			for (ParseObject country : countryObject) {
				countryList.add((String) country.get("Name"));
			}

			for (ParseObject stateName : stateObject) {
				// stateList.add((String) stateName.get("Name"));

				StateList sList = new StateList();
				sList.setStateName(stateName.get("Name").toString());
				sList.setCountryId(stateName.get("countryId").toString());
				dataStateList.add(sList);

			}
			setCountryAdapter();
			addStateForCountryIdOne();
			addStateForCountryIdTwo();

		}
	}

	public void addStateForCountryIdTwo() {

		for (int i = 0; i < dataStateList.size(); i++) {

			if (dataStateList.get(i).getCountryId().equals("2")) {

				usaStateList
						.add(dataStateList.get(i).getStateName().toString());

			}
		}

	}

	public void addStateForCountryIdOne() {

		for (int i = 0; i < dataStateList.size(); i++) {

			if (dataStateList.get(i).getCountryId().equals("1")) {

				canadaStateList.add(dataStateList.get(i).getStateName()
						.toString());

			}
		}

	}

	private void setCountryAdapter() {

		ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, countryList);
		countryAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCountry.setAdapter(countryAdapter);

	}

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
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

	private void initialiseControl() {

		edtFirstName = (EditText) findViewById(R.id.edt_first_name);
		edtLastName = (EditText) findViewById(R.id.edt_last_name);
		edtEmailId = (EditText) findViewById(R.id.edt_emailid_signup);
		edtPhoneNumber = (EditText) findViewById(R.id.edt_phone_number);
		edtAddress = (EditText) findViewById(R.id.edt_address);
		edtCity = (EditText) findViewById(R.id.edt_city);
		edtPostalCode = (EditText) findViewById(R.id.edt_postal_code);
		edtPassword = (EditText) findViewById(R.id.edt_password_signup);
		edtConfirmPassword = (EditText) findViewById(R.id.edt_confirm_password);
		imgBack = (ImageView) findViewById(R.id.img_back_from_signup);
		spnState = (Spinner) findViewById(R.id.spn_state);
		spnCountry = (Spinner) findViewById(R.id.spn_country);
		chkTermsAndCondition = (CheckBox) findViewById(R.id.chk_terms_and_condition);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SignUp.this, SignInOption.class);
				startActivity(intent);
				finish();
			}
		});

		spnCountry.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				strSelectedCountry = spnCountry.getItemAtPosition(position)
						.toString();
				if (strSelectedCountry.equalsIgnoreCase("Canada")) {

					setSateAdapter(canadaStateList);

				} else {

					setSateAdapter(usaStateList);

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spnState.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				strSelectedState = spnState.getItemAtPosition(position)
						.toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		txtTermAndCondition = (TextView) findViewById(R.id.txt_term_and_condition);
		txtTermAndCondition.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(SignUp.this, TermAndCondition.class);
				startActivity(intent);
				finish();

			}
		});

		btnSubmit = (Button) findViewById(R.id.btn_submit_signup);

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				strFirstName = edtFirstName.getText().toString();
				strLastName = edtLastName.getText().toString();
				strEmailId = edtEmailId.getText().toString();
				strPhoneNumber = edtPhoneNumber.getText().toString();
				strAddress = edtAddress.getText().toString();
				strCity = edtCity.getText().toString();
				strPostalCode = edtPostalCode.getText().toString();
				strPassword = edtPassword.getText().toString();
				strConfirmPassword = edtConfirmPassword.getText().toString();

				if (strFirstName.equals("")) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Enter First Name");
					return;
				}

				if (strLastName.equals("")) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Enter Last Name");
					return;
				}

				if (strEmailId.equals("")) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Please Enter email");
					return;
				}

				if (!emailValidator(strEmailId)) {
					CommanMethods.myCustomToast(SignUp.this,
							"Enter Valid email");
					return;
				}

				if (strPhoneNumber.equals("")) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Enter Phone Number");
					return;
				}

				if (strPhoneNumber.length() < 10
						|| strPhoneNumber.length() > 11) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Enter Valid Phone Number");
					return;
				}

				if (strAddress.equals("")) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Enter Address ");
					return;
				}

				if (strCity.equals("")) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Enter City");
					return;
				}

				if (strPostalCode.equals("")) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Enter Postal Code");
					return;
				}

				if (strPassword.equals("")) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Please Enter Password");
					return;
				}

				if (strConfirmPassword.equals("")) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Please Enter Confirm Password");
					return;
				}

				if (strSelectedCountry.equals("")) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Please select Country");
					return;
				}

				if (strSelectedState.equals("")) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Please select State");
					return;
				}

				if (!chkTermsAndCondition.isChecked()) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Please Accept Terms And Condition");
					return;
				}

				if (!CommanMethods.isConnected(getApplicationContext())) {
					CommanMethods.myCustomToast(getApplicationContext(),
							"Please Check Internet Connection");
					return;

				} else {

					startProcessBar();

					ParseUser user = new ParseUser();
					user.setUsername(strEmailId);
					user.setPassword(strPassword);
					user.put("email", strEmailId);
					user.put("firstName", strFirstName);
					user.put("lastName", strLastName);
					user.put("phone", strPhoneNumber);
					user.put("postalCode", strPostalCode);
					user.put("address", strAddress);
					user.put("city", strCity);
					user.put("country", strSelectedCountry);
					user.put("state", strSelectedState);

					user.signUpInBackground(new SignUpCallback() {

						@Override
						public void done(ParseException e) {
							stopProcessBar();
							if (e == null) {
								// Show a simple Toast message upon successful
								// registration
								sessionmanger= new SessionManager(SignUp.this);
								
								sessionmanger.createlogin(strEmailId,strPassword);
								Intent intent = new Intent(SignUp.this,
										MainActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
										| Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
								((MyApplication) getApplicationContext())
										.saveLoginStatus(true);
								Toast.makeText(getApplicationContext(),
										"Successfully Logged in",
										Toast.LENGTH_LONG).show();

							} else {

								Toast.makeText(getApplicationContext(),
										"Username Already Exist",
										Toast.LENGTH_LONG).show();

							}
						}

					});

				}

			}
		});

		btnSignUpFacebook = (Button) findViewById(R.id.btn_option_signup_facebook);
		btnSignUpFacebook.setOnClickListener(new OnClickListener() {

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

	}

	protected void loginToFB() {
		// TODO Auto-generated method stub
		if (CommanMethods.isConnected(getApplicationContext())) {
			adapter1.authorize(SignUp.this, providers[0]);
		}

	}

	public void Events(String providerName2) {

		adapter1.getUserProfileAsync(new ProfileDataListener());

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
			sessionmanger= new SessionManager(SignUp.this);
		
			if (profileMap != null) {
				strEmail = profileMap.getEmail();
				strFirstName = profileMap.getFirstName();
				strLastName = profileMap.getLastName();

				if (!strEmail.isEmpty()) {
					sessionmanger.createlogin(strEmailId,strPassword);
					Intent intentSignIN = new Intent(SignUp.this,
							MainActivity.class);
					startActivity(intentSignIN);
					((MyApplication) getApplicationContext())
							.saveLoginStatus(true);
					CommanMethods.myCustomToast(getApplicationContext(),
							"Login Successful");
					SignUp.this.finish();

				}

			} else {
				Toast.makeText(getApplicationContext(),
						"Person information is null", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void startProcessBar() {

		pDialog = new ProgressDialog(SignUp.this);
		pDialog.setMessage("Signing Up..."); // typically

		// will define
		// such
		// strings in a remote file.
		pDialog.setCancelable(false);
		pDialog.setIndeterminate(false);
		pDialog.show();

	}

	private void stopProcessBar() {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
		}

	}

	protected void setSateAdapter(List<String> stateList) {

		ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, stateList);
		stateAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnState.setAdapter(stateAdapter);
	}

	protected boolean emailValidator(String email) {
		Pattern pattern;
		Matcher matcher;
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[a-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}
	@Override
	public void onBackPressed() {
		Intent intentSignIN = new Intent(SignUp.this, SignInOption.class);
		startActivity(intentSignIN);
		finish();
	}

}

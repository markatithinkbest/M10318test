package tcnr18.com.m10318test;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

public class Maccount extends Activity {
	public final static String LOG_TAG = "markchen987";
	TextView status;
	EditText etEmail;
	EditText etPassword;
	EditText etUsername;
	EditText etGup;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maccount);

		status = (TextView) findViewById(R.id.status);
		etEmail = (EditText) findViewById(R.id.email);
		etPassword = (EditText) findViewById(R.id.password);
		etUsername = (EditText) findViewById(R.id.username);
		etGup = (EditText) findViewById(R.id.gup);
//			String strStatus = "";
//
//		if (Envir.eIsLogged) {
//			strStatus += "" + Envir.email;
//			strStatus += " �էO��:" + Envir.eActiveGroup;
//
//		} else {
//			strStatus += "���n�J";
//
//		}
//
//		status.setText(strStatus);
	}

	public void onClickLogin(View view) {
		String email = etEmail.getText().toString();
		String password = etPassword.getText().toString();
		Toast.makeText(getApplicationContext(), " " + email + " " + password,
				Toast.LENGTH_SHORT).show();
		//
		// String username = usernameField.getText().toString();
		// String password = passwordField.getText().toString();
		// method.setText("Get Method");
		// new SigninActivity(this,status,role,0).execute(username,password);
		
		Envir.eIsLogged=false;
//		Envir.eActiveGroup=	gup;	
		
		new SigninActivity().execute(email, password);

	}

	public class SigninActivity extends AsyncTask<String, Void, String> {

		private TextView statusField, roleField;
		private Context context;
		private int byGetOrPost = 0;

		// flag 0 means get and 1 means post.(By default it is get.)
		// public SigninActivity(Context context, TextView statusField,
		// TextView roleField, int flag) {
		// this.context = context;
		// this.statusField = statusField;
		// this.roleField = roleField;
		// byGetOrPost = flag;
		// }

		@Override
		protected String doInBackground(String... arg0) {
			try {
				String email = (String) arg0[0];
				String password = (String) arg0[1];
				String link = Common.LOGIN_URL + "email=" + email
						+ "&password=" + password;
				URL url = new URL(link);
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet();
				request.setURI(new URI(link));
				HttpResponse response = client.execute(request);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));

				StringBuffer sb = new StringBuffer("");
				String line = "";
				while ((line = in.readLine()) != null) {
					sb.append(line);
					break;
				}
				in.close();
				Log.d(LOG_TAG, "##########" + sb.toString());
				return sb.toString();
			} catch (Exception e) {
				Log.d(LOG_TAG, "####@#$%######?????????????????");
				return null;
				// return new String("Exception: " + e.getMessage());
			}

		}

		private void loginFailed() {
			Envir.eIsLogged = false;
		}

		@Override
		protected void onPostExecute(String result) {
			// this.statusField.setText("Login Successful");
			// this.roleField.setText(result);

			JSONArray jsonArray = null;
			try {
				jsonArray = new JSONArray(result);
			} catch (JSONException e) {
				e.printStackTrace();
				loginFailed();
				return;

			} catch (Exception e) {
				loginFailed();
				return;
			}

			if (jsonArray.length() != 1) {
				Log.d(LOG_TAG, "XXXX login failed XXXX");
				loginFailed();
				return;
			}

			Log.d(LOG_TAG, "@@@ login OKAY @@@");
			try {
				JSONObject jsonObject = jsonArray.getJSONObject(0);

				// *** NEED TO PAY ATTENTION TO ID
				int member_id = jsonObject.getInt("id"); // ###

				String username = jsonObject
						.getString(MembersProvider.COLUMN_USERNAME);
				String email = jsonObject
						.getString(MembersProvider.COLUMN_EMAIL);
				String gup = jsonObject.getString(MembersProvider.COLUMN_GROUP);

				Envir.id=member_id;
				Envir.username=username;
				Envir.gup=gup;
				
				etUsername.setText(username);
				etGup.setText(gup);
				Envir.eIsLogged=true;
				Envir.eActiveGroup=	gup;			
				
				// logged.setText(username);
				// grpId.setText(grp);

				int cnt = getContentResolver().delete(
						MembersProvider.CONTENT_URL, "", null);
				Log.d(LOG_TAG, "just update grp ,delete cnt= " + cnt);

				// updateSpinner();

				Log.d(LOG_TAG, "===> " + member_id + "," + username + "," + ","
						+ email + "," + gup);

			} catch (JSONException e) {
				e.printStackTrace();
				loginFailed();
				return;
			} catch (Exception e) {
				loginFailed();
				return;
			}

		}

	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.maccount, menu);
	// return true;
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// int id = item.getItemId();
	// if (id == R.id.action_settings) {
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }
}

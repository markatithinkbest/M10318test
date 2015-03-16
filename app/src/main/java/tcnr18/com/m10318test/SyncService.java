package tcnr18.com.m10318test;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SyncService extends IntentService {
	// ### NEED TO CHANGE TO YOUR DOMAIN
	// final String LIST_URL = "http://opensource-forever.com/final/list.php";
	// //Mark
	// final String BASE_URL ="http://mamamag77.er-webs.com/list.php"; // Maggie

	public final static String LOG_TAG = "markchen987";
	static String oldRaw = "";

	public SyncService() {
		super("SyncService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(LOG_TAG,
				"... GOING TO FETCH DATA   ***********************************");
		String raw = fetchCloudData();
		// if (raw.equals(oldRaw)) {
		// Log.d(LOG_TAG, "...%%% CONTENT NO CHANGE, NO NEED TO TOUCH SQLITE");
		// return;
		// }
		//
		// //
		// oldRaw = raw;

		//
		updateSQLite(raw);
	}

	private String fetchCloudData() {
		String result = null;
		Log.d(LOG_TAG, "Starting sync");

		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;

		// String forecastJsonStr = null;
		try {
			// final String BASE_URL =
			String testing= "http://opensource-forever.com/final/list.php";

			URL url = new URL(Common.LIST_URL);
//			URL url = new URL(testing);

			// Create the request to OpenWeatherMap, and open the connection
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();

			// Read the input stream into a String
			InputStream inputStream = urlConnection.getInputStream();
			StringBuffer buffer = new StringBuffer();
			if (inputStream == null) {
				// Nothing to do.
				return null;
			}
			reader = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			while ((line = reader.readLine()) != null) {
				// Since it's JSON, adding a newline isn't necessary (it won't
				// affect parsing)
				// But it does make debugging a *lot* easier if you print out
				// the completed
				// buffer for debugging.
				buffer.append(line + "\n");
			}

			if (buffer.length() == 0) {
				return null;
			}
			result = buffer.toString();
			Log.e(LOG_TAG, "result => " + result);
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error ", e);
			e.printStackTrace();
		} catch (Exception e) {
			Log.e(LOG_TAG,
					"^^^^^^^^^^^^^ connection failed ^^^^^^^^^^^^^^^^^^^^^Error ",
					e);
			e.printStackTrace();

		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					Log.e(LOG_TAG, "Error closing stream", e);
				}
			}
		}
		return result;
	}

	private void updateSQLite(String str) {
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(str);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (jsonArray.length() < 1)
			return;
		int cnt = getContentResolver().delete(MembersProvider.CONTENT_URL, "",
				null);
		Log.d(LOG_TAG, "delete cnt= " + cnt);

		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject jsonObject = jsonArray.getJSONObject(i);

				// *** NEED TO PAY ATTENTION TO ID
				int member_id = jsonObject.getInt("id"); // ###

				// `id` int(11) NOT NULL auto_increment,
				// `username` varchar(20) NOT NULL,
				// `password` varchar(32) NOT NULL,
				// `email` varchar(30) NOT NULL,
				// `authcode` varchar(8) default NULL,
				// `state` char(1) NOT NULL default '0',
				// `gup` varchar(10) NOT NULL,
				// `address1` double default NULL,
				// `address2` double default NULL,
				String username = jsonObject
						.getString(MembersProvider.COLUMN_USERNAME);
				String password = jsonObject
						.getString(MembersProvider.COLUMN_PASSWORD);
				String email = jsonObject
						.getString(MembersProvider.COLUMN_EMAIL);
				String authcode = jsonObject
						.getString(MembersProvider.COLUMN_AUTHCODE);
				String state = jsonObject
						.getString(MembersProvider.COLUMN_STATE);
				String gup = jsonObject.getString(MembersProvider.COLUMN_GUP);
				String address1 = jsonObject
						.getString(MembersProvider.COLUMN_ADDRESS1);
				String address2 = jsonObject
						.getString(MembersProvider.COLUMN_ADDRESS2);

				Log.d(LOG_TAG, "===> " + member_id + "," + username + ","
						 + "," + email + "," + gup);
				ContentValues values = new ContentValues();

				values.put(MembersProvider.COLUMN_MEMBERID, member_id);
				values.put(MembersProvider.COLUMN_USERNAME, username);
				values.put(MembersProvider.COLUMN_PASSWORD, password);
				values.put(MembersProvider.COLUMN_EMAIL, email);
				values.put(MembersProvider.COLUMN_AUTHCODE, authcode);
				values.put(MembersProvider.COLUMN_STATE, state);
				values.put(MembersProvider.COLUMN_GUP, gup);
				values.put(MembersProvider.COLUMN_ADDRESS1, address1);
				values.put(MembersProvider.COLUMN_ADDRESS2, address2);
				
				// Provides access to other applications Content Providers
				Uri uri = getContentResolver().insert(
						MembersProvider.CONTENT_URL, values);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

}

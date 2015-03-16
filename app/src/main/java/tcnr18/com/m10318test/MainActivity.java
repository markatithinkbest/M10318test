//package tcnr18.com.m10318test;
//
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.URI;
//import java.net.URL;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.SimpleCursorAdapter;
//import android.widget.Spinner;
//import android.widget.SpinnerAdapter;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//public class MainActivity extends Activity {
//    final String LOGIN_URL = "http://opensource-forever.com/final/login.php?"; //Mark
//
//    public final static String LOG_TAG = "markchen987";
//
//    private Spinner spinner;
//    private TextView username;
//    private TextView password;
//    private TextView grpId;
//    private TextView logged;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        spinner = (Spinner) findViewById(R.id.spinner);
//        username = (TextView) findViewById(R.id.username);
//        password = (TextView) findViewById(R.id.password);
//        logged = (TextView) findViewById(R.id.logged);
//        grpId = (TextView) findViewById(R.id.grpId);
//
//
//        // start auto sync, MySQL -> SQLite
//        startAlarmRepeating();
//
//        // UI Spinner with SQLite data
//        updateSpinner();
//
//    }
//
//    //
//    private void updateSpinner() {
//
//        int intGrpId = 0;
//
//        try {
//            intGrpId = Integer.parseInt(grpId.getText().toString());
//        } catch (Exception ex) {
//            Log.d(LOG_TAG, "!!!!!!!!!!!!!!!!!!!!!!!!!!!11NOT INTEGER");
//            return;
//        }
//
//        Cursor mCursor = getMembers(intGrpId);
//        startManagingCursor(mCursor);
//        SpinnerAdapter adapter = new SimpleCursorAdapter(
//                this,
//                android.R.layout.simple_list_item_2,
//                mCursor,
//                new String[]{
//                        MembersProvider.COLUMN_DISPLAY_NAME,
//                        MembersProvider.COLUMN_EMAIL
//                },
//                new int[]{android.R.id.text1, android.R.id.text2});
//
//        spinner.setAdapter(adapter);
//
//    }
//
//    //
//    private Cursor getMembers(int grpId) {
//        Uri uri = MembersProvider.CONTENT_URL;
//        String[] projection = new String[]{
//                MembersProvider.COLUMN_ID,
//                MembersProvider.COLUMN_DISPLAY_NAME,
//                MembersProvider.COLUMN_EMAIL};
//        //
//        String selection = MembersProvider.COLUMN_GRP + " = " + grpId;
//
//        String[] selectionArgs = null;
//        String sortOrder = MembersProvider.COLUMN_DISPLAY_NAME;
//
//        return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    // Using AlarmManager to repeat SyncService
//    private void startAlarmRepeating() {
//        Intent intent = new Intent(this, SyncReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(
//                Context.ALARM_SERVICE);
//
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME
//                , 0      // run first time immediately
//                , 15000  // repeat every 15 seconds
//                , pendingIntent);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//
//        if (id == R.id.action_settings) {
//            Toast.makeText(getApplicationContext(), "NO FUNCTION...action_settings", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        if (id == R.id.action_sync) {
//            Toast.makeText(getApplicationContext(), "NO FUNCTION...action_sync", Toast.LENGTH_SHORT).show();
//
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void onClickLogin(View view) {
//        String mUsername = username.getText().toString();
//        String mPassword = password.getText().toString();
//        Toast.makeText(getApplicationContext(), " " + mUsername + " " + mPassword, Toast.LENGTH_SHORT).show();
////
////        String username = usernameField.getText().toString();
////        String password = passwordField.getText().toString();
////        method.setText("Get Method");
////        new SigninActivity(this,status,role,0).execute(username,password);
//        new SigninActivity().execute(mUsername, mPassword);
//
//
//    }
//
////http://www.tutorialspoint.com/android/android_php_mysql.htm
//
//    public class SigninActivity extends AsyncTask<String, Void, String> {
//
//        private TextView statusField, roleField;
//        private Context context;
//        private int byGetOrPost = 0;
//
//        //flag 0 means get and 1 means post.(By default it is get.)
////        public SigninActivity(Context context, TextView statusField,
////                              TextView roleField, int flag) {
////            this.context = context;
////            this.statusField = statusField;
////            this.roleField = roleField;
////            byGetOrPost = flag;
////        }
//
//        @Override
//        protected String doInBackground(String... arg0) {
//            try {
//                String username = (String) arg0[0];
//                String password = (String) arg0[1];
//                String link = LOGIN_URL +
//                        "username=" + username +
//                        "&password=" + password;
//                URL url = new URL(link);
//                HttpClient client = new DefaultHttpClient();
//                HttpGet request = new HttpGet();
//                request.setURI(new URI(link));
//                HttpResponse response = client.execute(request);
//                BufferedReader in = new BufferedReader
//                        (new InputStreamReader(response.getEntity().getContent()));
//
//                StringBuffer sb = new StringBuffer("");
//                String line = "";
//                while ((line = in.readLine()) != null) {
//                    sb.append(line);
//                    break;
//                }
//                in.close();
//                Log.d(LOG_TAG, "##########" + sb.toString());
//                return sb.toString();
//            } catch (Exception e) {
//                Log.d(LOG_TAG, "####@#$%######?????????????????");
//                return null;
//             //   return new String("Exception: " + e.getMessage());
//            }
//
//        }
//
//        private void loginFailed(){
//            logged.setText("<none>");
//            grpId.setText("0");
//            spinner.setAdapter(null);
//        }
//
//
//
//
//
//        @Override
//        protected void onPostExecute(String result) {
////            this.statusField.setText("Login Successful");
////            this.roleField.setText(result);
//
//
//            JSONArray jsonArray = null;
//            try {
//                jsonArray = new JSONArray(result);
//            } catch (JSONException e) {
//                e.printStackTrace();
//                loginFailed();
//                return;
//
//            }catch (Exception e){
//                loginFailed();
//                return;
//            }
//
//            if (jsonArray.length() != 1) {
//                Log.d(LOG_TAG, "XXXX login failed XXXX");
//                loginFailed();
//                return;
//            }
//
//            Log.d(LOG_TAG, "@@@ login OKAY @@@");
//            try {
//                JSONObject jsonObject = jsonArray.getJSONObject(0);
//
//                // *** NEED TO PAY ATTENTION TO ID
//                int member_id = jsonObject.getInt(MembersProvider.COLUMN_ID); //###
//
//                String username = jsonObject.getString(MembersProvider.COLUMN_USERNAME);
//                String displayName = jsonObject.getString(MembersProvider.COLUMN_DISPLAY_NAME);
//                String email = jsonObject.getString(MembersProvider.COLUMN_EMAIL);
//                String grp = jsonObject.getString(MembersProvider.COLUMN_GRP);
//                logged.setText(username);
//                grpId.setText(grp);
//
//                int cnt = getContentResolver().delete(MembersProvider.CONTENT_URL, "", null);
//                Log.d(LOG_TAG, "just update grp ,delete cnt= " + cnt);
//
//                updateSpinner();
//
//                Log.d(LOG_TAG, "===> " + member_id + "," + username + "," + displayName + "," + email + "," + grp);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                loginFailed();
//                return ;
//            }catch(Exception e){
//                loginFailed();
//                return ;
//            }
//
//        }
//    }
//}

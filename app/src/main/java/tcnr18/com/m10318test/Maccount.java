package tcnr18.com.m10318test;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    TextView txtStatus;
    TextView txtWho;
    EditText txtEmail;
    EditText txtPassword;
    EditText txtUsername;
    EditText txtGroup;
    TextView lblUsername;
    TextView lblGroup;
    Button btnLogin ;
    Button btnEdit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maccount);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnEdit = (Button) findViewById(R.id.btnEdit);



        setTitle(getString(R.string.menu1));
        txtWho = (TextView) findViewById(R.id.txtWho);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        lblUsername = (TextView) findViewById(R.id.lblUsername);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        lblGroup = (TextView) findViewById(R.id.lblGroup);
        txtGroup = (EditText) findViewById(R.id.txtGroup);

        //
        txtWho.setText(Envir.username);
        lblUsername.setVisibility(View.INVISIBLE);
        txtUsername.setVisibility(View.INVISIBLE);
        lblGroup.setVisibility(View.INVISIBLE);
        txtGroup.setVisibility(View.INVISIBLE);
        btnEdit.setVisibility(View.INVISIBLE);

    }

    public void onClickLogin(View view) {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        txtStatus.setText("");
        if (email.length() == 0 || password.length() == 0) {
            txtStatus.setText(getString(R.string.email_password_required));
            return;
        }
        new LoginActivity().execute(email, password);

    }

    public class LoginActivity extends AsyncTask<String, Void, String> {

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
                return sb.toString();
            } catch (Exception e) {
                Log.d(LOG_TAG, "!@#$%exception " + e.toString());
                return null;
            }

        }

        private void loginFailed() {
            txtStatus.setText(getString(R.string.login_failed));
        }

        @Override
        protected void onPostExecute(String result) {
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
                loginFailed();
                return;
            }

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                // *** NEED TO PAY ATTENTION TO ID
                int member_id = jsonObject.getInt("id"); // ###

                String username = jsonObject
                        .getString(MembersProvider.COLUMN_USERNAME);
                String email = jsonObject
                        .getString(MembersProvider.COLUMN_EMAIL);
                String gup = jsonObject.getString(MembersProvider.COLUMN_GROUP);

                Envir.id = member_id;
                Envir.username = username;
                Envir.gup = gup;


                txtWho.setText(Envir.username);
                txtUsername.setText(username);
                txtGroup.setText(gup);
                //	Envir.eIsLogged=true;
                Envir.eActiveGroup = gup;

                txtStatus.setText(getString(R.string.login_successfully));


                //
                btnLogin.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);

                txtStatus.setVisibility(View.GONE);
                txtWho.setText(Envir.username);
                //
                lblUsername.setVisibility(View.VISIBLE);
                txtUsername.setVisibility(View.VISIBLE);
                lblGroup.setVisibility(View.VISIBLE);
                txtGroup.setVisibility(View.VISIBLE);

                // *** EDIT ***
                // http://tcnr18mark.er-webs.com/m_update.php?id=1&email=xxx@example.com&username=xxxyyy&gup=3&password=123

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

}

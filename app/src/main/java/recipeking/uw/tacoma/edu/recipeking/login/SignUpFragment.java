package recipeking.uw.tacoma.edu.recipeking.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import recipeking.uw.tacoma.edu.recipeking.R;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    private static final String ADD_USER_URL =
            "http://cssgate.insttech.washington.edu/~_450bteam7/adduser.php?";

    private EditText mUsername;
    private EditText mPassword;

    private AddUserTask mAddUserTask;


    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignUpFragment.
     */
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mUsername = (EditText) view.findViewById(R.id.username_signup);
        mPassword = (EditText) view.findViewById(R.id.password_signup);
        Button signUp = (Button) view.findViewById(R.id.button_signup);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });

        return view;
    }

    private void attemptSignUp() {
//        if (mAddUserTask != null) {
//            return;
//        }

        mUsername.setError(null);
        mPassword.setError(null);

        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (password.isEmpty() || !isPasswordValid(password)) {
            if(password.isEmpty()) {
                mPassword.setError(getString(R.string.error_field_required));
            } else {
                mPassword.setError(getString(R.string.error_invalid_password));
            }

            focusView = mPassword;
            cancel = true;
        }

        // Check for a empty username field.
        if (username.isEmpty() || isUsernameInvalid(username)) {
            if(username.isEmpty()) {
                mUsername.setError(getString(R.string.error_field_required));
            } else {
                mUsername.setError(getString(R.string.sign_up_username_invalid));
            }

            focusView = mUsername;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt sign up and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            String url = buildAddUserUrl(username, password);
            mAddUserTask = new AddUserTask();
            mAddUserTask.execute(new String[]{url});
        }

    }

    private boolean isUsernameInvalid(String username) {
        return username.contains(" ") || username.length() > 20;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    private String buildAddUserUrl(String username, String password) {
        StringBuilder sb = new StringBuilder(ADD_USER_URL);

        try {

            sb.append("username=");
            sb.append(username);

            sb.append("&password=");
            sb.append(password);

            Log.i("SignUpFragment", sb.toString());

        } catch(Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Something wrong with the url" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return sb.toString();
    }


    private class AddUserTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;

            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to add user, Reason: " + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject obj = new JSONObject(result);
                String r = obj.getString("result");

                if (r.equals("success")) {
                    //Open MAIN ACTIVITY and pass the USER to it
                    Toast.makeText(getActivity().getApplicationContext(), "You have been " +
                            "registered " +
                            "successfully", Toast.LENGTH_LONG).show();

                    getActivity().getSupportFragmentManager().popBackStackImmediate();

                } else {
                    String errorMsg = obj.getString("error");
                    Toast.makeText(getActivity().getApplicationContext(), errorMsg, Toast
                            .LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Something wrong with the " +
                        "data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

}

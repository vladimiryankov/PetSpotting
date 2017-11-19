package com.vladimiryankov.petspotting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener {
    EditText usernameView, passView;
    TextView switchLogView;
    Button logBtn;

    public void goHome(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

    public void hideKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }

    public void onSwitch(View view){
        switch (switchLogView.getText().toString()) {
            case "or Signup":
                logBtn.setText("Signup");
                switchLogView.setText("or Login");
                break;
            case "or Login":
                logBtn.setText("Login");
                switchLogView.setText("or Signup");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

            userAction(view);

        }
        return false;
    }

    public void userAction(View view) {
        switch (logBtn.getText().toString()){
            case "Signup":
                // create a new user
                Log.i("Action:", "create new user");

                if (usernameView.getText().toString() == "" ||
                        usernameView.getText().toString().length() < 3 ||
                        passView.getText().toString() == "" ||
                        passView.getText().toString().length() <= 7) {
                    Toast.makeText(this, "Invalid username or password. Password must be at least 7 symbols long", Toast.LENGTH_SHORT).show();
                } else {
                    ParseUser user = new ParseUser();
                    user.setUsername(usernameView.getText().toString());
                    user.setPassword(passView.getText().toString());
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(MainActivity.this, "User saved", Toast.LENGTH_SHORT).show();
                                // switch to maps activity
                                goHome();
                            } else {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }

                break;
            case "Login":
                // try to log user
                Log.i("Action:", "log user in");

                ParseUser.logInInBackground(usernameView.getText().toString(), passView.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null && user != null) {
                            Toast.makeText(MainActivity.this, user.getUsername() + " logged in", Toast.LENGTH_SHORT).show();
                            // switch to maps activity
                            goHome();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Pet Spotting Login");

        usernameView = (EditText) findViewById(R.id.usernameView);
        passView = (EditText) findViewById(R.id.passView);
        switchLogView = (TextView) findViewById(R.id.switchLogView);
        logBtn = (Button) findViewById(R.id.logButton);

        passView.setOnKeyListener(this);

        if (ParseUser.getCurrentUser() != null) {
            Log.i("User", "user logged in");
            goHome();
        } else {
            Log.i("User", "no user");
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}

package giacomo_chiarot_854893.stock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class LoginActivity extends AppCompatActivity {
    Button login;
    static String messageString = "";
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCompleteTextView loginField = (AutoCompleteTextView) findViewById(R.id.email_field);
                String email = loginField.getText().toString();
                EditText passwordField = (EditText) findViewById(R.id.password_field);
                String password = passwordField.getText().toString();
                if (!email.equals("") && !password.equals("")) {
                    String nameSurname = "";
                    String emailUrl = email.replace("@", "%40");
                    ConnectionLogin c = new ConnectionLogin(emailUrl, password);
                    c.start();
                    nameSurname = c.getNameSurname();
                    if (!nameSurname.equals("")) {
                        SharedPreferences sp = getApplicationContext().getSharedPreferences("Login", 0);
                        Editor ed = sp.edit();
                        ed.putString("EmailUrl", emailUrl);
                        ed.putString("Email", email);
                        ed.putString("NameSurname", nameSurname);
                        ed.commit();
                        ProfileActivity.setNameSurname(nameSurname);
                        Intent myIntent = new Intent(v.getContext(), ProfileActivity.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivityForResult(myIntent, 0);
                        finish();
                    }
                    else {
                        TextView errorField = (TextView) findViewById(R.id.error_field);
                        errorField.setText("Wrong email or password");
                    }
                }
                else {
                    TextView errorField = (TextView) findViewById(R.id.error_field);
                    errorField.setText("Missing parameters");
                }
            }
        });
        message = (TextView) findViewById(R.id.message);
        message.setText(messageString);
        messageString = "";
    }

    public static void putMessage(String s) {
        messageString = s;
    }
}

class ConnectionLogin extends Thread {
    String emailUrl;
    String password;
    boolean gotValue;
    String nameSurname;
    String url;

    public ConnectionLogin(String emailUrl, String password) {
        this.emailUrl = emailUrl;
        this.password = password;
        gotValue = false;
        nameSurname = "";
        url = "http://"+IPAddress.ip+"/Stock/NameSurname?email=" + emailUrl + "&password=" + password;
    }

    @Override
    public void run() {
        try {
            URLConnection connection = (new URL(url)).openConnection();
            connection.connect();
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder html = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                html.append(line);
            }
            nameSurname = html.toString();
            inserted();
            in.close();
        }
        catch (Exception e) { }
    }

    public synchronized  String getNameSurname() {
        while (!gotValue) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return nameSurname;
    }

    private synchronized void inserted() {
        gotValue = true;
        notifyAll();
    }
}
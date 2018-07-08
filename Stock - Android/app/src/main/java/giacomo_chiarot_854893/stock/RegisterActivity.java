package giacomo_chiarot_854893.stock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button register = (Button) findViewById(R.id.register_button);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameField = (EditText) findViewById(R.id.name_field);
                EditText surnameField = (EditText) findViewById(R.id.surname_field);
                EditText emailField = (EditText) findViewById(R.id.email_field);
                EditText passwordField = (EditText) findViewById(R.id.password_field);
                String name = nameField.getText().toString();
                String surname = surnameField.getText().toString();
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                if (!name.equals("") && !surname.equals("") && !email.equals("") && !password.equals("")) {
                    String urlEmail = email.replace("@", "%40");
                    ConnectionRegistration cn = new ConnectionRegistration(urlEmail, name, surname, password);
                    cn.start();
                    String ms = cn.waitInserted();
                    if (ms.equals("OK")) {
                        LoginActivity.putMessage("Registration successed");
                        Intent myIntent = new Intent(v.getContext(), LoginActivity.class);
                        startActivityForResult(myIntent, 0);
                        finish();
                    }
                    else if (ms.equals("EXISTS")) {
                        TextView error = (TextView) findViewById(R.id.error_field);
                        error.setText("Email already exists");
                    }
                    else {
                        TextView error = (TextView) findViewById(R.id.error_field);
                        error.setText("Error connection");
                    }
                }
                else {
                    TextView error = (TextView) findViewById(R.id.error_field);
                    error.setText("Missing values");
                }
            }
        });
    }
}

class ConnectionRegistration extends Thread {
    String email, name, surname, password;
    boolean inserted;
    String message;

    public ConnectionRegistration(String email, String name, String surname, String password) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        inserted = false;
        message = "";
    }

    @Override
    public void run() {
        String url = "http://"+IPAddress.ip+"/Stock/Registration?name="+name+"&surname="+surname+"&email="+email+"&password="+password;
        try {
            URLConnection connection = (new URL(url)).openConnection();
            connection.connect();
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder html = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                html.append(line);
            }
            message = html.toString();
            inserted();
            in.close();
        }
        catch (Exception e) { }
    }

    public synchronized String waitInserted() {
        while (!inserted) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    public synchronized void inserted() {
        inserted = true;
        notifyAll();
    }
}
package giacomo_chiarot_854893.stock;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class AddTitleActivity extends AppCompatActivity {
    public static ProfileActivity pa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_title);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("Login", 0);
        final String urlEmail = sp.getString("EmailUrl", null);
        ConnectionMissingTitlesList cmtl = new ConnectionMissingTitlesList(urlEmail);
        cmtl.start();
        String resultSet[] = cmtl.waitResult();
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.list);
        for (int i = 0; i < resultSet.length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(resultSet[i]);
            radioButton.setId(i);
            radioGroup.addView(radioButton);
        }
        Button addTitle = (Button) findViewById(R.id.button_add_title);
        addTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                String title = radioButton.getText().toString();
                String urlTitle = title.replace(" ", "+");
                ConnectionAddTitle cat = new ConnectionAddTitle(urlTitle, urlEmail);
                cat.start();
                cat.waitResult();
                pa.printTable(urlEmail);
                finish();
            }
        });
    }
}

class ConnectionMissingTitlesList extends Thread {
    String urlEmail;
    boolean gotResult;
    String result;

    public ConnectionMissingTitlesList(String urlEmail) {
        this.urlEmail = urlEmail;
        gotResult = false;
        result = "";
    }

    @Override
    public void run() {
        String url = "http://"+IPAddress.ip+"/Stock/ListTitlesMissing?email="+urlEmail;
        try {
            URLConnection connection = (new URL(url)).openConnection();
            connection.connect();
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder html = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                html.append(line);
            }
            result = html.toString();
            in.close();
            inserted();
        }
        catch (Exception e) { }
    }

    public synchronized String[] waitResult() {
        while (gotResult == false) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String[] resultSet = result.split("&");
        return resultSet;
    }

    public synchronized void inserted() {
        gotResult = true;
        notifyAll();
    }
}

class ConnectionAddTitle extends Thread {
    String urlTitle;
    String urlEmail;
    boolean inserted;

    public ConnectionAddTitle(String urlTitle, String urlEmail) {
        this.urlEmail = urlEmail;
        this.urlTitle = urlTitle;
        inserted = false;
    }

    public void run() {
        String url = "http://"+IPAddress.ip+"/Stock/AddTitleAndroid?email="+urlEmail+"&title="+urlTitle;
        try {
            URLConnection connection = (new URL(url)).openConnection();
            connection.connect();
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder html = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                html.append(line);
            }
            String result = html.toString();
            in.close();
            inserted();
        }
        catch (Exception e) { }
    }

    public synchronized void waitResult() {
        while (inserted == false) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void inserted() {
        inserted = true;
        notifyAll();
    }
}

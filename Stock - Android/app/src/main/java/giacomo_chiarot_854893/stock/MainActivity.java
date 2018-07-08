package giacomo_chiarot_854893.stock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button login;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("Login", 0);
        String nameSurname = sp.getString("NameSurname", null);
        if (nameSurname != null) {
            ProfileActivity.setNameSurname(nameSurname);
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            finish();
        }
        else {
            login = (Button) findViewById(R.id.login_button);
            register = (Button) findViewById(R.id.register_button);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(v.getContext(), LoginActivity.class);
                    startActivityForResult(myIntent, 0);
                }
            });
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(v.getContext(), RegisterActivity.class);
                    startActivityForResult(myIntent, 0);
                }
            });
        }
    }
}
package giacomo_chiarot_854893.stock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String nameSurname = "";
    String[] elements;
    ProfileActivity pa = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("Login", 0);
        String urlEmail = sp.getString("EmailUrl", null);
        printTable(urlEmail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView nameSurname = (TextView) headerView.findViewById(R.id.user_NameSurname);
        nameSurname.setText(ProfileActivity.nameSurname);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.exit) {
            SharedPreferences sp = getSharedPreferences("Login", 0);
            Editor ed = sp.edit();
            ed.remove("UrlEmail");
            ed.remove("NameSurname");
            ed.remove("Email");
            ed.commit();
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(myIntent);
            finish();
        }
        else if (id == R.id.addTitle) {
            AddTitleActivity.pa = this;
            Intent myIntent = new Intent(getApplicationContext(), AddTitleActivity.class);
            startActivity(myIntent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void setNameSurname(String nameSurname) {
        ProfileActivity.nameSurname = nameSurname;
    }

    public void printTable(String urlEmail) {
        ScrollView sv = (ScrollView) findViewById(R.id.scrollPanel);
        sv.removeAllViews();
        ConnectionTitles ct = new ConnectionTitles(urlEmail);
        ct.start();
        elements = ct.waitElements();
        String labels[] = {"Title", "Price", "Var%", "Time", "Min", "Max", "Qta"};
        TableLayout table = new TableLayout(this);
        for (int i = 0; i < elements.length; i++) {
            TableRow row = new TableRow(this);
            TextView label = new TextView(this);
            if (i % 7 == 0) {
                label.setTextSize(20);
                label.setText(elements[i]);
                label.setTextColor(getResources().getColor(R.color.colorPrimary));
                label.setTypeface(Typeface.SERIF, Typeface.BOLD);
                label.setOnClickListener(new OpenGraphic(elements[i]));
                row.addView(label);
            }
            else {
                TextView value = new TextView(this);
                label.setText(labels[i % 7] + ":");
                value.setText(elements[i]);
                label.setTextSize(20);
                value.setTextSize(20);
                label.setTextColor(Color.BLACK);
                value.setTextColor(Color.BLACK);
                row.addView(label);
                row.addView(value);
            }
            table.addView(row);
        }
        sv.addView(table);
    }

    class OpenGraphic implements View.OnClickListener {
        String titleName;

        OpenGraphic (String titleName) {
            this.titleName = titleName;
        }

        @Override
        public void onClick(View v) {
            Graphic.titleName = titleName;
            Graphic.profileActivity = pa;
            Intent i = new Intent(v.getContext(), Graphic.class);
            startActivityForResult(i, 0);
        }
    }
}

class ConnectionTitles extends Thread {
    String urlEmail;
    boolean parsed;
    String[] elements;

    public ConnectionTitles(String urlEmail) {
        this.urlEmail = urlEmail;
        parsed = false;
    }

    @Override
    public void run() {
        String url = "http://"+IPAddress.ip+"/Stock/DataTable?email="+urlEmail;
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
            elements = result.split("&");
            inserted();
            in.close();
        }
        catch (Exception e) { }
    }

    public synchronized void inserted() {
        parsed = true;
        notifyAll();
    }

    public synchronized String[] waitElements() {
        while (parsed == false) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return elements;
    }
}
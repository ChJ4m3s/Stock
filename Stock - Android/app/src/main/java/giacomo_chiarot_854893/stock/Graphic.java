package giacomo_chiarot_854893.stock;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Graphic extends AppCompatActivity {
    public static String titleName;
    public static ProfileActivity profileActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("Login", 0);
        final String urlEmail = sp.getString("EmailUrl", null);
        TextView label = (TextView) findViewById(R.id.title);
        label.setText(titleName);
        label.setTextSize(20);
        label.setTypeface(Typeface.SERIF, Typeface.BOLD);
        label.setTextColor(getResources().getColor(R.color.colorPrimary));
        ConnectionGetTitleData cgtd = new ConnectionGetTitleData(titleName);
        cgtd.start();
        String[][] values = cgtd.waitValues();
        LineChart chart = (LineChart) findViewById(R.id.table);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(true);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextColor(Color.BLACK);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setDrawGridLines(true);
        yAxis.setAxisLineColor(Color.BLACK);
        chart.getAxisRight().setEnabled(false);
        List<String> xLabels = new ArrayList<String>();
        for (String[] c : values)
            xLabels.add(c[0]);
        List<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < values.length; i++)
            yValues.add(new Entry(Float.parseFloat(values[i][1]), i));
        LineDataSet dataSet = new LineDataSet(yValues, "Price");
        LineData data = new LineData(xLabels, dataSet);
        chart.setData(data);
        chart.invalidate();
        Button deleteTitle = (Button) findViewById(R.id.deleteTitle);
        deleteTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDeleteTitle cdt = new ConnectionDeleteTitle(titleName, urlEmail);
                cdt.start();
                cdt.waitDeletion();
                profileActivity.printTable(urlEmail);
                finish();
            }
        });
    }
}

class ConnectionGetTitleData extends Thread {
    String title;
    boolean gotValues;
    String values;

    public ConnectionGetTitleData(String title) {
        this.title = title;
        gotValues = false;
        values = "";
    }

    @Override
    public void run() {
        String url = "http://"+IPAddress.ip+"/Stock/GetTitleValues?title=" + title;
        URLConnection connection = null;
        try {
            connection = (new URL(url)).openConnection();
            connection.connect();
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder html = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                html.append(line);
            }
            values = html.toString();
            inserted();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized String[][] waitValues() {
        String[][] result;
        while (gotValues == false) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String aux[] = values.split("&");
        result = new String[aux.length / 2][2];
        for (int i = 0; i < aux.length; i++)
            result[i / 2][i % 2] = aux[i];
        return result;
    }

    public synchronized void inserted() {
        gotValues = true;
        notifyAll();
    }
}

class ConnectionDeleteTitle extends Thread {
    String title, urlEmail;
    boolean deleted;

    public ConnectionDeleteTitle(String title, String urlEmail) {
        this.title = title;
        this.urlEmail = urlEmail;
        deleted = false;
    }

    @Override
    public void run() {
        String urlTitle = title.replace(" ", "+");
        String url = "http://"+IPAddress.ip+"/Stock/DeleteTitle?title=" + urlTitle + "&email=" + urlEmail;
        URLConnection connection = null;
        try {
            connection = (new URL(url)).openConnection();
            connection.connect();
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder html = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                html.append(line);
            }
            String response = html.toString();
            if (response.equals("OK")) {
                inserted();
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void waitDeletion() {
        while (deleted == false) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void inserted() {
        deleted = true;
        notifyAll();
    }
}
package database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseWebsite {
    public static List<String> parse() {
        List<String> parsedRows = new ArrayList<String>();
        try {
            Socket s = new Socket("www.traderlink.it", 80);
            //output
            OutputStream out = s.getOutputStream();
            PrintStream pout = new PrintStream(out);
            //input
            InputStream in = s.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            //GET request
            pout.println("GET /quotazioni/borsa-italiana.php?modo=lista&lista=ftsemib&refresh=1 HTTP/1.1");
            pout.println("Host: www.traderlink.it");
            pout.println("Accept: */*");
            pout.println("Accept-Language: it-it");
            pout.println("User-Agent: Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/124 (KHTML, like Gecko) Safari/125");
            pout.println();
            //read page
            String p;
            StringBuilder resource = new StringBuilder();
            while ((p = br.readLine()) != null) 
                resource.append(p + "\n");  
            //parsing della pagina
            Document d = Jsoup.parse(resource.toString());
            Element table = d.select("table").get(2);
            Elements rows = table.select("tr");
            boolean first = true;
            for (Element e : rows) {
                if (!first) {
                    Element row = e;
                    Elements cols = row.select("td");
                    String rowParsed = "";
                    for (int j = 0; j < cols.size(); j++) {
                        String aux = cols.get(j).text();
                        aux = aux.replace(".", "");
                        aux = aux.replace(",", ".");
                        aux = aux.replace("%", "");
                        if (j != 0) {
                            aux = aux.replace(" ", "");
                            aux = aux.replaceAll("[^0-9./-]", "");
                        }
                        rowParsed = rowParsed + aux + "&";
                    }
                    parsedRows.add(rowParsed);
                }
                else
                    first = false;
            }
            s.close();
        }
        catch (UnknownHostException e) {
            System.out.println("host not found");
        } 
        catch (java.io.IOException e) {
            System.out.println("error");
        }
        return parsedRows;
    }
    public static void main(String[] args) {
        Database db = new Database();
        Connection cn = db.getDb();
        List<String> rows = parse();
        Date date;
        int i = 0;
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        //while (true) {
            //if (sdf.format(new Date(System.currentTimeMillis())).equals("22:00:00")) {               
                date = new Date(System.currentTimeMillis());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ParseWebsite.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (String s : rows) {
                    String[] row = s.split("&");
                    System.out.println(s);
                    String query = 
                              "INSERT INTO Stock(Title, Code, Price, Var, Time, Min, Max, Qta, Date)"
                            + "VALUES(?, DEFAULT, ?, ?, ?, ?, ?, ?, ?)";
                    if (cn != null) {
                        try {
                            PreparedStatement ps = cn.prepareStatement(query);
                            ps.setString(1, row[0]);
                            ps.setFloat(2, ((row[1] != null && !row[1].equals(""))? Float.parseFloat(row[1]) : 0));
                            ps.setFloat(3, ((row[2] != null && !row[2].equals(""))? Float.parseFloat(row[2]) : 0));
                            ps.setString(4, row[3]);
                            ps.setFloat(5, ((row[4] != null && !row[4].equals(""))? Float.parseFloat(row[4]) : 0));
                            ps.setFloat(6, ((row[5] != null && !row[5].equals(""))? Float.parseFloat(row[5]) : 0));
                            ps.setFloat(7, ((row[6] != null && !row[6].equals(""))? Float.parseFloat(row[6]) : 0));
                            ps.setDate(8, date);                           
                            if (ps.executeUpdate() == 1) 
                                i++;
                        } catch (SQLException ex) {
                            System.err.println("error database");
                        }
                    }
                    else 
                        System.err.println("error connection database");
                }
                if (i == 40)
                    try {
                        cn.commit();
                    } catch (SQLException ex) {
                        Logger.getLogger(ParseWebsite.class.getName()).log(Level.SEVERE, null, ex);
                    }
                else 
                    try {
                        cn.rollback();
                    } catch (SQLException ex) {
                        Logger.getLogger(ParseWebsite.class.getName()).log(Level.SEVERE, null, ex);
                    }
                //}
            //}
    }
}
package databasetest;

import java.sql.*;
import org.json.simple.*;

public class DatabaseTest {

    public JSONArray getJSONData(){
        Connection conn = null;
        PreparedStatement pstSelect = null, pstUpdate = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        
        String query, key, value;
        
        
        JSONArray table = new JSONArray();
        
        boolean hasresults;
        int resultCount, columnCount, updateCount = 0;
        
        try {
            
            /* Identify the Server */
            
            String server = ("jdbc:mysql://localhost/p2_test");
            String username = "root";
            String password = "CS310";
            System.out.println("Connecting to " + server + "...");
            
            /* Load the MySQL JDBC Driver */
            
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            /* Open Connection */

            conn = DriverManager.getConnection(server, username, password);

            /* Test Connection */
            
            if (conn.isValid(0)) {
                
                /* Connection Open! */
                
                System.out.println("Connected Successfully!");
            }
            query = "SELECT * FROM people";
                pstSelect = conn.prepareStatement(query);
                
                /* Execute Select Query */
                
                System.out.println("Submitting Query ...");
                
                hasresults = pstSelect.execute();                
                
                /* Get Results */
                
                System.out.println("Getting Results ...");
                
                while ( hasresults || pstSelect.getUpdateCount() != -1 ) {

                    if ( hasresults ) {
                        
                        /* Get ResultSet Metadata */
                        
                        resultset = pstSelect.getResultSet();
                        metadata = resultset.getMetaData();
                        columnCount = metadata.getColumnCount();
                        
                        /* Get Column Names; Print as Table Header */
                        
                        for (int i = 1; i <= columnCount; i++) {

                            key = metadata.getColumnLabel(i);

                            System.out.format("%20s", key);

                        }
                        
                        /* Get Data; Print as Table Rows */
                        
                        while(resultset.next()) {
                            
                            /* Begin Next ResultSet Row */

                            System.out.println();
                            
                            /* Loop Through ResultSet Columns; Print Values */

                            JSONObject row = new JSONObject();
                            for (int i = 2; i <= columnCount; i++) {
                                value = resultset.getString(i);
                                row.put(metadata.getColumnLabel(i), value);
                                if (resultset.wasNull()) {
                                    System.out.format("%20s", "NULL");
                                }

                                else {
                                    System.out.format("%20s", value);
                                    
                                }

                            }
                            table.add(row);

                        }
                        
                    }

                    else {

                        resultCount = pstSelect.getUpdateCount();  

                        if ( resultCount == -1 ) {
                            break;
                        }

                    }
                    
                    /* Check for More Data */

                    hasresults = pstSelect.getMoreResults();

                }
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
        System.out.println();
        System.out.println(table.toString());
        return table;
    }
    
}
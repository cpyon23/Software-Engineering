import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***********************************************************************
* Name:    Search      :   Class                                       *
* Purpose: Search Objects can be supplied with properties that are     *
*              needed to generate a query string. Query strings will   *
*              be used to perform a database and retrieve a list of    *
*              Flight objects/ids that can be returned to the          *
*              SearchFlowController().                                 *
************************************************************************/
public class SearchModel {
    String src_location, dest_location, date, time;
    int availableSeat;
    String price;

    public SearchModel(){}

    /*******************************************************************
    * Name:    Search()    :   Constructor                             *
    * Purpose: Builds a Search object using the required inputs in     *
    *              order to perform a basic search.                    *
    * Params:                                                          *
    *      src - Departure Airport Location.                           *
    *      dest - Destination Airport Location.                        *
    *      d - Date Interval to search.                                *
    *      t - Time Interval to search.                                *
    * Postcondition:                                                   *
    *      Search object has been created.                             *
    ********************************************************************/
    public SearchModel(String src, String dest, String d, String t){
        this.src_location = src;
        this.dest_location = dest;
        this.date = d;
        this.time = t;
    }

    /********************************************************************
     * Query Building Methods                                           *
     * Purpose: These methods build a database query, execute using     *
     *              queryDB method, and send the ResultSet object to    *
     *              the proper handler. Returns results to Controller.  *
     ********************************************************************/

    public String[] getAllAirports(){
        String query_string = ("select short_name, long_name from airports;");
        ResultSet result_set = queryDB(query_string);
        String[] airport_list = handleListQuery(result_set);
        return airport_list;
    }

    public ArrayList<Map> getFlightData(){
        String query_string = ("select f.id, a.short_name, a.long_name, b.short_name, b.long_name, f.dt, f.price "+
                                "from flights as f "+
                                "inner join airports as a on a.id = f.src_id "+
                                "inner join airports as b on b.id = f.dest_id "+
                                "where a.short_name='"+src_location+"' and " +
                                "b.short_name='"+dest_location+"';");
        ResultSet result_set = queryDB(query_string);
        ArrayList<Map> flights = handleFlightQuery(result_set);
        return flights;
    }


    /********************************************************************************
     * Query Handlers                                                               *
     * Purpose:     These methods extract and return data from various ResultSets.  *
     ********************************************************************************/

    private String[] handleListQuery(ResultSet result_set) {

        try{
            ResultSetMetaData md = result_set.getMetaData();
            int columns = md.getColumnCount();
            List<String> items = new ArrayList();

            while (result_set.next()) {
                items.add(result_set.getObject(2).toString()+" ("+
                        result_set.getObject(1).toString()+")");
            }

            String[] string_array = new String[ items.size() ];
            return items.toArray(string_array);

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                result_set.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private ArrayList<Map> handleFlightQuery(ResultSet result_set){
        try{
            ResultSetMetaData md = result_set.getMetaData();
            int columns = md.getColumnCount();
            ArrayList<Map> flight_list = new ArrayList();

            while (result_set.next()){
                Map<String,String> this_flight = new HashMap();
                this_flight.put("flight_id", result_set.getObject(1).toString());
                this_flight.put("source", result_set.getObject(2).toString());
                this_flight.put("source_long", result_set.getObject(3).toString());
                this_flight.put("destination", result_set.getObject(4).toString());
                this_flight.put("destination_long", result_set.getObject(5).toString());
                this_flight.put("datetime", result_set.getObject(6).toString());
                this_flight.put("price", result_set.getObject(7).toString());

                System.out.println(this_flight.toString());
                flight_list.add(this_flight);
            }

            return flight_list;

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try{
                result_set.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /****************************************************************************
     * Name:    queryDB()                                                       *
     * Purpose: Utility Method to connect to SQLite Databse, returns ResultSet. *
     ****************************************************************************/
    private ResultSet queryDB(String query_string) {
        Connection connection = null;
        ResultSet result_set = null;
        Statement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:db/travlr.db");
            statement = connection.createStatement();

            result_set = statement.executeQuery(query_string);

            return result_set;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                result_set.close();
                statement.close();
                connection.close();
            } catch (Exception f) {
                f.printStackTrace();
            }
        }
        return null;
    }
}

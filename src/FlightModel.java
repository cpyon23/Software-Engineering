import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**#######################################################################
 * # Class Name:    Flight
 * # Descriptions:  A Flight is an object and a Start Location and a Destination Location.
 *                  Flights will have Time, Baggage, and a list of Available Seats.
 *                  Seats and Baggage Pricing Ratios will also be included. User should be
 *                  able to create an individual Booking using the variables within a Flight object.
 *
 *  Invariants:
 *                  Baggage is capped to a weight limit.
 *
 *                  getPassengers() < availableSeats
 *                     Number of passengers should never be greater than seats available
 *
 *
 */

public class FlightModel {
    private int flight_id;
    private String start_abbrev;
    private String start_location;
    private String destination_abbrev;
    private String destination_location;
    private String flight_datetime;
    private ArrayList<String> available_seats;
    private double baggage_price;
    private double seat_price;
    private int miles;

    public FlightModel(){};

    public FlightModel(int id){
        this.flight_id = id;
        this.queryFlightInfo(this.flight_id);
    }

    public FlightModel(int id, String start, String end, String datetime, ArrayList<String> seats,
                       float bags, float seat, int inMiles){
        flight_id = id;
        start_location = start;
        destination_location = end;
        flight_datetime = datetime;
        available_seats = seats;
        baggage_price = bags;
        seat_price = seat;
        miles = inMiles;
    }

    public FlightModel(Map<String, String> fdata){
        this.flight_id = Integer.parseInt(fdata.get("flight_id"));
        this.start_location = fdata.get("source");
        this.destination_location = fdata.get("destination");
        this.flight_datetime = fdata.get("datetime");
        this.seat_price = Double.parseDouble(fdata.get("price"));
    }


    private void queryFlightInfo(int flight_id){
        String query_string = ( "select f.id as f_id, air1.short_name as a1_short, "+
                "air2.short_name as a2_short, f.dt as f_dt, f.miles as f_miles, "+
                "f.avail_seats as f_avail, f.bag_price as f_bprice, f.seat_price as f_sprice "+
                "from flights as f " +
                "join airports as air1 on air1.id = f.src_id "+
                "join airports as air2 on air2.id = f.dest_id "+
                "where f.id = "+flight_id+";"
        );
        System.out.println(query_string);
        ResultSet rs = this.queryDB(query_string);

        try{
            while(rs.next()){
                this.setFlightID(Integer.parseInt(rs.getString("f_id")));
                this.setStartLocation(rs.getString("a1_short"));
                this.setDestLocation(rs.getString("a2_short"));
                this.setDateTime(rs.getString("f_dt"));
                this.setMiles(Integer.parseInt(rs.getString("f_miles")));
                this.setSeatPrice(Double.parseDouble(rs.getString("f_sprice")));
                this.setBagPrice(Double.parseDouble(rs.getString("f_bprice")));
                this.setAvailSeats(new ArrayList<String>(Arrays.asList(rs.getString("f_avail").split(","))));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void bookSeat(String seat_id){
        ArrayList<String> update_seat_list = this.getAvailSeats();
        update_seat_list.remove(seat_id);
        this.setAvailSeats(update_seat_list);

        String seats_as_string = seatsAsString(update_seat_list);
        String update_string = ( "update flights set avail_seats='"+seats_as_string+"' "+
                "where id="+Integer.toString(this.flight_id)+";");
        System.out.println(update_string);
        this.insertDB(update_string);
    }

    protected String seatsAsString(ArrayList<String> input_list){
        String return_string = "";

        for (String s: input_list){
            return_string += s + ",";
        }

        return_string = return_string.substring(0, return_string.length()-1);
        return return_string;
    }

    protected ResultSet queryDB(String query_string) {
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
        return result_set;
    }

    private void insertDB(String insert_string) {
        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:db/travlr.db");
            statement = connection.createStatement();

            statement.executeUpdate(insert_string);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                statement.close();
                connection.close();
            } catch (Exception f) {
                f.printStackTrace();
            }
        }
    }

    //** Getter/Setter/Add funcions for Flight
    protected void setFlightID(int f_id){ this.flight_id = f_id; }
    protected int getFlightID(){ return this.flight_id; }
    protected void setStartLocation(String s){ this.start_location = s; }
    protected String getStartLocation(){ return this.start_location; }
    protected void setDestLocation(String d){ this.destination_location = d; }
    protected String getDestLocation(){ return this.destination_location; }
    protected void setDateTime(String dt){ this.flight_datetime = dt; }
    protected String getDateTime(){ return this.flight_datetime; }
    protected void setAvailSeats(ArrayList<String> avail_seats){ this.available_seats = avail_seats; }
    protected void setAvailSeats(String[] avail_seats){ this.available_seats = new ArrayList<String>(Arrays.asList(avail_seats)); }
    protected ArrayList<String> getAvailSeats(){ return available_seats; }
    protected void setBagPrice(double bag_price){ this.baggage_price = bag_price; }
    protected double getBagPrice(){ return this.baggage_price;}
    protected void setSeatPrice(double seat_price){ this.seat_price = seat_price; }
    protected Double getSeatPrice(){ return seat_price; }
    protected void setMiles(int miles){ this.miles = miles; }
    protected int getMiles(){ return this.miles; }

    public String getDestinationShort() {
        return destination_abbrev;
    }

    public void setDestinationShort(String destination_abbrev) {
        this.destination_abbrev = destination_abbrev;
    }

    public String getStartShort() {
        return start_abbrev;
    }

    public void setStartShort(String start_abbrev) {
        this.start_abbrev = start_abbrev;
    }
}

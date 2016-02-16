import com.sun.xml.internal.ws.util.StringUtils;

import java.sql.*;

/**#######################################################################
 * # Class Name:    Booking
 * # Descriptions:  Booking object contains all necessary data relavent to a booking. 
 *     				It creates the connection between the Flight, a (optional) Users Account,
 *        		    and all (optional) Ancillaries
 *
 *  Invariants:
 *                  Credit card is declined.
 *
 *
 */
public class BookingModel {
    private String fullname;
    private String dob;
    private String gender;
    private String address;
    private String state;
    private String city;
    private String zip;
    private String email;
    private String phone;
    private int bags;
    private int account_id;
    private int personal_info_id;
    private int booking_id;
    private int ancillary_pkg_id;
    private int bookings_personal_fk_id;
    private int[] flight_ids;
    private Ancillary[] ancillaries;
    private String[] seat_numbers;

    private String bookings_insert;
    private String personal_info_insert;
    private String booking_personal_fk_insert;

    public BookingModel() {}

    public BookingModel(int flight_id1){
        flight_ids = new int[] {flight_id1};
    }

    public BookingModel(int flight_id1, int flight_id2) {
        flight_ids = new int[] {flight_id1, flight_id2};
    }

    protected void buildBookingInsert(){
        this.bookings_insert = ("update bookings set seat_numbers='1A,2B', bags=2, ancillary_pkg_id=1"+
                " where id="+Integer.toString(this.booking_id)+";");
        System.out.println(this.bookings_insert);
        this.insertDB(this.bookings_insert);
    }

    protected int reserveBookingID(){
        String insert_string = ("insert into bookings(seat_numbers, bags, ancillary_pkg_id) VALUES (null, null, null);");
        this.insertDB(insert_string);
        String query_string = ("select max(id) from bookings;");
        ResultSet rs = queryDB(query_string);
        int id = Integer.parseInt(handleReserveInsert(rs));
        return id;
    }

    protected void buildFlightsBookingsFKInsert(){
        for (int i=0; i<flight_ids.length; i++){
            String insert_string = ("insert into flights_bookings(flight_id, booking_id) VALUES "+
                                    "("+flight_ids[i]+", "+booking_id+");");
            System.out.println(insert_string);
            this.insertDB(insert_string);
        }
    }

    protected void buildPersonalInsert(){
        this.personal_info_insert = ("update personal_info set fullname='"+this.fullname+"', DOB='"+this.dob+"', gender"+
                "='"+this.gender+"', address1='"+this.address+"', city='"+this.city+"', state='"+this.state+"', "+
                "zipcode='"+this.zip+"', phone='"+this.phone + "', email='" + this.email + "' "+
                "where id="+Integer.toString(this.personal_info_id)+";");
        System.out.println(this.personal_info_insert);
        this.insertDB(this.personal_info_insert);
    }

    protected int reservePersonalInfoID(){
        System.out.println(fullname+" : "+dob+" : "+gender+" : "+address+" : "+state+" : "+city+" : "+zip+" : "+email+" : "+phone);
        String insert_string = ("insert into personal_info"+
                                "(fullname, DOB, gender, address1, address2, city, state, zipcode, phone, email) "+
                                "VALUES (null, null, null, null, null, null, null, null, null, null);");
        this.insertDB(insert_string);
        String query_string = ("select max(id) from personal_info;");
        ResultSet rs = queryDB(query_string);
        int id = Integer.parseInt(handleReserveInsert(rs));
        return id;
    }

    protected void buildBookingsPersonalFKInsert(){
        this.booking_personal_fk_insert = ("update bookings_personalinfo set booking_id="+Integer.toString(this.booking_id)+
                ", personal_info_id="+Integer.toString(this.personal_info_id) +
                " where id="+Integer.toString(this.bookings_personal_fk_id)+";");
        System.out.println(this.booking_personal_fk_insert);
        this.insertDB(this.booking_personal_fk_insert);
    }

    protected int reserveBookingsPersonalFK(){
        String insert_string = ("insert into bookings_personalinfo(personal_info_id, booking_id) VALUES (null, null);");
        insertDB(insert_string);
        String query_string = ("select max(id) from bookings_personalinfo;");
        ResultSet rs = queryDB(query_string);
        int id = Integer.parseInt(handleReserveInsert(rs));
        return id;
    }

    protected void buildAccountsBookingsFKInsert(){
        if (account_id == 0){
            System.out.println("No Account Connection found. Attaching Booking to admin account.");
            account_id = 1;
        }
        String account_booking_fk_insert = ("insert into accounts_bookings(account_id, booking_id) VALUES"+
                                            " ("+account_id+", "+booking_id+");");
        System.out.println(account_booking_fk_insert);
        insertDB(account_booking_fk_insert);
    }

    protected String queryCardInfo(){
        String card_info_query = ("select c.* from bookings_cards as bc "+
                "join cards as c on bc.card_id=c.id where booking_id="+booking_id+";");
        ResultSet rs = queryDB(card_info_query);
        String output = handleQueryRequest(rs);
        return output;
    }

    protected String queryBookingInfo(){
        String account_info_query = ("select b.* from bookings as b where booking_id="+booking_id+";");
        System.out.println(account_info_query);
        ResultSet rs = queryDB(account_info_query);
        String output = handleQueryRequest(rs);
        return output;
    }

    protected String queryAccountInfo(){
        String card_info_query = ("select a.* from accounts_bookings as ab join accounts as a on a.id=ab.account_id "+
                " where booking_id="+booking_id+";");
        ResultSet rs = queryDB(card_info_query);
        String output = handleQueryRequest(rs);
        return output;
    }

    protected String queryFlightInfo(){
        String card_info_query = ("select f.id, s.short_name, d.short_name f.dt, f.price from flights_bookings as fb "+
                "join flights as f on f.id=fb.flight_id join airports as s on s.id=f.src_id "+
                "join airports as d on d.id=f.dest_id where booking_id="+booking_id+";");
        ResultSet rs = queryDB(card_info_query);
        String output = handleQueryRequest(rs);
        return output;
    }

    protected String queryPersonalInfo(){
        String card_info_query = ("select p.* from bookings_personalinfo as bp join personal_info as p on p.id=bp.personal_info_id "+
                " where booking_id="+booking_id+";");
        ResultSet rs = queryDB(card_info_query);
        String output = handleQueryRequest(rs);
        return output;
    }

    protected String retrieveOutput(ResultSet rs){
        return rs.toString();
    }

    protected static void cancelBooking(){}

    private String handleQueryRequest(ResultSet result_set){
        try{
            ResultSetMetaData md = result_set.getMetaData();
            int columns = md.getColumnCount();
            String out_string = "";

            while (result_set.next()){
                for (int i = 1; i<columns; i++){
                    out_string = out_string + result_set.getObject(i).toString();
                }
                out_string += "\n";
            }
            System.out.println(out_string);
            return out_string;

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try{
                result_set.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /********************************************************************************
     * Query Handlers                                                               *
     * Purpose:     These methods extract and return data from various ResultSets.  *
     ********************************************************************************/

    private String handleReserveInsert(ResultSet result_set) {

        try{
            while (result_set.next()) {
                return result_set.getObject(1).toString();
            }
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

    public String getName() {
        return fullname;
    }

    public void setName(String fullname) {
        this.fullname = fullname;
    }

    public String getDOB() {
        return dob;
    }

    public void setDOB(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAccountId() {
        return account_id;
    }

    public void setAccountId(int account_id) {
        this.account_id = account_id;
    }

    public int getPersonalInfoId() {
        return personal_info_id;
    }

    public void setPersonalInfoId(int personal_info_id) {
        this.personal_info_id = personal_info_id;
    }

    public int getBookingId() {
        return booking_id;
    }

    public void setBookingId(int booking_id) {
        this.booking_id = booking_id;
    }

    public int getBookingsPersonalFKID() {
        return bookings_personal_fk_id;
    }

    public void setBookingsPersonalFKID(int bookings_personal_fk_id) {
        this.bookings_personal_fk_id = bookings_personal_fk_id;
    }

    public int[] getFlightIds() {
        return flight_ids;
    }

    public void setFlightIds(int[] flight_ids) {
        this.flight_ids = flight_ids;
    }

    public int getAncillaryPkgID() {
        return ancillary_pkg_id;
    }

    public void setAncillaryPkgID(int ancillary_pkg_id) {
        this.ancillary_pkg_id = ancillary_pkg_id;
    }

    public Ancillary[] getAncillaries() {
        return ancillaries;
    }

    public void setAncillaries(Ancillary[] ancillaries) {
        this.ancillaries = ancillaries;
    }

    public String[] getSeatNumbers() {
        return seat_numbers;
    }

    public void setSeatNumbers(String[] seat_ids) {
        this.seat_numbers = seat_ids;
    }

    public int getBags() {
        return bags;
    }

    public void setBags(int bags) {
        this.bags = bags;
    }
}

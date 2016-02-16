import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description:
 *      Account objects can be created for users. These objects
 *      allow the application to keep user information on record.
 *      These accounts also allow for the accumulation of Booking object
 *      to allow user to get information about their purchases. CreditCard
 *      objects will also be stored in order for a customer to save
 *      a CreditCard on Account for quick future purchases.
 *
 */

public class AccountModel {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String login;
    private String pass;
    private int account_id;
    private float rewardMiles = 0;
    private ArrayList<FlightModel> flights = new ArrayList();
    private ArrayList<BookingModel> bookings = new ArrayList();
    private ArrayList<CreditCardModel> cards = new ArrayList();
    private ArrayList<HashMap<String, String>> personal_infos = new ArrayList();

    protected AccountModel(){};
    
    protected AccountModel(String username){
        this.username = username;
        this.queryAccountInfo(this.username);
        this.queryAccountBookings(this.account_id);
        this.queryAccountBookingsFlights(this.account_id);
    }

    private void queryAccountInfo(String user){

        String query_string = ("select * from accounts where username = '"+user+"';");
        ResultSet rs = this.queryDB(query_string);
        try {
            while(rs.next()) {
                this.setAccountID(Integer.parseInt(rs.getString("id")));
                this.setFirstName(rs.getString("fname"));
                this.setLastName(rs.getString("lname"));
                this.setEmail(rs.getString("email"));
                this.setRewardMiles(Integer.parseInt(rs.getString("reward_miles")));
                this.setUsername(rs.getString("username"));
                this.setPassword(rs.getString("password"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void queryAccountBookings(int account_id){
        ArrayList<BookingModel> collected_bookings = new ArrayList<>();
        String query_string = ( "select p.id as p_id, p.fullname as p_fullname, p.DOB as p_DOB, " +
                                "p.gender as p_gender, p.address1 as p_add1, p.address2 as p_add2, " +
                                "p.city as p_city, p.zipcode as p_zip, p.state as p_state, "+
                                "p.phone as p_phone, " +
                                "p.email as p_email, b.bags as b_bags, b.seat_numbers as b_seats, "+
                                "b.ancillary_pkg_id as b_ancillaries "+
                "from accounts as a "+
                "join accounts_bookings as ab on ab.account_id = a.id "+
                "join bookings as b on ab.booking_id = b.id " +
                "join bookings_personalinfo as bp on bp.booking_id = b.id "+
                "join personal_info as p on bp.personal_info_id = p.id "+
                "where a.id = "+account_id+";"
        );
        System.out.println(query_string);

        ResultSet rs = this.queryDB(query_string);
        try{
            while(rs.next()){
                BookingModel booking_result = new BookingModel();
                booking_result.setPersonalInfoId(Integer.parseInt(rs.getString("p_id")));
                booking_result.setName(rs.getString("p_fullname"));
                booking_result.setDOB(rs.getString("p_DOB"));
                booking_result.setGender(rs.getString("p_gender"));
                booking_result.setAddress(rs.getString("p_add1"));
                booking_result.setCity(rs.getString("p_city"));
                booking_result.setZip(rs.getString("p_zip"));
                booking_result.setPhone(rs.getString("p_phone"));
                booking_result.setEmail(rs.getString("p_email"));

                booking_result.setBags(Integer.parseInt(rs.getString("b_bags")));
                //booking_result.setAncillaryPkgID(Integer.parseInt(rs.getString("b_ancillaries")));

                collected_bookings.add(booking_result);
            }
            this.setBookings(collected_bookings);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void queryAccountBookingsFlights(int account_id){
        ArrayList<FlightModel> collected_flights = new ArrayList<>();
        String query_string = ( "select f.id as f_id, air1.short_name as a1_short, "+
                                "air1.long_name as a1_long, air2.long_name as a2_long, "+
                                "air2.short_name as a2_short, f.dt as f_dt, f.miles as f_miles, "+
                                "f.avail_seats as f_avail, f.bag_price as f_bprice, f.seat_price as f_sprice "+
                                "from accounts as a "+
                "join accounts_bookings as ab on ab.account_id = a.id "+
                "join bookings as b on ab.booking_id = b.id "+
                "join flights_bookings as fb on fb.booking_id = b.id "+
                "join flights as f on fb.flight_id = f.id " +
                "join airports as air1 on air1.id = f.src_id "+
                "join airports as air2 on air2.id = f.dest_id "+
                "where a.id = "+account_id+";"
        );
        System.out.println(query_string);

        ResultSet rs = this.queryDB(query_string);
        try{
            ResultSetMetaData rsmd = rs.getMetaData();
            while(rs.next()){
                FlightModel flight_result = new FlightModel();
                flight_result.setFlightID(Integer.parseInt(rs.getString("f_id")));
                flight_result.setStartShort(rs.getString("a1_short"));
                flight_result.setStartShort(rs.getString("a1_long"));
                flight_result.setDestinationShort(rs.getString("a2_short"));
                flight_result.setDestLocation(rs.getString("a2_long"));
                flight_result.setDateTime(rs.getString("f_dt"));
                flight_result.setMiles(Integer.parseInt(rs.getString("f_miles")));
                flight_result.setSeatPrice(Double.parseDouble(rs.getString("f_sprice")));
                flight_result.setBagPrice(Double.parseDouble(rs.getString("f_bprice")));
                flight_result.setAvailSeats(rs.getString("f_avail").split(","));
                collected_flights.add(flight_result);
            }
            this.setFlights(collected_flights);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    static void paymentOnStoredCard(String four_digits, float pay_amnt){}
        //** find card with four_digits */
    
    static void addRewardsFromFlight(){}
    
	/*
	 * Reward program incorperated with miles 
	 * From 0 - 10,000 miles = regular member
	 * from 10,001 - 100,000 miles = gold member
	 * from 100,001 and greater = platinum member
	 */	
    
    public boolean checkLogin(String login, String pass) {
        this.setLogin(login);
        this.setPass(pass);
        boolean check = false;
        String query_string = ("select * from accounts where username = '"+this.getLogin()+"' and password = '"+this.getPass()+"';");
        ResultSet result_set = queryDB(query_string);
        try {
            while(result_set.next()) {
                check = true;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return check;
    }

    public boolean addAccountToDatabase(String firstName, String lastName, String email, String username, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        String query_string = ("select * from accounts where username = '"+username+"';");
        ResultSet result_set = queryDB(query_string);
        boolean status = handleInsertQuery(result_set);
        return status;
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

    public boolean handleInsertQuery(ResultSet rs) {
        try {
            while(rs.next()) {
                System.out.println(username);
                return false;
            }
            
            String insert_string= ( "insert into accounts (fname, lname, email, username, password, reward_miles)"+
                                    " VALUES ('"+firstName+"', '"+lastName+"', '"+email+"', '"+username+"', '"+
                                    password+"', '"+rewardMiles+"')");
            insertDB(insert_string);
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally{
            try {
                rs.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    
    public static void deleteAccount(){}

    public int getAccountID() {
        return account_id;
    }

    public void setAccountID(int account_id) {
        this.account_id = account_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public float getRewardMiles() {
        return rewardMiles;
    }

    public void setRewardMiles(float rewardMiles) {
        this.rewardMiles = rewardMiles;
    }

    public ArrayList<FlightModel> getFlights() {
        return flights;
    }

    public void setFlights(ArrayList<FlightModel> flights) {
        this.flights = flights;
    }

    public ArrayList<BookingModel>getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<BookingModel> bookings) {
        this.bookings = bookings;
    }

    public ArrayList<CreditCardModel> getCards() {
        return cards;
    }

    public void setCards(ArrayList<CreditCardModel> cards) {
        this.cards = cards;
    }

    public ArrayList<HashMap<String, String>> getPersonal_infos() {
        return personal_infos;
    }

    public void setPersonal_infos(ArrayList<HashMap<String, String>> personal_infos) {
        this.personal_infos = personal_infos;
    }
}
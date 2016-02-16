import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;

public class AccountView extends JPanel{
    private String display;
    private ArrayList<HashMap<String,String>> account_data = new ArrayList<>();
    private ArrayList<HashMap<String, String>> bookings_data = new ArrayList<>();
    private ArrayList<HashMap<String, String>> flights_data = new ArrayList<>();
    private ArrayList<HashMap<String, String>> cards_data = new ArrayList<>();

    private AccountModel acct_model;

    protected JPanel account_pane;
    protected JPanel login_pane;
    protected JPanel info_pane;
    protected JLabel first_name;
    protected JLabel last_name;
    protected JLabel email;
    protected JLabel user_name;
    protected JLabel password;
    protected JLabel conf_pass;
    protected JButton signup_btn;
    protected JButton cancel_btn;
    protected JButton login_btn;
    protected JTextField fname_text;
    protected JTextField lname_text;
    protected JTextField email_text;
    protected JTextField user_text;
    protected JTextField login_user_input;
    protected JPasswordField password_text;
    protected JPasswordField confirm_pass;
    protected JPasswordField password_input;

    private Border empty_border = BorderFactory.createEmptyBorder(10,10,10,10);
    private Border border = BorderFactory.createLineBorder(Color.BLACK);
    private Border inner_border = BorderFactory.createCompoundBorder(
            empty_border,
            border);

    private GridBagConstraints gbc;
    private Font font1 = new Font("SansSerif", Font.BOLD, 20);

    public AccountView()
    {
        this.display = "default";
        this.setLayout(new GridBagLayout());
        this.gbc = new GridBagConstraints();
    }
    
    protected void updateView(){
        this.removeAll();
        if (this.display == "default"){
            JLabel divider = new JLabel(" -- OR -- ");
            divider.setFont(font1);

            this.gbc.gridy = 0;
            this.add(generateCancelButton(), this.gbc);
            this.gbc.gridy = 1;
            this.add(loginView(), this.gbc);
            this.gbc.gridy = 2;
            this.add(divider, this.gbc);
            this.gbc.gridy = 3;
            this.add(signupView(), this.gbc);
        }
    }

    private JButton generateCancelButton(){
        cancel_btn = new JButton("Return to Search");
        cancel_btn.setFont(font1);
        return cancel_btn;
    }

    private JPanel loginView() {
        login_pane = new JPanel();
        login_pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel login_user_label = new JLabel("Username: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5,10,5,10);
        login_user_label.setFont(font1);
        login_pane.add(login_user_label, gbc);

        login_user_input = new JTextField(15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(5, 80, 5, 10);
        login_user_input.setFont(font1);
        login_pane.add(login_user_input, gbc);

        JLabel password_label = new JLabel("Password: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5,10,5,10);
        password_label.setFont(font1);
        login_pane.add(password_label, gbc);

        password_input = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(5,80,5,10);
        password_input.setFont(font1);
        login_pane.add(password_input, gbc);

        login_btn = new JButton("Login");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = (new Insets(5,80,5,10));
        login_btn.setFont(font1);
        login_pane.add(login_btn, gbc);

        login_pane.setBorder(inner_border);

        return login_pane;
    }

    private JPanel signupView() {
        account_pane = new JPanel();
        Font font1 = new Font("SansSerif", Font.BOLD,20);
        account_pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        first_name = new JLabel("First Name:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = (new Insets(5,10,5,10));
        first_name.setFont(font1);
        account_pane.add(first_name, c);
      
        fname_text = new JTextField(15);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 4;
        c.insets = (new Insets(5,80,5,10));
        fname_text.setFont(font1);
        account_pane.add(fname_text, c);
        
        last_name = new JLabel("Last Name:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = (new Insets(5,10,5,10));
        last_name.setFont(font1);
        account_pane.add(last_name, c);
        
        lname_text = new JTextField(15);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 4;
        c.insets = (new Insets(5,80,5,10));
        lname_text.setFont(font1);
        account_pane.add(lname_text, c);
        
        email = new JLabel("Email:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = (new Insets(5,10,5,10));
        email.setFont(font1);
        account_pane.add(email, c);
        
        email_text = new JTextField(15);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 4;
        c.insets = (new Insets(5,80,5,10));
        email_text.setFont(font1);
        account_pane.add(email_text, c);
        
        user_name = new JLabel("Username:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        c.insets = (new Insets(5,10,5,10));
        user_name.setFont(font1);
        account_pane.add(user_name, c);
        
        user_text = new JTextField(15);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 4;
        c.insets = (new Insets(5,80,5,10));
        user_text.setFont(font1);
        account_pane.add(user_text, c);
        
        password = new JLabel("Password:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        c.insets = (new Insets(5,10,5,10));
        password.setFont(font1);
        account_pane.add(password, c);
        
        password_text = new JPasswordField(15);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 4;
        c.insets = (new Insets(5,80,5,10));
        password_text.setFont(font1);
        account_pane.add(password_text, c);
        
        conf_pass = new JLabel("Confirm Password:");
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        c.insets = (new Insets(5,10,5,10));
        conf_pass.setFont(font1);
        account_pane.add(conf_pass, c);
        
        confirm_pass = new JPasswordField();
        c.gridx = 1;
        c.gridy = 5;
        c.gridwidth = 4;
        c.insets = (new Insets(5,80,5,10));
        confirm_pass.setFont(font1);
        account_pane.add(confirm_pass, c);
        
        signup_btn = new JButton("Sign-Up");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 6;
        c.gridwidth = 2;
        c.insets = (new Insets(5,80,5,10));
        signup_btn.setFont(font1);
        account_pane.add(signup_btn, c);
        
        account_pane.setBorder(inner_border);

        return account_pane;
    }
    
    protected JPanel accountInfo() {
        info_pane = new JPanel();
        info_pane.setLayout(new BoxLayout(info_pane, BoxLayout.Y_AXIS));
        acct_model = Travlr.account_flow.account_model;
        JScrollPane scrollPane;
        JTable table = new JTable();
        DefaultTableModel models;
        Vector columnNames = new Vector();
        Vector data = new Vector();

        columnNames.add("first_name");
        columnNames.add("last_name");
        columnNames.add("email");
        columnNames.add("username");
        columnNames.add("reward_miles");

        Vector info = new Vector();
        info.add(acct_model.getFirstName());
        info.add(acct_model.getLastName());
        info.add(acct_model.getEmail());
        info.add(acct_model.getUsername());
        info.add(acct_model.getRewardMiles());

        data.add(info);

        models = new DefaultTableModel(data, columnNames);
        table.setModel(models);
        for (int i=0; i<columnNames.size(); i++){
            table.getColumn(columnNames.get(i)).setPreferredWidth(400);
        }
        table.setPreferredSize(new Dimension(1500,200));
        table.setGridColor(Color.BLACK);
        scrollPane = new JScrollPane(table);
        scrollPane.setSize(new Dimension (1500, 200));

        info_pane.add(scrollPane);

        info_pane.add(accountBookingsDisplay());
        info_pane.add(accountFlightsDisplay());

        return info_pane;
    }

    protected JScrollPane accountBookingsDisplay(){
        JScrollPane bookings_scroll;
        JPanel bookings_panel = new JPanel();

        bookings_panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i=0; i<bookings_data.size(); i++) {
            System.out.println(bookings_data.toString());
            HashMap<String, String> data = bookings_data.get(i);
            bookings_panel.add(generateBookingSummary(data, i), gbc);
            gbc.gridy++;
        }

        bookings_panel.setPreferredSize(new Dimension(400,400));

        bookings_scroll = new JScrollPane(bookings_panel);
        bookings_scroll.setPreferredSize(new Dimension(400, 400));

        return bookings_scroll;
    }

    private JPanel generateBookingSummary(Map<String, String> data_map, int position){
        JPanel booking_panel = new JPanel();
        JLabel bags = new JLabel(data_map.get("bags"));
        JLabel booking_id = new JLabel(data_map.get("booking_id"));
        JLabel seats = new JLabel(data_map.get("seats"));
        JLabel personal_info_id = new JLabel(data_map.get("pinfo_id"));
        JLabel ancillary_id = new JLabel(data_map.get("additions_id"));

        booking_id.setBorder(empty_border);
        seats.setBorder(empty_border);
        bags.setBorder(empty_border);
        personal_info_id.setBorder(empty_border);
        ancillary_id.setBorder(empty_border);

        booking_panel.add(booking_id);
        booking_panel.add(seats);
        booking_panel.add(bags);
        booking_panel.add(personal_info_id);
        booking_panel.add(ancillary_id);
        booking_panel.setBorder(inner_border);

        return booking_panel;
    }

    protected JScrollPane accountFlightsDisplay(){
        JScrollPane flights_scroll;
        JPanel flights_panel = new JPanel();

        flights_panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i=0; i<flights_data.size(); i++) {
            HashMap<String, String> data = flights_data.get(i);
            flights_panel.add(generateFlightSummary(data, i), gbc);
            gbc.gridy++;
        }

        flights_panel.setPreferredSize(new Dimension(400,400));

        flights_scroll = new JScrollPane(flights_panel);
        flights_scroll.setPreferredSize(new Dimension(400, 400));

        return flights_scroll;
    }

    private JPanel generateFlightSummary(HashMap<String,String> data_map, int position){
        JPanel flight_panel = new JPanel();
        JLabel flight_id = new JLabel(data_map.get("flight_id"));
        JLabel src_short = new JLabel(data_map.get("source"));
        JLabel src_long = new JLabel(data_map.get("source_long"));
        JLabel dest_short = new JLabel(data_map.get("destination"));
        JLabel dest_long = new JLabel(data_map.get("destination_long"));
        JLabel date_time = new JLabel(data_map.get("datetime"));
        JLabel miles = new JLabel(data_map.get("miles"));

        flight_id.setBorder(empty_border);
        src_short.setBorder(empty_border);
        src_long.setBorder(empty_border);
        dest_short.setBorder(empty_border);
        dest_long.setBorder(empty_border);
        date_time.setBorder(empty_border);
        miles.setBorder(empty_border);

        flight_panel.add(flight_id);
        flight_panel.add(src_short);
        flight_panel.add(dest_short);
        flight_panel.add(date_time);
        flight_panel.add(miles);
        flight_panel.setBorder(inner_border);

        return flight_panel;
    }

    public ArrayList<HashMap<String, String>> getAccountData() {
        return account_data;
    }

    public void setAccountData(ArrayList<HashMap<String, String>> account_data) {
        this.account_data = account_data;
    }

    public void appendAccountData(HashMap<String, String> account_data) {
        ArrayList<HashMap<String, String>> updated_data= this.getAccountData();
        updated_data.add(account_data);
        this.setAccountData(updated_data);
    }

    public ArrayList<HashMap<String, String>> getBookingsData() {
        return bookings_data;
    }

    public void setBookingsData(ArrayList<HashMap<String, String>> bookings_data) {
        this.bookings_data = bookings_data;
    }

    public void appendBookingsData(HashMap<String, String> booking_data) {
        ArrayList<HashMap<String, String>> updated_data= this.getBookingsData();
        updated_data.add(booking_data);
        this.setBookingsData(updated_data);
    }

    public ArrayList<HashMap<String, String>> getFlightsData() {
        return flights_data;
    }

    public void setFlightsData(ArrayList<HashMap<String, String>> flights_data) {
        this.flights_data = flights_data;
    }

    public void appendFlightsData(HashMap<String, String> flight_data) {
        ArrayList<HashMap<String, String>> updated_data= this.getFlightsData();
        updated_data.add(flight_data);
        this.setFlightsData(updated_data);
    }

    public ArrayList<HashMap<String, String>> getCardsData() {
        return cards_data;
    }

    public void setCardsData(ArrayList<HashMap<String, String>> cards_data) {
        this.cards_data = cards_data;
    }

    public void appendCardsData(HashMap<String, String> card_data) {
        ArrayList<HashMap<String, String>> updated_data= this.getCardsData();
        updated_data.add(card_data);
        this.setCardsData(updated_data);
    }
}

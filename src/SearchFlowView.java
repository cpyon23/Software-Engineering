import com.toedter.calendar.JCalendar;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**********************************************************************
* Name:    SearchFlowView
* Description:
*      SearchFlowView is responsible for formatting the UserInterface
*      during the SearchFlow component of the application. This component
*      will take user input and use the SearchController to send queries to
*      the database for a list of flights. Once the SearchController returns
*      this list of Flights they will be displayed by the SearchFlowView.  The user
*      will be able to interact with this View in order to retrieve different data
*      about the Flight.
*
*      SearchView will be given a list of Flight objects.
*                  From this list SearchView will be able to rearrange the Flight objects
*                  into a variety of different layouts.
*                  These view include a Calander layout and List layout.
*
**********************************************************************/
public class SearchFlowView extends JPanel {
    // Global Variable Init of Class Base Properties
    private String display;
    private String[] airports;
    private ArrayList<Map> flight_data;
    private String src, dest, time, date, return_date, return_time;
    protected JPanel search_pane, content_pane;

    // Interactive Elements to be Accessed by Controller
    protected JButton search_submit_button, one_way_btn, two_way_btn;
    protected JButton[] booking_buttons = {};
    protected JPanel empty_last_row, last_row;
    protected JCalendar date_select, return_date_select, flights_calendar;
    protected JButton depart_date_btn, return_date_btn, calendar_btn;
    protected JDialog dialog;

    // Global Selection Variables
    private JComboBox src_select, dest_select;
    private JPanel empty_panel;
    //private JPanel date_flights_panel;
    private JScrollPane date_flights_panel, scroll_pane;

    // Global Border Variables
    private Border empty_border = BorderFactory.createEmptyBorder(10,10,10,10);
    private Border border = BorderFactory.createLineBorder(Color.BLACK);
    private Border inner_border = BorderFactory.createCompoundBorder(
            empty_border,
            border);
    private URL calendar_img = this.getClass().getResource("images/calendar_icon.jpg");
    private ImageIcon calendar_icon = new ImageIcon(calendar_img);

    /********************************************************************
     * Name:    SearchFlowView()    :   Constructor                     *
     * Purpose:                                                         *
     * Params:                                                          *
     * Postcondition:                                                   *
     *      SearchFlowView() object has been created.                   *
     ********************************************************************/
    public SearchFlowView(){
        this.display = "default";
    }

    /********************************************************************
     * Name:    displayView()   :   Method                              *
     * Purpose:                                                         *
     ********************************************************************/
    protected void updateView(){
        this.removeAll();

        // Determine Displays to Add.
        if ( this.display == "default" ){
            this.add( searchView() );
        } else if ( this.display == "calendar" ){
            this.add( calendarView() );
        } else if ( this.display == "list" ){
            this.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            this.add(search_pane, gbc);
            gbc.gridy = 2;
            this.add( listView(), gbc);
            gbc.gridy = 3;
        } else {
            System.out.println(" Invalid view type. ");
        }
        this.revalidate();
        this.repaint();

    }

    /*******************************************************************
    * Name:    searchView()   :   Method                               *
    * Purpose:                                                         *
    ********************************************************************/
    private JPanel searchView() {
        search_pane = new JPanel();
        search_pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Round-Trip vs One-Way Toggle
        one_way_btn = new JButton("One-Way");
        two_way_btn = new JButton("Two-Way");
        one_way_btn.setActionCommand("one");
        two_way_btn.setActionCommand("two");
        one_way_btn.setEnabled(false);
        one_way_btn.setBackground(Color.BLUE);

        // Add Trip Toggle Buttons to Panel
        JPanel first_row = new JPanel();
        gbc.gridx = 1;
        first_row.add(one_way_btn);
        first_row.add(two_way_btn);
        search_pane.add(first_row, gbc);

        // Init Search Input Displays
        src_select = new JComboBox(this.airports);
        dest_select = new JComboBox(this.airports);
        depart_date_btn = new JButton("Leave Date");
        return_date_btn = new JButton("Return Date");
        empty_panel = new JPanel();

        date_select = new JCalendar();
        return_date_select = new JCalendar();

        search_submit_button = new JButton("submit");

        // Add Input Displays to Panel
        gbc.fill = GridBagConstraints.REMAINDER;
        gbc.gridx = 1;
        gbc.gridy = 2;
        search_pane.add(src_select, gbc);
        gbc.gridy = 3;
        search_pane.add(dest_select, gbc);
        gbc.gridy = 4;
        gbc.gridx = 1;

        last_row = new JPanel();
        last_row.setLayout(new GridLayout(1, 3));
        last_row.add(depart_date_btn);
        last_row.add(empty_panel);
        last_row.add(search_submit_button);
        search_pane.add(last_row, gbc);

        return search_pane;
    }

    /*******************************************************************
    * Name:    calendarView()   :   Method                             *
    * Purpose:                                                         *
    ********************************************************************/
    private JPanel calendarView() {
        content_pane = new JPanel();
        Map<String, String> data = new HashMap(flight_data.get(1));

        flights_calendar = new JCalendar();
        //flights_calendar.getCalendar()(generateFlightListing(data, 1));
        content_pane.add(flights_calendar);
        return content_pane;
    }

    /*******************************************************************
    * Name:    listView()   :   Method                                 *
    * Purpose:                                                         *
    ********************************************************************/
    private JPanel listView() {
        booking_buttons = new JButton[flight_data.size()];

        // Initialize Outer Panel
        content_pane = new JPanel();
        content_pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;

        JPanel btn_panel = new JPanel();
        gbc.gridy = 2;
        scroll_pane = generateFlightsPane();
        content_pane.add(scroll_pane, gbc);

        return content_pane;
    }

    //private JPanel generateFlightsPane(){
    private JScrollPane generateFlightsPane(){

        //JPanel flights_panel = new JPanel();
        JScrollPane flights_panel = new JScrollPane();
        JPanel fp = new JPanel();
        //fp.setPreferredSize(new Dimension(600,600));
        //flights_panel = new JScrollPane(fp);
        fp.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        Boolean matching_values = false;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for(int i=0; i < flight_data.size(); i++){
            Map<String, String> data = new HashMap(flight_data.get(i));

            // Filter Results by Date
            Boolean date_filter = dateFilter(data.get("datetime"));

            // Create Displays for Matching Flights
            if (date_filter){
                fp.add(generateFlightListing(data, i), gbc);
                gbc.gridy++;
                matching_values = true;
            } else if (parseDate(this.getDate()+" "+this.getTime()) == null){
                fp.add(generateFlightListing(data, i), gbc);
                gbc.gridy++;
                matching_values = true;
            }
        }

        flights_panel.setPreferredSize(new Dimension(600,600));

        if (!matching_values){
            JPanel label_panel = new JPanel();
            label_panel.setBorder(inner_border);
            label_panel.add(new JLabel("No Flights Matching Your Criteria"));
            gbc.fill = GridBagConstraints.BOTH;
            fp.add(label_panel, gbc);
            gbc.gridy++;
            for(int i=0; i < flight_data.size(); i++){
                Map<String, String> data = new HashMap(flight_data.get(i));
                fp.add(generateFlightListing(data, i), gbc);
                gbc.gridy++;
            }

        }
        flights_panel = new JScrollPane(fp);
        flights_panel.setPreferredSize(new Dimension(600,600));

        return flights_panel;
    }

    protected void addDateListings() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.setDate(formatter.format(this.flights_calendar.getDate()));
        try {
            this.content_pane.remove(1);
        } catch (Exception e) {

        }
        this.date_flights_panel = this.generateFlightsPane();
        this.content_pane.add(this.date_flights_panel);
        this.revalidate();
        this.repaint();
    }

    private JPanel generateFlightListing(Map<String, String> data_map, int position){
        JPanel flight_panel = new JPanel();

        // Create Information Displays
        JLabel flight_id = new JLabel(data_map.get("flight_id"));
        JLabel airport_info = new JLabel(data_map.get("source") + " --> " + data_map.get("destination"));
        JLabel date_info = new JLabel(data_map.get("datetime"));
        JLabel price_info = new JLabel("$"+data_map.get("price"));
        JButton book_btn = new JButton("Book Flight");
        booking_buttons[position] = book_btn;

        // Provide Information Spacing
        flight_id.setBorder(empty_border);
        airport_info.setBorder(empty_border);
        date_info.setBorder(empty_border);
        price_info.setBorder(empty_border);
        book_btn.setBorder(empty_border);

        // Add Individual Information Dispalys to Panel
        flight_panel.add(flight_id);
        flight_panel.add(airport_info);
        flight_panel.add(date_info);
        flight_panel.add(price_info);
        flight_panel.add(book_btn);
        flight_panel.setBorder(inner_border);

        return flight_panel;
    }

    protected void replaceLastRow(String new_row){

        GridBagConstraints gbc = new GridBagConstraints();
        this.search_pane.remove(last_row);
        last_row = new JPanel();
        last_row.setLayout(new GridLayout(1, 3));

        gbc.gridy = 4;
        gbc.gridx = 1;
        if (new_row == "two-way"){
            last_row.add(depart_date_btn);
            last_row.add(return_date_btn);
            last_row.add(search_submit_button);
        } else if (new_row == "one-way"){
            last_row.add(depart_date_btn);
            last_row.add(empty_panel);
            last_row.add(search_submit_button);
        }
        this.search_pane.add(last_row, gbc);
        this.search_pane.revalidate();
        this.search_pane.repaint();
    }

    private Boolean dateFilter(String input_date){
        // Initialize Selected Date Variables
        Date selected_date = parseDate(this.getDate() + " " + this.getTime());
        Calendar date1 = dateToCalendar(selected_date);

        // Initialize Flight Date Variables
        Date flight_date = parseDate(input_date);
        Calendar date2 = dateToCalendar(flight_date);


        if ( date1 != null && date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
                date1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR)) {
            return true;
        } else {
            return false;
        }
    }

    /********************************************************************
     *  Utility Functions                                               *
     ********************************************************************/
    protected GridBagConstraints getConstraints(){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        return gbc;
    }

    protected Date parseDate(String input_date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d HH:mm:ss");
        Date parsed_date = null;
        try {
            parsed_date = sdf.parse(input_date);
        } catch (ParseException e){}
        return parsed_date;
    }

    private Calendar dateToCalendar(Date d){
        if (d != null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            return cal;
        } else {
            return null;
        }
    }

    private ImageIcon resizeIcon(ImageIcon img_icon, int height, int width) {
        img_icon = new ImageIcon(img_icon.getImage().getScaledInstance(
                width,
                height,
                Image.SCALE_SMOOTH));
        return img_icon;
    }

    /********************************************************************
     *  Getter and Setter Functions.                                    *
     ********************************************************************/
    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
        this.updateView();
    }

    public String[] getAirports() {
        return airports;
    }

    public void setAirports(String[] airports) {
        this.airports = airports;
    }

    public ArrayList<Map> getFlightData() {
        return flight_data;
    }

    public void setFlightData(ArrayList<Map> flight_list) {
        this.flight_data = flight_list;
    }

    public String getSrc() {
        this.setSrc( (String) src_select.getSelectedItem());
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        this.setDest((String) dest_select.getSelectedItem());
        return this.dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getTime() {
        this.setTime("00:00:00");
        //this.setTime(Integer.toString((int) time_select.getValue()));
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReturnDate(){
        return return_date;
    }

    public void setReturnDate(String date){
        this.return_date = date;
    }

    public String getReturnTime(){
        return return_time;
    }

    public void setReturnTime(String time){
        this.return_time = time;
    }
}

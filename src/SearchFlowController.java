import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/****************************************************************************
* Name:    SearchContoller
* Purpose:  The search controller will handle any user interaction
* within the SearchFlow component of the application. This will involve
* interactions with Search and Flight objects.  SearchController will
* create a Basic search object from inputs on the SearchFlowView to build
* a query, collect Flight objects from the DB and pass those Flight objects
* back to the user trough the SearchFlowView formatting.
*
******************************************************************************/
public class SearchFlowController {
    SearchFlowView search_view;
    Container parent_container;
    Travlr parent_frame;
    SearchModel search_model;
    FlightModel flight1;
    FlightModel flight2;
    GridBagConstraints search_view_constraints;
    String[] locations;
    ArrayList<Map> flight_data;
    Boolean needs_return_flight = false;

    /*******************************************************************
    * Name:    SearchFlowController()    :   Constructor               *
    * Purpose:                                                         *
    * Params:                                                          *
    * Postcondition:                                                   *
    *      SearchFlowController() object has been created.             *
    ********************************************************************/
    public SearchFlowController(Travlr pframe, Container pcontain ){
        parent_frame = pframe;
        parent_container = pcontain;
        search_model = new SearchModel();
        locations = search_model.getAllAirports();
        search_view = new SearchFlowView();
        search_view.setAirports(locations);
        search_view.updateView();
        addSearchControls();
    }

    public void addSearchControls() {
        search_view.one_way_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                toggleReturnFlight(actionEvent.getActionCommand());
            }
        });

        search_view.two_way_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                toggleReturnFlight(actionEvent.getActionCommand());
            }
        });

        search_view.depart_date_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                search_view.dialog = new JDialog();
                search_view.dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                search_view.dialog.getContentPane().add(search_view.date_select);
                search_view.dialog.pack();
                search_view.dialog.setLocationRelativeTo(null);
                search_view.dialog.setVisible(true);
            }
        });

        search_view.return_date_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                search_view.dialog = new JDialog();
                search_view.dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                search_view.dialog.getContentPane().add(search_view.return_date_select);
                search_view.dialog.pack();
                search_view.dialog.setLocationRelativeTo(null);
                search_view.dialog.setVisible(true);
            }
        });

        search_view.date_select.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                if ("calendar".equals(propertyChangeEvent.getPropertyName())) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    search_view.depart_date_btn.setText(formatter.format(search_view.date_select.getDate()));
                    search_view.setDate(formatter.format(search_view.date_select.getDate()));
                }
            }
        });

        search_view.return_date_select.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                if ("calendar".equals(propertyChangeEvent.getPropertyName())) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    search_view.return_date_btn.setText(formatter.format(search_view.return_date_select.getDate()));
                    search_view.setReturnDate(formatter.format(search_view.return_date_select.getDate()));
                }
            }
        });

        search_view.search_submit_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flight1 = null;
                flight2 = null;
                performBasicSearch();
            }
        });

    }

    private void addBookingControls(){
        /*search_view.calendar_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                search_view.setDisplay("calendar");
                search_view.updateView();
                addCalendarControls();
            }
        });*/
        for (int i = 0; i < search_view.booking_buttons.length; i++){
            final int j = i;
            if (search_view.booking_buttons[i] != null) {
                search_view.booking_buttons[i].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        if (needs_return_flight && flight1==null) {
                            flight1 = new FlightModel(search_view.getFlightData().get(j));
                            performBasicSearch();
                        } else if (needs_return_flight && flight2==null){
                            flight2 = new FlightModel(search_view.getFlightData().get(j));
                            parent_frame.startBookingsFlow(flight1, flight2);
                        } else {
                            flight1 = new FlightModel(search_view.getFlightData().get(j));
                            parent_frame.startBookingsFlow(flight1);
                        }
                    }
                });
            }
        }
    }

    private void addCalendarControls(){
        search_view.flights_calendar.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                if ("calendar".equals(propertyChangeEvent.getPropertyName())) {
                    search_view.addDateListings();
                }
            }
        });
    }

    private String pullShortName(String full_text){
        String pattern = ".*\\((\\w+)\\)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(full_text);
        if (m.find()){
            return m.group(1);
        } else {
            return full_text;
        }
    }

    private void toggleReturnFlight(String btn_value){
        if ("two".equals(btn_value)){
            needs_return_flight = true;
            search_view.one_way_btn.setEnabled(true);
            search_view.two_way_btn.setEnabled(false);
            search_view.two_way_btn.setBackground(Color.BLUE);
            search_view.one_way_btn.setBackground(null);
            search_view.replaceLastRow("two-way");
        } else {
            needs_return_flight = false;
            search_view.one_way_btn.setEnabled(false);
            search_view.two_way_btn.setEnabled(true);
            search_view.one_way_btn.setBackground(Color.BLUE);
            search_view.two_way_btn.setBackground(null);
            search_view.replaceLastRow("one-way");
        }
        System.out.print(needs_return_flight);
    }

    private void performBasicSearch(){
        System.out.println("Performing Basic Search");
        String src = pullShortName(search_view.getSrc());
        String dest = pullShortName(search_view.getDest());
        String date = search_view.getDate();
        String time = search_view.getTime();
        if (flight1 == null) {
            search_model = new SearchModel(src, dest, date, time);
        } else {
            search_view.setDate(search_view.getReturnDate());
            search_view.setTime(search_view.getReturnTime());
            date = search_view.getReturnDate();
            time = search_view.getReturnTime();
            search_model = new SearchModel(dest, src, date, time);
        }
        flight_data = search_model.getFlightData();
        search_view.setFlightData(flight_data);
        search_view.setDisplay("list");
        addBookingControls();
    }
}
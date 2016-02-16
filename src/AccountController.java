import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AccountController {
    AccountView account_view;
    Container parent_container;
    Travlr parent_frame;
    AccountModel account_model;
    String fname, lname, email, user, pass, conf_pass, login, password;
    CreditCardController card_control;
    SearchFlowController search_control;
    
    /*******************************************************************
    * Name:    AccountController()    :   Constructor               *
    * Purpose:                                                         *
    * Params:                                                          *
    * Postcondition:                                                   *
    *      AccountController() object has been created.             *
    ********************************************************************/
    
    public AccountController(Travlr pframe, Container pcontain){
        parent_frame = pframe;
        parent_container = pcontain;
        account_model = new AccountModel();
        account_view = new AccountView();
        account_view.updateView();
        parent_container.add(account_view);
        addAccountControls();
    }
    
    public void addAccountControls() {
        account_view.signup_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                fname = account_view.fname_text.getText();
                lname = account_view.lname_text.getText();
                email = account_view.email_text.getText();
                user = account_view.user_text.getText();
                pass = account_view.password_text.getText();
                conf_pass = account_view.confirm_pass.getText();
                boolean check = true;
                if (!email.contains("@") || !email.contains(".")) {
                    JOptionPane.showMessageDialog(account_view.account_pane, "Invalid email address");
                    check = false;
                }
                if (!pass.equals(conf_pass)) {
                    JOptionPane.showMessageDialog(account_view.account_pane, "Passwords do not match");
                    check = false;
                }
                if (check) {
                    boolean success = account_model.addAccountToDatabase(fname, lname, email, user, pass);
                    if (success) {
                        Random rn = new Random();
                        int rand = rn.nextInt(5) + 0;
                        String newUser = parent_frame.disc_coupons[rand];
                        JOptionPane.showMessageDialog(account_view.account_pane, "Thank You for Registering!");
                        JOptionPane.showMessageDialog(account_view.account_pane, "First Time Registration will get you a "
                                + "25% Discount on next flight purchace. Just enter the coupon code: "
                                + newUser);
                        if (parent_frame.search_flow == null) {
                            parent_frame.startSearchFlow();
                        }
                    } else {
                        JOptionPane.showMessageDialog(account_view.account_pane, "Username already exists");
                    }
                }
                parent_frame.startAccountFlow();
            }
        });
        
        account_view.cancel_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (parent_frame.search_flow == null) {
                    parent_frame.startSearchFlow();
                } else {
                    parent_frame.returnSearchFlow();
                }
                parent_frame.setAccountSessionStatus(false);
                parent_frame.account_flow = null;
            }
        });

        account_view.login_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                login = account_view.login_user_input.getText();
                password = account_view.password_input.getText();
                boolean success = account_model.checkLogin(login, password);
                if (success) {

                    account_model = new AccountModel(account_view.login_user_input.getText());
                    setViewFlightData();
                    setViewBookingData();
                    setViewPersonalData();
                    setViewCardData();

                    parent_frame.returnSearchFlow();
                    JOptionPane.showMessageDialog(account_view.login_pane, "Welcome back " + login);
                    parent_frame.setAccountSessionStatus(true);
                    parent_frame.logo_panel.removeAll();
                    parent_frame.logo_panel.addAccountButton(success);
                    parent_frame.logo_panel.addAccountButtonControls();
                    parent_frame.logo_panel.revalidate();
                    parent_frame.logo_panel.repaint();
                    parent_frame.returnSearchFlow();
                } else {
                    JOptionPane.showMessageDialog(account_view.login_pane, "Invalid Login");
                }
            }
        });

    }

    private void setViewFlightData(){
        ArrayList<FlightModel> flight_models = account_model.getFlights();
        for (FlightModel f: flight_models){
            HashMap<String,String> this_flight = new HashMap();
            this_flight.put("flight_id", Integer.toString(f.getFlightID()));
            this_flight.put("source", f.getStartShort());
            this_flight.put("source_long", f.getStartLocation());
            this_flight.put("destination", f.getDestinationShort());
            this_flight.put("destination_long", f.getDestLocation());
            this_flight.put("datetime", f.getDateTime());
            this_flight.put("miles", Integer.toString(f.getMiles()));

            account_view.appendFlightsData(this_flight);
        }
    }

    private void setViewBookingData(){
        ArrayList<BookingModel> booking_models = account_model.getBookings();
        for (BookingModel b: booking_models){
            HashMap<String,String> this_booking = new HashMap();
            this_booking.put("booking_id", Integer.toString(b.getBookingId()));
            this_booking.put("bags", Integer.toString(b.getBags()));
            //this_booking.put("seats", b.getSeatNumbers().toString());
            this_booking.put("seats", "B2");
            this_booking.put("personal_info_id", Integer.toString(b.getPersonalInfoId()));
            //this_booking.put("ancillary_id", Integer.toString(b.getAncillaryPkgID()));

            account_view.appendBookingsData(this_booking);
        }
    }

    private void setViewCardData(){
        ArrayList<CreditCardModel> cc_models = account_model.getCards();
        for (CreditCardModel cc: cc_models){
            HashMap<String, String> this_card = new HashMap();
            this_card.put("id", Integer.toString(cc.getCardID()));
            this_card.put("name", cc.getNameOnCard());
            this_card.put("number", cc.getMaskedCardNum());
            this_card.put("expiration", cc.getCardExpiration());
            this_card.put("company", cc.getCardType());

            account_view.appendCardsData(this_card);
        }
    }

    private void setViewPersonalData(){}

}
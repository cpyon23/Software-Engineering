import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class BookingsFlowController {
    BookingsFlowView bookings_view;
    Container parent_container;
    Travlr parent_frame;
    BookingModel booking_model;


    /*******************************************************************
     * Name:    SearchFlowController()    :   Constructor               *
     * Purpose:                                                         *
     * Params:                                                          *
     * Postcondition:                                                   *
     *      SearchFlowController() object has been created.             *
     ********************************************************************/
    public BookingsFlowController(Travlr pframe, Container pcontain, FlightModel f1){
        parent_frame = pframe;
        parent_container = pcontain;
        booking_model = new BookingModel(f1.getFlightID());
        bookings_view = new BookingsFlowView(f1);
        addReturnControl();
        addContinueControl();
        parent_container.revalidate();
        parent_container.repaint();
    }

    public BookingsFlowController(Travlr pframe, Container pcontain, FlightModel f1, FlightModel f2){
        parent_frame = pframe;
        parent_container = pcontain;
        booking_model = new BookingModel(f1.getFlightID(), f2.getFlightID());
        bookings_view = new BookingsFlowView(f1, f2);
        addReturnControl();
        addContinueControl();
        parent_container.revalidate();
        parent_container.repaint();
    }

    private void addContinueControl(){
        bookings_view.continue_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                boolean check_status = true;
                String newline = System.getProperty("line.separator");

                if (bookings_view.getBookingState() == 1) {
                    System.out.println(bookings_view.getDiscount());
                    if (!(bookings_view.getDiscount().trim().equals("")) ) {
                        if (bookings_view.getDiscount().equals(parent_frame.used_disc_coupons[0])) {
                            JOptionPane.showMessageDialog(parent_container, "The coupon you have "
                                    + "entered is not valid.");
                        }
                        for (int i = 0; i <= 5; i++) {
                            if (parent_frame.disc_coupons[i].equals(bookings_view.getDiscount())) {
                                parent_frame.used_disc_coupons[0] = parent_frame.disc_coupons[i];
                                JOptionPane.showMessageDialog(parent_container, "You have entered a valid "
                                        + "coupon" + newline + "New price:" + bookings_view.buildDiscountPriceSum());
                            } else if (i == 5 && !parent_frame.used_disc_coupons[0].equals(bookings_view.getDiscount())) {
                                JOptionPane.showMessageDialog(parent_container, "The coupon you have "
                                        + "entered is not valid.");
                            }
                        }
                    }
                }
                if (bookings_view.getBookingState() == 2){
                /*    booking_model.setSeatNumbers(bookings_view.getSeatNumbers());
                    booking_model.setBags(bookings_view.getBags());
                    booking_model.setAncillaries(bookings_view.getAncillaries());*/

                    // Create Booking in Database
                    booking_model.setBookingId(booking_model.reserveBookingID());
                    booking_model.buildBookingInsert();
                    booking_model.buildFlightsBookingsFKInsert();
                } else if (bookings_view.getBookingState() == 3){
                    check_status = bookings_view.credit_card_controller.performChecks();
                    if (check_status==true) {
                        booking_model.setName(bookings_view.info_panel.getFName().getText() + " " +
                                bookings_view.info_panel.getMI().getText() + " " +
                                bookings_view.info_panel.getLName().getText());
                        booking_model.setDOB(bookings_view.info_panel.getDOB_Month().getSelectedItem().toString() + "/" +
                                bookings_view.info_panel.getDOB_Day().getSelectedItem().toString() + "/" +
                                bookings_view.info_panel.getDOB_Year().getSelectedItem().toString());
                        if (bookings_view.info_panel.getGender_Male().isCursorSet()) {
                            booking_model.setGender("Male");
                        } else {
                            booking_model.setGender("Female");
                        }
                        booking_model.setAddress(bookings_view.info_panel.getAddress1().getText());
                        booking_model.setState(bookings_view.info_panel.getState().getSelectedItem().toString());
                        booking_model.setCity(bookings_view.info_panel.getCity().getText());
                        booking_model.setZip(bookings_view.info_panel.getZip().getText());
                        booking_model.setEmail(bookings_view.info_panel.getEmail().getText());
                        booking_model.setPhone(bookings_view.info_panel.getPhone().getText());

                        // Create PersonalInfo entry in Database
                        booking_model.setPersonalInfoId(booking_model.reservePersonalInfoID());
                        booking_model.buildPersonalInsert();

                        // Attach PersonalInfo entry to previously created Booking entry
                        booking_model.setBookingsPersonalFKID(booking_model.reserveBookingsPersonalFK());
                        booking_model.buildBookingsPersonalFKInsert();

                        // Attach Booking to Account if logged in, otherwise attach to admin/guest account.
                        if (parent_frame.account_flow == null) {
                            booking_model.setAccountId(0);
                        } else {
                            booking_model.setAccountId(parent_frame.account_flow.account_model.getAccountID());
                        }
                        booking_model.buildAccountsBookingsFKInsert();

                        // Create Entries for Card and Attach to Booking & Account (Done through CardController)
                        bookings_view.addPaymentToAccount();
                        bookings_view.addPaymentToBooking(booking_model.getBookingId());

                        //bookings_view.continue_btn = new JButton("Continue");
                        //  addContinueControl();
                        //booking_model.executePersonalInfoInsert();
                        //booking_model.executeBookingInsert();

                        String[] summary_strings;
                        String account_summary = booking_model.queryAccountInfo();
                        String booking_summary = booking_model.queryBookingInfo();
                        String flight_summary = booking_model.queryFlightInfo();
                        String card_summary = booking_model.queryCardInfo();
                        String personal_info_summary = booking_model.queryPersonalInfo();
                        summary_strings = new String[]{account_summary, booking_summary, flight_summary, card_summary, personal_info_summary};
                        System.out.println(summary_strings.toString());
                        bookings_view.setBookingSummary(summary_strings);
                    }

                }
                if(check_status) {
                    bookings_view.setBookingState(bookings_view.getBookingState() + 1);
                    bookings_view.updateView();
                    bookings_view.revalidate();
                    bookings_view.repaint();
                }
            }
        });
    }

    private void addReturnControl(){
        bookings_view.return_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                parent_frame.returnSearchFlow();
            }
        });
    }
    
    public void addCreditView() {
        
    }
}

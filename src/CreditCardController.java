import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CreditCardController {
    CreditCardView card_view;
    Container parent_container;
    Travlr parent_frame;
    CreditCardModel card_model;
    String card_num, card_cvv, name_on_card, card_exp, card_type;
    int card_id, account_id, booking_id, account_card_fk_id, booking_card_fk_id = 0;
    
    /*******************************************************************
    * Name:    CreditCardController()    :   Constructor               *
    * Purpose:                                                         *
    * Params:                                                          *
    * Postcondition:                                                   *
    *      CreditCardController() object has been created.             *
    ********************************************************************/

    public CreditCardController(Travlr pframe) {
        parent_frame = pframe;
        card_model = new CreditCardModel();
        card_view = new CreditCardView();
        card_view.updateView();
        //addCreditCardControls();
    }

    public CreditCardController(Travlr pframe, JButton control_btn) {
        parent_frame = pframe;
        card_model = new CreditCardModel();
        card_view = new CreditCardView();
        card_view.updateView();
        //addCreditCardControls(control_btn);
    }

    public CreditCardController(Travlr pframe, Container pcontain) {
        parent_frame = pframe;
        parent_container = pcontain;
        card_model = new CreditCardModel();
        card_view = new CreditCardView();
        card_view.updateView();
        //parent_container.add(card_view);
        //addCreditCardControls();
        
    }


    protected void addCardToAccount(){
        if (parent_frame.account_flow == null){
            System.out.println("No account connection could be found, Attaching card to admin account.");
            account_id = 1;
        } else {
            account_id = parent_frame.account_flow.account_model.getAccountID();
        }
        String account_card_fk_insert = ("insert into accounts_cards(account_id, card_id) VALUES ("+account_id+","+card_id+");");
        System.out.println(account_card_fk_insert);
        insertDB(account_card_fk_insert);
    }

    protected void addCardToBooking(int booking_id){
        this.booking_id = booking_id;
        String booking_card_fk_insert = ("insert into bookings_cards(booking_id, card_id) VALUES ("+booking_id+","+card_id+");");
        System.out.println(booking_card_fk_insert);
        insertDB(booking_card_fk_insert);
    }

    protected void addCreditCardToDB(){
        String cc_insert_string = ("insert into cards(num, cvv, company, name_on_card, expiration) VALUES "+
                "('"+card_view.cardNum.getText()+"', '"+card_view.cardCVV.getText()+"', 'Visa', '"+
                card_view.nameCard.getText()+"','"+card_view.cardExp.getText()+"');");
        insertDB(cc_insert_string);
        System.out.println(cc_insert_string);
        String query_string = ("select max(id) from cards;");
        System.out.println(query_string);
        ResultSet rs = queryDB(query_string);
        card_id = Integer.parseInt(handleReserveInsert(rs));

    }
    protected boolean performChecks() {
        card_num = card_view.cardNum.getText();
        card_cvv = card_view.cardCVV.getText();
        name_on_card = card_view.nameCard.getText();
        card_exp = card_view.cardExp.getText();
        if (card_view.visa.isSelected()) {
            card_type = "Visa";
            int countNum = 0;
            int countCVV = 0;
            for (int i = 0; i < card_num.length(); i++) {
                if (Character.isDigit(card_num.charAt(i))) {
                    countNum++;
                }
            }
            for (int i = 0; i < card_cvv.length(); i++) {
                if (Character.isDigit(card_cvv.charAt(i))) {
                    countCVV++;
                }
            }
            boolean check = false;
            if (card_exp.length() == 5) {
                int month = Integer.parseInt(card_exp.substring(0, 2));
                int year = Integer.parseInt(card_exp.substring(3, 5));
                if (card_exp.matches("([0-1]{1})([0-9]{1})/([1-2]{1})([0-9]{1})")) {
                    if (month > 13 || year < 15) {
                        check = false;
                    } else {
                        check = true;
                    }
                }
            } else {
                check = false;
            }
            if (((countNum == 13 && countCVV == 3) || (countNum == 16 && countCVV == 3)) && check) {
                addCreditCardToDB();
                return true;
               /*boolean success = card_model.addCardToDatabase(card_num, card_cvv, name_on_card, card_exp, card_type);
               if (success) {
                   JOptionPane.showMessageDialog(card_view.card_pane, "Your Credit Card was added!");
               }*/
            } else {
                JOptionPane.showMessageDialog(card_view.card_pane, "Invalid Credit Card");
                return false;
            }
        }
        if (card_view.mastercard.isSelected()) {
            card_type = "Mastercard";
            int countNum = 0;
            int countCVV = 0;
            for (int i = 0; i < card_num.length(); i++) {
                if (Character.isDigit(card_num.charAt(i))) {
                    countNum++;
                }
            }
            for (int i = 0; i < card_cvv.length(); i++) {
                if (Character.isDigit(card_cvv.charAt(i))) {
                    countCVV++;
                }
            }
            boolean check = false;
            if (card_exp.length() == 5){
                int month = Integer.parseInt(card_exp.substring(0, 2));
                int year = Integer.parseInt(card_exp.substring(3, 5));
                if (card_exp.matches("([0-1]{1})([0-9]{1})/([1-2]{1})([0-9]{1})")) {
                    if (month > 13 || year < 15) {
                        check = false;
                    } else {
                        check = true;
                    }
                }
            } else {
                check = false;
            }
            if (countNum == 16 && countCVV == 3 && check) {
                addCreditCardToDB();
                return true;
           /*    boolean success = card_model.addCardToDatabase(card_num, card_cvv, name_on_card, card_exp, card_type);
               if (success) {
                   JOptionPane.showMessageDialog(card_view.card_pane, "Your Credit Card was added!");
               }*/
            } else {
                JOptionPane.showMessageDialog(card_view.card_pane, "Invalid Credit Card");
                return false;
            }
        }
        if (card_view.amex.isSelected()) {
            card_type = "Amex";
            int countNum = 0;
            int countCVV = 0;
            for (int i = 0; i < card_num.length(); i++) {
                if (Character.isDigit(card_num.charAt(i))) {
                    countNum++;
                }
            }
            for (int i = 0; i < card_cvv.length(); i++) {
                if (Character.isDigit(card_cvv.charAt(i))) {
                    countCVV++;
                }
            }
            boolean check = false;
            if (card_exp.length() == 5){
                int month = Integer.parseInt(card_exp.substring(0, 2));
                int year = Integer.parseInt(card_exp.substring(3, 5));
                if (card_exp.matches("([0-1]{1})([0-9]{1})/([1-2]{1})([0-9]{1})")) {
                    if (month > 13 || year < 15) {
                        check = false;
                    } else {
                        check = true;
                    }
                }
            } else {
                check = false;
            }
            if (countNum == 15 && countCVV == 4 && check) {
                addCreditCardToDB();
                return true;
           /*    boolean success = card_model.addCardToDatabase(card_num, card_cvv, name_on_card, card_exp, card_type);
               if (success) {
                   JOptionPane.showMessageDialog(card_view.card_pane, "Your Credit Card was added!");
               }*/
            } else {
                JOptionPane.showMessageDialog(card_view.card_pane, "Invalid Credit Card");
                return false;
            }
        }
        if (card_view.discover.isSelected()) {
            card_type = "Discover";
            int countNum = 0;
            int countCVV = 0;
            for (int i = 0; i < card_num.length(); i++) {
                if (Character.isDigit(card_num.charAt(i))) {
                    countNum++;
                }
            }
            for (int i = 0; i < card_cvv.length(); i++) {
                if (Character.isDigit(card_cvv.charAt(i))) {
                    countCVV++;
                }
            }
            boolean check = false;
            if (card_exp.length() == 5){
                int month = Integer.parseInt(card_exp.substring(0, 2));
                int year = Integer.parseInt(card_exp.substring(3, 5));
                if (card_exp.matches("([0-1]{1})([0-9]{1})/([1-2]{1})([0-9]{1})")) {
                    if (month > 13 || year < 15) {
                        check = false;
                    } else {
                        check = true;
                    }
                }
            } else {
                check = false;
            }
            if (countNum == 16 && countCVV == 3 && check) {
                addCreditCardToDB();
                return true;
           /*    boolean success = card_model.addCardToDatabase(card_num, card_cvv, name_on_card, card_exp, card_type);
               if (success) {
                   JOptionPane.showMessageDialog(card_view.card_pane, "Your Credit Card was added!");
               }*/
            } else {
                JOptionPane.showMessageDialog(card_view.card_pane, "Invalid Credit Card");
                return false;
            }
        }
        return false;
    }

    /*public void addCreditCardControls(JButton input_button) {
       input_button.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent actionEvent) {
               card_num = card_view.cardNum.getText();
               card_cvv = card_view.cardCVV.getText();
               name_on_card = card_view.nameCard.getText();
               card_exp = card_view.cardExp.getText();
               if (card_view.visa.isSelected()) {
                   card_type = "Visa";
                   int countNum = 0;
                   int countCVV = 0;
                   for (int i = 0; i < card_num.length(); i++) {
                       if (Character.isDigit(card_num.charAt(i))) {
                           countNum++;
                       }
                   }
                   for (int i = 0; i < card_cvv.length(); i++) {
                       if (Character.isDigit(card_cvv.charAt(i))) {
                           countCVV++;
                       }
                   }
                   boolean check = false;
                   int month = Integer.parseInt(card_exp.substring(0, 2));
                   int year = Integer.parseInt(card_exp.substring(3, 5));
                   if (card_exp.matches("([0-1]{1})([0-9]{1})/([1-2]{1})([0-9]{1})")) {
                       if (month > 13 || year < 15) {
                           check = false;
                       } else {
                           check = true;
                       }
                   }
                   if (((countNum == 13 && countCVV == 3) || (countNum == 16 && countCVV == 3)) && check) {
                       addCreditCardToDB();
                   /*    boolean success = card_model.addCardToDatabase(card_num, card_cvv, name_on_card, card_exp, card_type);
                       if (success) {
                           JOptionPane.showMessageDialog(card_view.card_pane, "Your Credit Card was added!");
                       }
                   } else {
                       JOptionPane.showMessageDialog(card_view.card_pane, "Invalid Credit Card");
                   }
               }
               if (card_view.mastercard.isSelected()) {
                   card_type = "Mastercard";
                   int countNum = 0;
                   int countCVV = 0;
                   for (int i = 0; i < card_num.length(); i++) {
                       if (Character.isDigit(card_num.charAt(i))) {
                           countNum++;
                       }
                   }
                   for (int i = 0; i < card_cvv.length(); i++) {
                       if (Character.isDigit(card_cvv.charAt(i))) {
                           countCVV++;
                       }
                   }
                   boolean check = false;
                   int month = Integer.parseInt(card_exp.substring(0, 2));
                   int year = Integer.parseInt(card_exp.substring(3, 5));
                   if (card_exp.matches("([0-1]{1})([0-9]{1})/([1-2]{1})([0-9]{1})")) {
                       if (month > 13 || year < 15) {
                           check = false;
                       } else {
                           check = true;
                       }
                   }
                   if (countNum == 16 && countCVV == 3 && check) {
                       addCreditCardToDB();
                   /*    boolean success = card_model.addCardToDatabase(card_num, card_cvv, name_on_card, card_exp, card_type);
                       if (success) {
                           JOptionPane.showMessageDialog(card_view.card_pane, "Your Credit Card was added!");
                       }
                   } else {
                       JOptionPane.showMessageDialog(card_view.card_pane, "Invalid Credit Card");
                   }
               }
               if (card_view.amex.isSelected()) {
                   card_type = "Amex";
                   int countNum = 0;
                   int countCVV = 0;
                   for (int i = 0; i < card_num.length(); i++) {
                       if (Character.isDigit(card_num.charAt(i))) {
                           countNum++;
                       }
                   }
                   for (int i = 0; i < card_cvv.length(); i++) {
                       if (Character.isDigit(card_cvv.charAt(i))) {
                           countCVV++;
                       }
                   }
                   boolean check = false;
                   int month = Integer.parseInt(card_exp.substring(0, 2));
                   int year = Integer.parseInt(card_exp.substring(3, 5));
                   if (card_exp.matches("([0-1]{1})([0-9]{1})/([1-2]{1})([0-9]{1})")) {
                       if (month > 13 || year < 15) {
                           check = false;
                       } else {
                           check = true;
                       }
                   }
                   if (countNum == 15 && countCVV == 4 && check) {
                       addCreditCardToDB();
                   /*    boolean success = card_model.addCardToDatabase(card_num, card_cvv, name_on_card, card_exp, card_type);
                       if (success) {
                           JOptionPane.showMessageDialog(card_view.card_pane, "Your Credit Card was added!");
                       }
                   } else {
                       JOptionPane.showMessageDialog(card_view.card_pane, "Invalid Credit Card");
                   }
               }
               if (card_view.discover.isSelected()) {
                   card_type = "Discover";
                   int countNum = 0;
                   int countCVV = 0;
                   for (int i = 0; i < card_num.length(); i++) {
                       if (Character.isDigit(card_num.charAt(i))) {
                           countNum++;
                       }
                   }
                   for (int i = 0; i < card_cvv.length(); i++) {
                       if (Character.isDigit(card_cvv.charAt(i))) {
                           countCVV++;
                       }
                   }
                   boolean check = false;
                   int month = Integer.parseInt(card_exp.substring(0, 2));
                   int year = Integer.parseInt(card_exp.substring(3, 5));
                   if (card_exp.matches("([0-1]{1})([0-9]{1})/([1-2]{1})([0-9]{1})")) {
                       if (month > 13 || year < 15) {
                           check = false;
                       } else {
                           check = true;
                       }
                   }
                   if (countNum == 16 && countCVV == 3 && check) {
                       addCreditCardToDB();
                   /*    boolean success = card_model.addCardToDatabase(card_num, card_cvv, name_on_card, card_exp, card_type);
                       if (success) {
                           JOptionPane.showMessageDialog(card_view.card_pane, "Your Credit Card was added!");
                       }
                   } else {
                       JOptionPane.showMessageDialog(card_view.card_pane, "Invalid Credit Card");
                   }
               }
           }
       });
    }*/
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
}

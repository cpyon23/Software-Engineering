import javax.swing.*;
import java.awt.*;

public class CreditCardView extends JPanel {
    String display;
    JPanel card_pane;
    JLabel card_num, card_cvv, name_on_card, card_exp;
    JTextField cardNum, cardCVV, nameCard, cardExp;
    JRadioButton visa,mastercard,amex, discover;
    ButtonGroup group;
    JButton submit;
    
   public CreditCardView() {
        this.display = "default";
    }
   
   protected void updateView() {
      // this.removeAll();
       if(this.display == "default") {
           this.add(creditView());
       }
   }
   
   private JPanel creditView() {
        card_pane = new JPanel();
        card_pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        name_on_card = new JLabel("Name on Card:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = (new Insets(5,10,5,10));
        card_pane.add(name_on_card, c);
        
        nameCard = new JTextField(15);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.gridx = 1;
        c.gridy = 0;
        c.insets = (new Insets(5,60,5,10));
        card_pane.add(nameCard, c);
        
        card_num = new JLabel("Credit Card Number:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = (new Insets(5,10,5,10));
        card_pane.add(card_num, c);
        
        cardNum = new JTextField(15);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.gridx = 1;
        c.gridy = 1;
        c.insets = (new Insets(5,60,5,10));
        card_pane.add(cardNum, c);
        
        card_cvv = new JLabel("Card CVV:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = (new Insets(5,10,5,10));
        card_pane.add(card_cvv, c);
        
        cardCVV = new JTextField(3);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.gridx = 1;
        c.gridy = 2;
        c.insets = (new Insets(5,60,5,10));
        card_pane.add(cardCVV, c);
        
        card_exp = new JLabel("Card Expiration:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        c.insets = (new Insets(5,10,5,10));
        card_pane.add(card_exp, c);
        
        cardExp = new JTextField(15);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.gridx = 1;
        c.gridy = 3;
        c.insets = (new Insets(5,60,5,10));
        card_pane.add(cardExp, c);
        
        
        visa = new JRadioButton("Visa", true);
        mastercard = new JRadioButton("Mastercard");
        amex = new JRadioButton("Amex");
        discover = new JRadioButton("Discover");
        group = new ButtonGroup();
        group.add(visa);
        group.add(mastercard);
        group.add(amex);
        group.add(discover);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 4;
        c.insets = (new Insets(5,10,5,10));
        card_pane.add(visa,c);
        c.gridx++;
        card_pane.add(mastercard,c);
        c.gridx++;
        card_pane.add(amex,c);
        c.gridx++;
        card_pane.add(discover,c);
        
        return card_pane;
   }
}

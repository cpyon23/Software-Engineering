import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by lbontecou on 10/26/15.
 */
public class ImagePanel extends JPanel {

    private BufferedImage image;
    protected JButton account_btn, info_btn, home_btn;
    protected JPanel btn_panel;
    protected Travlr parent_frame = Travlr.main_frame;
    protected AccountView acct_view;

    public ImagePanel(URL file_path) {
        try {
            image = ImageIO.read(file_path);
        } catch (IOException ex) {
            System.out.println("Failure Displaying Image.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = this.getWidth();
        int height = this.getHeight();

        int h = (int)Math.round(height*.25);
        int w = (int)Math.round(width*.25);
        g.drawImage(image, 0, 0, width, height, this); // see javadoc for more info on the parameters
    }

    protected void addAccountButton(Boolean session_active_status){
        btn_panel = new JPanel();
        btn_panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.setLayout(new BorderLayout());
        account_btn = new JButton("Logout");
        info_btn = new JButton("User Information");
        home_btn = new JButton("Home");
        if (session_active_status){
            btn_panel.add(home_btn);
            btn_panel.add(info_btn);
        } else {
            account_btn = new JButton("Login");
            info_btn = new JButton();
        }
        btn_panel.add(account_btn);
        btn_panel.setOpaque(false);
        this.add(btn_panel, BorderLayout.NORTH);
    }

    protected void addAccountButtonControls(){
        account_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (parent_frame.getAccountSessionStatus()){
                    parent_frame.setAccountSessionStatus(false);
                    account_btn.setText("Login");
                    parent_frame.account_flow = null;
                    btn_panel.removeAll();
                    btn_panel.add(account_btn);
                    btn_panel.revalidate();
                    btn_panel.repaint();

                    parent_frame.returnSearchFlow();
                    parent_frame.revalidate();
                    parent_frame.repaint();
                } else {
                    parent_frame.startAccountFlow();
                }
            }
        });

        info_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                parent_frame.enterAccountFlow();
            }
        });

        home_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                parent_frame.returnSearchFlow();
            }
        });
        
    }
}

package laptrinhmang_doan;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

public class ReviewFrame {
    private JPanel panel_36;
    private JFXPanel javafxPanel;
    public JTabbedPane tabbedPane;
    private String conutryName;
    JFrame f = new JFrame();
    public static void main(String[] args) {
        ReviewFrame reviewFrame = new ReviewFrame();
    }

    public ReviewFrame(String conutryName) {
        initialize();
        initSwingComponents();
        loadJavaFXScene(conutryName);
    }

    public ReviewFrame() {
        this.conutryName = "Việt Nam";
        initialize();
        initSwingComponents();
        loadJavaFXScene(conutryName);
    }
    private void initialize() {
        
        f.setBounds(100, 100, 1350, 800); // set size frame
        f.setLocationRelativeTo(null);
        //f.setDefaultCloseOperation(D);
        
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setForeground(new Color(0, 0, 128));
        tabbedPane.setBounds(0, 0, f.getWidth(), f.getHeight());
        f.getContentPane().add(tabbedPane);
        f.setVisible(true);
    }

    public void initSwingComponents() {
        javafxPanel = new JFXPanel();
        javafxPanel.setBounds(0, 0, f.getWidth(), f.getHeight()); // Set size google
        panel_36 = new JPanel();
        panel_36.setLayout(null);
        panel_36.setBounds(0, 0, f.getWidth(), f.getHeight());
        panel_36.add(javafxPanel);
        tabbedPane.addTab("Review",
            new ImageIcon("src/Icons/literature-30.png"), panel_36);
    }

    public void loadJavaFXScene(String countryName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                BorderPane borderPane = new BorderPane();
                WebView webComponent = new WebView();
                webComponent.getEngine().load("https://vi.wikipedia.org/wiki/"+countryName);////https://cacnuoc.vn/nhat-ban/
                borderPane.setCenter(webComponent);
                Scene scene = new Scene(borderPane);
                javafxPanel.setScene(scene);
                webComponent.autosize();
            }
        });
    }
}
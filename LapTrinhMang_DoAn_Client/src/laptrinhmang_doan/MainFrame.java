package laptrinhmang_doan;

import ComponentCustomize.ConnectServer;
import ComponentCustomize.SecurityData_Client;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainFrame extends javax.swing.JFrame {
    private final String COLOR = "#669900";
    private final String HOVER_COLOR = "#669933";
    ArrayList<JPanel> listPanelItem = new ArrayList<>();
    public static final ConnectServer _CONNECT_SERVER = new ConnectServer();
    //hương thức java.util.UUID.randomUUID() được sử dụng để sinh ra một chuỗi ngẫu nhiên có độ dài 32 ký tự (128 bit)
    //được biểu diễn ở hệ hệ thập lục phân (hex: 0-9A-F) và 4 ký tự phân tách (–). ví dụ: ef41d4cb-5d1b-4a83-a4c1-f1397e3fcdb7
    public static final String _SESSION_KEY = UUID.randomUUID().toString();
    public static final SecurityData_Client _SERCURITY_CLIENT = new SecurityData_Client();
    
    public MainFrame() {
        _CONNECT_SERVER.senData(_SERCURITY_CLIENT.maHoaRSA(_SESSION_KEY));
        initComponents();
        initMoving(this);
        //MainPanel.setBackground(Color.decode(COLOR));
        LeftPanel.setBackground(Color.decode(COLOR));
        HeadPanel.setBackground(Color.decode(COLOR));
        //jPanel3.setSize(MainPanel.getWidth()-LeftPanel.getWidth(), MainPanel.getHeight()-HeadPanel.getHeight());
        listPanelItem.add(Panel_Item_ThongKeCovid);
        listPanelItem.add(Panel_Item_TraCuuCovid);
        listPanelItem.add(Panel_TraCuuDiaLy);
        listPanelItem.add(Panel_TraCuuDiaLyQuocGia);
        setSelectedItem(Panel_Item_ThongKeCovid, HOVER_COLOR);
        addPanel(new ThongKeCovidPanel());
        
    }

    public void setSelectedItem(JPanel panel, String decodeColor){
        for(int i = 0; i < listPanelItem.size(); i++){
            if(listPanelItem.get(i)==panel){
                listPanelItem.get(i).setBackground(Color.decode(HOVER_COLOR));
            }else{
                listPanelItem.get(i).setBackground(Color.decode(COLOR));
            }
        }
    }
    
    private int x;
    private int y;
    public void initMoving(JFrame frame){
        HeadPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                x = e.getX();
                y = e.getY();
            }
        });
        HeadPanel.addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseDragged(MouseEvent me){
                frame.setLocation(me.getXOnScreen() - x, me.getYOnScreen() - y);
            }
        });
    }
    //Hàm add chức năng được chọn vào panel chính (Panel Center)
    public void addPanel(JPanel panel){
        CenterPanel.removeAll();
        CenterPanel.add(panel);
        CenterPanel.repaint();
        CenterPanel.revalidate();
    }
    public static void exitFrame(){
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainPanel = new javax.swing.JPanel();
        LeftPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        Panel_Item_ThongKeCovid = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        Panel_TraCuuDiaLy = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Panel_Item_TraCuuCovid = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        Panel_TraCuuDiaLyQuocGia = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        HeadPanel = new javax.swing.JPanel();
        CloseButton = new javax.swing.JButton();
        MiniMizeButton = new javax.swing.JButton();
        MaximizeButton = new javax.swing.JButton();
        CenterPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jLabel3.setFont(new java.awt.Font("Viner Hand ITC", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/logo-64.png"))); // NOI18N
        jLabel3.setText("Application");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Thông tin Covid");

        Panel_Item_ThongKeCovid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel_Item_ThongKeCovidMouseClicked(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/chart-24.png"))); // NOI18N
        jLabel2.setText("Thống kê Covid");

        javax.swing.GroupLayout Panel_Item_ThongKeCovidLayout = new javax.swing.GroupLayout(Panel_Item_ThongKeCovid);
        Panel_Item_ThongKeCovid.setLayout(Panel_Item_ThongKeCovidLayout);
        Panel_Item_ThongKeCovidLayout.setHorizontalGroup(
            Panel_Item_ThongKeCovidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
        );
        Panel_Item_ThongKeCovidLayout.setVerticalGroup(
            Panel_Item_ThongKeCovidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        Panel_TraCuuDiaLy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel_TraCuuDiaLyMouseClicked(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/taxi-location-24.png"))); // NOI18N
        jLabel4.setText("Tra cứu thành phố");

        javax.swing.GroupLayout Panel_TraCuuDiaLyLayout = new javax.swing.GroupLayout(Panel_TraCuuDiaLy);
        Panel_TraCuuDiaLy.setLayout(Panel_TraCuuDiaLyLayout);
        Panel_TraCuuDiaLyLayout.setHorizontalGroup(
            Panel_TraCuuDiaLyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_TraCuuDiaLyLayout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
        );
        Panel_TraCuuDiaLyLayout.setVerticalGroup(
            Panel_TraCuuDiaLyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Thông tin địa lý");

        Panel_Item_TraCuuCovid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel_Item_TraCuuCovidMouseClicked(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search-24.png"))); // NOI18N
        jLabel6.setText("Tra cứu Covid");

        javax.swing.GroupLayout Panel_Item_TraCuuCovidLayout = new javax.swing.GroupLayout(Panel_Item_TraCuuCovid);
        Panel_Item_TraCuuCovid.setLayout(Panel_Item_TraCuuCovidLayout);
        Panel_Item_TraCuuCovidLayout.setHorizontalGroup(
            Panel_Item_TraCuuCovidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Item_TraCuuCovidLayout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        Panel_Item_TraCuuCovidLayout.setVerticalGroup(
            Panel_Item_TraCuuCovidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        Panel_TraCuuDiaLyQuocGia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Panel_TraCuuDiaLyQuocGiaMouseClicked(evt);
            }
        });

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/location-24.png"))); // NOI18N
        jLabel8.setText("Tra cứu quốc gia");

        javax.swing.GroupLayout Panel_TraCuuDiaLyQuocGiaLayout = new javax.swing.GroupLayout(Panel_TraCuuDiaLyQuocGia);
        Panel_TraCuuDiaLyQuocGia.setLayout(Panel_TraCuuDiaLyQuocGiaLayout);
        Panel_TraCuuDiaLyQuocGiaLayout.setHorizontalGroup(
            Panel_TraCuuDiaLyQuocGiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_TraCuuDiaLyQuocGiaLayout.createSequentialGroup()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
        );
        Panel_TraCuuDiaLyQuocGiaLayout.setVerticalGroup(
            Panel_TraCuuDiaLyQuocGiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout LeftPanelLayout = new javax.swing.GroupLayout(LeftPanel);
        LeftPanel.setLayout(LeftPanelLayout);
        LeftPanelLayout.setHorizontalGroup(
            LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(LeftPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LeftPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Panel_Item_TraCuuCovid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Panel_Item_ThongKeCovid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(LeftPanelLayout.createSequentialGroup()
                        .addGroup(LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1)
                            .addGroup(LeftPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Panel_TraCuuDiaLyQuocGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Panel_TraCuuDiaLy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 18, Short.MAX_VALUE))
        );
        LeftPanelLayout.setVerticalGroup(
            LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftPanelLayout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Panel_Item_ThongKeCovid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Panel_Item_TraCuuCovid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Panel_TraCuuDiaLy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Panel_TraCuuDiaLyQuocGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        HeadPanel.setBackground(new java.awt.Color(255, 255, 255));

        CloseButton.setBackground(new java.awt.Color(255, 0, 0));
        CloseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/close-30.png"))); // NOI18N
        CloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseButtonActionPerformed(evt);
            }
        });

        MiniMizeButton.setBackground(new java.awt.Color(51, 255, 51));
        MiniMizeButton.setForeground(new java.awt.Color(51, 255, 51));
        MiniMizeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8-minimize-window-24.png"))); // NOI18N
        MiniMizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MiniMizeButtonActionPerformed(evt);
            }
        });

        MaximizeButton.setBackground(new java.awt.Color(255, 255, 51));
        MaximizeButton.setForeground(new java.awt.Color(255, 255, 51));
        MaximizeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icons8-maximize-window-24.png"))); // NOI18N
        MaximizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MaximizeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout HeadPanelLayout = new javax.swing.GroupLayout(HeadPanel);
        HeadPanel.setLayout(HeadPanelLayout);
        HeadPanelLayout.setHorizontalGroup(
            HeadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HeadPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(MiniMizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MaximizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );
        HeadPanelLayout.setVerticalGroup(
            HeadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeadPanelLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(HeadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MaximizeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MiniMizeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CloseButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        CenterPanel.setBackground(new java.awt.Color(255, 255, 255));
        CenterPanel.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1125, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 840, Short.MAX_VALUE)
        );

        CenterPanel.add(jPanel1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addComponent(LeftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(HeadPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(CenterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        MainPanelLayout.setVerticalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(LeftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, MainPanelLayout.createSequentialGroup()
                .addComponent(HeadPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CenterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void CloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseButtonActionPerformed
        // Thoát chương trình
        int hoi = JOptionPane.showConfirmDialog(this, "Bạn có muốn thoát chương trình không?", null, JOptionPane.YES_NO_OPTION);
        if (hoi == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_CloseButtonActionPerformed

    private void MiniMizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MiniMizeButtonActionPerformed
        // Thu giao diện xuống taskbar
        setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_MiniMizeButtonActionPerformed
    int dem = 1;
    private void MaximizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MaximizeButtonActionPerformed
        // Toàn màn hình cho giao diện
        if(dem%2!=0){
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        else{
            setExtendedState(JFrame.NORMAL);
        }
        dem++;
    }//GEN-LAST:event_MaximizeButtonActionPerformed

    private void Panel_Item_ThongKeCovidMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel_Item_ThongKeCovidMouseClicked
        setSelectedItem(Panel_Item_ThongKeCovid, HOVER_COLOR);
        addPanel(new ThongKeCovidPanel());
        
    }//GEN-LAST:event_Panel_Item_ThongKeCovidMouseClicked

    private void Panel_Item_TraCuuCovidMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel_Item_TraCuuCovidMouseClicked
        setSelectedItem(Panel_Item_TraCuuCovid, HOVER_COLOR);
        addPanel(new TraCuuCovidPanel());
        
    }//GEN-LAST:event_Panel_Item_TraCuuCovidMouseClicked

    private void Panel_TraCuuDiaLyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel_TraCuuDiaLyMouseClicked
        setSelectedItem(Panel_TraCuuDiaLy, HOVER_COLOR);
        addPanel(new TraCuuDiaLyThanhPhoPanel());
        
    }//GEN-LAST:event_Panel_TraCuuDiaLyMouseClicked

    private void Panel_TraCuuDiaLyQuocGiaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel_TraCuuDiaLyQuocGiaMouseClicked
        setSelectedItem(Panel_TraCuuDiaLyQuocGia, HOVER_COLOR);
        addPanel(new TraCuuDiaLyQuocGiaPanel());
    }//GEN-LAST:event_Panel_TraCuuDiaLyQuocGiaMouseClicked

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CenterPanel;
    private javax.swing.JButton CloseButton;
    private javax.swing.JPanel HeadPanel;
    private javax.swing.JPanel LeftPanel;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JButton MaximizeButton;
    private javax.swing.JButton MiniMizeButton;
    private javax.swing.JPanel Panel_Item_ThongKeCovid;
    private javax.swing.JPanel Panel_Item_TraCuuCovid;
    private javax.swing.JPanel Panel_TraCuuDiaLy;
    private javax.swing.JPanel Panel_TraCuuDiaLyQuocGia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}

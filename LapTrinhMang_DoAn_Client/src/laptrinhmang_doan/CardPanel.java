package laptrinhmang_doan;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class CardPanel extends javax.swing.JPanel {
    public Color color1;
    public Color color2;
    public Color circle;
    public String TitleText;
    public String SubText;
    public String HomNayText;

    public JLabel getLabel_HomNay() {
        return Label_HomNay;
    }

    public void setLabel_HomNay(JLabel Label_HomNay) {
        this.Label_HomNay = Label_HomNay;
    }

    public JLabel getLabel_Sub() {
        return Label_Sub;
    }

    public void setLabel_Sub(JLabel Label_Sub) {
        this.Label_Sub = Label_Sub;
    }

    public JLabel getLabel_Title() {
        return Label_Title;
    }

    public void setLabel_Title(JLabel Label_Title) {
        this.Label_Title = Label_Title;
    }

    public String getHomNayText() {
        return HomNayText;
    }

    public void setHomNayText(String HomNayText) {
        this.HomNayText = HomNayText;
        Label_HomNay.setText(HomNayText);
    }
    public ImageIcon imageIcon = new ImageIcon("src/Icons/verified-account-30.png");
    
    public Color getColor1() {
        return color1;
    }

    public void setColor1(Color color1) {
        this.color1 = color1;
    }

    public Color getColor2() {
        return color2;
    }

    public void setColor2(Color color2) {
        this.color2 = color2;
    }

    public Color getCircle() {
        return circle;
    }

    public void setCircle(Color circle) {
        this.circle = circle;
    }

    public JLabel getLabelIcon() {
        return LabelIcon;
    }

    public void setLabelIcon(JLabel LabelIcon) {
        this.LabelIcon = LabelIcon;
    }
    
    public String getTitleText() {
        return TitleText;
    }

    public void setTitleText(String TitleText) {
        this.TitleText = TitleText;
        Label_Title.setText(TitleText);
    }

    public String getSubText() {
        return SubText;
    }

    public void setSubText(String SubText) {
        this.SubText = SubText;
        Label_Sub.setText(SubText);
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }
    public CardPanel() {
        initComponents();
        setOpaque(false);
        color1 = Color.WHITE;
        color2 = Color.orange; 
        circle = new Color(255,255,255,50);
        
        setImageIcon(new ImageIcon("src/Icons/verified-account-30.png"));
        setTitleText("Khỏi bệnh");
        setSubText("1.234.234.12345");
        setHomNayText("Hôm nay: 1,234");
    }


    @Override
    protected void paintComponent(Graphics gr){
        Graphics2D g2 = (Graphics2D) gr;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint g = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
        g2.setPaint(g);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.setColor(circle);
        g2.fillOval(getWidth() - (getHeight() / 2), 10, getHeight(), getHeight());
        g2.fillOval(getWidth() - (getHeight() / 2) - 10, getHeight() / 2 + 20, getHeight(), getHeight());
        LabelIcon.setIcon(getImageIcon());
        Label_Title.setText(getTitleText());
        Label_Sub.setText(getSubText());
        Label_HomNay.setText(getHomNayText());
        super.paintComponent(gr);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LabelIcon = new javax.swing.JLabel();
        Label_Title = new javax.swing.JLabel();
        Label_Sub = new javax.swing.JLabel();
        Label_HomNay = new javax.swing.JLabel();

        LabelIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/verified-account-30.png"))); // NOI18N

        Label_Title.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_Title.setForeground(new java.awt.Color(255, 255, 255));
        Label_Title.setText("Khỏi bệnh");

        Label_Sub.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Label_Sub.setForeground(new java.awt.Color(255, 255, 255));
        Label_Sub.setText("1.234.234.234");

        Label_HomNay.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_HomNay.setForeground(new java.awt.Color(255, 255, 255));
        Label_HomNay.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Label_HomNay.setText("Hôm nay: 1,234");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Label_Sub)
                    .addComponent(Label_Title)
                    .addComponent(LabelIcon))
                .addContainerGap(123, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label_HomNay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(LabelIcon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_Title)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Label_Sub)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(Label_HomNay)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelIcon;
    private javax.swing.JLabel Label_HomNay;
    private javax.swing.JLabel Label_Sub;
    private javax.swing.JLabel Label_Title;
    // End of variables declaration//GEN-END:variables
}

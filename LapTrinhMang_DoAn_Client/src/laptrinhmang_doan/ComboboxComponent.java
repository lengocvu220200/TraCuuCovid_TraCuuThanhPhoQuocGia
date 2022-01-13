package laptrinhmang_doan;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.border.EmptyBorder;

public class ComboboxComponent extends JComboBox{
    private Color backgroungColor = Color.WHITE;
    public String hintText = "Search ...";
    
    public ComboboxComponent(){
        setBackground(new Color(255,255,255,0));
        setOpaque(false);
        setBorder(new EmptyBorder(10,10,10,50));
        setFont(new Font("sansserif", 0, 16));
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (getSelectedIndex() == 0) {
            int h = getHeight();
            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets ins = getInsets();
            FontMetrics fm = g.getFontMetrics();
            int c0 = getBackground().getRGB();
            int c1 = getForeground().getRGB();
            int m = 0xfefefefe;
            int c2 = ((c0 & m) >>> 1) + ((c1 & m) >>> 1);
            g.setColor(new Color(c2, true));
            g.drawString(hintText, ins.left, h / 2 + fm.getAscent() / 2 - 2);
        }
    }
    
    @Override
    protected void paintComponent(Graphics gr){
        int width = getWidth();
        int height = getHeight();
        Graphics2D g2 = (Graphics2D) gr;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setColor(backgroungColor);
        g2.fillRoundRect(0, 0, width, height, height, height);
        //super.paintComponent(gr);
        
    }
}

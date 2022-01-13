package laptrinhmang_doan;

import ComponentCustomize.SearchCallBack;
import ComponentCustomize.SearchEvent;
import com.sun.xml.internal.ws.util.StringUtils;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TraCuuDiaLyQuocGiaPanel extends javax.swing.JPanel {
    List<String> list = new ArrayList<>();
    private Socket socket = null;
    BufferedWriter out = null;
    BufferedReader in = null;
    BufferedReader stdIn = null;
    String tenQuocGia = "";
    public TraCuuDiaLyQuocGiaPanel() {
        initComponents();
        jLabel30.getCursor();
        //jScrollPane2.createVerticalScrollBar();
        //gửi yêu cầu tới server lấy danh sách quốc gia
        MainFrame._CONNECT_SERVER.senData(MainFrame._SERCURITY_CLIENT.maHoaAES("danhsachquocgia#", MainFrame._SESSION_KEY));
        //giả mã danh sách quốc gia nhận được từ server
        checkFunction(MainFrame._SERCURITY_CLIENT.giaiMaAES(MainFrame._CONNECT_SERVER.receiveData(), MainFrame._SESSION_KEY));
        AutoCompleteDecorator.decorate(Combobox_DanhSachQuocGia);
    }

    private void checkFunction(String string){
        try {
            JSONParser parse = new JSONParser();
            JSONObject data = (JSONObject) parse.parse(string);
            switch(data.get("function").toString()){
                case "danhsachquocgia":
                    Label_KetQuTimKiem.setText("Quốc gia hiện tại");
                    danhSachQuocGia(data);
                    break;
                case "tracuudialyquocgia":
                    Label_KetQuTimKiem.setText("Kết quả tìm kiếm");
                    traCuuQuocGia(data);
                    break;
            }
        } catch (ParseException ex) {
            ex.getMessage();
        }
    }
    public void danhSachQuocGia(JSONObject json){
        DefaultComboBoxModel modelComobox = new DefaultComboBoxModel();
        traCuuQuocGia(json);
        JSONArray arr = (JSONArray) json.get("geonames");
        
        for(int i=0;i<arr.size();i++){
            JSONObject getObjectArray = (JSONObject) arr.get(i);
            modelComobox.addElement(getObjectArray.get("countryName").toString());
        }
        Combobox_DanhSachQuocGia.setModel(modelComobox);
        Combobox_DanhSachQuocGia.setSelectedItem(tenQuocGia);
    }
    public void traCuuQuocGia(JSONObject json){
        String status = json.get("status").toString();
        if(status.equals("success")){
            tenQuocGia = json.get("countryName").toString();
            setImageNationalFlag(labelQuocKy,"https://img.geonames.org/flags/x/"+json.get("countryCode").toString().toLowerCase()+".gif", 268, 174);
            setImageNationalFlag(labelBanDo,"https://img.geonames.org/img/country/250/"+json.get("countryCode").toString()+".png", 480, 280);
            label_TenQuocGia.setText(tenQuocGia);
            label_DanSo.setText(json.get("population").toString());
            label_DienTich.setText(json.get("area").toString());
            label_ThuDo.setText(json.get("capital").toString());
            label_TienTe.setText(json.get("currency").toString());
            label_NgonNgu.setText(json.get("languages").toString());
            label_ToaDo.setText(json.get("countryName").toString());
            label_MaQuocGia.setText(json.get("countryCode").toString());
            Label_ChauLuc.setText(json.get("continentName").toString());
            if(json.get("neighbours").toString().isEmpty()){
                txt_QuocGiaLienKe.setText("Không có quốc gia liền kề");
            }else{
                txt_QuocGiaLienKe.setText(json.get("neighbours").toString());
            }
        }else{
            JOptionPane.showMessageDialog(null, "Không tìm thấy kết quả!");
        }
    }
    
    private void setImageNationalFlag(JLabel lable, String urlImage, int width, int hieght){
        try {
            URL url = new URL(urlImage);
            BufferedImage image = ImageIO.read(url);
            int x = width;
            int y = hieght;
            int ix = image.getWidth();
            int iy = image.getHeight();
            int dx = 0, dy = 0;
            if (x / y > ix / iy) {
                dy = y;    
                dx = dy * ix / iy;
            } else {
                dx = x;
                dy = dx * iy / ix;
            }
            ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(dx, dy, BufferedImage.SCALE_SMOOTH));
            lable.setIcon(imageIcon);
        } catch (MalformedURLException ex) {
            Logger.getLogger(TraCuuDiaLyThanhPhoPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TraCuuDiaLyThanhPhoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private String toUpperCaseString(String string){
        StringTokenizer d = new StringTokenizer(string," ");
        String kq = "";
        while(d.hasMoreTokens()){
            kq = kq + StringUtils.capitalize(d.nextToken()+" ");
        }
        return kq;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        labelQuocKy = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        labelBanDo = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        Label_KetQuTimKiem = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        label_TenQuocGia = new javax.swing.JLabel();
        label_MaQuocGia = new javax.swing.JLabel();
        label_ToaDo = new javax.swing.JLabel();
        label_TienTe = new javax.swing.JLabel();
        label_ThuDo = new javax.swing.JLabel();
        label_DienTich = new javax.swing.JLabel();
        label_DanSo = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txt_QuocGiaLienKe = new javax.swing.JTextArea();
        jLabel30 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        Label_ChauLuc = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        label_NgonNgu = new javax.swing.JTextArea();
        Combobox_DanhSachQuocGia = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(204, 204, 204));

        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        labelQuocKy.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jPanel10.setPreferredSize(new java.awt.Dimension(5, 0));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 351, Short.MAX_VALUE)
        );

        jLabel50.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel50.setText("Quốc kỳ:");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel49.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel49.setText("Bản đồ:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel49))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(labelBanDo, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel49)
                .addGap(10, 10, 10)
                .addComponent(labelBanDo, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(134, 134, 134)
                        .addComponent(labelQuocKy, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addComponent(jLabel50)
                    .addContainerGap(1044, Short.MAX_VALUE)))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(labelQuocKy, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addGap(35, 35, 35)
                    .addComponent(jLabel50)
                    .addContainerGap(434, Short.MAX_VALUE)))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setFocusable(false);

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel19.setText("Tên quốc gia:");

        Label_KetQuTimKiem.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        Label_KetQuTimKiem.setText("Vị trí hiện tại");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel20.setText("Dân số:");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel21.setText("Diện tích:");

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel35.setText("Thủ đô:");

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel36.setText("Đơn vị tiền tệ:");

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel38.setText("Tọa độ:");

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel39.setText("Mã quốc gia:");

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel40.setText("Quốc gia liền kề:");

        label_TenQuocGia.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        label_TenQuocGia.setForeground(new java.awt.Color(0, 102, 204));
        label_TenQuocGia.setText("Việt Nam");

        label_MaQuocGia.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        label_MaQuocGia.setText("Quốc gia liền kề:");

        label_ToaDo.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        label_ToaDo.setText("Quốc gia liền kề:");

        label_TienTe.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        label_TienTe.setText("as");

        label_ThuDo.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        label_ThuDo.setText("abc");

        label_DienTich.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        label_DienTich.setText("123");

        label_DanSo.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        label_DanSo.setText("123");

        txt_QuocGiaLienKe.setColumns(20);
        txt_QuocGiaLienKe.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_QuocGiaLienKe.setLineWrap(true);
        txt_QuocGiaLienKe.setRows(5);
        txt_QuocGiaLienKe.setText("jjhhhjhjjhhjgjhgghghgghfghfghfjhfgghfhgfghjgfghfghfghfghfhgfhgfgfghfgfghhjahhdhjsdshfhhjdsfdhfhdsjfjsdhfjksdhfksjhfsdfjksdhfkjsdfhjsdfhkdjshfkjsdhfkjshdfhsdfhsdjfhjksdhfjksdhfjkdshfjksdhfjsdhfjhsdfjhsdjkfhjkdshfjksdhfjdkshfjkdshfjksdhfjshdsfhskhcfg");
        txt_QuocGiaLienKe.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        txt_QuocGiaLienKe.setCaretColor(new java.awt.Color(255, 255, 255));
        txt_QuocGiaLienKe.setDisabledTextColor(new java.awt.Color(0, 102, 255));
        jScrollPane3.setViewportView(txt_QuocGiaLienKe);

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 102, 204));
        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/share-3-24.png"))); // NOI18N
        jLabel30.setToolTipText("Xem các bài viết liên quan");
        jLabel30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel30MouseClicked(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel41.setText("Châu lục:");

        Label_ChauLuc.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        Label_ChauLuc.setText("Quốc gia liền kề:");

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel37.setText("Ngôn ngữ:");

        label_NgonNgu.setColumns(20);
        label_NgonNgu.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        label_NgonNgu.setLineWrap(true);
        label_NgonNgu.setRows(5);
        label_NgonNgu.setText("jjhhhjhjjhhjgjhgghghgghfghfghfjhfgghfhgfghjgfghfghfghfghfhgfhgfgfghfgfghhjahhdhjsdshfhhjdsfdhfhdsjfjsdhfjksdhfksjhfsdfjksdhfkjsdfhjsdfhkdjshfkjsdhfkjshdfhsdfhsdjfhjksdhfjksdhfjkdshfjksdhfjsdhfjhsdfjhsdjkfhjkdshfjksdhfjdkshfjkdshfjksdhfjshdsfhskhcfg");
        label_NgonNgu.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        label_NgonNgu.setCaretColor(new java.awt.Color(255, 255, 255));
        label_NgonNgu.setDisabledTextColor(new java.awt.Color(0, 102, 255));
        jScrollPane4.setViewportView(label_NgonNgu);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(Label_KetQuTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(187, 187, 187)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel39)
                            .addComponent(jLabel38)
                            .addComponent(jLabel36)
                            .addComponent(jLabel35)
                            .addComponent(jLabel21)
                            .addComponent(jLabel20)
                            .addComponent(jLabel41)
                            .addComponent(jLabel37))
                        .addGap(37, 37, 37))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_MaQuocGia)
                    .addComponent(label_ToaDo)
                    .addComponent(label_ThuDo)
                    .addComponent(label_TienTe)
                    .addComponent(label_DienTich)
                    .addComponent(label_DanSo)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(label_TenQuocGia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel30))
                    .addComponent(Label_ChauLuc)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(Label_KetQuTimKiem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(label_TenQuocGia)
                    .addComponent(jLabel30))
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(label_DanSo))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(label_DienTich))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(label_ThuDo))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(label_TienTe))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(label_ToaDo))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(label_MaQuocGia))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(Label_ChauLuc))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 339, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 38, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jPanel1);

        Combobox_DanhSachQuocGia.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Combobox_DanhSachQuocGia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        Combobox_DanhSachQuocGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combobox_DanhSachQuocGiaActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 153, 255));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search-textfield-24.png"))); // NOI18N
        jButton1.setText("Tìm kiếm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1128, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Combobox_DanhSachQuocGia, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Combobox_DanhSachQuocGia)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel30MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel30MouseClicked
        ReviewFrame reviewFrame = new ReviewFrame(toUpperCaseString(tenQuocGia));
    }//GEN-LAST:event_jLabel30MouseClicked

    private void Combobox_DanhSachQuocGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combobox_DanhSachQuocGiaActionPerformed
        
    }//GEN-LAST:event_Combobox_DanhSachQuocGiaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(!Combobox_DanhSachQuocGia.getSelectedItem().toString().isEmpty()){
            //gửi yêu cầu tới server lấy danh sách quốc gia
            MainFrame._CONNECT_SERVER.senData(MainFrame._SERCURITY_CLIENT.maHoaAES("tracuudialyquocgia#"+Combobox_DanhSachQuocGia.getSelectedItem().toString(), MainFrame._SESSION_KEY));
            //giả mã danh sách quốc gia nhận được từ server
            String nhan = MainFrame._SERCURITY_CLIENT.giaiMaAES(MainFrame._CONNECT_SERVER.receiveData(), MainFrame._SESSION_KEY);
            
            checkFunction(nhan);
        }else{
            JOptionPane.showMessageDialog(null, "Vui lòng chọn quốc gia cần tìm.");
        }

    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Combobox_DanhSachQuocGia;
    private javax.swing.JLabel Label_ChauLuc;
    private javax.swing.JLabel Label_KetQuTimKiem;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel labelBanDo;
    private javax.swing.JLabel labelQuocKy;
    private javax.swing.JLabel label_DanSo;
    private javax.swing.JLabel label_DienTich;
    private javax.swing.JLabel label_MaQuocGia;
    private javax.swing.JTextArea label_NgonNgu;
    private javax.swing.JLabel label_TenQuocGia;
    private javax.swing.JLabel label_ThuDo;
    private javax.swing.JLabel label_TienTe;
    private javax.swing.JLabel label_ToaDo;
    private javax.swing.JTextArea txt_QuocGiaLienKe;
    // End of variables declaration//GEN-END:variables
}

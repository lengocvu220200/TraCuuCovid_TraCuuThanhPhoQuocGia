package laptrinhmang_doan;

import ComponentCustomize.SearchCallBack;
import ComponentCustomize.SearchEvent;
import com.sun.xml.internal.ws.util.StringUtils;
import java.awt.Color;
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TraCuuDiaLyThanhPhoPanel extends JPanel {
    List<String> list = new ArrayList<>();
    private Socket socket = null;
    BufferedWriter out = null;
    BufferedReader in = null;
    BufferedReader stdIn = null;
    private boolean checkViSiblePanel = false;
    
    public TraCuuDiaLyThanhPhoPanel() {
        initComponents();
        btn_Xemthem.setBackground(Color.decode("#3399FF"));
        Panel_XemThem.setVisible(checkViSiblePanel);
        searchComponent1.hintText = "Nhập vào tên thành phố cần tìm ...";
        searchComponent1.addEvent(new SearchEvent() {
            @Override
            public void onPressed(SearchCallBack call) {
                if(!searchComponent1.getText().isEmpty() && !Pattern.matches(".*[(#\"*=<>$%@()'+/)].*", searchComponent1.getText())){
                    ///gửi chuỗi dữ liệu đã được mã hóa AES tới server
                    MainFrame._CONNECT_SERVER.senData(MainFrame._SERCURITY_CLIENT.maHoaAES("tracuudialythanhpho#"+searchComponent1.getText(), MainFrame._SESSION_KEY));
                    
                    //nhận và giải mã dữ liệu nhận được từ server
                    String ketquaNhan = MainFrame._SERCURITY_CLIENT.giaiMaAES(MainFrame._CONNECT_SERVER.receiveData(), MainFrame._SESSION_KEY);
                    call.done();
                    docDuLieu(ketquaNhan);
                    //Ẩn panel thời tiết
                    Panel_XemThem.setVisible(false);
                    checkViSiblePanel = false;
                    jLabel35.setText("Xem thêm");
                    jLabel34.setIcon(new ImageIcon("src/Icons/arrow-24.png"));
                }else{
                    call.done();
                    if(searchComponent1.getText().isEmpty())
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập thành phố cần tìm.");
                    else
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập thành phố cần tìm không chứa các ký tự đặt biệt. (#\"*=<>$%@()'+/)");
                }
            }

            @Override
            public void onCancel() {
                
            }
        });
        searchComponent1.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
//                if(searchComponent1.getText().isEmpty()){
//                    menu.setVisible(false);
//                }else{
//                    menu.show(searchComponent1, 20, searchComponent1.getHeight());
//                    showJList(searchComponent1.getText());
//                }
            }
        });
        ///Lấy vị trí thành phố hiện tại theo IP
        ///gửi chuỗi dữ liệu đã được mã hóa AES tới server
        MainFrame._CONNECT_SERVER.senData(MainFrame._SERCURITY_CLIENT.maHoaAES("thanhphohientai#", MainFrame._SESSION_KEY));
        //nhận và giải mã dữ liệu nhận được từ server
        String ketquaNhan = MainFrame._SERCURITY_CLIENT.giaiMaAES(MainFrame._CONNECT_SERVER.receiveData(), MainFrame._SESSION_KEY);
        docDuLieu(ketquaNhan);
    }
    public void docDuLieu(String string){
        try {
            JSONParser parse = new JSONParser();
            JSONObject data = (JSONObject) parse.parse(string);
            if(data.get("status").toString().equals("success")){
                Label_DiaDiem.setText(data.get("TenThanhPho").toString() + ", "+data.get("KhuVucHanhChinh").toString()+", "+data.get("TenQuocGia").toString());
                Label_NhietDoHienTai.setText(data.get("NhietDoHienTai").toString());
                Label_TinhHinh.setText(data.get("TinhHinh").toString());
                colorChiSoUV(data.get("UV").toString());
                Label_SucGio.setText(data.get("TocDogio").toString());
                LAbel_DoAm.setText(data.get("DoAm").toString());
                Label_Gio1.setText(data.get("Gio1").toString());
                Label_Gio2.setText(data.get("Gio2").toString());
                Label_Gio3.setText(data.get("Gio3").toString());
                Label_Gio4.setText(data.get("Gio4").toString());
                Label_Gio5.setText(data.get("Gio5").toString());
                Label_NhietDoThapNhatHomNay.setText(data.get("NhietDoTheoNgayBanDem1").toString());
                Label_NhietDoNgayMai.setText(data.get("NhietDoTheoNgayBanNgay2").toString());
                Label_NhietDoDemMai.setText(data.get("NhietDoTheoNgayBanNgay3").toString());
                Label_NhietDoGio1.setText(data.get("NhietDoTheoGio1").toString());
                Label_NhietDoGio2.setText(data.get("NhietDoTheoGio2").toString());
                Label_NhietDoGio3.setText(data.get("NhietDoTheoGio3").toString());
                Label_NhietDoGio4.setText(data.get("NhietDoTheoGio4").toString());
                Label_NhietDoGio5.setText(data.get("NhietDoTheoGio5").toString());
                Label_KhaNangCoMuaTheoGio1.setText(data.get("KhaNangCoMuaTheoGio1").toString());
                Label_KhaNangCoMuaTheoGio2.setText(data.get("KhaNangCoMuaTheoGio2").toString());
                Label_KhaNangCoMuaTheoGio3.setText(data.get("KhaNangCoMuaTheoGio3").toString());
                Label_KhaNangCoMuaTheoGio4.setText(data.get("KhaNangCoMuaTheoGio4").toString());
                Label_KhaNangCoMuaTheoGio5.setText(data.get("KhaNangCoMuaTheoGio5").toString());
                Label_Ngay1.setText(data.get("Ngay1").toString());
                Label_Ngay2.setText(data.get("Ngay2").toString());
                Label_Ngay3.setText(data.get("Ngay3").toString());
                Label_Ngay4.setText(data.get("Ngay4").toString());
                Label_Ngay5.setText(data.get("Ngay5").toString());
                Label_KhaNngMuaTheoNgay1.setText(data.get("KhaNangCoMuaTheoNgay1").toString());
                Label_KhaNngMuaTheoNgay2.setText(data.get("KhaNangCoMuaTheoNgay2").toString());
                Label_KhaNngMuaTheoNgay3.setText(data.get("KhaNangCoMuaTheoNgay3").toString());
                Label_KhaNngMuaTheoNgay4.setText(data.get("KhaNangCoMuaTheoNgay4").toString());
                Label_KhaNngMuaTheoNgay5.setText(data.get("KhaNangCoMuaTheoNgay5").toString());
                Label_TenThanhPho.setText(data.get("TenThanhPho").toString());
                Label_DanSo.setText(dinhDangSo(Float.parseFloat(data.get("DanSo").toString())));
                Label_KhuVucHanhChinh.setText(data.get("KhuVucHanhChinh").toString());
                Label_LatLong.setText("(Lat) "+data.get("Latitude").toString()+", (Long) "+data.get("Longitude").toString());
                Label_MuiGio.setText(data.get("MaGioDiaPhuong").toString());
                Label_QuocGia.setText(data.get("TenQuocGia").toString());
                //Label_ChauLuc.setText(data.get("ChauLuc").toString());
                setImageNationalFlag("https://img.geonames.org/flags/x/"+data.get("MaQuocGia").toString().toLowerCase()+".gif",268,174);
                khaNangMua(Label_ThoiTiet1, data.get("KhaNangCoMuaTheoNgay1").toString());
                khaNangMua(Label_ThoiTiet2, data.get("KhaNangCoMuaTheoNgay2").toString());
                khaNangMua(Label_ThoiTiet3, data.get("KhaNangCoMuaTheoNgay3").toString());
                Label_NhietDoNgay1.setText(data.get("NhietDoTheoNgayBanNgay1").toString());
                Label_NhietDoNgay2.setText(data.get("NhietDoTheoNgayBanNgay2").toString());
                Label_NhietDoNgay3.setText(data.get("NhietDoTheoNgayBanNgay3").toString());
                Label_NhietDoNgay4.setText(data.get("NhietDoTheoNgayBanNgay4").toString());
                Label_NhietDoNgay5.setText(data.get("NhietDoTheoNgayBanNgay5").toString());
                Label_NhietDoDem1.setText(data.get("NhietDoTheoNgayBanDem1").toString());
                Label_NhietDoDem2.setText(data.get("NhietDoTheoNgayBanDem2").toString());
                Label_NhietDoDem3.setText(data.get("NhietDoTheoNgayBanDem3").toString());
                Label_NhietDoDem4.setText(data.get("NhietDoTheoNgayBanDem4").toString());
                Label_NhietDoDem5.setText(data.get("NhietDoTheoNgayBanDem5").toString());
                
            }else{
                JOptionPane.showMessageDialog(null, "Không tìm thấy kết quả!");
            }
        } catch (ParseException | NullPointerException ex) {
            
        }
    }
    public String dinhDangSo(Float str){
        // tạo 1 NumberFormat để định dạng số theo tiêu chuẩn của nước Anh
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);

        // đối với số có kiểu long được định dạng theo chuẩn của nước Anh
        // thì phần ngàn của số được phân cách bằng dấu phẩy
        float longNumber = str;
        String str1 = en.format(longNumber);
        return str1;
    }
    private void setImageNationalFlag(String urlImage, int width, int hieght){
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
            labelQuocKy.setIcon(imageIcon);
        } catch (MalformedURLException ex) {
            Logger.getLogger(TraCuuDiaLyThanhPhoPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TraCuuDiaLyThanhPhoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void colorChiSoUV(String string){
        StringTokenizer s = new StringTokenizer(string, "/");
        float chiSo = Float.parseFloat(s.nextToken());
        if(chiSo <= 2.9){
            Label_UV.setText(string + " (Thấp)");
            Label_UV.setForeground(Color.decode("#00DD00"));
        }else{
            if(chiSo <=5.9){
                Label_UV.setText(string + " (Trung bình)");
                Label_UV.setForeground(Color.decode("#CCFF33"));
            }
            if(chiSo >= 6.0 && chiSo <=7.9){
                Label_UV.setText(string + " (Cao)");
                Label_UV.setForeground(Color.decode("#FF9900"));
            }
            if(chiSo >= 8.0){
                Label_UV.setText(string + " (Rất cao)");
                Label_UV.setForeground(Color.decode("#FF3333"));
            }
        }
    }
    private void khaNangMua(JLabel label, String string){
        String[] chuoi = string.split("%");
        float num = Float.parseFloat(chuoi[0]);
        if(num<=20.0){
            label.setText("Khả năng có mưa thấp");
        }else{
            if(num > 75.0){
                label.setText("Khả năng có mưa cao");
            }else{
                label.setText("Khả năng có mưa");
            }
        }
    }
    public void showJList(String searchInput){
        DefaultListModel jListModel = new DefaultListModel();
        for(String str : list){
            if(str.toLowerCase().contains(searchInput.toLowerCase())){
                jListModel.addElement(str);
            }
        }
        jList1.setModel(jListModel);
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

        listPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        menu = new javax.swing.JPopupMenu();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel12 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        weatherPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Label_TinhHinh = new javax.swing.JLabel();
        Label_NhietDoHienTai = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        Label_UV = new javax.swing.JLabel();
        Label_SucGio = new javax.swing.JLabel();
        LAbel_DoAm = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btn_Xemthem = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        Label_DiaDiem = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        Label_NhietDoThapNhatHomNay = new javax.swing.JLabel();
        Label_ThoiTiet1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        Label_NhietDoNgayMai = new javax.swing.JLabel();
        Label_ThoiTiet2 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        Label_NhietDoDemMai = new javax.swing.JLabel();
        Label_ThoiTiet3 = new javax.swing.JLabel();
        Panel_XemThem = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        Label_Gio1 = new javax.swing.JLabel();
        Label_NhietDoGio1 = new javax.swing.JLabel();
        Label_KhaNangCoMuaTheoGio1 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        Label_Gio2 = new javax.swing.JLabel();
        Label_NhietDoGio2 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        Label_KhaNangCoMuaTheoGio2 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        Label_Gio3 = new javax.swing.JLabel();
        Label_NhietDoGio3 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        Label_KhaNangCoMuaTheoGio3 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        Label_Gio4 = new javax.swing.JLabel();
        Label_NhietDoGio4 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        Label_KhaNangCoMuaTheoGio4 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        Label_Gio5 = new javax.swing.JLabel();
        Label_NhietDoGio5 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        Label_KhaNangCoMuaTheoGio5 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        Label_Ngay1 = new javax.swing.JLabel();
        Label_NhietDoNgay1 = new javax.swing.JLabel();
        Label_KhaNngMuaTheoNgay1 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        Label_NhietDoDem1 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        Label_Ngay2 = new javax.swing.JLabel();
        Label_NhietDoNgay2 = new javax.swing.JLabel();
        Label_KhaNngMuaTheoNgay2 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        Label_NhietDoDem2 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        Label_Ngay5 = new javax.swing.JLabel();
        Label_NhietDoNgay5 = new javax.swing.JLabel();
        Label_KhaNngMuaTheoNgay5 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        Label_NhietDoDem5 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        Label_Ngay4 = new javax.swing.JLabel();
        Label_NhietDoNgay4 = new javax.swing.JLabel();
        Label_KhaNngMuaTheoNgay4 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        Label_NhietDoDem4 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        Label_Ngay3 = new javax.swing.JLabel();
        Label_NhietDoNgay3 = new javax.swing.JLabel();
        Label_KhaNngMuaTheoNgay3 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        Label_NhietDoDem3 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        Label_TenThanhPho = new javax.swing.JLabel();
        Label_DanSo = new javax.swing.JLabel();
        Label_LatLong = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        Label_MuiGio = new javax.swing.JLabel();
        Label_KhuVucHanhChinh = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        Label_XemThem = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        labelQuocKy = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        Label_QuocGia = new javax.swing.JLabel();
        searchComponent1 = new laptrinhmang_doan.SearchComponent();

        listPanel.setPreferredSize(new java.awt.Dimension(1067, 187));

        jList1.setFont(new java.awt.Font("Times New Roman", 3, 16)); // NOI18N
        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "a", "b", "c" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jList1MousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout listPanelLayout = new javax.swing.GroupLayout(listPanel);
        listPanel.setLayout(listPanelLayout);
        listPanelLayout.setHorizontalGroup(
            listPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1067, Short.MAX_VALUE)
        );
        listPanelLayout.setVerticalGroup(
            listPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
        );

        menu.setFocusable(false);

        setBackground(new java.awt.Color(204, 204, 204));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        weatherPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Thời Tiết hiện tại");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rain-cloud-80.png"))); // NOI18N

        Label_TinhHinh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_TinhHinh.setText("Mây và nắng");

        Label_NhietDoHienTai.setFont(new java.awt.Font("Verdana", 1, 72)); // NOI18N
        Label_NhietDoHienTai.setText("24°C");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel5.setText("Chỉ số UV");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel6.setText("Sức gió");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel7.setText("Độ ẩm");

        Label_UV.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        Label_UV.setForeground(new java.awt.Color(255, 0, 0));
        Label_UV.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Label_UV.setText("Có hại (120 AQI)");

        Label_SucGio.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        Label_SucGio.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Label_SucGio.setText("7 km/h");

        LAbel_DoAm.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        LAbel_DoAm.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        LAbel_DoAm.setText("9 km/h");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        btn_Xemthem.setBackground(new java.awt.Color(0, 102, 255));
        btn_Xemthem.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_Xemthem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_XemthemMouseClicked(evt);
            }
        });

        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/arrow-24.png"))); // NOI18N

        jLabel35.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Xem thêm");

        javax.swing.GroupLayout btn_XemthemLayout = new javax.swing.GroupLayout(btn_Xemthem);
        btn_Xemthem.setLayout(btn_XemthemLayout);
        btn_XemthemLayout.setHorizontalGroup(
            btn_XemthemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_XemthemLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel34)
                .addContainerGap())
        );
        btn_XemthemLayout.setVerticalGroup(
            btn_XemthemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_Xemthem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(LAbel_DoAm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(0, 140, Short.MAX_VALUE)
                                        .addComponent(Label_SucGio, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(Label_UV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGap(28, 28, 28))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(Label_UV))
                .addGap(13, 13, 13)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(Label_SucGio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(LAbel_DoAm))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_Xemthem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Label_DiaDiem.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_DiaDiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/location-pin-24.png"))); // NOI18N
        Label_DiaDiem.setText("HỒ CHÍ MINH, VIỆT NAM");

        javax.swing.GroupLayout weatherPanelLayout = new javax.swing.GroupLayout(weatherPanel);
        weatherPanel.setLayout(weatherPanelLayout);
        weatherPanelLayout.setHorizontalGroup(
            weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weatherPanelLayout.createSequentialGroup()
                .addGroup(weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(weatherPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Label_DiaDiem))
                    .addGroup(weatherPanelLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, weatherPanelLayout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addGap(23, 23, 23)))
                            .addComponent(Label_TinhHinh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Label_NhietDoHienTai, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        weatherPanelLayout.setVerticalGroup(
            weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weatherPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label_DiaDiem)
                .addGap(1, 1, 1)
                .addGroup(weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(weatherPanelLayout.createSequentialGroup()
                        .addGroup(weatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, weatherPanelLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Label_TinhHinh))
                            .addComponent(Label_NhietDoHienTai, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Nhiệt độ thấp nhất hôm nay"));
        jPanel6.setPreferredSize(new java.awt.Dimension(122, 47));

        Label_NhietDoThapNhatHomNay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Label_NhietDoThapNhatHomNay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoThapNhatHomNay.setText("24°C");

        Label_ThoiTiet1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_ThoiTiet1.setText("Mây và nắng");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(Label_NhietDoThapNhatHomNay, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_ThoiTiet1, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Label_NhietDoThapNhatHomNay, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
            .addComponent(Label_ThoiTiet1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Ngày mai"));
        jPanel7.setPreferredSize(new java.awt.Dimension(122, 47));

        Label_NhietDoNgayMai.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Label_NhietDoNgayMai.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoNgayMai.setText("20°C");

        Label_ThoiTiet2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_ThoiTiet2.setText("Mưa rào");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(Label_NhietDoNgayMai, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_ThoiTiet2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Label_NhietDoNgayMai, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
            .addComponent(Label_ThoiTiet2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Ngày mốt"));
        jPanel8.setPreferredSize(new java.awt.Dimension(122, 47));

        Label_NhietDoDemMai.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Label_NhietDoDemMai.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoDemMai.setText("21°C");

        Label_ThoiTiet3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_ThoiTiet3.setText("Mưa rào");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(Label_NhietDoDemMai, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_ThoiTiet3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Label_NhietDoDemMai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Label_ThoiTiet3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(weatherPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(weatherPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );

        Panel_XemThem.setBackground(new java.awt.Color(204, 204, 204));

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 0, 0));
        jLabel36.setText("Dự báo từng giờ");

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        Label_Gio1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_Gio1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_Gio1.setText("Bây giờ");

        Label_NhietDoGio1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Label_NhietDoGio1.setForeground(new java.awt.Color(0, 102, 204));
        Label_NhietDoGio1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoGio1.setText("24°C");

        Label_KhaNangCoMuaTheoGio1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_KhaNangCoMuaTheoGio1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_KhaNangCoMuaTheoGio1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rainy-weather-16.png"))); // NOI18N
        Label_KhaNangCoMuaTheoGio1.setText("12%");
        Label_KhaNangCoMuaTheoGio1.setToolTipText("Khả năng có mưa");

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rain-cloud-80.png"))); // NOI18N

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Label_Gio1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Label_NhietDoGio1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Label_KhaNangCoMuaTheoGio1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(Label_Gio1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_NhietDoGio1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_KhaNangCoMuaTheoGio1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));

        Label_Gio2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_Gio2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_Gio2.setText("Bây giờ");

        Label_NhietDoGio2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        Label_NhietDoGio2.setForeground(new java.awt.Color(0, 102, 204));
        Label_NhietDoGio2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoGio2.setText("24°C");

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rain-cloud-80.png"))); // NOI18N

        Label_KhaNangCoMuaTheoGio2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_KhaNangCoMuaTheoGio2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_KhaNangCoMuaTheoGio2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rainy-weather-16.png"))); // NOI18N
        Label_KhaNangCoMuaTheoGio2.setText("12%");
        Label_KhaNangCoMuaTheoGio2.setToolTipText("Khả năng có mưa");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Label_Gio2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Label_NhietDoGio2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
            .addComponent(Label_KhaNangCoMuaTheoGio2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addComponent(Label_Gio2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_NhietDoGio2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_KhaNangCoMuaTheoGio2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));

        Label_Gio3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_Gio3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_Gio3.setText("Bây giờ");

        Label_NhietDoGio3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        Label_NhietDoGio3.setForeground(new java.awt.Color(0, 102, 204));
        Label_NhietDoGio3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoGio3.setText("24°C");

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rain-cloud-80.png"))); // NOI18N

        Label_KhaNangCoMuaTheoGio3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_KhaNangCoMuaTheoGio3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_KhaNangCoMuaTheoGio3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rainy-weather-16.png"))); // NOI18N
        Label_KhaNangCoMuaTheoGio3.setText("12%");
        Label_KhaNangCoMuaTheoGio3.setToolTipText("Khả năng có mưa");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Label_Gio3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Label_NhietDoGio3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
            .addComponent(Label_KhaNangCoMuaTheoGio3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(Label_Gio3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_NhietDoGio3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel47)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Label_KhaNangCoMuaTheoGio3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));

        Label_Gio4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_Gio4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_Gio4.setText("Bây giờ");

        Label_NhietDoGio4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        Label_NhietDoGio4.setForeground(new java.awt.Color(0, 102, 204));
        Label_NhietDoGio4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoGio4.setText("24°C");

        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rain-cloud-80.png"))); // NOI18N

        Label_KhaNangCoMuaTheoGio4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_KhaNangCoMuaTheoGio4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_KhaNangCoMuaTheoGio4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rainy-weather-16.png"))); // NOI18N
        Label_KhaNangCoMuaTheoGio4.setText("12%");
        Label_KhaNangCoMuaTheoGio4.setToolTipText("Khả năng có mưa");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Label_Gio4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Label_NhietDoGio4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
            .addComponent(Label_KhaNangCoMuaTheoGio4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(Label_Gio4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_NhietDoGio4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Label_KhaNangCoMuaTheoGio4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));

        Label_Gio5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_Gio5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_Gio5.setText("Bây giờ");

        Label_NhietDoGio5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        Label_NhietDoGio5.setForeground(new java.awt.Color(0, 102, 204));
        Label_NhietDoGio5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoGio5.setText("24°C");

        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel55.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rain-cloud-80.png"))); // NOI18N

        Label_KhaNangCoMuaTheoGio5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_KhaNangCoMuaTheoGio5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_KhaNangCoMuaTheoGio5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rainy-weather-16.png"))); // NOI18N
        Label_KhaNangCoMuaTheoGio5.setText("12%");
        Label_KhaNangCoMuaTheoGio5.setToolTipText("Khả năng có mưa");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Label_Gio5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Label_NhietDoGio5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
            .addComponent(Label_KhaNangCoMuaTheoGio5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(Label_Gio5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_NhietDoGio5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Label_KhaNangCoMuaTheoGio5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel36)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 0, 0));
        jLabel57.setText("Dự báo hàng ngày");

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));

        Label_Ngay1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_Ngay1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_Ngay1.setText("Bây giờ");

        Label_NhietDoNgay1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Label_NhietDoNgay1.setForeground(new java.awt.Color(0, 102, 204));
        Label_NhietDoNgay1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoNgay1.setText("24°C");

        Label_KhaNngMuaTheoNgay1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_KhaNngMuaTheoNgay1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_KhaNngMuaTheoNgay1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rainy-weather-16.png"))); // NOI18N
        Label_KhaNngMuaTheoNgay1.setText("12%");
        Label_KhaNngMuaTheoNgay1.setToolTipText("Khả năng có mưa");

        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rain-cloud-80.png"))); // NOI18N

        Label_NhietDoDem1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_NhietDoDem1.setForeground(new java.awt.Color(0, 102, 204));
        Label_NhietDoDem1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoDem1.setText("4°C");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Label_Ngay1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Label_NhietDoNgay1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Label_KhaNngMuaTheoNgay1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel61, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
            .addComponent(Label_NhietDoDem1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addComponent(Label_Ngay1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_NhietDoNgay1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Label_NhietDoDem1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Label_KhaNngMuaTheoNgay1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));

        Label_Ngay2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_Ngay2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_Ngay2.setText("Bây giờ");

        Label_NhietDoNgay2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        Label_NhietDoNgay2.setForeground(new java.awt.Color(0, 102, 204));
        Label_NhietDoNgay2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoNgay2.setText("24°C");

        Label_KhaNngMuaTheoNgay2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_KhaNngMuaTheoNgay2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_KhaNngMuaTheoNgay2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rainy-weather-16.png"))); // NOI18N
        Label_KhaNngMuaTheoNgay2.setText("12%");
        Label_KhaNngMuaTheoNgay2.setToolTipText("Khả năng có mưa");

        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel71.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rain-cloud-80.png"))); // NOI18N

        Label_NhietDoDem2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_NhietDoDem2.setForeground(new java.awt.Color(0, 102, 204));
        Label_NhietDoDem2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoDem2.setText("4°C");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Label_Ngay2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Label_NhietDoNgay2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Label_KhaNngMuaTheoNgay2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel71, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
            .addComponent(Label_NhietDoDem2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(Label_Ngay2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_NhietDoNgay2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Label_NhietDoDem2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Label_KhaNngMuaTheoNgay2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));

        Label_Ngay5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_Ngay5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_Ngay5.setText("Bây giờ");

        Label_NhietDoNgay5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        Label_NhietDoNgay5.setForeground(new java.awt.Color(0, 102, 204));
        Label_NhietDoNgay5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoNgay5.setText("24°C");

        Label_KhaNngMuaTheoNgay5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_KhaNngMuaTheoNgay5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_KhaNngMuaTheoNgay5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rainy-weather-16.png"))); // NOI18N
        Label_KhaNngMuaTheoNgay5.setText("12%");
        Label_KhaNngMuaTheoNgay5.setToolTipText("Khả năng có mưa");

        jLabel76.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel76.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rain-cloud-80.png"))); // NOI18N

        Label_NhietDoDem5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_NhietDoDem5.setForeground(new java.awt.Color(0, 102, 204));
        Label_NhietDoDem5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoDem5.setText("4°C");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Label_Ngay5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Label_NhietDoNgay5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Label_KhaNngMuaTheoNgay5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel76, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
            .addComponent(Label_NhietDoDem5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addComponent(Label_Ngay5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_NhietDoNgay5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Label_NhietDoDem5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Label_KhaNngMuaTheoNgay5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));

        Label_Ngay4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_Ngay4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_Ngay4.setText("Bây giờ");

        Label_NhietDoNgay4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        Label_NhietDoNgay4.setForeground(new java.awt.Color(0, 102, 204));
        Label_NhietDoNgay4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoNgay4.setText("24°C");

        Label_KhaNngMuaTheoNgay4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_KhaNngMuaTheoNgay4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_KhaNngMuaTheoNgay4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rainy-weather-16.png"))); // NOI18N
        Label_KhaNngMuaTheoNgay4.setText("12%");
        Label_KhaNngMuaTheoNgay4.setToolTipText("Khả năng có mưa");

        jLabel86.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel86.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rain-cloud-80.png"))); // NOI18N

        Label_NhietDoDem4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_NhietDoDem4.setForeground(new java.awt.Color(0, 102, 204));
        Label_NhietDoDem4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoDem4.setText("4°C");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Label_Ngay4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Label_NhietDoNgay4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Label_KhaNngMuaTheoNgay4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel86, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
            .addComponent(Label_NhietDoDem4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(Label_Ngay4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_NhietDoNgay4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Label_NhietDoDem4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Label_KhaNngMuaTheoNgay4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));

        Label_Ngay3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_Ngay3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_Ngay3.setText("Bây giờ");

        Label_NhietDoNgay3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        Label_NhietDoNgay3.setForeground(new java.awt.Color(0, 102, 204));
        Label_NhietDoNgay3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoNgay3.setText("24°C");

        Label_KhaNngMuaTheoNgay3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Label_KhaNngMuaTheoNgay3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_KhaNngMuaTheoNgay3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rainy-weather-16.png"))); // NOI18N
        Label_KhaNngMuaTheoNgay3.setText("12%");
        Label_KhaNngMuaTheoNgay3.setToolTipText("Khả năng có mưa");

        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel91.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rain-cloud-80.png"))); // NOI18N

        Label_NhietDoDem3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Label_NhietDoDem3.setForeground(new java.awt.Color(0, 102, 204));
        Label_NhietDoDem3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_NhietDoDem3.setText("4°C");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Label_Ngay3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Label_NhietDoNgay3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Label_KhaNngMuaTheoNgay3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel91, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
            .addComponent(Label_NhietDoDem3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(Label_Ngay3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Label_NhietDoNgay3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Label_NhietDoDem3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Label_KhaNngMuaTheoNgay3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel57))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel57)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout Panel_XemThemLayout = new javax.swing.GroupLayout(Panel_XemThem);
        Panel_XemThem.setLayout(Panel_XemThemLayout);
        Panel_XemThemLayout.setHorizontalGroup(
            Panel_XemThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        Panel_XemThemLayout.setVerticalGroup(
            Panel_XemThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_XemThemLayout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel18.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel18.setText("Thông tin chung");

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel19.setText("Tên thành phố/địa danh:");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel20.setText("Khu vực hành chính:");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel21.setText("Tọa độ:");

        Label_TenThanhPho.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        Label_TenThanhPho.setForeground(new java.awt.Color(0, 102, 204));
        Label_TenThanhPho.setText("Hồ Chí Minh");

        Label_DanSo.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        Label_DanSo.setForeground(new java.awt.Color(255, 102, 0));
        Label_DanSo.setText("102,029382932");

        Label_LatLong.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        Label_LatLong.setText("102,029382932");

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel22.setText("Múi giờ:");

        jPanel10.setPreferredSize(new java.awt.Dimension(5, 0));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel26.setText("Dân số:");

        Label_MuiGio.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        Label_MuiGio.setText("102,029382932");

        Label_KhuVucHanhChinh.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        Label_KhuVucHanhChinh.setText("102,029382932");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel23.setText("Bài viết liên quan:");

        Label_XemThem.setFont(new java.awt.Font("Tahoma", 2, 16)); // NOI18N
        Label_XemThem.setForeground(new java.awt.Color(0, 0, 204));
        Label_XemThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/share-3-24.png"))); // NOI18N
        Label_XemThem.setText("Xem thêm");
        Label_XemThem.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Label_XemThem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Label_XemThemMouseClicked(evt);
            }
        });

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(null);
        jPanel13.add(labelQuocKy);
        labelQuocKy.setBounds(79, 80, 250, 150);

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Quốc gia trực thuộc");
        jPanel13.add(jLabel30);
        jLabel30.setBounds(0, 0, 397, 20);

        Label_QuocGia.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        Label_QuocGia.setForeground(new java.awt.Color(255, 51, 51));
        Label_QuocGia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_QuocGia.setText("Viet Nam");
        jPanel13.add(Label_QuocGia);
        Label_QuocGia.setBounds(0, 33, 397, 20);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(jLabel19)
                    .addComponent(jLabel26)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Label_XemThem)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(Label_TenThanhPho, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Label_KhuVucHanhChinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Label_DanSo, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                        .addComponent(Label_LatLong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Label_MuiGio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel19)
                                    .addComponent(Label_TenThanhPho))
                                .addGap(37, 37, 37)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel20)
                                    .addComponent(Label_KhuVucHanhChinh))
                                .addGap(38, 38, 38)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel26)
                                    .addComponent(Label_DanSo))
                                .addGap(39, 39, 39)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel21)
                                    .addComponent(Label_LatLong))
                                .addGap(43, 43, 43)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel22)
                                    .addComponent(Label_MuiGio))
                                .addGap(44, 44, 44)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel23)
                                    .addComponent(Label_XemThem))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Panel_XemThem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Panel_XemThem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel12);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchComponent1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchComponent1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jList1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MousePressed
        if (jList1.getSelectedIndex() != -1) {
            //searchComponent1.setText(jList1.getSelectedValue());
            menu.setVisible(false);
        }
    }//GEN-LAST:event_jList1MousePressed

    private void btn_XemthemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_XemthemMouseClicked
        // xêm thêm thời tiết
        if(checkViSiblePanel){
            Panel_XemThem.setVisible(false);
            checkViSiblePanel = false;
            jLabel35.setText("Xem thêm");
            jLabel34.setIcon(new ImageIcon("src/Icons/arrow-24.png"));
        }else{
            Panel_XemThem.setVisible(true);
            checkViSiblePanel = true;
            jLabel35.setText("Thu gọn");
            jLabel34.setIcon(new ImageIcon("src/Icons/arrow-pointing-left-24.png"));
        }
    }//GEN-LAST:event_btn_XemthemMouseClicked

    private void Label_XemThemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Label_XemThemMouseClicked
        ReviewFrame reviewFrame = new ReviewFrame(toUpperCaseString(Label_TenThanhPho.getText()));
    }//GEN-LAST:event_Label_XemThemMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LAbel_DoAm;
    private javax.swing.JLabel Label_DanSo;
    private javax.swing.JLabel Label_DiaDiem;
    private javax.swing.JLabel Label_Gio1;
    private javax.swing.JLabel Label_Gio2;
    private javax.swing.JLabel Label_Gio3;
    private javax.swing.JLabel Label_Gio4;
    private javax.swing.JLabel Label_Gio5;
    private javax.swing.JLabel Label_KhaNangCoMuaTheoGio1;
    private javax.swing.JLabel Label_KhaNangCoMuaTheoGio2;
    private javax.swing.JLabel Label_KhaNangCoMuaTheoGio3;
    private javax.swing.JLabel Label_KhaNangCoMuaTheoGio4;
    private javax.swing.JLabel Label_KhaNangCoMuaTheoGio5;
    private javax.swing.JLabel Label_KhaNngMuaTheoNgay1;
    private javax.swing.JLabel Label_KhaNngMuaTheoNgay2;
    private javax.swing.JLabel Label_KhaNngMuaTheoNgay3;
    private javax.swing.JLabel Label_KhaNngMuaTheoNgay4;
    private javax.swing.JLabel Label_KhaNngMuaTheoNgay5;
    private javax.swing.JLabel Label_KhuVucHanhChinh;
    private javax.swing.JLabel Label_LatLong;
    private javax.swing.JLabel Label_MuiGio;
    private javax.swing.JLabel Label_Ngay1;
    private javax.swing.JLabel Label_Ngay2;
    private javax.swing.JLabel Label_Ngay3;
    private javax.swing.JLabel Label_Ngay4;
    private javax.swing.JLabel Label_Ngay5;
    private javax.swing.JLabel Label_NhietDoDem1;
    private javax.swing.JLabel Label_NhietDoDem2;
    private javax.swing.JLabel Label_NhietDoDem3;
    private javax.swing.JLabel Label_NhietDoDem4;
    private javax.swing.JLabel Label_NhietDoDem5;
    private javax.swing.JLabel Label_NhietDoDemMai;
    private javax.swing.JLabel Label_NhietDoGio1;
    private javax.swing.JLabel Label_NhietDoGio2;
    private javax.swing.JLabel Label_NhietDoGio3;
    private javax.swing.JLabel Label_NhietDoGio4;
    private javax.swing.JLabel Label_NhietDoGio5;
    private javax.swing.JLabel Label_NhietDoHienTai;
    private javax.swing.JLabel Label_NhietDoNgay1;
    private javax.swing.JLabel Label_NhietDoNgay2;
    private javax.swing.JLabel Label_NhietDoNgay3;
    private javax.swing.JLabel Label_NhietDoNgay4;
    private javax.swing.JLabel Label_NhietDoNgay5;
    private javax.swing.JLabel Label_NhietDoNgayMai;
    private javax.swing.JLabel Label_NhietDoThapNhatHomNay;
    private javax.swing.JLabel Label_QuocGia;
    private javax.swing.JLabel Label_SucGio;
    private javax.swing.JLabel Label_TenThanhPho;
    private javax.swing.JLabel Label_ThoiTiet1;
    private javax.swing.JLabel Label_ThoiTiet2;
    private javax.swing.JLabel Label_ThoiTiet3;
    private javax.swing.JLabel Label_TinhHinh;
    private javax.swing.JLabel Label_UV;
    private javax.swing.JLabel Label_XemThem;
    private javax.swing.JPanel Panel_XemThem;
    private javax.swing.JPanel btn_Xemthem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelQuocKy;
    private javax.swing.JPanel listPanel;
    private javax.swing.JPopupMenu menu;
    private laptrinhmang_doan.SearchComponent searchComponent1;
    private javax.swing.JPanel weatherPanel;
    // End of variables declaration//GEN-END:variables
}

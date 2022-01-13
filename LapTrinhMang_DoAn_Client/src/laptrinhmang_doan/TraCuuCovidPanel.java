package laptrinhmang_doan;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TraCuuCovidPanel extends javax.swing.JPanel {
    DefaultComboBoxModel comboBoxModel ;
    public TraCuuCovidPanel() {
        initComponents();
        cardPanel_CaNhiem.imageIcon = new ImageIcon("src/Icons/health-book-30.png");
        cardPanel_KhoiBenh.imageIcon = new ImageIcon("src/Icons/verified-account-30.png");
        cardPanel_TuVong.imageIcon = new ImageIcon("src/Icons/heart-30.png");
        cardPanel_CaNhiem.setHomNayText("");
        cardPanel_KhoiBenh.setHomNayText("");
        cardPanel_TuVong.setHomNayText("");
        //gửi yêu cầu tới server lấy danh sách quốc gia
        MainFrame._CONNECT_SERVER.senData(MainFrame._SERCURITY_CLIENT.maHoaAES("danhsachquocgia#", MainFrame._SESSION_KEY));
        //giả mã danh sách quốc gia nhận được từ server
        checkFunction(MainFrame._SERCURITY_CLIENT.giaiMaAES(MainFrame._CONNECT_SERVER.receiveData(), MainFrame._SESSION_KEY));
        
        //gửi yêu cầu tới server lấy kết quả tìm kiếm theo ngày
        LocalDateTime now = LocalDateTime.now();  
        LocalDate date = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
        //trừ đi 6 ngày
        LocalDate ngayBatDau = date.minusYears(0).minusMonths(0).minusWeeks(0).minusDays(7);
        LocalDate denNgay = date.minusYears(0).minusMonths(0).minusWeeks(0).minusDays(1);
        String tenChucNang = "tracuucovid#Vietnam#"+ngayBatDau.toString()+"#"+denNgay;
        
        MainFrame._CONNECT_SERVER.senData(MainFrame._SERCURITY_CLIENT.maHoaAES(tenChucNang, MainFrame._SESSION_KEY));

        //giả mã kết quả tìm kiếm theo ngày nhận được từ server
        checkFunction(MainFrame._SERCURITY_CLIENT.giaiMaAES(MainFrame._CONNECT_SERVER.receiveData(), MainFrame._SESSION_KEY));
        Label_QuocGia.setText("Vietnam (7 ngày gần đây)");
        //thư viện tìm kiếm trên combobox
        AutoCompleteDecorator.decorate(Combobox_QuocGia);
        
    }
    private void checkFunction(String string){
        try {
            JSONParser parse = new JSONParser();
            JSONObject data = (JSONObject) parse.parse(string);
            switch(data.get("function").toString()){
                case "danhsachquocgia":
                    danhSachQuocGia(data);
                    break;
                case "tracuucovid":
                    traCuuCovid(data);
                    break;
                
            }
        } catch (ParseException ex) {
            ex.getMessage();
        }
        
    }
    public void danhSachQuocGia(JSONObject json){
        //traCuuQuocGia(json);
        JSONArray arr = (JSONArray) json.get("geonames");
        comboBoxModel = new DefaultComboBoxModel();
        Combobox_QuocGia.removeAllItems();
        comboBoxModel.addElement("");
        for(int i=0;i<arr.size();i++){
            JSONObject getObjectArray = (JSONObject) arr.get(i);
            comboBoxModel.addElement(getObjectArray.get("countryName").toString());
        }
        Combobox_QuocGia.setModel(comboBoxModel);
    }
    public void traCuuCovid(JSONObject object){
        if(object.get("status").equals("success")){
            //set data card
            cardPanel_CaNhiem.setSubText(dinhDangSo(Float.parseFloat(object.get("TongSoCaNhiem").toString())));
            cardPanel_KhoiBenh.setSubText(dinhDangSo(Float.parseFloat(object.get("TongSoCaKhoiBenh").toString())));
            cardPanel_TuVong.setSubText(dinhDangSo(Float.parseFloat(object.get("TongSoCaTuVong").toString())));

            //set data chart
            JSONArray array = (JSONArray) object.get("SoLieu");
            showMultiLineChart(array);
        }else{
            JOptionPane.showMessageDialog(null, "Không tìm thấy dữ liệu!");
        }
    }
    public void showMultiLineChart(JSONArray array){
        String series1 = "Ca nhiễm";  
        String series2 = "Khỏi bệnh";  
        String series3 = "Tử vong";

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
        for(int i = 0;i<array.size();i++){
            JSONObject ob = (JSONObject) array.get(i);
            dataset.addValue(Integer.parseInt(ob.get("Confirmed").toString()), series1, ob.get("Date").toString());  
            dataset.addValue(Integer.parseInt(ob.get("Recovered").toString()), series2, ob.get("Date").toString());  
            dataset.addValue(Integer.parseInt(ob.get("Deaths").toString()), series3, ob.get("Date").toString());  
        }

        JFreeChart chart = ChartFactory.createLineChart(  
            "Biểu đồ thống kê Covid-19", // Chart title  
            "Thời gian", // X-Axis Label  
            "Số ca", // Y-Axis Label  
            dataset  
            );  
        //ChartPanel panel = new ChartPanel(chart);  
        CategoryPlot linecatagoryPlot = chart.getCategoryPlot();
        linecatagoryPlot.setBackgroundPaint(Color.WHITE);
        linecatagoryPlot.setRangeGridlinePaint(Color.BLACK);
        //create render object to change the moficy the line properties like color
        LineAndShapeRenderer lineRender = (LineAndShapeRenderer) linecatagoryPlot.getRenderer();
        Color lineChartColor1 = new Color(255,204,0);//
        Color lineChartColor2 = new Color(51,255,0);
        Color lineChartColor3 = new Color(255,51,51);
        lineRender.setSeriesPaint(0, lineChartColor1);
        lineRender.setSeriesStroke(0, new BasicStroke(3.0f));
        lineRender.setSeriesPaint(1, lineChartColor2);
        lineRender.setSeriesStroke(1, new BasicStroke(3.0f));
        lineRender.setSeriesPaint(2, lineChartColor3);
        lineRender.setSeriesStroke(2, new BasicStroke(3.0f));
        //
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        Panel_LineChart.removeAll();
        Panel_LineChart.add(chartPanel, BorderLayout.CENTER);
        Panel_LineChart.repaint();
        Panel_LineChart.validate();
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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Combobox_QuocGia = new javax.swing.JComboBox<>();
        Date_TuNgay = new com.toedter.calendar.JDateChooser();
        Date_DenNgay = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        Label_QuocGia = new javax.swing.JLabel();
        cardPanel_CaNhiem = new laptrinhmang_doan.CardPanel();
        cardPanel_KhoiBenh = new laptrinhmang_doan.CardPanel();
        cardPanel_TuVong = new laptrinhmang_doan.CardPanel();
        jPanel3 = new javax.swing.JPanel();
        Panel_LineChart = new javax.swing.JPanel();

        setBackground(new java.awt.Color(204, 204, 204));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        Combobox_QuocGia.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Combobox_QuocGia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        Combobox_QuocGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combobox_QuocGiaActionPerformed(evt);
            }
        });

        Date_TuNgay.setDateFormatString("dd-MM-yyyy");
        Date_TuNgay.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N

        Date_DenNgay.setDateFormatString("dd-MM-yyyy");
        Date_DenNgay.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N

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

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Quốc gia:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Từ ngày:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Đến ngày:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("TRA CỨU COVID-19 ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Combobox_QuocGia, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(61, 61, 61)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Date_TuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Date_DenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(jButton1)))
                .addContainerGap(90, Short.MAX_VALUE))
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Date_TuNgay, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Combobox_QuocGia, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Date_DenNgay, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(21, 21, 21))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(null);

        Label_QuocGia.setBackground(new java.awt.Color(0, 102, 255));
        Label_QuocGia.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Label_QuocGia.setForeground(new java.awt.Color(0, 102, 255));
        Label_QuocGia.setText("Việt Nam");
        jPanel4.add(Label_QuocGia);
        Label_QuocGia.setBounds(67, 26, 1030, 22);

        cardPanel_CaNhiem.setColor1(new java.awt.Color(255, 204, 0));
        cardPanel_CaNhiem.setTitleText("Ca nhiễm");
        jPanel4.add(cardPanel_CaNhiem);
        cardPanel_CaNhiem.setBounds(70, 60, 295, 161);

        cardPanel_KhoiBenh.setColor1(new java.awt.Color(51, 255, 0));
        cardPanel_KhoiBenh.setColor2(new java.awt.Color(0, 204, 0));
        jPanel4.add(cardPanel_KhoiBenh);
        cardPanel_KhoiBenh.setBounds(440, 60, 295, 161);

        cardPanel_TuVong.setColor1(new java.awt.Color(255, 51, 51));
        cardPanel_TuVong.setColor2(new java.awt.Color(255, 102, 102));
        cardPanel_TuVong.setTitleText("Tử vong");
        jPanel4.add(cardPanel_TuVong);
        cardPanel_TuVong.setBounds(800, 60, 295, 161);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        Panel_LineChart.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel_LineChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel_LineChart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(Combobox_QuocGia.getSelectedIndex()>0){
            try{
                Date getdate1 = Date_TuNgay.getDate();
                Date getdate2 = Date_DenNgay.getDate();
                long d1 = getdate1.getTime();
                long d2 = getdate2.getTime();
                java.sql.Date tuNgay = new java.sql.Date(d1);
                java.sql.Date denNgay = new java.sql.Date(d2);
                Date today = new Date();
                //System.err.println(tuNgay.toString()+", "+denNgay.toString());
                if(getdate2.after(today)){
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập ngày kết thúc nhỏ hơn hoặc bằng hôm nay!");
                }else{
                    if(getdate1.after(getdate2) || tuNgay.toString().equals(denNgay.toString())){
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập ngày sau lớn hơn ngày trước!");
                    }else{
                        
                        //gửi yêu cầu tới server lấy kết quả tìm kiếm theo ngày
                        String tenChucNang = "tracuucovid#"+Combobox_QuocGia.getItemAt(Combobox_QuocGia.getSelectedIndex())+"#"+tuNgay.toString()+"#"+denNgay.toString();

                        MainFrame._CONNECT_SERVER.senData(MainFrame._SERCURITY_CLIENT.maHoaAES(tenChucNang, MainFrame._SESSION_KEY));

                        //giả mã kết quả tìm kiếm theo ngày nhận được từ server
                        checkFunction(MainFrame._SERCURITY_CLIENT.giaiMaAES(MainFrame._CONNECT_SERVER.receiveData(), MainFrame._SESSION_KEY));
                        Label_QuocGia.setText(Combobox_QuocGia.getSelectedItem().toString());
                    }
                }
            }catch(Exception e){//NullPointerException
                JOptionPane.showMessageDialog(null, "Vui lòng nhập ngày đúng định dạng DD-MM-YYYY!\nVí dụ: 02-12-2021");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Vui lòng chọn quốc gia cần tìm!");
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void Combobox_QuocGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combobox_QuocGiaActionPerformed
        
    }//GEN-LAST:event_Combobox_QuocGiaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Combobox_QuocGia;
    private com.toedter.calendar.JDateChooser Date_DenNgay;
    private com.toedter.calendar.JDateChooser Date_TuNgay;
    private javax.swing.JLabel Label_QuocGia;
    private javax.swing.JPanel Panel_LineChart;
    private laptrinhmang_doan.CardPanel cardPanel_CaNhiem;
    private laptrinhmang_doan.CardPanel cardPanel_KhoiBenh;
    private laptrinhmang_doan.CardPanel cardPanel_TuVong;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    // End of variables declaration//GEN-END:variables
}

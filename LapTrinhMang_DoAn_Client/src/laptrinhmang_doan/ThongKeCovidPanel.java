package laptrinhmang_doan;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ThongKeCovidPanel extends javax.swing.JPanel {
    DefaultTableModel modelTable;
    String clickButtonColor = "#3399FF";
    String ButtonColor = "#999999";
    ArrayList<JSONObject> listSortTable;
    Vector header;
    
    public ThongKeCovidPanel() {
        initComponents();
        cardPanel1.imageIcon = new ImageIcon("src/Icons/total-sales-30.png");
        cardPanel4.imageIcon = new ImageIcon("src/Icons/verified-account-30.png");
        cardPanel2.imageIcon = new ImageIcon("src/Icons/health-book-30.png");
        cardPanel3.imageIcon = new ImageIcon("src/Icons/heart-30.png");
        Button_TheGioi.setBackground(Color.decode(clickButtonColor));
        Button_VietNam.setBackground(Color.decode(ButtonColor));
        
        styleTable(table);
        callServer("thongkecovidthegioi#");
    }

    public void callServer(String tenChucNang){
        MainFrame._CONNECT_SERVER.senData(MainFrame._SERCURITY_CLIENT.maHoaAES(tenChucNang, MainFrame._SESSION_KEY));  
        //nhận và giải mã dữ liệu nhận được từ server
        String ketquaNhan = MainFrame._SERCURITY_CLIENT.giaiMaAES(MainFrame._CONNECT_SERVER.receiveData(), MainFrame._SESSION_KEY);
        setDataTable(ketquaNhan);
    }
    public void showPieChart(Double khoiBenh, Double tuVong, Double caMoi){
        //create dataset
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Ca mới", caMoi);
        dataset.setValue("Khỏi bệnh", khoiBenh);
        dataset.setValue("Tử vong", tuVong);
        
        //create chart
        JFreeChart piechar = ChartFactory.createPieChart("Hôm nay", dataset, true, true, true);
        PiePlot piePlot = (PiePlot) piechar.getPlot();
        
        //changing pie chart blocks colors
        piePlot.setSectionPaint("Ca mới", new Color(255,204,0));
        piePlot.setSectionPaint("Khỏi bệnh", new Color(102,255,102));
        piePlot.setSectionPaint("Tử vong", new Color(255,102,153));
        
        piePlot.setBackgroundPaint(Color.white);
        
        //create chartPanel to display chart(graph)
        ChartPanel chartPanel = new ChartPanel(piechar);
        chartPanel.setMouseWheelEnabled(true);
        //chartPanel.setPreferredSize(new Dimension(200,100));
        PanelPieChart.removeAll();
        //PanelPieChart.setLayout(new BorderLayout());
        PanelPieChart.add(chartPanel, BorderLayout.CENTER);
        
        PanelPieChart.repaint();
        PanelPieChart.validate();

    }
    public void showLineChart(JSONArray array){
        String series1 = "Ca nhiễm";  
        String series2 = "Khỏi bệnh";  
        String series3 = "Tử vong";
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(int i = 0;i<array.size();i++){
            JSONObject ob = (JSONObject) array.get(i);
            dataset.addValue(Integer.parseInt(ob.get("NewConfirmed").toString()), series1, ob.get("Date").toString());  
            dataset.addValue(Integer.parseInt(ob.get("NewRecovered").toString()), series2, ob.get("Date").toString());  
            dataset.addValue(Integer.parseInt(ob.get("NewDeaths").toString()), series3, ob.get("Date").toString());  
        }
        JFreeChart linechar = ChartFactory.createLineChart("Số ca nhiễm 7 ngày qua", "Thời gian", "Số ca", dataset, PlotOrientation.VERTICAL, true, true, true);
        CategoryPlot linecatagoryPlot = linechar.getCategoryPlot();
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
        
        //
        ChartPanel chartPanel = new ChartPanel(linechar);
        PanelLineChart.removeAll();
        PanelLineChart.add(chartPanel, BorderLayout.CENTER);
        PanelLineChart.repaint();
        PanelLineChart.validate();
    }
    public void showBarChart(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(200, "Amount", "january");
        dataset.setValue(150, "Amount", "february");
        dataset.setValue(18, "Amount", "march");
        dataset.setValue(100, "Amount", "april");
        dataset.setValue(80, "Amount", "may");
        dataset.setValue(250, "Amount", "june");
        JFreeChart barchar = ChartFactory.createBarChart("Cases", "monthly", "amount", dataset, PlotOrientation.VERTICAL, true, true, true);
        CategoryPlot catagoryPlot = barchar.getCategoryPlot();
        catagoryPlot.setBackgroundPaint(Color.white);
        BarRenderer renderer = (BarRenderer) catagoryPlot.getRenderer();
        Color lineChartColor = new Color(204,0,51);
        renderer.setSeriesPaint(0, lineChartColor);
        ChartPanel chartPanel = new ChartPanel(barchar);
//        PanelBarChart.removeAll();
//        PanelBarChart.add(chartPanel, BorderLayout.CENTER);
//        PanelBarChart.repaint();
//        PanelBarChart.validate();
    }
    public void setDataTable(String ketquaNhan){
        JSONArray arr = null;
        try{
            JSONParser parse = new JSONParser();
            JSONObject data = (JSONObject) parse.parse(ketquaNhan);
            if(data.get("status").equals("success")){
                header = new Vector();
                listSortTable = new ArrayList<>();
                if(data.get("function").toString().equals("tracuucovidthegioi")){
                    Label_Title.setText("Thế Giới");
                    arr = (JSONArray) data.get("TheGioi");
                    header.add("Quốc gia");
                    //đưa vô list để sau này dùng cho chức năng sort, arr.size - 1 vì cuối mảng là thống kê chung cho TG, mình ko đưa nó vào table
                    for(int i = 0;i<arr.size()-1;i++){
                        listSortTable.add((JSONObject) arr.get(i));
                    }
                }else{
                    Label_Title.setText("Việt Nam");
                    arr = (JSONArray) data.get("VietNam");
                    header.add("Tỉnh thành");
                    //đưa vô list để sau này dùng cho chức năng sort, arr.size - 1 vì cuối mảng là thống kê chung cho TG, mình ko đưa nó vào table
                    for(int i = 0;i<arr.size()-1;i++){
                        listSortTable.add((JSONObject) arr.get(i));
                    }
                }
                //card
                JSONObject getLastArray = (JSONObject) arr.get(arr.size()-1);//thống kê chung của TG/VN nằm cuối mảng
                setDataCard(getLastArray);
                
                //table
                header.add("Tổng số ca mắc");
                header.add("Tổng số ca hồi phục");
                header.add("Tổng số ca tử vong");
                header.add("Số ca mới trong ngày");
                header.add("Số ca hồi phục trong ngày");
                header.add("Tử vong trong ngày");
                
                sortByColume(Combobox_ColumeName.getSelectedItem().toString());//mặc định lấy lên sẽ sắp xếp giảm dần theo tổng số ca mắc
               //chart
               JSONArray arrayChart = (JSONArray) data.get("ThongKeBayNgay");
               showLineChart(arrayChart);
            }else{
                if(data.get("status").equals("updating")){
                    //tạo frame đang update dữ liệu
                    jPanel3.setVisible(false);
                    Panel_Updating.setBounds(0, 0, 1130, 840);
                    this.add(Panel_Updating);
                }else{
                    JOptionPane.showMessageDialog(null, "Lấy dữ liệu từ server fail.");
                }
            }
            
        }catch(HeadlessException | NumberFormatException | ParseException e){
            System.err.println(e.getMessage());
        }
    }
    public void styleTable(JTable table){
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setBackground(Color.decode("#00CCFF"));
        table.getTableHeader().setFont(new Font("Tahoma", 1, 13));
        table.setRowHeight(30);
    }
    
    //Tổng số ca mắc, Tổng số ca hồi phục, Tổng số ca tử vong, Số ca mới trong ngày, Số ca hồi phục trong ngày, Tử vong trong ngày
    public void sortByColume(String columeName){
        switch(columeName){
            case "Tổng số ca mắc":
                getSortByName("confirmed");
                break;
            case "Tổng số ca hồi phục":
                getSortByName("recovered");
                break;
            case "Tổng số ca tử vong":
                getSortByName("deaths");
                break;
            case "Số ca mới trong ngày":
                getSortByName("increase_confirmed");
                break;
            case "Số ca hồi phục trong ngày":
                getSortByName("increase_recovered");
                break;
            case "Tử vong trong ngày":
                getSortByName("increase_deaths");
                break;     
        }
    }
    //truyền tên Key cần sort trong arrayJson
    public void getSortByName(String name){
        //sort lại list theo đối tượng
        Collections.sort(listSortTable, new Comparator<JSONObject>() {
            @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    long v1 = (long) o1.get(name);
                    long v2 = (long) o2.get(name);
                    if(Combobox_TangGiam.getSelectedItem().toString().equalsIgnoreCase("Tăng dần")){
                        return (int) (v1 - v2);
                    }
                    //giảm dần
                    return (int) (v2 - v1);
                }
        });
        //set data lại cho table
        modelTable = new DefaultTableModel(header,0);
                
        for(JSONObject object : listSortTable) {
            Vector row = new Vector();
            row.add(object.get("province_name").toString());
            row.add(dinhDangSo(Float.parseFloat(object.get("confirmed").toString())));//format có dấu phẩ ở hàng nhìn cho số
            row.add(dinhDangSo(Float.parseFloat(object.get("recovered").toString())));
            row.add(dinhDangSo(Float.parseFloat(object.get("deaths").toString())));
            row.add(dinhDangSo(Float.parseFloat(object.get("increase_confirmed").toString())));
            row.add(dinhDangSo(Float.parseFloat(object.get("increase_recovered").toString())));
            row.add(dinhDangSo(Float.parseFloat(object.get("increase_deaths").toString())));
            modelTable.addRow(row);
        }
       table.setModel(modelTable);
    }
    public void setDataCard(JSONObject ob){
        //chung
        cardPanel1.setSubText(" "+ dinhDangSo(Float.parseFloat(ob.get("confirmed").toString())));
        cardPanel1.setHomNayText("Hôm nay: "+ dinhDangSo(Float.parseFloat(ob.get("increase_confirmed").toString())));
        //khỏi bệnh
        cardPanel4.setSubText(" "+ dinhDangSo(Float.parseFloat(ob.get("recovered").toString())));
        cardPanel4.setHomNayText("Hôm nay: "+ dinhDangSo(Float.parseFloat(ob.get("increase_recovered").toString())));
        //đang điều trị
        int tongSoCaDieuTri = Integer.parseInt(ob.get("confirmed").toString()) - (Integer.parseInt(ob.get("recovered").toString()) + Integer.parseInt(ob.get("deaths").toString()));
        cardPanel2.setSubText(" "+ dinhDangSo(Float.parseFloat(String.valueOf(tongSoCaDieuTri))));
        //int tongSoCaDieuTriTrongNgay = Integer.parseInt(ob.get("increase_confirmed").toString()) - (Integer.parseInt(ob.get("recovered").toString()) + Integer.parseInt(ob.get("deaths").toString()));
        cardPanel2.setHomNayText("");
        //tử vong
        cardPanel3.setSubText(" "+ dinhDangSo(Float.parseFloat(ob.get("deaths").toString())));
        cardPanel3.setHomNayText("Hôm nay: "+ dinhDangSo(Float.parseFloat(ob.get("increase_deaths").toString())));
        showPieChart(Double.parseDouble(ob.get("increase_recovered").toString()), Double.parseDouble(ob.get("increase_deaths").toString()), Double.parseDouble(ob.get("increase_confirmed").toString()));
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

        Panel_Updating = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        cardPanel1 = new laptrinhmang_doan.CardPanel();
        cardPanel4 = new laptrinhmang_doan.CardPanel();
        cardPanel2 = new laptrinhmang_doan.CardPanel();
        cardPanel3 = new laptrinhmang_doan.CardPanel();
        Label_Title = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        PanelLineChart = new javax.swing.JPanel();
        PanelPieChart = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        Button_VietNam = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Combobox_ColumeName = new javax.swing.JComboBox<>();
        Combobox_TangGiam = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        Button_TheGioi = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        Panel_Updating.setBackground(new java.awt.Color(204, 204, 204));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Server thống kê đang cập nhật dữ liệu, vui lòng trở lại trong vài phút tới!");

        javax.swing.GroupLayout Panel_UpdatingLayout = new javax.swing.GroupLayout(Panel_Updating);
        Panel_Updating.setLayout(Panel_UpdatingLayout);
        Panel_UpdatingLayout.setHorizontalGroup(
            Panel_UpdatingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 1089, Short.MAX_VALUE)
        );
        Panel_UpdatingLayout.setVerticalGroup(
            Panel_UpdatingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_UpdatingLayout.createSequentialGroup()
                .addGap(181, 181, 181)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(365, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1130, 840));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(1130, 840));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(null);

        cardPanel1.setColor1(new java.awt.Color(255, 204, 0));
        cardPanel1.setColor2(new java.awt.Color(255, 204, 102));
        cardPanel1.setSubText(" 1.234.234.12345");
        cardPanel1.setTitleText(" Tổng số ca");
        jPanel5.add(cardPanel1);
        cardPanel1.setBounds(55, 28, 238, 153);

        cardPanel4.setColor1(new java.awt.Color(51, 255, 0));
        cardPanel4.setColor2(new java.awt.Color(0, 204, 0));
        cardPanel4.setSubText(" 1.234.234.12345");
        cardPanel4.setTitleText(" Khỏi bệnh");
        jPanel5.add(cardPanel4);
        cardPanel4.setBounds(320, 28, 238, 153);

        cardPanel2.setColor1(new java.awt.Color(0, 204, 255));
        cardPanel2.setColor2(new java.awt.Color(0, 153, 255));
        cardPanel2.setSubText(" 1.234.234.12345");
        cardPanel2.setTitleText(" Đang điều trị");
        jPanel5.add(cardPanel2);
        cardPanel2.setBounds(581, 28, 238, 153);

        cardPanel3.setColor1(new java.awt.Color(255, 51, 51));
        cardPanel3.setColor2(new java.awt.Color(255, 102, 102));
        cardPanel3.setSubText(" 1.234.234.12345");
        cardPanel3.setTitleText(" Tử vong");
        jPanel5.add(cardPanel3);
        cardPanel3.setBounds(837, 28, 238, 153);

        Label_Title.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        Label_Title.setText("Thế Giới");
        jPanel5.add(Label_Title);
        Label_Title.setBounds(30, 0, 83, 22);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("THỐNG KÊ COVID-19 ");

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        PanelLineChart.setBackground(new java.awt.Color(204, 204, 204));
        PanelLineChart.setLayout(new java.awt.BorderLayout());

        PanelPieChart.setBackground(new java.awt.Color(204, 204, 204));
        PanelPieChart.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(PanelLineChart, javax.swing.GroupLayout.PREFERRED_SIZE, 843, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelPieChart, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelLineChart, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PanelPieChart, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(table);

        Button_VietNam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Button_VietNamMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/viet-nam-flag-24px.gif"))); // NOI18N
        jLabel1.setText("Việt Nam");

        javax.swing.GroupLayout Button_VietNamLayout = new javax.swing.GroupLayout(Button_VietNam);
        Button_VietNam.setLayout(Button_VietNamLayout);
        Button_VietNamLayout.setHorizontalGroup(
            Button_VietNamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Button_VietNamLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
        );
        Button_VietNamLayout.setVerticalGroup(
            Button_VietNamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Button_VietNamLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Combobox_ColumeName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tổng số ca mắc", "Tổng số ca hồi phục", "Tổng số ca tử vong", "Số ca mới trong ngày", "Số ca hồi phục trong ngày", "Tử vong trong ngày" }));
        Combobox_ColumeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combobox_ColumeNameActionPerformed(evt);
            }
        });

        Combobox_TangGiam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Giảm dần", "Tăng dần" }));
        Combobox_TangGiam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Combobox_TangGiamActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel4.setText("Sắp xếp:");

        Button_TheGioi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Button_TheGioiMouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/globe-30.png"))); // NOI18N
        jLabel3.setText("Thế Giới");

        javax.swing.GroupLayout Button_TheGioiLayout = new javax.swing.GroupLayout(Button_TheGioi);
        Button_TheGioi.setLayout(Button_TheGioiLayout);
        Button_TheGioiLayout.setHorizontalGroup(
            Button_TheGioiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Button_TheGioiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Button_TheGioiLayout.setVerticalGroup(
            Button_TheGioiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(Button_TheGioi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Button_VietNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Combobox_ColumeName, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Combobox_TangGiam, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Combobox_ColumeName, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Combobox_TangGiam, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Button_VietNam, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Button_TheGioi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void Button_TheGioiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Button_TheGioiMouseClicked
        Button_TheGioi.setBackground(Color.decode(clickButtonColor));
        Button_VietNam.setBackground(Color.decode(ButtonColor));
        callServer("thongkecovidthegioi#");
    }//GEN-LAST:event_Button_TheGioiMouseClicked

    private void Button_VietNamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Button_VietNamMouseClicked
        Button_TheGioi.setBackground(Color.decode(ButtonColor));
        Button_VietNam.setBackground(Color.decode(clickButtonColor));
        callServer("thongkecovidvietnam#");
        //cardPanel3.setLabel_Sub("al");
    }//GEN-LAST:event_Button_VietNamMouseClicked

    private void Combobox_ColumeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combobox_ColumeNameActionPerformed
        //gọi hàm sort
        sortByColume(Combobox_ColumeName.getSelectedItem().toString());
    }//GEN-LAST:event_Combobox_ColumeNameActionPerformed

    private void Combobox_TangGiamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Combobox_TangGiamActionPerformed
        //gọi hàm sort
        sortByColume(Combobox_ColumeName.getSelectedItem().toString());
    }//GEN-LAST:event_Combobox_TangGiamActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Button_TheGioi;
    private javax.swing.JPanel Button_VietNam;
    private javax.swing.JComboBox<String> Combobox_ColumeName;
    private javax.swing.JComboBox<String> Combobox_TangGiam;
    private javax.swing.JLabel Label_Title;
    private javax.swing.JPanel PanelLineChart;
    private javax.swing.JPanel PanelPieChart;
    private javax.swing.JPanel Panel_Updating;
    private laptrinhmang_doan.CardPanel cardPanel1;
    private laptrinhmang_doan.CardPanel cardPanel2;
    private laptrinhmang_doan.CardPanel cardPanel3;
    private laptrinhmang_doan.CardPanel cardPanel4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}

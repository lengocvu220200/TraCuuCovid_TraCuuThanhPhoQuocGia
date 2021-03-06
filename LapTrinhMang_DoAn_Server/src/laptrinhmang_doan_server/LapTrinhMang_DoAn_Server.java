package laptrinhmang_doan_server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class LapTrinhMang_DoAn_Server {
    private Socket socket = null;
    private ServerSocket server = null;
    int dem = 1;
    public LapTrinhMang_DoAn_Server(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started");
            while(true){
                socket = server.accept();
                System.out.println("Client accepted");
                new ServiceThread(socket, dem++).start();
            }
            
        } catch (IOException ex) {
            
        }
    }
    
    
    public static void main(String[] args) {
        LapTrinhMang_DoAn_Server server = new LapTrinhMang_DoAn_Server(3456);
    }
    
    private static class ServiceThread extends Thread{
        private int clientNumber;
        private Socket socketOfServer = null;
        private BufferedWriter out = null;
        private BufferedReader in = null;
        private URL url;
        private HttpURLConnection conn;
        private Scanner scanner;
        private String SESSION_KEY = "";
        private SecurityData_Server sercurityData = new SecurityData_Server();
        private JSONParser parse;
        private JSONObject jsonXuat;
                
        public ServiceThread(Socket socketOfServer, int clientNumber) {
           this.clientNumber = clientNumber;
           this.socketOfServer = socketOfServer;

            System.out.println("K???t n???i m???i t??? Client th??? " + this.clientNumber + " t???i " + socketOfServer);
        }
        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
                String line = in.readLine();//d??ng n??y nh???n d??? li???u g???i ?????u ti??n khi client k???t n???i server(client g???i session key)
                SESSION_KEY = sercurityData.giaiMaRSA(line);
                System.out.println("SESSION_KEY: "+SESSION_KEY);
                out = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
                in = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
                while(true){
                    try {
                        line = in.readLine();
                        String kq = Menu(line);
                        out.write(sercurityData.maHoaAES(kq,SESSION_KEY));
                        out.newLine();
                        out.flush();
                    } catch (IOException e) {
                        break;
                    }
                }
                
                System.out.println("Client ???? ????ng k???t n???i.");
                in.close();
                out.close();
                socketOfServer.close();
                
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        //tenchucnang#bien1#bien2
        private String Menu(String stringReceived){
            //c?? ph??p ch???c n??ng nh?? sau: tenchucnang#Bien1#Bien2#Bien3
            System.out.println("chu???i m?? h??a nh???n ???????c: "+stringReceived);
            String xuatKetQua = "";
            if(stringReceived!=null){
                stringReceived = sercurityData.giaiMaAES(stringReceived, SESSION_KEY);
                StringTokenizer st = new StringTokenizer(stringReceived, "#", false);
                String tenChucNang = st.nextToken();//l???y ra t??n ch???c n??ng nh???n ???????c

                switch(tenChucNang){
                    case "thongkecovidthegioi":
                        //ch???c n??ng th???ng k??
                        xuatKetQua = thongKeCovidTheGioi();
                        break;
                    case "thongkecovidvietnam":
                        //ch???c n??ng th???ng k??
                        xuatKetQua = thongKeCovidVietNam();
                        break;
                    case "tracuucovid":
                        //ch???c n??ng tra c???u covid
                        xuatKetQua = traCuuCovid(st.nextToken(), st.nextToken(), st.nextToken());
                        break;
                    case "tracuudialyquocgia":
                        //ch???c n??ng tra c???u ?????i l?? c???a qu???c gia
                        xuatKetQua = traCuuQuocGia(st.nextToken());
                        break;
                    case "danhsachquocgia":
                        xuatKetQua = danhSachQuocGia();
                        break;
                    case "tracuudialythanhpho":
                        //ch???c n??ng tra c???u ?????a l?? c???a th??nh ph???
                        xuatKetQua = traCuuThanhPho(st.nextToken());
                        break;
                    case "thanhphohientai":
                        //ch???c n??ng tra c???u ?????a l?? c???a th??nh ph??? hi???n t???i theo ?????a ch??? IP
                        xuatKetQua = thanhPhoHienTai();
                        break;
                }
            }
            return xuatKetQua;
        }
        //k???t qu??? tr??? v??? c???a c??c h??m l?? 1 chu???i Json
        //link covid c???a c??c n?????c tr??n th??? gi???i: https://ncovi.vnpt.vn/thongtindichbenh_v2
        //link th???ng k?? t??? ng??y t???i ng??y th??? gi???i: https://api.covid19api.com/world?from=2021-12-12&to=2021-12-18
        private String thongKeCovidTheGioi(){
            //l???y ng??y th??ng n??m hi???n t???i
            LocalDateTime now = LocalDateTime.now();  
            LocalDate date = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
            //tr??? ??i 7 ng??y
            LocalDate ngayBatDau = date.minusYears(0).minusMonths(0).minusWeeks(0).minusDays(7);
            String ngayKetThuc = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(now);
            String link1 = "https://ncovi.vnpt.vn/thongtindichbenh_v2";
            String link2 = "https://api.covid19api.com/world?from="+ngayBatDau+"&to="+ngayKetThuc;
            jsonXuat = new  JSONObject();
            jsonXuat.put("function", "tracuucovidthegioi");  
            try {
                Connection.Response res = Jsoup.connect(link1)
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();
                parse = new JSONParser();
                JSONObject data = (JSONObject) parse.parse(res.body());
                data = (JSONObject) data.get("data");
                if(data.isEmpty()){
                    jsonXuat.put("status", "updating");
                }else{
                    JSONArray arrayTG = (JSONArray) data.get("TG");
                    jsonXuat.put("TheGioi", arrayTG);
                    
                    ///l???y th???ng k?? theo tu???n
                    res = Jsoup.connect(link2)
                            .ignoreContentType(true)
                            .method(Connection.Method.GET)
                            .execute();
                    JSONArray  arrayObject = (JSONArray) parse.parse(res.body());
                    ArrayList<JSONObject> list = new ArrayList<>();
                    for(int i = 0;i<arrayObject.size();i++){
                        list.add((JSONObject) arrayObject.get(i));
                    }
                    Collections.sort(list, new Comparator<JSONObject>() {
                    @Override
                        public int compare(JSONObject o1, JSONObject o2) {
                            String v1 =  o1.get("Date").toString();
                            String v3 = o2.get("Date").toString();
                            return v1.compareTo(v3);
                        }
                    });
                    arrayObject = new JSONArray();
                    for (JSONObject obj : list) {
                        JSONObject newObject = new JSONObject();//b?????c n??y ????? l???y ph???n ng??y th??ng n??m, b??? ??i sau ch??? T
                        newObject.put("NewConfirmed", obj.get("NewConfirmed"));
                        newObject.put("NewRecovered", obj.get("NewRecovered"));
                        newObject.put("NewDeaths", obj.get("NewDeaths"));
                        newObject.put("Date", DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH).format(LocalDate.parse(new StringTokenizer(obj.get("Date").toString(), "T").nextToken())));
                        arrayObject.add(newObject);
                    }
                    
                    jsonXuat.put("ThongKeBayNgay", arrayObject);
                    //arrayObject.add(arrayTG.get(arrayTG.size()-1));
                    jsonXuat.put("status", "success");
                }
            } catch (IOException | ParseException ex) {
                System.err.println("l???i truy c???p web. "+ex.getMessage());
                jsonXuat.put("status", "fail");
                return jsonXuat.toString();
            }
            
            return jsonXuat.toString();
        }
        
        //link covid c???a c??c t???nh VN: https://ncovi.vnpt.vn/thongtindichbenh_v2
        //link th???ng k?? t??? ng??y t???i ng??y VN: https://api.covid19api.com/country/vietnam?from=2021-12-12&to=2021-12-18
        private String thongKeCovidVietNam(){
            String link = "https://ncovi.vnpt.vn/thongtindichbenh_v2";
            jsonXuat = new  JSONObject();
            jsonXuat.put("function", "thongkecovidvietnam");  
            try {
                Connection.Response res = Jsoup.connect(link)
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();
                parse = new JSONParser();
                JSONObject data = (JSONObject) parse.parse(res.body());
                data = (JSONObject) data.get("data");
                jsonXuat.put("VietNam", data.get("VN"));
                
                //l???y th???ng k?? 7 ng??y
                LocalDateTime now = LocalDateTime.now();  
                LocalDate date = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
                //tr??? ??i 7 ng??y
                LocalDate ngayBatDau = date.minusYears(0).minusMonths(0).minusWeeks(0).minusDays(8);
                LocalDate ngayKetThuc = date.minusYears(0).minusMonths(0).minusWeeks(0).minusDays(1);
                //data = (JSONObject) parse.parse(traCuuCovid("vietnam", ngayBatDau.toString(), ngayKetThuc));
                res = Jsoup.connect("https://api.covid19api.com/country/vietnam?from="+ngayBatDau.toString()+"&to="+ngayKetThuc)
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();
                JSONArray arrayObject = (JSONArray) parse.parse(res.body());
                JSONArray arrayTKBayNgay = new JSONArray();
                for(int i = 0;i<arrayObject.size()-1;i++){
                    JSONObject ob1 = (JSONObject) arrayObject.get(i);
                    JSONObject ob2 = (JSONObject) arrayObject.get(i+1);
                    JSONObject newObject = new JSONObject();
                    int NewConfirmed = Integer.parseInt(ob2.get("Confirmed").toString()) - Integer.parseInt(ob1.get("Confirmed").toString());
                    int NewRecovered = Integer.parseInt(ob2.get("Recovered").toString()) - Integer.parseInt(ob1.get("Recovered").toString());
                    int NewDeaths = Integer.parseInt(ob2.get("Deaths").toString()) - Integer.parseInt(ob1.get("Deaths").toString());
                    newObject.put("NewConfirmed", NewConfirmed);
                    newObject.put("NewRecovered", NewRecovered);
                    newObject.put("NewDeaths", NewDeaths);
                    newObject.put("Date", DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH).format(LocalDate.parse(new StringTokenizer(ob2.get("Date").toString(), "T").nextToken())));
                    arrayTKBayNgay.add(newObject);
                }
                jsonXuat.put("ThongKeBayNgay", arrayTKBayNgay);
            } catch (IOException | ParseException ex) {
                System.err.println("l???i truy c???p web. "+ex.getMessage());
                jsonXuat.put("status", "fail");
                return jsonXuat.toString();
            }
            jsonXuat.put("status", "success");
            return jsonXuat.toString();
        }

        //link tra c???u theo qu???c gia t??? ng??y t???i ng??y: https://api.covid19api.com/country/vietnam?from=2021-12-12&to=2021-12-18
        private String traCuuCovid(String tenQuocGia, String ngayBatDau, String ngayKetThuc){
            //v?? c???u tr??c api n??n ph???i tr??? ??i 1 ng??y ????? t??nh to??n(L???y ng??y sau tr??? ng??y tr?????c)
            //ngayBatDau c?? ?????nh d???ng "2015-08-04";
            LocalDate parseLocalDate = LocalDate.parse(ngayBatDau);
            LocalDate date = LocalDate.of(parseLocalDate.getYear(), parseLocalDate.getMonth(), parseLocalDate.getDayOfMonth());
            //ng??y b???t d???u tr??? ??i m???t ng??y
            LocalDate ngayBatDauTruMot = date.minusYears(0).minusMonths(0).minusWeeks(0).minusDays(1);
            //
            String link = "https://api.covid19api.com/country/"+tenQuocGia.toLowerCase()+"?from="+ngayBatDauTruMot.toString()+"&to="+ngayKetThuc;
            JSONArray mangXuat = new JSONArray();
            jsonXuat = new  JSONObject();
            try{
                Connection.Response res = Jsoup.connect(link)
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();
                parse = new JSONParser();
                JSONArray arr = (JSONArray) parse.parse(res.body());
                if(arr.size()!=0){
                    
                    int TongSoCaNhiem = Integer.parseInt(((JSONObject) arr.get(arr.size()-1)).get("Confirmed").toString()) - Integer.parseInt(((JSONObject) arr.get(1)).get("Confirmed").toString());
                    int TongSoCaKhoiBenh = Integer.parseInt(((JSONObject) arr.get(arr.size()-1)).get("Recovered").toString())-Integer.parseInt(((JSONObject) arr.get(1)).get("Recovered").toString());
                    int TongSoCaTuVong = Integer.parseInt(((JSONObject) arr.get(arr.size()-1)).get("Deaths").toString())-Integer.parseInt(((JSONObject) arr.get(1)).get("Deaths").toString());
                    
                    jsonXuat.put("TongSoCaNhiem", TongSoCaNhiem);
                    jsonXuat.put("TongSoCaKhoiBenh", TongSoCaKhoiBenh);
                    jsonXuat.put("TongSoCaTuVong", TongSoCaTuVong);

                    for(int i = 0; i<arr.size()-1;i++)
                    {
                        JSONObject ob1 = (JSONObject) arr.get(i);
                        JSONObject ob2 = (JSONObject) arr.get(i+1);
                        JSONObject newObject = new JSONObject();
                        int Confirmed, Recovered, Deaths;
                        if(Integer.parseInt(ob2.get("Confirmed").toString())==0){
                            Confirmed = 0;
                        }else{
                            Confirmed = Integer.parseInt(ob2.get("Confirmed").toString()) - Integer.parseInt(ob1.get("Confirmed").toString());
                        }
                        if(Integer.parseInt(ob2.get("Recovered").toString())==0){
                            Recovered = 0;
                        }else{
                            Recovered = Integer.parseInt(ob2.get("Recovered").toString()) - Integer.parseInt(ob1.get("Recovered").toString());
                        }
                        if(Integer.parseInt(ob2.get("Deaths").toString())==0){
                            Deaths = 0;
                        }else{
                            Deaths = Integer.parseInt(ob2.get("Deaths").toString()) - Integer.parseInt(ob1.get("Deaths").toString());
                        }
                        newObject.put("Confirmed", Confirmed);
                        newObject.put("Deaths", Deaths);
                        newObject.put("Recovered", Recovered);
                        //newObject.put("Active", getObjectArray.get("Active"));
                        newObject.put("Date", DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH).format(LocalDate.parse(new StringTokenizer(ob2.get("Date").toString(), "T").nextToken())));
                        mangXuat.add(newObject);
                    }
                }else{
                    jsonXuat.put("status", "fail");
                    jsonXuat.put("function", "tracuucovid");
                    return jsonXuat.toString();
                }
            }catch(IOException | ParseException e){
                System.err.println("l???i truy c???p "+e.getMessage());
                jsonXuat.put("status", "fail");
                jsonXuat.put("function", "tracuucovid");
                return jsonXuat.toString();
            }
            jsonXuat.put("status", "success");
            jsonXuat.put("function", "tracuucovid");
            jsonXuat.put("SoLieu", mangXuat);
            
            return jsonXuat.toString();
        }
        //link l???y v??? tr?? theo IP wifi hi???n t???i (trong h??m c?? ch???y v???i header, ????? test tr??n web th?? c???n email): https://spott.p.rapidapi.com/places/ip/me
        //link l???y th???i ti???t ti???t(truy???n v??o lat,long): https://weather.com/vi-VN/weather/today/l/12.213,109.194
        private String thanhPhoHienTai(){
            String linkJson = "https://spott.p.rapidapi.com/places/ip/me";
            String linkJsoup = "https://weather.com/vi-VN/weather/today/l/";
            jsonXuat = new  JSONObject();
            JSONObject object;
            try {
                Connection.Response res = Jsoup.connect(linkJson)
                        .followRedirects(true)
                        .ignoreContentType(true)
                        .header("x-rapidapi-host", "spott.p.rapidapi.com")
                        .header("x-rapidapi-key", "3e63d55898msh9b4abcff5eaa398p198644jsn254394a9fc52")
                        .method(Connection.Method.GET)
                        .execute();
                parse = new JSONParser();
                JSONObject mainObject = (JSONObject) parse.parse(res.body());
                jsonXuat.put("TenThanhPho", mainObject.get("name"));//t??n tp
                jsonXuat.put("DanSo", mainObject.get("population"));
                jsonXuat.put("MaGioDiaPhuong", mainObject.get("timezoneId"));
                object = (JSONObject) mainObject.get("country");
                jsonXuat.put("TenQuocGia", object.get("name"));//t??n qu???c gia
                jsonXuat.put("MaQuocGia", object.get("id"));//m?? qu???c gia
                object = (JSONObject) mainObject.get("adminDivision1");
                jsonXuat.put("KhuVucHanhChinh", object.get("name"));
                object = (JSONObject) mainObject.get("coordinates");
                String Latitude = object.get("latitude").toString();
                String Longitude = object.get("longitude").toString();
                jsonXuat.put("Latitude", Latitude);//lat
                jsonXuat.put("Longitude", Longitude);//long
                
                //l???y th???i ti???t
                res = Jsoup.connect(linkJsoup + Latitude + "," + Longitude)
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();
                Document doc = res.parse();
                //l???y nhi???t ????? hi???n t???i
                jsonXuat.put("NhietDoHienTai", doc.select("span[class$=CurrentConditions--tempValue--3a50n]").text());
                jsonXuat.put("TinhHinh", doc.select("div[data-testid$=wxPhrase]").text());
                //l???y th??ng tin li??n quan
                Elements e  = doc.select("div[class$=TodayDetailsCard--detailsContainer--16Hg0]");
                jsonXuat.put("DoAm", e.select("span[data-testid$=PercentageValue]").text());
                String[] gio = e.select("span[data-testid$=Wind]").text().split("Wind Direction ");
                jsonXuat.put("TocDogio", gio[1]);
                jsonXuat.put("UV", e.select("span[data-testid$=UVIndexValue]").text());
                jsonXuat.put("TamNhin", e.select("span[data-testid$=VisibilityValue]").text());
                //l???y d??? b??o theo gi???
                e = doc.select("section[data-testid$=HourlyWeatherModule]");
                Elements a = e.select("h3");
                for(int i = 0; i<a.size();i++){
                    jsonXuat.put("Gio"+(i+1), a.get(i).text());
                }
                a=e.select("div[data-testid$=SegmentHighTemp]");
                for(int i = 0; i<a.size();i++){
                    jsonXuat.put("NhietDoTheoGio"+(i+1), a.get(i).text());
                }
                StringTokenizer bn = new StringTokenizer(e.select("div[data-testid$=SegmentPrecipPercentage]").text(),"RainKh??? n??ng c?? m??a");
                int dem = 1;
                while(bn.hasMoreTokens()){
                    jsonXuat.put("KhaNangCoMuaTheoGio"+dem, bn.nextToken());
                    dem++;
                }
                //l???y d??? b??o h??ng ng??y
                e = doc.select("section[data-testid$=DailyWeatherModule]");
                a = e.select("h3");
                for(int i = 0; i<a.size();i++){
                    jsonXuat.put("Ngay"+(i+1), a.get(i).text());
                }
                a=e.select("div[data-testid$=SegmentHighTemp]");
                for(int i = 0; i<a.size();i++){
                    jsonXuat.put("NhietDoTheoNgayBanNgay"+(i+1), a.get(i).text());
                }
                a=e.select("div[data-testid$=SegmentLowTemp]");
                for(int i = 0; i<a.size();i++){
                    jsonXuat.put("NhietDoTheoNgayBanDem"+(i+1), a.get(i).text());
                }
                bn = new StringTokenizer(e.select("div[data-testid$=SegmentPrecipPercentage]").text(),"RainKh??? n??ng c?? m??a");
                dem = 1;
                while(bn.hasMoreTokens()){
                    jsonXuat.put("KhaNangCoMuaTheoNgay"+dem, bn.nextToken());
                    dem++;
                }
            } catch (IOException | ParseException | IndexOutOfBoundsException ex) {
                System.err.println("l???i server "+ex.getMessage());
                jsonXuat.put("status", "fail");
                return jsonXuat.toString();
            }
            jsonXuat.put("status", "success");
            return jsonXuat.toString();
        }
        //link tra c???u th??nh ph??? (trong h??m c?? ch???y v???i header, ????? test tr??n web th?? c???n email): https://spott.p.rapidapi.com/places?type=CITY&skip=0&limit=1&q=phan thi???t
        //link l???y th???i ti???t ti???t(truy???n v??o lat,long): https://weather.com/vi-VN/weather/today/l/12.213,109.194
        private String traCuuThanhPho(String tenThanhPho){
            jsonXuat = new  JSONObject();
            JSONObject object;
            String linkJson = "https://spott.p.rapidapi.com/places?type=CITY&skip=0&limit=1&q="+tenThanhPho;
            String linkJsoup = "https://weather.com/vi-VN/weather/today/l/";
            try{
                Connection.Response res = Jsoup.connect(linkJson)
                        .followRedirects(true)
                        .ignoreContentType(true)
                        .header("x-rapidapi-host", "spott.p.rapidapi.com")
                        .header("x-rapidapi-key", "3e63d55898msh9b4abcff5eaa398p198644jsn254394a9fc52")
                        .method(Connection.Method.GET)
                        .execute();
                
                parse = new JSONParser();
                JSONArray data = (JSONArray) parse.parse(res.body());
                JSONObject getObjectArray = (JSONObject) data.get(0);
                jsonXuat.put("TenThanhPho", getObjectArray.get("name"));//t??n tp
                jsonXuat.put("DanSo", getObjectArray.get("population"));
                object = (JSONObject) getObjectArray.get("country");
                jsonXuat.put("TenQuocGia", object.get("name"));//t??n qu???c gia
                jsonXuat.put("MaQuocGia", object.get("id"));//m?? qu???c gia
                object = (JSONObject) getObjectArray.get("adminDivision1");
                jsonXuat.put("KhuVucHanhChinh", object.get("name")/*.toString() +" "+ object.get("LocalizedType").toString()*/);//khu v???c h??nh ch??nh
                jsonXuat.put("MaGioDiaPhuong", getObjectArray.get("timezoneId"));//m??i gi???
                object = (JSONObject) getObjectArray.get("coordinates");
                String Latitude = object.get("latitude").toString();
                String Longitude = object.get("longitude").toString();
                jsonXuat.put("Latitude", Latitude);//lat
                jsonXuat.put("Longitude", Longitude);//long
                //l???y th???i ti???t
                res = Jsoup.connect(linkJsoup + Latitude + "," + Longitude)
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();
                Document doc = res.parse();
                //l???y nhi???t ????? hi???n t???i
                jsonXuat.put("NhietDoHienTai", doc.select("span[class$=CurrentConditions--tempValue--3a50n]").text());
                jsonXuat.put("TinhHinh", doc.select("div[data-testid$=wxPhrase]").text());
                //l???y th??ng tin li??n quan
                Elements e  = doc.select("div[class$=TodayDetailsCard--detailsContainer--16Hg0]");
                jsonXuat.put("DoAm", e.select("span[data-testid$=PercentageValue]").text());
                String[] gio = e.select("span[data-testid$=Wind]").text().split("Wind Direction ");
                jsonXuat.put("TocDogio", gio[1]);
                jsonXuat.put("UV", e.select("span[data-testid$=UVIndexValue]").text());
                jsonXuat.put("TamNhin", e.select("span[data-testid$=VisibilityValue]").text());
                //l???y d??? b??o theo gi???
                e = doc.select("section[data-testid$=HourlyWeatherModule]");
                Elements a = e.select("h3");
                for(int i = 0; i<a.size();i++){
                    jsonXuat.put("Gio"+(i+1), a.get(i).text());
                }
                a=e.select("div[data-testid$=SegmentHighTemp]");
                for(int i = 0; i<a.size();i++){
                    jsonXuat.put("NhietDoTheoGio"+(i+1), a.get(i).text());
                }
                StringTokenizer bn = new StringTokenizer(e.select("div[data-testid$=SegmentPrecipPercentage]").text(),"RainKh??? n??ng c?? m??a");
                int dem = 1;
                while(bn.hasMoreTokens()){
                    jsonXuat.put("KhaNangCoMuaTheoGio"+dem, bn.nextToken());
                    dem++;
                }
                //l???y d??? b??o h??ng ng??y
                e = doc.select("section[data-testid$=DailyWeatherModule]");
                a = e.select("h3");
                for(int i = 0; i<a.size();i++){
                    jsonXuat.put("Ngay"+(i+1), a.get(i).text());
                }
                a=e.select("div[data-testid$=SegmentHighTemp]");
                for(int i = 0; i<a.size();i++){
                    jsonXuat.put("NhietDoTheoNgayBanNgay"+(i+1), a.get(i).text());
                }
                a=e.select("div[data-testid$=SegmentLowTemp]");
                for(int i = 0; i<a.size();i++){
                    jsonXuat.put("NhietDoTheoNgayBanDem"+(i+1), a.get(i).text());
                }
                bn = new StringTokenizer(e.select("div[data-testid$=SegmentPrecipPercentage]").text(),"RainKh??? n??ng c?? m??a");
                dem = 1;
                while(bn.hasMoreTokens()){
                    jsonXuat.put("KhaNangCoMuaTheoNgay"+dem, bn.nextToken());
                    dem++;
                }
            }catch(IOException | ParseException | IndexOutOfBoundsException e){
                //System.err.println("L???i server tra c???u th??nh ph??? "+e.getMessage());
                jsonXuat.put("status", "fail");
                return jsonXuat.toString();
            }
            jsonXuat.put("status", "success");
            return jsonXuat.toString();
        }
        //link l???y danh s??ch qu???c gia: http://api.geonames.org/countryInfoJSON?formatted=true&lang=en&countarrayListNationry=&username=leminhcuong2988&style=full
        //link l???y v??? tr?? qu???c gia hi???n t???i (L???y d???a v??o countryCode): http://ip-api.com/json/
        private String danhSachQuocGia(){
            JSONArray mangXuat = new JSONArray();
            String link = "http://api.geonames.org/countryInfoJSON?formatted=true&lang=en&countarrayListNationry=&username=leminhcuong2988&style=full";
            String linkViTriHienTai = "http://ip-api.com/json/";
            jsonXuat = new  JSONObject();
            
            //X??? l?? t???i ????y
            try{
                Connection.Response getCountryCode = Jsoup.connect(linkViTriHienTai)
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();
                parse = new JSONParser();
                JSONObject code = (JSONObject) parse.parse(getCountryCode.body());
                String maQuocGia = code.get("countryCode").toString();
                // l???y th??ng tin c???a qu???c gia
                Connection.Response res = Jsoup.connect(link)
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();
                parse = new JSONParser();
                JSONObject data = (JSONObject) parse.parse(res.body());
                JSONArray arr = (JSONArray) data.get("geonames");
                for(int i=0;i<arr.size();i++){
                    JSONObject getObjectArray = (JSONObject) arr.get(i);
                    JSONObject newObject = new JSONObject();
                    //newObject.put("countryCode", getObjectArray.get("countryCode"));
                    newObject.put("countryName", getObjectArray.get("countryName"));
                    if(getObjectArray.get("countryCode").equals(maQuocGia)){
                        jsonXuat.put("QuocGiaHienTai", traCuuQuocGia(getObjectArray.get("countryName").toString()));//s??i l???i h??m
                    }
                    mangXuat.add(newObject);
                }
            }catch(IOException | ParseException e){
                System.err.println("l???i server "+e.getMessage());
                jsonXuat.put("function", "danhsachquocgia");
                jsonXuat.put("status", "fail");
                return jsonXuat.toString();
            }
            jsonXuat.put("status", "success");
            jsonXuat.put("geonames", mangXuat);
            jsonXuat.put("function", "danhsachquocgia");//????? d?????i ????y ????? kh???i tr??ng l???p  vi???c g???i h??m tra c???u
            return jsonXuat.toString();
        }
        //link tra c???u qu???c gia s???a d???ng Json: http://api.geonames.org/countryInfoJSON?formatted=true&lang=en&countarrayListNationry=&username=leminhcuong2988&style=full
        //link tra c???u qu???c gia s???a d???ng Jsoup: https://www.geonames.org/countries/VN/
        private String traCuuQuocGia(String countryName){
            jsonXuat = new JSONObject();
            jsonXuat.put("function", "tracuudialyquocgia");
            String linkJson = "http://api.geonames.org/countryInfoJSON?formatted=true&lang=en&countarrayListNationry=&username=leminhcuong2988&style=full";
            String linkJsoup = "https://www.geonames.org/countries/";
            //X??? l?? t???i ????y
            try{
                Connection.Response res = Jsoup.connect(linkJson)
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();
                parse = new JSONParser();
                JSONObject data = (JSONObject) parse.parse(res.body());
                JSONArray arr = (JSONArray) data.get("geonames");
                boolean check = false;
                String countryCode = null;
                //String countryID = null;
                for(int i=0;i<arr.size();i++){
                    JSONObject getObjectArray = (JSONObject) arr.get(i);
                    String name = String.valueOf(getObjectArray.get("countryName"));
                    if(name.equalsIgnoreCase(countryName)){
                        jsonXuat.put("status", "success");
                        jsonXuat.put("geonameId", getObjectArray.get("geonameId"));
                        jsonXuat.put("countryName", getObjectArray.get("countryName"));
                        jsonXuat.put("capital", getObjectArray.get("capital"));
                        countryCode = getObjectArray.get("countryCode").toString();
                        jsonXuat.put("countryCode", countryCode);
                        jsonXuat.put("continentName", getObjectArray.get("continentName"));
                        check = true;
                        //countryID = getObjectArray.get("geonameId").toString();
                        break;
                    }
                }
                if(check){
                    linkJsoup = linkJsoup + countryCode + "/";
                    res = Jsoup.connect(linkJsoup)
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();
                    Document doc = res.parse();
                    Elements e = doc.select("table[cellpadding$=5] tr td:nth-of-type(2)");
                    jsonXuat.put("area", e.get(4).text());
                    jsonXuat.put("population", e.get(5).text());
                    jsonXuat.put("currency", e.get(6).text());
                    jsonXuat.put("languages", e.get(7).text());
                    jsonXuat.put("neighbours", e.get(8).text());
                    
                }else{
                    jsonXuat.put("status", "fail");
                }
            }catch(IOException | ParseException e){
                System.err.println("l???i server "+e.getMessage());
                jsonXuat.put("status", "fail");
                return jsonXuat.toString();
            }
            return jsonXuat.toString();
        }
    }
}

//private String traCuuThanhPho(String tenThanhPho){
//    jsonXuat = new  JSONObject();
//    JSONObject object;
//    String linkJson = "https://dataservice.accuweather.com/locations/v1/cities/search?apikey=aAEQINHigdUn6V5oX17hOAzreBhQGeRT&q="+tenThanhPho;//aAEQINHigdUn6V5oX17hOAzreBhQGeRT//682500PcukwQUtq1UDd6XimUfAmBA5HL
//    String linkJsoup = "https://weather.com/vi-VN/weather/today/l/";
//    try{
//        Connection.Response res = Jsoup.connect(linkJson)
//                .ignoreContentType(true)
//                .method(Connection.Method.GET)
//                .execute();
//
//        parse = new JSONParser();
//        JSONArray data = (JSONArray) parse.parse(res.body());
//        JSONObject getObjectArray = (JSONObject) data.get(0);
//        jsonXuat.put("TenThanhPho", getObjectArray.get("LocalizedName"));//t??n tp
//        object = (JSONObject) getObjectArray.get("Region");
//        jsonXuat.put("ChauLuc", object.get("LocalizedName"));//t??n ch??u l???c
//        object = (JSONObject) getObjectArray.get("Country");
//        jsonXuat.put("TenQuocGia", object.get("LocalizedName"));//t??n qu???c gia
//        jsonXuat.put("MaQuocGia", object.get("ID"));//m?? qu???c gia
//        object = (JSONObject) getObjectArray.get("AdministrativeArea");
//        jsonXuat.put("KhuVucHanhChinh", object.get("LocalizedName")/*.toString() +" "+ object.get("LocalizedType").toString()*/);//khu v???c h??nh ch??nh
//        object = (JSONObject) getObjectArray.get("TimeZone");
//        jsonXuat.put("MaGioDiaPhuong", object.get("Code"));//m??i gi???
//        jsonXuat.put("TenGioDiaPhuong", object.get("Name"));
//        jsonXuat.put("MaGioQuocTe", object.get("GmtOffset"));
//        object = (JSONObject) getObjectArray.get("GeoPosition");
//        String Latitude = object.get("Latitude").toString();
//        String Longitude = object.get("Longitude").toString();
//        jsonXuat.put("Latitude", Latitude);//lat
//        jsonXuat.put("Longitude", Longitude);//long
//        //l???y th???i ti???t
//        res = Jsoup.connect(linkJsoup + Latitude + "," + Longitude)
//                .ignoreContentType(true)
//                .method(Connection.Method.GET)
//                .execute();
//        Document doc = res.parse();
//        //l???y nhi???t ????? hi???n t???i
//        jsonXuat.put("NhietDoHienTai", doc.select("span[class$=CurrentConditions--tempValue--3a50n]").text());
//        jsonXuat.put("TinhHinh", doc.select("div[data-testid$=wxPhrase]").text());
//        //l???y th??ng tin li??n quan
//        Elements e  = doc.select("div[class$=TodayDetailsCard--detailsContainer--16Hg0]");
//        jsonXuat.put("DoAm", e.select("span[data-testid$=PercentageValue]").text());
//        String[] gio = e.select("span[data-testid$=Wind]").text().split("Wind Direction ");
//        jsonXuat.put("TocDogio", gio[1]);
//        jsonXuat.put("UV", e.select("span[data-testid$=UVIndexValue]").text());
//        jsonXuat.put("TamNhin", e.select("span[data-testid$=VisibilityValue]").text());
//        //l???y d??? b??o theo gi???
//        e = doc.select("section[data-testid$=HourlyWeatherModule]");
//        Elements a = e.select("h3");
//        for(int i = 0; i<a.size();i++){
//            jsonXuat.put("Gio"+(i+1), a.get(i).text());
//        }
//        a=e.select("div[data-testid$=SegmentHighTemp]");
//        for(int i = 0; i<a.size();i++){
//            jsonXuat.put("NhietDoTheoGio"+(i+1), a.get(i).text());
//        }
//        StringTokenizer bn = new StringTokenizer(e.select("div[data-testid$=SegmentPrecipPercentage]").text(),"RainKh??? n??ng c?? m??a");
//        int dem = 1;
//        while(bn.hasMoreTokens()){
//            jsonXuat.put("KhaNangCoMuaTheoGio"+dem, bn.nextToken());
//            dem++;
//        }
//        //l???y d??? b??o h??ng ng??y
//        e = doc.select("section[data-testid$=DailyWeatherModule]");
//        a = e.select("h3");
//        for(int i = 0; i<a.size();i++){
//            jsonXuat.put("Ngay"+(i+1), a.get(i).text());
//        }
//        a=e.select("div[data-testid$=SegmentHighTemp]");
//        for(int i = 0; i<a.size();i++){
//            jsonXuat.put("NhietDoTheoNgayBanNgay"+(i+1), a.get(i).text());
//        }
//        a=e.select("div[data-testid$=SegmentLowTemp]");
//        for(int i = 0; i<a.size();i++){
//            jsonXuat.put("NhietDoTheoNgayBanDem"+(i+1), a.get(i).text());
//        }
//        bn = new StringTokenizer(e.select("div[data-testid$=SegmentPrecipPercentage]").text(),"RainKh??? n??ng c?? m??a");
//        dem = 1;
//        while(bn.hasMoreTokens()){
//            jsonXuat.put("KhaNangCoMuaTheoNgay"+dem, bn.nextToken());
//            dem++;
//        }
//    }catch(IOException | ParseException | IndexOutOfBoundsException e){
//        jsonXuat.put("status", "fail");
//        return jsonXuat.toString();
//    }
//    jsonXuat.put("status", "success");
//    return jsonXuat.toString();
//}
//        private String traCuuCovid(String tenQuocGia, String ngayBatDau, String ngayKetThuc){
//            String link = "https://api.covid19api.com/country/vietnam?from="+ngayBatDau+"&to="+ngayKetThuc;//2021-05-05T00:00:00Z
//            jsonXuat = new  JSONObject();
//            try{
//                Connection.Response res = Jsoup.connect(link)
//                        .ignoreContentType(true)
//                        .method(Connection.Method.GET)
//                        .execute();
//                parse = new JSONParser();
//                JSONArray data = (JSONArray) parse.parse(res.body());
//                if(data.size()>0){
//                    
//                }else{
//                    jsonXuat.put("status", "fail");
//                    return jsonXuat.toString();
//                }
//            }catch(IOException | ParseException e){
//                System.err.println("l???i truy c???p web. "+e.getMessage());
//                jsonXuat.put("status", "fail");
//                return jsonXuat.toString();
//            }
//            jsonXuat.put("status", "success");
//            jsonXuat.put("function", "tracuucovid");
//            return jsonXuat.toString();
//        }
        ///

///ko s??? d???ng
//        private String danhSachThanhPho(){
//            String linkJson = "https://countriesnow.space/api/v0.1/countries/population/cities?fbclid=IwAR0cR4DLQN_MYC_rY3HcwbJBEWFvjD1ZxLaW5w_qlBweWiCq6XeKEeoM5Uo";
//            try{
//                Connection.Response res = Jsoup.connect(linkJson)
//                        .ignoreContentType(true)
//                        .method(Connection.Method.GET)
//                        .execute();
//                parse = new JSONParser();
//                JSONArray data = (JSONArray) parse.parse(res.body());
//                
//            }catch(IOException | ParseException e){
//                return "L???i t??? ph??a server.";
//            }
//            return "";
//        }
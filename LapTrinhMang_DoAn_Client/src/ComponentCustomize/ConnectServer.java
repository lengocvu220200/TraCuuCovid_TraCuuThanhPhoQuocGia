package ComponentCustomize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectServer {
    private Socket socket = null;
    BufferedWriter out = null;
    BufferedReader in = null;
    BufferedReader stdIn = null;
    
    public ConnectServer(){
        try {
            socket = new Socket("127.0.0.1",3456);
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ConnectServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public boolean senData(String data){
        try {
            
            
            out.write(data);
            out.newLine();
            out.flush();
            //out.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ConnectServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public String receiveData(){
        try {
            
            String nhan = in.readLine();
            //in.close();
            return nhan;
        } catch (IOException ex) {
            Logger.getLogger(ConnectServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

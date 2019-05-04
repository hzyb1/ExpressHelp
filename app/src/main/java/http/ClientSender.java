package http;

import java.io.OutputStream;
import java.net.Socket;

public class ClientSender {
    Socket sender = null;
    public ClientSender(Socket sender){
        this.sender = sender;
    }

//    private static ClientSender instance;
//    public static ClientSender getInstance(){
//        if(instance==null){
//            synchronized(Client.class){
//                instance = new ClientSender();
//            }
//        }
//        return instance;
//    }
    public void send(){
        try{
  //          sender = new Socket(InetAddress.getLocalHost(),25535);
            while(true){
                OutputStream outputStream = sender.getOutputStream();
                outputStream.write("heart8848".getBytes("utf-8"));
                outputStream.flush();
                Thread.sleep(10000);
            }
        }
        catch(Exception e){
        }
    }


}
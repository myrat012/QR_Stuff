package com.example.new_qr_stuff;

public class Connection {
    private final static String IP = "http://192.168.1.100/";

    public String getShadatnama(){
        return IP + "qr/getUserJsonForStuff.php";
    }

    public static String getIP() {
        return IP;
    }

    public String getGurat(){
        return IP + "qr/getGuratJson.php";
    }
    public String getGuwa(){
        return IP + "qr/getGuwaJson.php";
    }
}

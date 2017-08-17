package ru.startandroid.freeadvertising.http;


public class Config {


    public final static String urlServer = "http://192.168.43.8:8085/";
    public final static String product = "product";
    public final static String register = "register";
    public final static String urlserverjusthtttp = "http://192.168.43.8/";
    public final static String auth = "auth";
    public final static String delet = "productdelete";
    public final static String productwithid = "productwithid";
    public final static String account = "useraccount";
    public final static String upd = "updrek";
    public final static String type = "type";
    public final static String addnewrek = "addnewreklam";

    public static String getUpd() {
        return urlServer + upd;
    }

    public static String getDelet() {
        return urlServer + delet;
    }

    public static String getProductwithid() {
        return urlServer + productwithid;
    }

    public static String getUrlserverjusthtttp() {
        return urlserverjusthtttp;
    }

    public static String getAddnewrek() {
        return urlServer + addnewrek;
    }

    public static String getType() {
        return urlServer + type;
    }

    public static String getAccount() {
        return urlServer + account;
    }

    public static String getAuth() {
        return urlServer + auth;
    }

    public static String getProduct() {
        return urlServer + product;
    }

    public static String getRegister() {
        return urlServer + register;
    }
}
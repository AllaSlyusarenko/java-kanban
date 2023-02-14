package server;

import java.io.IOException;

public class MainKV {

    public static void main(String[] args) throws IOException {
        new KVServer().start();
       // new HttpTaskServer(); //создается и запускается
    }
}

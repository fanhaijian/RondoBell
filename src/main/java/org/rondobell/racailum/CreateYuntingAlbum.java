package org.rondobell.racailum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CreateYuntingAlbum {
    public static void main(String[] args) {
        try {
            BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\fanhj\\Desktop\\grab\\财经149a.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                System.out.println(str);
                System.out.println(str.split("\t")[0]+"=");
                System.out.println(str.split("\t")[1]+"=");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

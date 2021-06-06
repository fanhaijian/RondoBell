package org.rondobell.racailum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class CountYunTingAudioCount {
    public static void main(String[] args) throws Exception {
        Set<String> list = new HashSet<>();
        List<String> listId = new ArrayList<>();

        List<String> listNew = new ArrayList<>();
        List<String> listAll = new ArrayList<>();
        List<String> list7 = new ArrayList<>();
        Map<String,String> map = new HashMap<>();


        FileReader fileReader = new FileReader("C:\\Users\\fanhj\\Downloads\\pay2原始out.txt");
        BufferedReader in = new BufferedReader(fileReader);
        String str;
        while ((str = in.readLine()) != null) {
            String[] infos = str.split("\t");
            list.add(infos[0]);

        }
        fileReader.close();
        in.close();

        int sum = 0;
        fileReader = new FileReader("D:\\project\\kaola-server\\kaola-job\\src\\main\\resources\\yunting_album.txt");
        in = new BufferedReader(fileReader);
        while ((str = in.readLine()) != null) {
            String[] infos = str.split("\t");
            if(list.contains(infos[0])){
                System.out.println(infos[0]);
            }

        }
        fileReader.close();
        in.close();

        System.out.println(sum);

    }


}

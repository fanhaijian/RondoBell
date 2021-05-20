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


        FileReader fileReader = new FileReader("C:\\Users\\fanhj\\Desktop\\专辑数据 (5.7).csv");
        BufferedReader in = new BufferedReader(fileReader);
        String str;
        while ((str = in.readLine()) != null) {
            String[] infos = str.split(",");
                //list.add(infos[0]+"\t"+infos[1]);
              map.put(infos[0], infos[5]);

        }
        fileReader.close();
        in.close();

        int sum = 0;
        fileReader = new FileReader("C:\\Users\\fanhj\\Desktop\\newgrab\\yunting_album_finish_filter_use.txt");
        in = new BufferedReader(fileReader);
        while ((str = in.readLine()) != null) {
            String[] infos = str.split("\t");
            String count = map.get(infos[1]);
            sum+=Integer.valueOf(count);

        }
        fileReader.close();
        in.close();

        System.out.println(sum);

    }


}

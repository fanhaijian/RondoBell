package org.rondobell.racailum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterYunTingPayAlbum {
    public static void main(String[] args) throws Exception {
        Set<String> list = new HashSet<>();
        List<String> listId = new ArrayList<>();

        List<String> listNew = new ArrayList<>();
        List<String> listAll = new ArrayList<>();
        List<String> list7 = new ArrayList<>();


        FileReader fileReader = new FileReader("C:\\Users\\fanhj\\Desktop\\专辑数据 (5.7).csv");
        BufferedReader in = new BufferedReader(fileReader);
        String str;
        while ((str = in.readLine()) != null) {
            String[] infos = str.split(",");
            if("是".equals(infos[6])){
                //list.add(infos[0]+"\t"+infos[1]);
                listId.add(infos[0]);
            //System.out.println(infos[0]+"。");
            }
        }
        fileReader.close();
        in.close();

        System.out.println("size  "+listId.size());

        fileReader = new FileReader("C:\\Users\\fanhj\\Desktop\\newgrab\\tmp.txt");
        in = new BufferedReader(fileReader);
        while ((str = in.readLine()) != null) {
            //String[] infos = str.split("\t");
            str = str.trim();
            //System.out.println(str+"。。");
            if(listId.contains(str)){
                //list.add(str);
                System.out.println(str);
            }else{
                //listNew.add(str);
            }
        }
        fileReader.close();
        in.close();

        /*
        fileReader = new FileReader("C:\\Users\\fanhj\\Desktop\\newgrab\\yunting_album_finish.txt");
        in = new BufferedReader(fileReader);
        while ((str = in.readLine()) != null) {
            String[] infos = str.split("\t");
            if(listId.contains(infos[1])){
                list.add(str);
            }else{
                listAll.add(str);
            }
        }
        fileReader.close();
        in.close();


        fileReader = new FileReader("C:\\Users\\fanhj\\Desktop\\newgrab\\yunting_album7.txt");
        in = new BufferedReader(fileReader);
        while ((str = in.readLine()) != null) {
            String[] infos = str.split("\t");
            if(listId.contains(infos[1])){
                list.add(str);
            }else{
                list7.add(str);
            }
        }
        fileReader.close();
        in.close();

        for(String line:list){
            System.out.println(line);
        }
        System.out.println("===============================");
        for(String line:listNew){
            System.out.println(line);
        }
        System.out.println("===============================");

        for(String line:listAll){
            System.out.println(line);
        }
        System.out.println("===============================");

        for(String line:list7){
            System.out.println(line);
        }*/
    }


}

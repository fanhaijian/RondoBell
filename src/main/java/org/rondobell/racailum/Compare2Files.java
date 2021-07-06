package org.rondobell.racailum;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Compare2Files {
    public static void main(String[] args) throws Exception {
        Set<String> list = new HashSet<>();
        List<String> listId = new ArrayList<>();

        List<String> listNew = new ArrayList<>();
        List<String> listAll = new ArrayList<>();
        List<String> list7 = new ArrayList<>();
        Map<String,String> map = new HashMap<>();


        FileReader fileReader = new FileReader("D:\\work\\全部替换源\\二次-电台播放地址.csv");
        BufferedReader in = new BufferedReader(fileReader);
        String str;
        while ((str = in.readLine()) != null) {
            String[] ary = str.split(",");
            String yunName = ary[1].trim();
            String url = ary[2].trim();
            map.put(yunName,url);
        }
        fileReader.close();
        in.close();

        //System.out.println("size  "+listId.size());

        fileReader = new FileReader("D:\\work\\全部替换源\\重叠的排除第一次比对的即需要替换的.csv");
        in = new BufferedReader(fileReader);
        int count2 = 0;
        while ((str = in.readLine()) != null) {
            String[] ary = str.split(",");
            String yName = ary[3].trim();
            String tId = ary[1].trim();
            String tName = ary[2].trim();
            String stream = map.get(yName);
            if(StringUtils.isNotBlank(stream)){
                if(stream.endsWith("type=1")){
                    stream = Stream2Sign.addSign(stream);
                }
                //System.out.println("\"id\":"+tId+",\"pullUrl\":[\""+newUrl+"\"]");
                //System.out.println(tId+","+tName+","+stream);
                System.out.println("1,2,"+tId+",4,"+stream);
                //System.out.println(tId);
                count2++;
            }

        }
        fileReader.close();
        in.close();

        System.out.println(count2);
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

package org.rondobell.racailum;

import org.apache.ibatis.session.SqlSession;
import org.rondobell.racailum.base.dao.MzMapper;
import org.rondobell.racailum.base.dao.SqlSessionFactoryHolder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateYuntingAlbum {

    public static void main(String[] args) throws IOException {
        List<String> list = new ArrayList<>();
        List<String> list7 = new ArrayList<>();
        List<String> listNew = new ArrayList<>();
        List<String> listTimeLineAll = new ArrayList<>();
        List<String> list2 = new ArrayList<>();


        SqlSession session = SqlSessionFactoryHolder.getSession();
        MzMapper mapper = session.getMapper(MzMapper.class);
        List<Map<String,Object>> catalogList = mapper.queryAllCatalog();
        Map<String,String> catalogMap = new HashMap<>();
        for(Map<String,Object> map: catalogList){
            String id = String.valueOf(map.get("id"));
            String name = (String)map.get("name");
            catalogMap.put(name,id);
        }

        FileReader fileReader = new FileReader("D:\\project\\kaola-server\\kaola-job\\src\\main\\resources\\yunting_album.txt");
        BufferedReader in = new BufferedReader(fileReader);
        String str;
        while ((str = in.readLine()) != null) {
            list.add(str.split("\t")[1]);
        }
        fileReader.close();
        in.close();

        fileReader = new FileReader("D:\\project\\kaola-server\\kaola-job\\src\\main\\resources\\yunting_album_finish.txt");
        in = new BufferedReader(fileReader);
        while ((str = in.readLine()) != null) {
            list.add(str.split("\t")[1]);
        }
        fileReader.close();
        in.close();

        fileReader = new FileReader("C:\\Users\\fanhj\\Desktop\\newAllTingban.out");
        in = new BufferedReader(fileReader);
        int i=0;
        while ((str = in.readLine()) != null) {
            String[] info = str.split("\t");
            String id = info[0];
            String oid = info[1];


            String name = info[2];
            String cid = info[3];

            String finish = info[4];
            String time = info[5];

            if(finish.equals("是")){
                //完结全爪
                listTimeLineAll.add(id+"\t"+oid+"\t"+name+"\t"+cid);
            }else{
                //add实时
                listNew.add(id+"\t"+oid+"\t"+name+"\t"+cid);
                if(time.equals("弱时效")){
                    //完结全爪
                    listTimeLineAll.add(id+"\t"+oid+"\t"+name+"\t"+cid);
                }else if(time.equals("中时效")){
                    //爪7天
                    list7.add(id+"\t"+oid+"\t"+name+"\t"+cid);
                }
            }
        }
        fileReader.close();
        in.close();
        for(String s:listTimeLineAll){
            System.out.println(s);
        }
        System.out.println("============================");
        for(String s:listNew){
            System.out.println(s);
        }
        System.out.println("============================");
        for(String s:list7){
            System.out.println(s);
        }
        System.out.println("============================");
    }

}

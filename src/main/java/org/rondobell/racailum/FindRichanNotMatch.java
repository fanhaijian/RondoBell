package org.rondobell.racailum;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.rondobell.racailum.base.dao.MzMapper;
import org.rondobell.racailum.base.dao.SqlSessionFactoryHolder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FindRichanNotMatch {
    public static void main(String[] args) throws Exception {
        SqlSession session = null;
        Set<String> list = new HashSet<>();
        Set<String> list2 = new HashSet<>();

        FileReader fileReader = new FileReader("C:\\Users\\fanhj\\Downloads\\da_id.txt");
        BufferedReader in = new BufferedReader(fileReader);
        String line;
        int em = 0;
        int fi = 0;
        Map<String,Integer> cMap = new HashMap<>();
        while ((line = in.readLine()) != null) {
            //String da = line.trim();
            list.add(line);

        }
        fileReader.close();
        in.close();


        fileReader = new FileReader("C:\\Users\\fanhj\\Desktop\\richan_05.txt");
        in = new BufferedReader(fileReader);
        while ((line = in.readLine()) != null) {
            //String da = line.trim();
            list2.add(line);

        }
        fileReader.close();
        in.close();

        Set<String> set = new HashSet<String>();
        set.addAll(list2);
        set.removeAll(list);
        for(String cha:set){
            System.out.println(cha);

        }



    }
}

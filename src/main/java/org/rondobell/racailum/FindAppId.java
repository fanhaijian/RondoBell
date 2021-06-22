package org.rondobell.racailum;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.rondobell.racailum.base.dao.MzMapper;
import org.rondobell.racailum.base.dao.SqlSessionFactoryHolder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FindAppId {
    public static void main(String[] args) throws Exception {
        SqlSession session = null;
        Set<String> list = new HashSet<>();
        //FileReader fileReader = new FileReader("C:\\Users\\fanhj\\Downloads\\da_id.txt");
        FileReader fileReader = new FileReader("D:\\yunting.txt");
        BufferedReader in = new BufferedReader(fileReader);
        String line;
        int em = 0;
        int fi = 0;
        Map<String,Integer> cMap = new HashMap<>();
        while ((line = in.readLine()) != null) {
            String da = line.trim();
            session = SqlSessionFactoryHolder.getSession();
            MzMapper mapper = session.getMapper(MzMapper.class);
            String app = mapper.getAppId(da);
            if(StringUtils.isEmpty(app)){
                em++;
            }else{
                fi++;
                if(list.add(app)){
                    System.out.println(app);
                }
                Integer count = cMap.get(app);
                if(count==null){
                    count=1;
                }else{
                    count+=1;
                }
                cMap.put(app,count);
            }
            session.close();
            Thread.sleep(11);

        }
        fileReader.close();
        in.close();
        System.out.println("em "+em);
        System.out.println("fi "+fi);
        for(Map.Entry<String,Integer> entry:cMap.entrySet()){
            System.out.println(entry.getKey()+"\t"+entry.getValue());
        }


    }
}

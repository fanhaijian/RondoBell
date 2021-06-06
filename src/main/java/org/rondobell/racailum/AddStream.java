package org.rondobell.racailum;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.rondobell.racailum.base.dao.MzMapper;
import org.rondobell.racailum.base.dao.SqlSessionFactoryHolder;
import org.rondobell.racailum.base.dto.BroadcastStream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class AddStream {
    public static void main(String[] args) throws Exception {
        SqlSession session = null;
        //FileReader fileReader = new FileReader("C:\\Users\\fanhj\\Desktop\\stream.csv");
        FileReader fileReader = new FileReader("/usr/local/task/stream.csv");

        BufferedReader in = new BufferedReader(fileReader);
        String line;
        int em = 0;
        int fi = 0;
        Map<String,Integer> cMap = new HashMap<>();
        while ((line = in.readLine()) != null) {
            String[] da = line.split(",");
            String id = da[2];
            String stream = da[4].trim();
            //System.out.println(id+"\t"+stream);
            session = SqlSessionFactoryHolder.getSession();
            MzMapper mapper = session.getMapper(MzMapper.class);
            List<BroadcastStream> list = mapper.queryStream(id);
            session.close();
            System.out.println(list.size());
            boolean find = false;
            for(BroadcastStream bs:list){
                if(stream.equals(bs.getStream().trim())){
                    if(bs.getStatus().intValue()==0){
                        //System.out.println("update\t"+bs.getBroadcast_id()+"\t"+stream);
                        session = SqlSessionFactoryHolder.getSession();
                        mapper = session.getMapper(MzMapper.class);
                        mapper.updateStream(bs.getId());
                        session.close();
                        System.out.println(id);
                    }
                    find = true;
                    break;
                }
            }
            if(!find){
                //System.out.println("insert\t"+id+"\t"+stream);
                session = SqlSessionFactoryHolder.getSession();
                mapper = session.getMapper(MzMapper.class);
                mapper.insetStream(id,stream,1,0,1,"admin",new Date());
                session.close();
                System.out.println(id);
            }

        }
        fileReader.close();
        in.close();


    }
}

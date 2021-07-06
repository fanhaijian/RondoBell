package org.rondobell.racailum;

import org.apache.ibatis.session.SqlSession;
import org.rondobell.racailum.base.dao.MzMapper;
import org.rondobell.racailum.base.dao.SqlSessionFactoryHolder;
import org.rondobell.racailum.base.dto.BroadcastStream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckStream {
    public static void main(String[] args) throws Exception {
        SqlSession session = null;
        //FileReader fileReader = new FileReader("C:\\Users\\fanhj\\Desktop\\stream.csv");
        FileReader fileReader = new FileReader("D:\\work\\全部替换源\\匹配上的需要替换的.csv");

        BufferedReader in = new BufferedReader(fileReader);
        String line;
        int em = 0;
        int fi = 0;
        Map<String,Integer> cMap = new HashMap<>();
        while ((line = in.readLine()) != null) {
            String[] da = line.split(",");
            String id = da[0];
            //String stream = da[4].trim();
            //System.out.println(id+"\t"+stream);
            session = SqlSessionFactoryHolder.getSession();
            MzMapper mapper = session.getMapper(MzMapper.class);
            List<BroadcastStream> list = mapper.queryStream(id);
            session.close();
            System.out.println("");
            System.out.println(id+":  "+list.size());
            boolean find = false;
            for(BroadcastStream bs:list){
                System.out.println(bs.getStatus()+"\t"+bs.getStream());
            }


        }
        fileReader.close();
        in.close();


    }
}

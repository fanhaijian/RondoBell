package org.rondobell.racailum;

import org.apache.ibatis.session.SqlSession;
import org.rondobell.racailum.base.dao.MzMapper;
import org.rondobell.racailum.base.dao.SqlSessionFactoryHolder;

import java.util.ArrayList;
import java.util.List;

public class UpdateSource {
    public static void main(String[] args) throws Exception {
        List<String> listId = new ArrayList<>();
        SqlSession session = null;

        //FileReader fileReader = new FileReader("C:\\Users\\fanhj\\Desktop\\newgrab\\pay.txt");
        //FileReader fileReader = new FileReader("/usr/local/task/pay.txt");

        //BufferedReader in = new BufferedReader(fileReader);
        //String str;
        //while ((str = in.readLine()) != null) {
        //String[] infos = str.split("\t");
        session = SqlSessionFactoryHolder.getSession();
        MzMapper mapper = session.getMapper(MzMapper.class);

        int count = mapper.updateSource();
        //System.out.println("update count "+count);
        session.close();
        //Thread.sleep(1111);
        //}
        //fileReader.close();
        //in.close();





        /*session = SqlSessionFactoryHolder.getSession();
        mapper = session.getMapper(MzMapper.class);
        mapper.simpleUpdate2();
        session.close();*/

    }
}

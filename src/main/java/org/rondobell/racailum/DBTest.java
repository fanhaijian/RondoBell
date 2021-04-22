package org.rondobell.racailum;

import org.apache.ibatis.session.SqlSession;
import org.rondobell.racailum.base.dao.MzMapper;
import org.rondobell.racailum.base.dao.SqlSessionFactoryHolder;

public class DBTest {
    public static void main(String[] args) {
        SqlSession session = SqlSessionFactoryHolder.getSession();
        MzMapper mapper = session.getMapper(MzMapper.class);

        mapper.simpleUpdate();
        session.close();


        session = SqlSessionFactoryHolder.getSession();
        mapper = session.getMapper(MzMapper.class);
        mapper.simpleUpdate2();
        session.close();

    }
}

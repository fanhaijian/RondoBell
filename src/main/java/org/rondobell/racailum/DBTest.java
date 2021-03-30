package org.rondobell.racailum;

import org.apache.ibatis.session.SqlSession;
import org.rondobell.racailum.base.dao.MzMapper;
import org.rondobell.racailum.base.dao.SqlSessionFactoryHolder;

public class DBTest {
    public static void main(String[] args) {
        SqlSession session = SqlSessionFactoryHolder.getSession();
        MzMapper mapper = session.getMapper(MzMapper.class);

        mapper.simpleUpdate(1100002156589L);

        session.close();


        /*session = SqlSessionFactoryHolder.getSession();
        mapper = session.getMapper(MzMapper.class);
        mapper.simpleUpdate(1100002156580l);
        session.close();


        session = SqlSessionFactoryHolder.getSession();
        mapper = session.getMapper(MzMapper.class);
        mapper.simpleUpdate(1100002156581l);
        session.close();

        session = SqlSessionFactoryHolder.getSession();
        mapper = session.getMapper(MzMapper.class);
        mapper.simpleUpdate(1100002156582l);
        session.close();

        session = SqlSessionFactoryHolder.getSession();
        mapper = session.getMapper(MzMapper.class);
        mapper.simpleUpdate(1100002156583l);
        session.close();

        session = SqlSessionFactoryHolder.getSession();
        mapper = session.getMapper(MzMapper.class);
        mapper.simpleUpdate(1100002156584l);
        session.close();

        session = SqlSessionFactoryHolder.getSession();
        mapper = session.getMapper(MzMapper.class);
        mapper.simpleUpdate(1100002156585l);
        session.close();

        session = SqlSessionFactoryHolder.getSession();
        mapper = session.getMapper(MzMapper.class);
        mapper.simpleUpdate(1100002156586l);
        session.close();

        session = SqlSessionFactoryHolder.getSession();
        mapper = session.getMapper(MzMapper.class);
        mapper.simpleUpdate(1100002156587l);
        session.close();

        session = SqlSessionFactoryHolder.getSession();
        mapper = session.getMapper(MzMapper.class);
        mapper.simpleUpdate(1100002156588l);
        session.close();

        session = SqlSessionFactoryHolder.getSession();
        mapper = session.getMapper(MzMapper.class);
        mapper.simpleUpdate(1100002156589l);
        session.close();*/

    }
}

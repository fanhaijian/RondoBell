package org.rondobell.racailum;

import org.apache.ibatis.session.SqlSession;
import org.rondobell.racailum.base.dao.MzMapper;
import org.rondobell.racailum.base.dao.SqlSessionFactoryHolder;

public class UpdateNumber {
    public static void main(String[] args) {
        SqlSession session = null;
        session = SqlSessionFactoryHolder.getSession();
        MzMapper mapper = session.getMapper(MzMapper.class);

        int count = mapper.updateNumber();
        System.out.println("update count "+count);
        session.close();
    }
}

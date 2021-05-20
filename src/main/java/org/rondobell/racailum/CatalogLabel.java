package org.rondobell.racailum;

import org.apache.ibatis.session.SqlSession;
import org.rondobell.racailum.base.dao.MzMapper;
import org.rondobell.racailum.base.dao.SqlSessionFactoryHolder;
import org.rondobell.racailum.base.dto.Catalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogLabel {
    public static void main(String[] args) {
        SqlSession session = null;
        session = SqlSessionFactoryHolder.getSession();
        MzMapper mapper = session.getMapper(MzMapper.class);

        List<Map<String,Object>> catalogList = mapper.queryAllCatalog();
        session.close();

        Map<String,String> catalogMap = new HashMap<>();
        for(Map<String,Object> map: catalogList){
            Integer id = Integer.valueOf((Integer)map.get("id"));
            String name = (String)map.get("name");
            System.out.println(name+" 频道");

            session = SqlSessionFactoryHolder.getSession();
            mapper = session.getMapper(MzMapper.class);

            List<Map<String,Object>> labelList = mapper.queryLabelByCatalogId(id);
            session.close();

            for(Map<String,Object> labelMap: labelList){
                Integer id1 = Integer.valueOf((Integer)labelMap.get("id"));
                String name1 = (String)labelMap.get("name");
                System.out.print("\t"+name1+"：");
                session = SqlSessionFactoryHolder.getSession();
                mapper = session.getMapper(MzMapper.class);

                List<String> values = mapper.queryValueByLabelId(id1);
                session.close();
                for(String name2:values){
                    System.out.print(name2+",");
                }
                System.out.println("");
            }
        }
    }
}

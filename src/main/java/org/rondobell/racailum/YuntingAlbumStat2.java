package org.rondobell.racailum;

import org.apache.ibatis.session.SqlSession;
import org.rondobell.racailum.base.dao.MzMapper;
import org.rondobell.racailum.base.dao.SqlSessionFactoryHolder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class YuntingAlbumStat2 {
    public static void main(String[] args) {
        try {
            BufferedReader in = new BufferedReader(new FileReader("/usr/local/task/yunting_album.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                String[] arr = str.split("\t");
                Long albumId = Long.valueOf(arr[0]);
                String albumName = arr[2];
                Integer cat = Integer.parseInt(arr[3].trim());
                SqlSession session = SqlSessionFactoryHolder.getSession();
                MzMapper mapper = session.getMapper(MzMapper.class);

                int count = mapper.conutUpdateYes(albumId);
                session.close();

                //还要更新文本文件！！！

                session = SqlSessionFactoryHolder.getSession();
                mapper = session.getMapper(MzMapper.class);
                int countAll = mapper.conutUpdateAll(albumId);
                session.close();
                session = SqlSessionFactoryHolder.getSession();
                mapper = session.getMapper(MzMapper.class);
                String catName = mapper.getCatName(cat);
                session.close();
                System.out.println(albumName+"\t"+catName+"\t总更新期数\t"+countAll+"\t昨日更新期数\t"+count);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

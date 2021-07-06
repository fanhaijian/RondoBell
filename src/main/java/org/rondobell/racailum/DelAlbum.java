package org.rondobell.racailum;

import org.apache.ibatis.session.SqlSession;
import org.rondobell.racailum.base.dao.MzMapper;
import org.rondobell.racailum.base.dao.SqlSessionFactoryHolder;
import org.rondobell.racailum.base.redis.RedisUCloud;

import java.util.ArrayList;
import java.util.List;

public class DelAlbum {
    public static void main(String[] args) throws Exception {
        List<Long> listId = new ArrayList<>();
        SqlSession session = null;
        RedisUCloud redis = new RedisUCloud();

        listId.add(1100002157285L);
        listId.add(1100002157295L);
        listId.add(1100002157287L);
        listId.add(1100002157288L);
        listId.add(1100002157286L);
        listId.add(1100002157301L);
        listId.add(1100002157302L);
        listId.add(1100002157303L);
        listId.add(1100002161892L);
        listId.add(1100002161869L);
        listId.add(1100002161870L);
        listId.add(1100002148756L);
        listId.add(1100002155420L);
        listId.add(1100002152222L);
        listId.add(1100002150358L);
        listId.add(1100002145946L);
        listId.add(1100002145038L);
        listId.add(1100002144588L);
        listId.add(1100002144582L);
        listId.add(1100002144752L);
        listId.add(1100002161905L);
        listId.add(1100002161841L);
        listId.add(1100002161843L);
        listId.add(1100002161844L);
        listId.add(1100002156557L);
        listId.add(1100002155882L);
        listId.add(1100002154044L);
        listId.add(1100002153988L);
        listId.add(1100002154850L);
        listId.add(1100002154851L);
        listId.add(1100002154852L);
        listId.add(1100002161857L);
        listId.add(1100002161856L);
        listId.add(1100002161855L);
        listId.add(1100002161854L);
        listId.add(1100002161858L);
        listId.add(1100002122377L);
        listId.add(1100002122378L);
        listId.add(1100002122379L);
        listId.add(1100002156008L);
        listId.add(1100002156006L);
        listId.add(1100002122381L);
        listId.add(1100002135651L);
        listId.add(1100002122380L);
        listId.add(1100002156065L);
        listId.add(1100002152680L);
        listId.add(1100002150411L);
        listId.add(1100002149760L);
        listId.add(1100002149045L);
        listId.add(1100002141680L);
        listId.add(1100002141678L);
        listId.add(1100002125008L);
        listId.add(1100002122806L);
        listId.add(1100002122804L);
        listId.add(1100002134916L);
        listId.add(1100002152281L);
        listId.add(1100002155711L);
        listId.add(1100002155305L);
        listId.add(1100002153176L);
        listId.add(1100002153175L);
        listId.add(1100002153174L);
        listId.add(1100002153170L);
        listId.add(1100002153121L);
        listId.add(1100002153074L);
        listId.add(1100002153048L);
        listId.add(1100002153045L);
        listId.add(1100002152890L);
        listId.add(1100002152699L);
        listId.add(1100002152029L);
        listId.add(1100002151038L);
        listId.add(1100002150408L);
        listId.add(1100002150407L);
        listId.add(1100002150406L);
        listId.add(1100002150405L);
        listId.add(1100002150404L);
        listId.add(1100002150084L);
        listId.add(1100002150083L);
        listId.add(1100002150082L);
        listId.add(1100002150081L);
        listId.add(1100002150061L);
        listId.add(1100002144363L);
        listId.add(1100002144362L);
        listId.add(1100002143957L);
        listId.add(1100002141681L);
        listId.add(1100002141679L);
        listId.add(1100002133720L);
        listId.add(1100002122582L);
        listId.add(1100002122581L);
        listId.add(1100002122580L);
        listId.add(1100002122579L);
        listId.add(1100002122578L);
        listId.add(1100002106954L);
        listId.add(1100002106953L);
        listId.add(1100002106951L);
        listId.add(1100002106948L);
        listId.add(1100002106933L);
        listId.add(1100002153720L);
        listId.add(1100002152675L);
        listId.add(1100002152674L);
        listId.add(1100002152668L);
        //FileReader fileReader = new FileReader("C:\\Users\\fanhj\\Desktop\\newgrab\\pay.txt");
        //FileReader fileReader = new FileReader("/usr/local/task/pay.txt");

        //BufferedReader in = new BufferedReader(fileReader);
        //String str;
        //while ((str = in.readLine()) != null) {
        //String[] infos = str.split("\t");
        session = SqlSessionFactoryHolder.getSession();
        MzMapper mapper = session.getMapper(MzMapper.class);

        List<Long> albums = mapper.queryToDelAlbum();
        //System.out.println("update count "+count);
        session.close();
        System.out.println(albums.size());
        for(Long albumId: albums){
            if(listId.contains(albumId)){
                System.out.println("passssss "+albumId);
                continue;
            }
            session = SqlSessionFactoryHolder.getSession();
            mapper = session.getMapper(MzMapper.class);
            mapper.delAlbum(albumId);
            mapper.delAlbumAllAudio(albumId);
            //System.out.println("update count "+count);
            session.close();
            String sleep = redis.get("ytgrv1:sl");
            long sleepLong = 2000L;
            try {
                sleepLong = Long.parseLong(sleep);
            } catch (Exception e) {
                sleepLong = 2000L;
            }
            System.out.println(albumId+"    sleep "+sleepLong);
            Thread.sleep(sleepLong);
        }
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

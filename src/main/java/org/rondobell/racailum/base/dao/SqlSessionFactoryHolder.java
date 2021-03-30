package org.rondobell.racailum.base.dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;

/**
 * Created by Chung.
 * Usage: 初始化SessionFactory
 * Description: 复用sqlSession
 * Create dateTime: 17/7/10
 */
public class SqlSessionFactoryHolder {

	public static SqlSessionFactory sessionFactory;

	private static final Logger logger = LoggerFactory.getLogger(SqlSessionFactory.class);

	static {
		try {
			//使用MyBatis提供的Resources类加载mybatis的配置文件
			Reader reader = Resources.getResourceAsReader( "mybatis.xml" );
			//构建sqlSession的工厂
			sessionFactory = new SqlSessionFactoryBuilder().build( reader );
		} catch ( Exception e ) {
			logger.error( e.getMessage(), e );
			e.printStackTrace();
		}
	}

	//创建能执行映射文件中sql的sqlSession
	public static SqlSession getSession() {
		return sessionFactory.openSession(true);
	}
	
	public static void main (String[] args) {
		//MzMapper mzDaoMapper = DaoHelper.getSession().getMapper(MzMapper.class);
		//List<Map<String,Object>> audioIds = mzDaoMapper.findAudioIdsByAlbumId(1100000000078L);
	
	}
}

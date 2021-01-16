package org.rondobell.racailum;

import org.apache.ibatis.session.SqlSession;
import org.rondobell.racailum.base.dao.MzMapper;
import org.rondobell.racailum.base.dao.SqlSessionFactoryHolder;
import org.rondobell.racailum.base.dto.BroadcastAdditionDto;
import org.rondobell.racailum.base.redis.RedisUCloud;

import java.util.List;

public class BroadcastLikeRedisSet {


	public static void main(String[] args) {



		RedisUCloud redis = new RedisUCloud();

		SqlSession session = SqlSessionFactoryHolder.getSession();
		MzMapper mapper = session.getMapper(MzMapper.class);
		List<Long> idList = mapper.queryBroadcastId();
		session.close();

		for(Long id : idList){

			session = SqlSessionFactoryHolder.getSession();
			mapper = session.getMapper(MzMapper.class);
			BroadcastAdditionDto broadcast = mapper.getBroadcastAddition(Long.valueOf(id));
			session.close();
			if (broadcast == null) {
				broadcast = new BroadcastAdditionDto();
				broadcast.setId(Long.valueOf(id));
				broadcast.setLikedNum(0l);
			}
			//redis.set("broadcast_likednum_"+id, JsonUtil.toJsonString(broadcast), 60*60);
			System.out.println("set ok "+redis.get("broadcast_likednum_"+id));
			//System.out.println(JsonUtil.toJsonString(broadcast));
			//System.out.println(JsonUtil.toObject(JsonUtil.toJsonString(broadcast), BroadcastAdditionDto.class));
		}

	}
}

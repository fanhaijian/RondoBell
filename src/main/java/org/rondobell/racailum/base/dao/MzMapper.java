package org.rondobell.racailum.base.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.rondobell.racailum.base.dto.BroadcastAdditionDto;
import org.rondobell.racailum.base.dto.ConvertInfo;

import java.util.Date;
import java.util.List;

public interface MzMapper {

	@Select("select oid from user.user_following where followed_id = #{albumId} and type = 0 and update_time > #{updateTime}")
	List<String> queryUidsByAlbumId(@Param("albumId") Long albumId, @Param("updateTime") Long updateTime);

	@Select("select oid from user.open_following where followed_id = #{albumId} and type = 0 and update_time > #{updateTime}")
	List<String> queryOpenIdsByAlbumId(@Param("albumId") Long albumId, @Param("updateTime") Long updateTime);

	@Select("select kradio_uid from user.kradio_following where followed_id = #{albumId} and type = 0 and status =0 and update_time > #{updateTime}")
	List<String> queryKradioIdsByAlbumId(@Param("albumId") Long albumId, @Param("updateTime") Date updateTime);

	@Select("select id as audioId, catalog_id catalogId, file_path filePath, create_date createDate from media_resources.tb_album_audio where status <> 5 and status <> 0 and file_path like 'http://image%3' and id<#{id} order by id desc limit #{size}")
	List<ConvertInfo> queryAudioConvertInfo(@Param("id") Long id, @Param("size") int size);

	@Select("select id ,liked_num as likedNum from media_resources.tb_broadcast_addition where id = #{id}")
	BroadcastAdditionDto getBroadcastAddition(@Param("id") Long id);

	@Select("select id from media_resources.tb_broadcast")
	List<Long> queryBroadcastId();
}

package org.rondobell.racailum.base.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

	@Select("select id as audioId, catalog_id catalogId, file_path filePath, create_date createDate from media_resources.tb_album_audio where status <> 5 and status <> 0 and file_path like 'http://image%3' and id<#{id} and album_id not in (1100002156558,1100002156579,1100002156580,1100002156581,1100002156582,1100002156583,1100002156584,1100002156585,1100002156586,1100002156587,1100002156588,1100002156589) order by id desc limit #{size}")
	List<ConvertInfo> queryAudioConvertInfo(@Param("id") Long id, @Param("size") int size);

	@Select("select id ,liked_num as likedNum from media_resources.tb_broadcast_addition where id = #{id}")
	BroadcastAdditionDto getBroadcastAddition(@Param("id") Long id);

	@Select("select id from media_resources.tb_broadcast")
	List<Long> queryBroadcastId();

	@Update("delete from media_resources.tb_album where id in (1100002156670,1100002156671,1100002156675,1100002156676,1100002156678,1100002156683)")
    void simpleUpdate();

	@Update("delete from media_resources.tb_album_application_scope where album_id in (1100002156670,1100002156671,1100002156675,1100002156676,1100002156678,1100002156683)")
	void simpleUpdate2();

	@Select("SELECT count(id) FROM media_resources.tb_album_audio WHERE STATUS = 2 AND album_id = #{albumId}  AND valid_startdate BETWEEN '2021-04-24 00:00:00' AND '2021-04-25 00:00:00' ")
	Integer conutUpdateYes(Long albumId);

	@Select("SELECT count(id) FROM media_resources.tb_album_audio WHERE STATUS = 2 AND album_id = #{albumId}")
	Integer conutUpdateAll(Long albumId);

	@Select("select name from media_resources.tb_catalog where id = #{cat}")
	String getCatName(Integer cat);
}

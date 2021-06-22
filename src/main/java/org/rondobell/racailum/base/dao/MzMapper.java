package org.rondobell.racailum.base.dao;

import org.apache.ibatis.annotations.*;
import org.rondobell.racailum.base.dto.BroadcastAdditionDto;
import org.rondobell.racailum.base.dto.BroadcastStream;
import org.rondobell.racailum.base.dto.ConvertInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

	@Update("update media_resources.tb_album set source=34,uploader_id=15798 where id in (1100002161901,1100002161902,1100002161903)")
    int updateSource();

	@Update("update media_resources.tb_album set status=0 where id = #{id}")
	Integer simpleUpdate2(Long id);

	@Select("SELECT count(id) FROM media_resources.tb_album_audio WHERE STATUS = 2 AND album_id = #{albumId}  AND valid_startdate BETWEEN '2021-04-29 00:00:00' AND '2021-04-30 00:00:00' ")
	Integer conutUpdateYes(Long albumId);

	@Select("SELECT count(id) FROM media_resources.tb_album_audio WHERE STATUS = 2 AND album_id = #{albumId}")
	Integer conutUpdateAll(Long albumId);

	@Select("select name from media_resources.tb_catalog where id = #{cat}")
	String getCatName(Integer cat);

	@Select("select id,name from media_resources.tb_catalog")
	List<Map<String, Object>> queryAllCatalog();

	@Select("select id as audioId, catalog_id catalogId, file_path filePath from media_resources.tb_album_audio where status=2 and id> 1000001045508 and album_id = #{l}")
    List<ConvertInfo> queryAudioByAlbumId(Long l);

	@Select("SELECT id,name FROM media_resources.tb_label WHERE id in (select label_id from media_resources.lk_label_catalog where catalog_id = #{id})")
	List<Map<String, Object>> queryLabelByCatalogId(Integer id);

	@Select("SELECT name FROM media_resources.tb_label_value WHERE label_id=#{id}")
	List<String> queryValueByLabelId(Integer id);

	@Delete("update media_resources.tb_album_audio set status = 5 where id=#{id}")
	int deleteAudio(Long id);

	@Update("update user.open_install set car_type=#{carType} where device_id=#{deviceId}")
	int updateCarType(@Param("carType") String carType, @Param("deviceId") String deviceId);

	@Update("update media_resources.tb_album_audio set number = number-5 where number>50 and album_id=1100002156581 and id not in (1000026630749,1000026630750,1000026630751,1000026630752,1000026630753)")
	int updateNumber();

	@Select("select id from media_resources.tb_album WHERE SOURCE = 34 and status = 1")
	List<Long> queryYunIds();

	@Select("select appid from user.open_install where device_id=#{da}")
	String getAppId(String da);

	@Select("select id,broadcast_id,stream,status from media_resources.tb_broadcast_stream where broadcast_id = #{id}")
    List<BroadcastStream> queryStream(String id);

	@Update("update media_resources.tb_broadcast_stream set status = 1 where id = #{id}")
	void updateStream(Integer id);

	@Insert("insert into media_resources.tb_broadcast_stream (broadcast_id,stream,status,current_use,createby,creater_name,create_date) values " +
			"(#{bid}, #{stream}, #{status}, #{use}, #{by}, #{name}, #{date})")
	void insetStream(@Param("bid") String bid, @Param("stream") String stream, @Param("status") int status, @Param("use") int use, @Param("by") int by, @Param("name") String name, @Param("date") Date date);
}

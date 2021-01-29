package org.rondobell.racailum;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;

import java.io.File;

public class UploadTest {
	public static void main(String[] args) throws UfileServerException, UfileClientException {
		final String PRIVATE_KEY = "259c7662-e486-4907-bd4c-5aa784af9e4d";
		final String PUBLIC_KEY = "TOKEN_2c851fd8-c420-44f5-afa1-e1c7fa6a4855";

		final String bucketName = "zth-audio";


		ObjectAuthorization OBJECT_AUTHORIZER = new UfileObjectLocalAuthorization(
				PUBLIC_KEY, PRIVATE_KEY);

		ObjectConfig config = null;
		if (args.length>0) {
			config = new ObjectConfig("zth-audio.ufile.cn-north-04.ucloud.cn");
		}
		//ObjectConfig config = new ObjectConfig("cn-bj","ufileos.com");

		System.out.println(config.getCustomHost());


		File file = new File("test1.txt");

		PutObjectResultBean response = UfileClient.object(OBJECT_AUTHORIZER, config)
				.putObject(file, "text/plain")
				.nameAs("/test/test1.txt")
				.toBucket(bucketName)
				/**
				 * 是否上传校验MD5, Default = true
				 */
				//  .withVerifyMd5(false)
				/**
				 * 指定progress callback的间隔, Default = 每秒回调
				 */
				//  .withProgressConfig(ProgressConfig.callbackWithPercent(10))
				/**
				 * 配置进度监听
				 */
				/*.setOnProgressListener(new OnProgressListener() {
					@Override
					public void onProgress(long bytesWritten, long contentLength) {
						System.out.println("bytesWritten "+bytesWritten+" ,contentLength "+contentLength);

					}
				})*/
				.execute();
		System.out.println(response);
	}
}

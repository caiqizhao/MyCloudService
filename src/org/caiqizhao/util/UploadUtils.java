package org.caiqizhao.util;

import java.util.UUID;

/***
 * 文件上传的工具类
 * @author caiqizhao
 *
 */
public class UploadUtils {
	/**
	 * 生成一个唯一的文件名：
	 */
	public static String getUUIDFileName(String fileName) {
		//获取扩展名
		int idx = fileName.lastIndexOf(".");
		String extention = fileName.substring(idx);
		String uuidFileName = UUID.randomUUID().toString().replace("-", "")+extention;
		return uuidFileName;
	}
}

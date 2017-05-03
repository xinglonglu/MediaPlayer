package com.webapp.dao;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;

import com.webapp.entity.Media;
import com.webapp.util.DBConn;

public abstract class MediaDao {
	
	public static Connection conn = null;
	public static QueryRunner qRunner = null;

	/**
	 * 视频转码
	 * 
	 * @param ffmpegPath
	 *            转码工具的存放路径
	 * @param upFilePath
	 *            用于指定要转换格式的文件,要截图的视频源文件
	 * @param codcFilePath
	 *            格式转换后的的文件保存路径
	 * @param mediaPicPath
	 *            截图保存路径
	 * @return
	 * @throws Exception
	 */
	public boolean executeCodecs(String ffmpegPath, String upFilePath,
			String codcFilePath, String mediaPicPath) throws Exception {
		return false;
	}

	/**
	 * 保存文件
	 * 
	 * @param media
	 * @return
	 * @throws Exception
	 */
	public boolean saveMedia(Media media) throws Exception {
		return false;
	}

	/**
	 * 查询本地库中所有记录的数目
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getAllMediaCount() throws Exception {
		return 0;
	}

	/**
	 * 带分页的查询
	 * 
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	public List<Media> queryALlMedia(int firstResult, int maxResult)
			throws Exception {
		return null;
	}

	/**
	 * 根据Id查询视频
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Media queryMediaById(int id) throws Exception {
		return null;
	}
	
	/**
	 * 获取数据库连接
	 */
	public static void init(){
    	DBConn dbConn  = new DBConn();
        conn = dbConn.getConntion(); 
        //创建SQL执行工具   
       qRunner = new QueryRunner();   
    }
}

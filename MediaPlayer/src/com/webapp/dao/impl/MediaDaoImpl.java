package com.webapp.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.webapp.dao.MediaDao;
import com.webapp.entity.Media;

public class MediaDaoImpl extends MediaDao{
	

	/**
     * 视频转码
     * @param ffmpegPath    转码工具的存放路径
     * @param upFilePath    用于指定要转换格式的文件,要截图的视频源文件
     * @param codcFilePath    格式转换后的的文件保存路径
     * @param mediaPicPath    截图保存路径
     * @return
     * @throws Exception
     */
	public boolean executeCodecs(String ffmpegPath, String upFilePath, String codcFilePath,
            String mediaPicPath) throws Exception {
        // 创建一个List集合来保存转换视频文件为flv格式的命令
        List<String> convert = new ArrayList<String>();
        convert.add(ffmpegPath); // 添加转换工具路径
        convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
        convert.add(upFilePath); // 添加要转换格式的视频文件的路径
        convert.add("-qscale");     //指定转换的质量
        convert.add("6");
        convert.add("-ab");        //设置音频码率
        convert.add("64");
        convert.add("-ac");        //设置声道数
        convert.add("2");
        convert.add("-ar");        //设置声音的采样频率
        convert.add("22050");
        convert.add("-r");        //设置帧频
        convert.add("24");
        convert.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
        convert.add(codcFilePath);

        // 创建一个List集合来保存从视频中截取图片的命令
        List<String> cutpic = new ArrayList<String>();
        cutpic.add(ffmpegPath);
        cutpic.add("-i");
        cutpic.add(upFilePath); // 同上（指定的文件即可以是转换为flv格式之前的文件，也可以是转换的flv文件）
        cutpic.add("-y");
        cutpic.add("-f");
        cutpic.add("image2");
        cutpic.add("-ss"); // 添加参数＂-ss＂，该参数指定截取的起始时间
        cutpic.add("17"); // 添加起始时间为第17秒
        cutpic.add("-t"); // 添加参数＂-t＂，该参数指定持续时间
        cutpic.add("0.001"); // 添加持续时间为1毫秒
        cutpic.add("-s"); // 添加参数＂-s＂，该参数指定截取的图片大小
        cutpic.add("800*280"); // 添加截取的图片大小为350*240
        cutpic.add(mediaPicPath); // 添加截取的图片的保存路径

        boolean mark = true;
        ProcessBuilder builder = new ProcessBuilder();
        try {
            builder.command(convert);
            builder.redirectErrorStream(true);
            builder.start();
            
            builder.command(cutpic);
            builder.redirectErrorStream(true);
            // 如果此属性为 true，则任何由通过此对象的 start() 方法启动的后续子进程生成的错误输出都将与标准输出合并，
            //因此两者均可使用 Process.getInputStream() 方法读取。这使得关联错误消息和相应的输出变得更容易
            builder.start();
        } catch (Exception e) {
            mark = false;
            System.out.println(e);
            e.printStackTrace();
        }
        return mark;
    }

	

	public int getAllMediaCount() throws Exception {
		super.init();
		//动态创建SQL语句
		String strSQL = "select COUNT(*) from tb_media";
		int counter = qRunner.query(conn, strSQL,null);
		DbUtils.closeQuietly(conn); 
		return counter;
	}

	@SuppressWarnings("deprecation")
	public List<Media> queryALlMedia(int firstResult,
			int maxResult) throws Exception {
		super.init();
		//动态创建SQL语句
		String strSQL = "select * from tb_media limit ?,?";
		List<Media> lstMedia = (List<Media>)qRunner.query(conn, strSQL,new Object[]{firstResult,maxResult},new BeanListHandler<Media>(Media.class));
		DbUtils.closeQuietly(conn); 
		return lstMedia;
	}

	@SuppressWarnings("deprecation")
	public Media queryMediaById(int id) throws Exception {
		super.init();
		//动态创建SQL语句
		String strSQL = "select * from tb_media where id = ?";
		Media media = qRunner.query(conn, strSQL,new Object[]{id},new BeanListHandler<Media>(Media.class)).get(0);
		DbUtils.closeQuietly(conn); 
		return media;
	}

	public boolean saveMedia(Media media) throws Exception {
		super.init();
		String insertSQL = "insert into tb_media(title,src,picture,descript,uptime) values(?,?,?,?,?)";
		int flag = qRunner.update(conn, insertSQL, new Object[]{media.getTitle(),media.getSrc(),media.getPicture()
				,media.getDescript(),media.getUptime()});
		DbUtils.closeQuietly(conn); 
		return flag>0 ? true : false;
	}
}

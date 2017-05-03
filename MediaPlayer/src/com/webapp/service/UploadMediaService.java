package com.webapp.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;

import com.webapp.dao.DaoFactory;
import com.webapp.dao.MediaDao;
import com.webapp.entity.Media;

public class UploadMediaService extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		MediaDao mediaDao = DaoFactory.getMediaDao();
		String message = "";

		String uri = request.getRequestURI();
		System.out.println("uri>>"+uri);
		String path = uri.substring(uri.lastIndexOf("/"), uri.lastIndexOf("."));

		if ("/uploadFile".equals(path)) {
			// 提供解析时的一些缺省配置
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// 创建一个解析器,分析InputStream,该解析器会将分析的结果封装成一个FileItem对象的集合
			// 一个FileItem对象对应一个表单域
			ServletFileUpload sfu = new ServletFileUpload(factory);

			try {
				Media media = new Media();
				List<FileItem> items = sfu.parseRequest(request);
				boolean flag = false; // 转码成功与否的标记
				for (int i = 0; i < items.size(); i++) {
					FileItem item = items.get(i);
					// 要区分是上传文件还是普通的表单域
					if (item.isFormField()) {// isFormField()为true,表示这不是文件上传表单域
						// 普通表单域
						String paramName = item.getFieldName();
						
						 String paramValue = item.getString();
						  System.out.println("参数名称为:" + paramName +
						 ", 对应的参数值为: " + paramValue);
						 
						if (paramName.equals("title")) {
							media.setTitle(new String(item.getString()
									.getBytes("ISO8859-1"), "UTF-8"));
						}
						if (paramName.equals("descript")) {
							media.setDescript(new String(item.getString()
									.getBytes("ISO8859-1"), "UTF-8"));
						}

					} else {
						// 上传文件
						// System.out.println("上传文件" + item.getName());
						ServletContext sctx = this.getServletContext();
						// 获得保存文件的路径
						String basePath = sctx.getRealPath("videos");
						// 获得文件名
						String fileUrl = item.getName();
						// 在某些操作系统上,item.getName()方法会返回文件的完整名称,即包括路径
						String fileType = fileUrl.substring(fileUrl
								.lastIndexOf(".")); // 截取文件格式
						// 自定义方式产生文件名
						String serialName = String.valueOf(System
								.currentTimeMillis());
						// 待转码的文件
						File uploadFile = new File(basePath + "/temp/"
								+ serialName + fileType);
						item.write(uploadFile);

						if (item.getSize() > 500 * 1024 * 1024) {
							message = "<li>上传失败！您上传的文件太大,系统允许最大文件500M</li>";
						}
						String codcFilePath = basePath + "/" + serialName
								+ ".flv"; // 设置转换为flv格式后文件的保存路径
						String mediaPicPath = basePath + "/images"
								+ File.separator + serialName + ".jpg"; // 设置上传视频截图的保存路径

						// 获取配置的转换工具（ffmpeg.exe）的存放路径
						String ffmpegPath = getServletContext().getRealPath(
								"/tools/ffmpeg.exe");

						media.setSrc("videos/" + serialName + ".flv");
						media.setPicture("videos/images/" + serialName + ".jpg");
						String now = System.currentTimeMillis()+"";
						media.setUptime(now);

						// 转码

						flag = mediaDao.executeCodecs(ffmpegPath,
								uploadFile.getAbsolutePath(), codcFilePath,
								mediaPicPath);
					}
				}
				if (flag) {
					// 转码成功,向数据表中添加该视频信息
					mediaDao.saveMedia(media);
					message = "<li>上传成功!</li>";
				}

				request.setAttribute("message", message);
				request.getRequestDispatcher("media_upload.jsp").forward(
						request, response);

			} catch (Exception e) {
				e.printStackTrace();
				throw new ServletException(e);
			}
		}

		if ("/queryAll".equals(path)) {
			List<Media> mediaList;
			try {
				mediaList = mediaDao.queryALlMedia(0, 6);
				request.setAttribute("mediaList", mediaList);
				request.getRequestDispatcher("media_list.jsp").forward(request,
						response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if ("/play".equals(path)) {
			String idstr = request.getParameter("id");
			int mediaId = -1;
			Media media = null;
			if (null != idstr) {
				mediaId = Integer.parseInt(idstr);
			}
			try {
				media = mediaDao.queryMediaById(mediaId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("media", media);
			request.getRequestDispatcher("media_player.jsp").forward(request,
					response);
		}
	}

}

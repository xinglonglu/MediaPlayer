package com.webapp.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.webapp.dao.DaoFactory;
import com.webapp.dao.MediaDao;
import com.webapp.entity.Media;


public class PlayMediaService extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idstr = request.getParameter("id");
		int mediaId = -1;
		Media media = null;
		MediaDao mediaDao = DaoFactory.getMediaDao();
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

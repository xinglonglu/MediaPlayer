package com.webapp.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.webapp.dao.DaoFactory;
import com.webapp.dao.MediaDao;
import com.webapp.entity.Media;

public class ShowMediaList extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Media> mediaList;
		MediaDao mediaDao = DaoFactory.getMediaDao();
		try {
			mediaList = mediaDao.queryALlMedia(0, 100);
			request.setAttribute("mediaList", mediaList);
			request.getRequestDispatcher("media_list.jsp").forward(request,
					response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

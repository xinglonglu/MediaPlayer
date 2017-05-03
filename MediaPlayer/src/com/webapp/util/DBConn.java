package com.webapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DBConn {
	//三属性、四方法
	
	//三大核心接口
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	//四个方法
	//method1: 创建数据库的连接
	public Connection getConntion(){		
		try {
			//1: 加载连接驱动，Java反射原理
			Class.forName(Config.CLASS_NAME);
			//2:创建Connection接口对象，用于获取MySQL数据库的连接对象。三个参数：url连接字符串    账号  密码
			String url = Config.DATABASE_URL+"://"+Config.SERVER_IP+":"+Config.SERVER_PORT+"/"+Config.DATABASE_SID;
			conn = DriverManager.getConnection(url,Config.USERNAME,Config.PASSWORD);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	
	//method2：关闭数据库的方法
	public void closeConn(){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(pstmt!=null){
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

    
	//method3: 专门用于发送增删改语句的方法
	public int execOther(final String strSQL,final Object[] params ){
		//1、获取数据库连接
		getConntion();
		//2、预先打印出即将执行的SQL语句(便于项目测试，仿Hibernate框架)
		System.out.println("SQL:> "+strSQL);		
		try {
			//3、创建Statement接口对象
			pstmt = conn.prepareStatement(strSQL);
			//4、动态为pstmt对象赋值
			for(int i=0; i< params.length ;i++){
				pstmt.setObject(i+1, params[i]);
			}
			//5、使用Statement对象发送SQL语句
			int affectedRows = pstmt.executeUpdate();
			//6、返回结果
			return affectedRows;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}


	//method4: 专门用于发送查询语句
	public ResultSet execQuery(final String strSQL,final Object[] params){
		//1、获取数据库连接
		getConntion();
		//2、预先打印出即将执行的SQL语句(便于项目测试，仿Hibernate框架)
		System.out.println("SQL:> "+strSQL);
		try {
			//3、创建PreparedStatement接口对象
			pstmt = conn.prepareStatement(strSQL);
			//4、动态为pstmt对象赋值
			for(int i=0; i< params.length ;i++){
				pstmt.setObject(i+1, params[i]);
			}		
			//5、使用Statement对象发送SQL语句
			rs = pstmt.executeQuery();
			//6、返回结果
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}

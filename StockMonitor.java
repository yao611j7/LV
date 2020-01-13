package com.LVmonitor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date; 

class MyThread extends Thread {
	public void run() {
		try {
			// 此处参数为监控持续分钟数
			while(true) {
				StockMonitor.monitorButton();
				sleep(60000L);  //间隔1秒执行一次！
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
};


public class StockMonitor {
	// 监控的商品URL
		private static String URL = "https://us.louisvuitton.com/eng-us/products/pochette-accessoires-monogram-005656";
		//private static String URL = "https://us.louisvuitton.com/eng-us/products/neverfull-mm-damier-ebene-008116";
	 
		// 监视按钮
		public static void monitorButton() throws Exception {
	        
			String currentButton = getCurrentButtonAndForm(URL);
			////System.out.println(currentButton);
			if(currentButton=="in stock") {
				System.out.println("yesyesyesyesyesyesyesyesyesyesyesyesyesyes！");				
				AmazonSES.sendEmail();
				System.exit(0); 
			}
			else {
				SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间 
		        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记  
		        Date date = new Date();// 获取当前时间 
		        //System.out.println("现在时间：" + sdf.format(date)); // 输出已经格式化的现在时间（24小时制） 
				System.out.println("no" + "现在时间：" + sdf.format(date));
			}
		}
	 
		// 获取当前按钮状态
		public static String getCurrentButtonAndForm(String url) {
			if (url == null || "".equals(url.trim()))
				return null;
			String buttonState = "";
			try {
				// 新建URL对象
				URL u = new URL(url);
				InputStream is = new BufferedInputStream(u.openStream());
				InputStreamReader theHTML = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(theHTML);
				String s = "";
				while ((s = br.readLine()) != null) {
					if (s.indexOf("\"availability\": \"http://schema.org/InStock\"") != -1) {
						buttonState = "in stock";
						break;
					} else if (s.indexOf("\"availability\": \"http://schema.org/OutOfStock\"") != -1) {
						buttonState = "no stock";
						break;
					}
					
				}
				br.close();
			} catch (Exception e) {
				System.err.println(e);
				return "Open URL Error";
			}
			return buttonState;
		}
	 
		// 提交表单
		public static String doPost(String form) {
			StringBuffer content = new StringBuffer();
			try {
				URLConnection connection = new URL(URL).openConnection();
				connection.setDoOutput(true);
				OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
				os.write(form);
				os.flush();
				os.close();
				InputStream is = connection.getInputStream();
				InputStreamReader theHTML = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(theHTML);
				String s = "";
				while ((s = br.readLine()) != null) {
					content.append(s + "\r\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 返回提交表单后返回的页面内容
			return content.toString();
		}
		
		// 登录
		public static void doLogin(String username, String password) {
			String form = "<form id=\"J_StaticForm\" action=\"https://login.taobao.com/member/login.jhtml\" method=\"post\" autocomplete=\"on\"><input type=\"text\" name=\"TPL_username\" id=\"TPL_username_1\" value=\"" + username + "\"><input type=\"password\" name=\"TPL_password\" id=\"TPL_password_1\" value=\"" + password + "\"><input type=\"hidden\" id=\"J_TPL_redirect_url\" name=\"TPL_redirect_url\" value=\"http://www.taobao.com/?spm=a2107.1.1000340.1.AL2Mpn\"><button type=\"submit\" id=\"J_SubmitStatic\">登　录</button></form>";
			doPost(form);
		}
	 
		public static void main(String[] args) {
			//doLogin();
			// new MyThread().start();
			// new MyThread().start();
			// new MyThread().start();
			// new MyThread().start();
			// new MyThread().start();
			// new MyThread().start();
			// new MyThread().start();
			System.out.println("here");
			new MyThread().start();
		}

}

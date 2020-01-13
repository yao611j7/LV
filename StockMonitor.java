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
			// �˴�����Ϊ��س���������
			while(true) {
				StockMonitor.monitorButton();
				sleep(60000L);  //���1��ִ��һ�Σ�
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
};


public class StockMonitor {
	// ��ص���ƷURL
		private static String URL = "https://us.louisvuitton.com/eng-us/products/pochette-accessoires-monogram-005656";
		//private static String URL = "https://us.louisvuitton.com/eng-us/products/neverfull-mm-damier-ebene-008116";
	 
		// ���Ӱ�ť
		public static void monitorButton() throws Exception {
	        
			String currentButton = getCurrentButtonAndForm(URL);
			////System.out.println(currentButton);
			if(currentButton=="in stock") {
				System.out.println("yesyesyesyesyesyesyesyesyesyesyesyesyesyes��");				
				AmazonSES.sendEmail();
				System.exit(0); 
			}
			else {
				SimpleDateFormat sdf = new SimpleDateFormat();// ��ʽ��ʱ�� 
		        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// aΪam/pm�ı��  
		        Date date = new Date();// ��ȡ��ǰʱ�� 
		        //System.out.println("����ʱ�䣺" + sdf.format(date)); // ����Ѿ���ʽ��������ʱ�䣨24Сʱ�ƣ� 
				System.out.println("no" + "����ʱ�䣺" + sdf.format(date));
			}
		}
	 
		// ��ȡ��ǰ��ť״̬
		public static String getCurrentButtonAndForm(String url) {
			if (url == null || "".equals(url.trim()))
				return null;
			String buttonState = "";
			try {
				// �½�URL����
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
	 
		// �ύ��
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
			// �����ύ���󷵻ص�ҳ������
			return content.toString();
		}
		
		// ��¼
		public static void doLogin(String username, String password) {
			String form = "<form id=\"J_StaticForm\" action=\"https://login.taobao.com/member/login.jhtml\" method=\"post\" autocomplete=\"on\"><input type=\"text\" name=\"TPL_username\" id=\"TPL_username_1\" value=\"" + username + "\"><input type=\"password\" name=\"TPL_password\" id=\"TPL_password_1\" value=\"" + password + "\"><input type=\"hidden\" id=\"J_TPL_redirect_url\" name=\"TPL_redirect_url\" value=\"http://www.taobao.com/?spm=a2107.1.1000340.1.AL2Mpn\"><button type=\"submit\" id=\"J_SubmitStatic\">�ǡ�¼</button></form>";
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

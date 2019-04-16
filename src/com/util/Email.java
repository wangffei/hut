package com.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

	public static Properties props;
	public static String myEmailAccount = "613465432@qq.com";
	public static String myEmailPassword = "vzyxclpzpzvcbbhd";
	public static String myEmailSMTPHost = "smtp.qq.com";
	public static String receiveMailAccount = "374519711@qq.com";

	static {
		props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", myEmailSMTPHost);
		props.setProperty("mail.smtp.auth", "true");
		final String smtpPort = "465";
		props.setProperty("mail.smtp.port", smtpPort);
		props.setProperty("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.socketFactory.port", smtpPort);

	}

	public static MimeMessage createMimeMessage(Session session, String send,
			String receive, String str) throws Exception {
		MimeMessage message = new MimeMessage(session);

		message.setFrom(new InternetAddress(send, "客户端", "UTF-8"));

		message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(
				receive, "服务器", "UTF-8"));

		message.setSubject("建议", "UTF-8");

		message.setContent(str, "text/html;charset=UTF-8");

		message.setSentDate(new Date());

		message.saveChanges();
		session.setDebug(false);

		return message;

	}

	public static void send(String str) throws Exception {
		
		Session session = Session.getInstance(props);
		session.setDebug(true);
		MimeMessage message = createMimeMessage(session, myEmailAccount,
				receiveMailAccount, str);
		Transport transport = session.getTransport();

		transport.connect(myEmailAccount, myEmailPassword);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}
//	public static void main(String[] args) {
//		try {
//			send("测试邮箱发送");
//		} catch (Exception e) {
//			System.out.println("发送失败");
//			e.printStackTrace();
//		}
//	}

}

package com.itheima.acl.util;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class MailUtils {

    private static Properties props;

    static{
        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        props = new Properties();                       // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", "smtp.163.com");    // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");
    }

    public static void sendMail(String emailAddress, String username, String password) {
        //发送邮件进行验证

        String myEmailAccount = "shan19920501@163.com";
        String myEmailPassword = "shan1992";

        //发送地址
        String receiveMailAccount = emailAddress;

        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(props);
        session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log

        // 3. 创建一封邮件
        //  MimeMessage message = createMimeMessage(session, myEmailAccount, receiveMailAccount);

        // 3.1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        Transport transport = null;

        try {
            // 3.2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
            message.setFrom(new InternetAddress(myEmailAccount, "用户管理系统", "UTF-8"));

            // 3.3. To: 收件人（可以增加多个收件人、抄送、密送）
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMailAccount, username, "UTF-8"));


            // 3.4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
            message.setSubject("权限系统注册成功!", "UTF-8");

            // 3.5. Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发送内容）
            message.setContent("系统生成密码: " + password + " , 请妥善保管!", "text/html;charset=UTF-8");

            // 3.6. 设置发件时间
            message.setSentDate(new Date());

            // 3.7. 保存设置
            message.saveChanges();

            // 4. 根据 Session 获取邮件传输对象
            transport = session.getTransport();

            // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
            transport.connect(myEmailAccount, myEmailPassword);

            // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.getAllRecipients());


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 7. 关闭连接
            if(transport != null)
                try {
                    transport.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
        }



    }

}

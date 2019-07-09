package com.example.advance.utils;

//public class MailSender implements InitializingBean {
//    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
//    private JavaMailSenderImpl mailSender;
//
//    public boolean sendWithHTMLTemplate(String to, String subject, String template, Map<String, Object> model) {
//        try {
//            String nick = MimeUtility.encodeText("牛客网");
//            InternetAddress from = new InternetAddress(nick + "<1054651571@qq.com>");
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
//            String result = "";
//
//        } catch (Exception e) {
//
//        }
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        mailSender = new javaMailSenderImpl();
//        mailSender.setUsername("1054651571@qq.com");
//        mailSender.setPassword("lall");
//        mailSender.setHost("smtp.qq.com");
//        mailSender.setPort(465);
//        mailSender.setProtocol("smtps");
//        mailSender.setDefaultEncoding("utf8");
//        Properties javaMailProperties = new Properties();
//        javaMailProperties.put("mail.smtp.ssl.enable", true);
//    }
//}

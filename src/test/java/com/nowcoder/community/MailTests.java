package com.nowcoder.community;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;

import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

/*    @Autowired
    private MailClient mailClient;*/

    @Autowired
    private TemplateEngine templateEngine;
    /*@Test
    public void testTextMail(){
        mailClient.sendMail("602563146@qq.com","TEST","hello");
    }

    @Test
    public void testHtmlMall(){
        Context context = new Context();
        context.setVariable("username","sleep");

        String content = templateEngine.process("/mail/register.html",context);

        mailClient.sendMail("602563146@qq.com","HTML",content);
    }*/
}

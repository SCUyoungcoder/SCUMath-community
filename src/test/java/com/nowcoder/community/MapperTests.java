package com.nowcoder.community;

import com.nowcoder.community.dao.*;
import com.nowcoder.community.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {
    @Autowired
    private UserMapper userMapper;/*dao里的usermapper*/
    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private LoginticketMapper loginticketMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ClassificationMapper classificationMapper;
    @Test
    public void xxx(){
        User user = userMapper.selectById(111);
        System.out.println(user);
    }
    @Test
    public void selectallclass(){
        List<Classification> classes = classificationMapper.selectAll();
        for (Classification class1:classes){
            System.out.println(class1);
        }
    }
    @Test
    public void insertclass(){
        Classification classification = new Classification();
        classification.setName("计算数学");
        classification.setSearchname("compute");
        int row = classificationMapper.insertClassification(classification);
        System.out.println(row);
    }
    @Test
    public void selectclass(){
        /*Classification classification = classificationMapper.selectByName("基础数学");*/
        System.out.println(classificationMapper.selectByName("基础数学"));
    }
    @Test
    public void deleteclass(){
        int row = classificationMapper.deleteByName("计算数学");
        System.out.println(row);
    }
    @Test
    public void insertcomment(){
        Date dayy=new Date();
        Comment comment = new Comment();
        comment.setUserid(3);
        comment.setEntitytype(4);
        comment.setEntityid(5);
        /*comment.setTargetid(6);*/
        comment.setContent("dsad545");
        comment.setStatus(7);
        comment.setCreatetime(dayy);
        comment.setType(8);
        int row = commentMapper.insertComment(comment);
        System.out.println(row);
    }
    @Test
    public void selectcomment(){
        Comment comment;
        /*comment = commentMapper.selectByEntity(4,5);*/
        System.out.println(commentMapper.selectByEntity(0,18,0));
    }
    @Test
    public void deletecomment(){
        int row = commentMapper.deleteById(2);
        System.out.println(row);
    }
    @Test
    public void insertticket(){
        Date dayy=new Date();
        Loginticket login = new Loginticket();
        login.setUserid(1);
        login.setType(2);
        login.setTicket("dsadsadsadsaw");
        login.setStatus(5);
        login.setExpired(dayy);
        int row = loginticketMapper.insertTicket(login);
        System.out.println(row);
    }
    @Test
    public void delectticket(){
        int row = loginticketMapper.deleteByTicket("dsadsadsadsaw");
        System.out.println(row);
    }
    @Test
    public void selectticket(){
        Loginticket loginticket;
        /*loginticket = loginticketMapper.selectByTicket("dsadsadsadsaw");*/
        System.out.println(loginticketMapper.selectByTicket("dsadsadsadsaw"));
    }

    @Test
    public void updatepaper(){
        int raw = paperMapper.updateDownloadcount(2,35);
        System.out.println(raw);
    }
    @Test
    public void selectpaper(){
        Paper paper;
        /*paper= paperMapper.selectByTitle("asleep");*/
        System.out.print(paperMapper.selectByTitle("asleep"));
    }
    @Test
    public void delectpaper(){
        int raw=paperMapper.delectById(1);
        System.out.println(raw);
    }
    @Test
    public void testInsertpaper(){
        Date dayy=new Date();

        Paper paper = new Paper();
        paper.setFatherid("计算数学基础数学");
        paper.setUserid(2);
        paper.setTitle("asleep");
        paper.setContent("dsadsadsa");
        paper.setFilepath("c:sdsadsa");/*试试斜杠能不能存储*/
        paper.setStatus(1);
        paper.setCreatetime(dayy);
        paper.setDownloadcount(1);
        int rows = paperMapper.insertPaper(paper);
        System.out.println(rows);
        System.out.println(paper.getId());
    }

    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser() {
        /*id自动生成，另外有两个没写的默认值为0*/
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");/*密码末尾加入的乱码*/
        user.setEmail("test@qq.com");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);/*运行2次均为1.rows为操作的行数*/
        System.out.println(user.getId());/*运行两次150，151*/
    }

    @Test
    public void updateUser() {
        int rows = userMapper.updateStatus(110, 1);
        System.out.println(rows);

        System.out.println(rows);

        rows = userMapper.updatePassword(110, "hello" ,"adsa");
        System.out.println(rows);
    }
}

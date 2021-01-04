package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.AdminRequired;
import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.dao.elasticsearth.PaperRepository;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.Paper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.ClassificationService;
import com.nowcoder.community.service.ElasticsearchService;
import com.nowcoder.community.service.PaperOfClassService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {
    /*记录日志*/
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    /*@Value("${server.servlet.context-path}")
    private String contextPath;*//*修改。。。*/
    @Autowired
    private ElasticsearchService elasticsearchService;
    @Autowired
    private UserService userService;
    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private PaperOfClassService paperOfClassService;
    @Autowired
    public ClassificationService classificationService;
    @Autowired
    private HostHolder hostHolder;

    /*获取用户的类型，管理员返回不同界面。待处理*/
    @LoginRequired/*自定义注解，未登录用户做需要登录的操作时,用拦截器拦截*/
    @RequestMapping(path = "/manage", method = RequestMethod.GET)/*用户管理员个人信息管理均在这里。。。待完善*/
    public String getSettingPage(Page page,Model model) {
        User user = hostHolder.getUser();
        model.addAttribute("username",user.getUsername());
        model.addAttribute("email",user.getEmail());
        model.addAttribute("createtime",user.getCreateTime());
        if (user.getType()==0){
            model.addAttribute("type","普通用户");
        }else {
            model.addAttribute("type","管理员");
        }
        model.addAttribute("createtime",user.getCreateTime());
        if (user.getType()==0){                             /*type1为管理*/
            return "/site/usermanage";
        }else {
            //org.springframework.data.domain.Page<Paper> statusof1 = paperOfClassService.selectpaperByStatus(1,page.getCurrent()-1,page.getLimit());
            page.setPath("/manage");
            page.setRows(paperOfClassService.countstatus(1));
            List<Map<String,Object>> papers = new ArrayList<>();
            List<Map<String,Object>> classify = new ArrayList<>();
            String[] allfather={"logic","compute","number","algebra","geometry","topology","analysis","ODE","PDE","dynamical","functional","probability","statistics","opsearch","combinatorial","fuzzy","quantum","applied"};
            //System.out.println(page.getOffset());
            //List<Paper> statusof1 = paperOfClassService.selectpaperByStatus(1,page.getOffset(),page.getLimit());
            List<Paper> statusof1 = paperOfClassService.selectpaperByStatus(1,page.getOffset(),page.getLimit());
            /*如果报错，研究下这里取出来的数据*/
            if (statusof1!=null){
                for (Paper paper:statusof1){
                    Map<String,Object> map = new HashMap<>();
                    System.out.println(paper.getFatherid());
                    String[] fathers =paper.getFatherid().split(",");
                    /*map.put("papertitle",paper.getTitle());
           //java.lang.NullPointerException: null报错，已解决。。。。paper表里第一个的userid为2，而user表里没有这个，故报错。已修改
                    map.put("username",userService.findUserById(paper.getUserid()).getUsername());
                    map.put("papercontent",paper.getContent());
                    map.put("type1","type1 in fatherid");       //    待完成         考虑用勾选框展示type
                    map.put("type2","type2 in fatherid");
                    map.put("paperpath",paper.getFilepath());
                    map.put("createtime",paper.getCreatetime());*/
                    List<String> otherfathers = new ArrayList<>();
                    for (String str:allfather){
                        otherfathers.add(str);
                    }
                    for (String str:fathers){
                        if (otherfathers.contains(str)){
                            otherfathers.remove(str);
                        }
                    }
                    List<Map<String,Object>> fatherss = new ArrayList<>();
                    for (String str:fathers){
                        Map<String,Object> map1 = new HashMap<>();
                        map1.put("searchname",str);
                        System.out.println(str);
                        map1.put("name",classificationService.GetNameBySearchname(str).getName());
                        fatherss.add(map1);
                    }
                    List<Map<String,Object>> otherfatherss = new ArrayList<>();
                    for (String str:otherfathers){
                        Map<String,Object> map2 = new HashMap<>();
                        map2.put("searchname",str);
                        map2.put("name",classificationService.GetNameBySearchname(str).getName());
                        otherfatherss.add(map2);
                    }
                    /*paper.setTitle(paper.getFilepath().substring(paper.getFilepath().lastIndexOf("/")));*/
                    paper.setTitle(paper.getTitle()+paper.getFilepath().substring(paper.getFilepath().lastIndexOf(".")));
                    map.put("paper",paper);
                    map.put("fathers",fatherss);
                    map.put("otherfathers",otherfatherss);
                    papers.add(map);
                }
            }
            model.addAttribute("papers",papers);
            //page.setRows(statusof1 == null?0:(int)statusof1.);
            return "/site/adminmanage";
        }

    }
    /*/user/search?keyword=ODE&fieldname=fatherid*/
    @RequestMapping(path = "/search" ,method = RequestMethod.GET)
    public String search(String keyword, String fieldname, String sortname, Page page, Model model){
        if (sortname == null){
            sortname = "createtime";
        }
        org.springframework.data.domain.Page<Paper> searchpapers = elasticsearchService.searchPaperByFieldname(keyword,fieldname,sortname,page.getCurrent() - 1,page.getLimit());
        List<Map<String ,Object>> papers = new ArrayList<>();
        if (searchpapers !=null){
            for (Paper paper:searchpapers){
                if(paper.getStatus()==0){                   /*针对论文的搜索，是否需要写针对悬赏的搜索*/
                    Map<String,Object> map = new HashMap<>();
                    paper.setFatherid((userService.findUserById(paper.getUserid())).getUsername());
                    map.put("paper",paper);
                    papers.add(map);
                }
            }
        }
        model.addAttribute("papers",papers);
        model.addAttribute("fieldname" ,fieldname);
        page.setPath("/search?keyword="+keyword);
        page.setRows(searchpapers == null?0:searchpapers.getTotalPages());
        return "/site/searchresult";
    }
    /*管理员更改上传的论文的状态*/
   /* @LoginRequired
    @RequestMapping(path = "/changestatus",method = RequestMethod.GET)
    public void changestatus(int id){   *//*管理员点击论文确认上传按钮后，前端改变button样式++++++待完成*//*
        Paper paper = paperMapper.selectById(id);
        paper.setStatus(0);
        paperMapper.insertPaper(paper);
        paperRepository.save(paper);
    }*/

    /*拦截器判断是否为管理员*/
   @AdminRequired
   @RequestMapping(path = "/update",method = RequestMethod.POST)
   public String updatePaper(Paper paper,Model model){
        Paper pp = paperOfClassService.selectPaperById(paper.getId());
            pp.setContent(paper.getContent());
            pp.setFatherid(paper.getFatherid());
            pp.setStatus(0);
            paperOfClassService.updatastatus(paper);
            paperRepository.save(pp);
       //return "/site/adminmanage";
        return "redirect:/user/manage";
   }
   @LoginRequired
   @RequestMapping(path = "/reward",method = RequestMethod.POST)
   public String offerreward(Paper paper,Model model){
       User user = hostHolder.getUser();
       Date dayy=new Date();
       paper.setCreatetime(dayy);
       paper.setUserid(user.getId());
       paper.setStatus(2);
       paperOfClassService.uploadpaper(paper);
       paperRepository.save(paper);                                                /*每次上传论文存储信息进数据库的同时也存储进elasticsearth*/
       model.addAttribute("error1", "悬赏发布成功！");
       model.addAttribute("username",user.getUsername());
       model.addAttribute("email",user.getEmail());
       model.addAttribute("createtime",user.getCreateTime());
       if (user.getType()==0){
           model.addAttribute("type","普通用户");
       }else {
           model.addAttribute("type","管理员");
       }
       if (user.getType()==1){
           return "/site/adminmanage";
       }else {
           return "/site/usermanage";
       }
   }
    /*上传表单必须用post.....model用于向页面返回数据*/
    /*上传论文时同时需要输入fatherid，content ,是否需要添加作者字段*/
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile paperfile, Paper paper, Model model) {
        User user = hostHolder.getUser();
        if (paperfile == null) {
            model.addAttribute("error", "您还没有选择文件!");
            if (user.getType()==1){
                return "/site/adminmanage";
            }else {
                return "/site/usermanage";
            }
        }
        /*获取上传文件名*/
        String fileName = paperfile.getOriginalFilename();
        /*获取前缀存入数据库*/
        String title = fileName.substring(0,fileName.lastIndexOf("."));
        /*截取后缀*/
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确!");
            if (user.getType()==1){
                return "/site/adminmanage";
            }else {
                return "/site/usermanage";
            }
        }
        /*判断fatherid不能太长，（不能为空,前端判断）     content前端做成勾选框       +++++++++++++++++++++待处理*/
        if (paper.getContent().length()> 250){
            model.addAttribute("error","摘要过长!");
            if (user.getType()==1){
                return "/site/adminmanage";
            }else {
                return "/site/usermanage";
            }
        }
        // 生成随机文件名....因为是上传论文，重名声明已经有人上传过，故拒绝，故不用更改文件名
        /*fileName = CommunityUtil.generateUUID() + suffix;*/
        // 确定文件存放的路径
        /*检查文件名在数据库中是否存在，存在说明有人上传过，拒绝*/
        if (paperOfClassService.selectpaperByTitle(title)!=null){
            model.addAttribute("error","该文章已经上传过了！");
            if (user.getType()==1){
                return "/site/adminmanage";
            }else {
                return "/site/usermanage";
            }
        }
        File dest = new File(uploadPath + "/" + fileName);
        try {
            // 存储文件
            paperfile.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败: " + e.getMessage());
            /*异常抛出，会打断程序，单独编码处理异常*/
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }

        // 文章的访问路径(web访问路径)
        // http://localhost:8080/community/user/header/xxx.png
        /*User user = hostHolder.getUser();
        String headerUrl = domain +  "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);*/
        /*paper数据存到数据库*/
        paper.setTitle(title);                                                  /*考虑是由用户输入title还是自动从文件中获取*/
        String filepath = domain +  "/user/file/" + fileName;
        paper.setFilepath(filepath);
        paper.setDownloadcount(0);
        Date dayy=new Date();
        paper.setCreatetime(dayy);
        paper.setUserid(user.getId());
        if (user.getType() == 1){/*type为1即管理员*/
            paper.setStatus(0);
        }
        else {
            paper.setStatus(1);/*设置状态未为1即不可展示给用户看，待管理员通过设置为0即可展示*/
        }
        paperOfClassService.uploadpaper(paper);
        paperRepository.save(paper);                                                /*每次上传论文存储信息进数据库的同时也存储进elasticsearth*/
        model.addAttribute("error", "上传成功！");
        model.addAttribute("username",user.getUsername());
        model.addAttribute("email",user.getEmail());
        model.addAttribute("createtime",user.getCreateTime());
        if (user.getType()==0){
            model.addAttribute("type","普通用户");
        }else {
            model.addAttribute("type","管理员");
        }
        if (user.getType()==1){
            return "/site/adminmanage";
        }else {
            return "/site/usermanage";
        }
        //return "redirect:/user/manage";
        /*return "redirect:/index";*/
    }

    /*下载文件接口*/    /*1.文件下载下来全是空的.2.下载后下载数没有+1            全解决*/
    /*google浏览器会将最新下载的文件显示为未确认，ie浏览器没这个问题。在response.setHeader中解决中文编码问题后又没问题了。。*/
    /*@LoginRequired
    @RequestMapping(path = "/file/{fileName}", method = RequestMethod.GET)
    public void getfile(@PathVariable("fileName") String fileName, HttpServletResponse response) {
         String filename = fileName;
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        //response.setContentType("image/" + suffix);
        //response.setContentType("text/html;charset=utf-8");
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");

        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream outputStream = null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + filenamenew String(filename.getBytes("gb2321"),"ISO8859-1"));
            outputStream = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(fileName)));
            int read = bis.read(buffer);
            while (read != -1){
                outputStream.write(buffer,0,buffer.length);
                outputStream.flush();
                outputStream.close();
                read = bis.read(buffer);

            }
            //outputStream.close();
        }catch (IOException e){

        }


    }
*/
    @LoginRequired
    @RequestMapping(path = "/file/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //文件前缀
        String title = fileName.substring(0,fileName.lastIndexOf("."));
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 设置类型为文件
        response.setContentType("application/octet-stream");
        //response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("下载文件失败: " + e.getMessage());
        }
        System.out.println(title);
        Paper paper = paperOfClassService.selectpaperByTitle(title);

        paperOfClassService.updateDownloadcount(paper.getId(),paper.getDownloadcount()+1);
    }
    /*更改密码*/
    @LoginRequired
    @RequestMapping(path = "updatepassword",method = RequestMethod.POST)
    public String updatepassword(String password,Model model){
        User user = hostHolder.getUser();
        userService.updatepassword(user.getId(),password);
        model.addAttribute("passwordMsg","密码更改成功！");
        model.addAttribute("username",user.getUsername());
        model.addAttribute("email",user.getEmail());
        model.addAttribute("createtime",user.getCreateTime());
        if (user.getType()==0){
            model.addAttribute("type","普通用户");
        }else {
            model.addAttribute("type","管理员");
        }
        if (user.getType()==1){
            return "/site/adminmanage";
        }else {
            return "/site/usermanage";
        }
       /* return "redirect:/manage";*/
    }
}

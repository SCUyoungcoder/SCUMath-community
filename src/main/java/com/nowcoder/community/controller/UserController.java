package com.nowcoder.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nowcoder.community.annotation.AdminRequired;
import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.dao.GroupMapper;
import com.nowcoder.community.dao.elasticsearth.PaperRepository;
import com.nowcoder.community.entity.*;
import com.nowcoder.community.service.*;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    private UserinfoService userinfoService;
    @Autowired
    private ElasticsearchService elasticsearchService;
    @Autowired
    private UserService userService;
    @Autowired
    private AttentionService attentionService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private PaperOfClassService paperOfClassService;
    @Autowired
    public ClassificationService classificationService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(path = "/searchByName",method = RequestMethod.GET)
    public String searchByName(String username){
        User user = hostHolder.getUser();
        User user1 = userService.selectByUserName(username);
        if (user1 == null){
            return "redirect:/user/blog/"+user.getId()+"?searchResult=1";
        }
        return "redirect:/user/blog/"+user1.getId();
    }
    @LoginRequired
    @RequestMapping(path = "/deleteMember/{groupMemberId}",method = RequestMethod.GET)
    public String deleteMember(@PathVariable("groupMemberId") int groupMemberId){
        User user = hostHolder.getUser();
        GroupMember groupMember = userService.getGroupMemberById(groupMemberId);
        Group group = userService.getGroupById(groupMember.getGroupId());
        if (user.getId()!=group.getOwnerId()){
            return "redirect:/user/groupDetail/"+group.getId();
        }
        userService.deleteGroupMemberById(groupMemberId,group.getId());
        return "redirect:/user/groupDetail/"+group.getId();
    }
    @LoginRequired
    @RequestMapping(path = "/addGroupMember/{groupId}",method = RequestMethod.POST)
    public String addGroupMember(String allMembers,@PathVariable("groupId") int groupId){
        if (allMembers==null){
            return "redirect:/user/groupDetail/"+groupId;
        }
        User user = hostHolder.getUser();
        Group group = userService.getGroupById(groupId);
        if (user.getId()!=group.getOwnerId()){
            return "redirect:/user/groupDetail/"+groupId;
        }
        String[] memberList = allMembers.split(" ");
        userService.addGroupMember(memberList,groupId);
        return "redirect:/user/groupDetail/"+groupId;
    }
    @LoginRequired
    @RequestMapping(path = "/groupDetail/{id}",method = RequestMethod.GET)
    public String groupDetail(Model model,@PathVariable("id") int id){
        User user = hostHolder.getUser();
        Group group = userService.getGroupById(id);
        if (group==null){
            return "redirect:/user/groupList?msg=4";
        }
        if (group.getOwnerId()!=user.getId()){
            return "redirect:/user/groupList?msg=5";
        }
        List<GroupMember> groupMemberList1 = userService.getGroupMemberByGroupId(id);
        List<GroupMember> groupMemberList = new ArrayList<>();
        for (GroupMember groupMember:groupMemberList1){
            GroupMember groupMember1 = new GroupMember();
            groupMember1.setId(0);
            groupMember1.setGroupId(0);
            groupMember1.setUserId(groupMember.getUserId());
            groupMember1.setUsername(groupMember.getUsername());
            groupMemberList.add(groupMember1);
        }
        List<Attention> attentionList1 = attentionService.SelectByUserId(user.getId());
        List<Attention> attentionList2 = attentionService.SelectByFocusId(user.getId());
        attentionList1.addAll(attentionList2);
        List<GroupMember> groupMembers = new ArrayList<>();
        for (Attention attention:attentionList1){
            GroupMember groupMember = new GroupMember();
            if (attention.getUserId()==user.getId()){
                groupMember.setUserId(attention.getFocusId());
            }else {
                groupMember.setUserId(attention.getUserId());
            }
            groupMember.setUsername(userService.findUserById(groupMember.getUserId()).getUsername());
            if (!groupMembers.contains(groupMember) && !groupMemberList.contains(groupMember)){
                groupMembers.add(groupMember);
            }
        }
        int i = 1;
        for (GroupMember groupMember:groupMembers){
            groupMember.setId(i);
            i++;
        }
        model.addAttribute("groupMembers",groupMembers);
        model.addAttribute("group",group);
        model.addAttribute("groupMemberList",groupMemberList1);
        return "group/detail";
    }

    @LoginRequired
    @RequestMapping(path = "/deleteGroup/{id}",method = RequestMethod.GET)
    public String deleteGroup(@PathVariable("id") int id){
        User user = hostHolder.getUser();
        int result = userService.deleteGroupById(id,user.getId());
        if (result == -1){
            return "redirect:/user/groupList?msg=2";
        }
        if (result == 0){
            return "redirect:/user/groupList?msg=3";
        }
        return  "redirect:/user/groupList";
    }
    @LoginRequired
    @RequestMapping(path = "/addGroup",method = RequestMethod.POST)
    public String addGroup(Model model,Group group){
        User user = hostHolder.getUser();
        group.setOwnerId(user.getId());
        int result = userService.addGroup(group);
        if (result==0){
            return "redirect:/user/groupList?msg=1";
        }
        return "redirect:/user/groupList";
    }
    @LoginRequired
    @RequestMapping(path = "/groupList",method = RequestMethod.GET)
    public String groupList(Model model,
                            @RequestParam(defaultValue = "0" ) int msg,
                            @RequestParam(defaultValue = "1" ) int page,
                            @RequestParam(defaultValue = "10") int limit){
        User user = hostHolder.getUser();
        PageHelper.startPage(page,limit);
        PageInfo<Group> info = new PageInfo<>(userService.getGroupByOwnerId(user.getId()));
        switch (msg){
            case 1:
                model.addAttribute("regMsg","该小组名已存在！");
                break;
            case 2:
                model.addAttribute("regMsg","不存在该小组！");
                break;
            case 3:
                model.addAttribute("regMsg","没有权限删除该小组!");
                break;
            case 4:
                model.addAttribute("regMsg","该小组不存在！");
                break;
            case 5:
                model.addAttribute("regMsg","没有权限查看该小组!");
                break;
        }
        model.addAttribute("info",info);
        return "group/list";
    }

    @AdminRequired
    @RequestMapping(path = "/addCode",method = RequestMethod.POST)
    public String addCode(Model model,InvitationCode invitationCode){
        User user = hostHolder.getUser();
        invitationCode.setAdminId(user.getId());
        int result = userService.addCode(invitationCode);
        if (result == 0){
            return "redirect:/user/invitationCode?msg=1";
        }
        return "redirect:/user/invitationCode";
    }

    @AdminRequired
    @RequestMapping(path = "/deleteCode/{id}",method = RequestMethod.GET)
    public String deleteCode(@PathVariable("id") int id){
        int result = userService.deleteCodeById(id);
        if (result == 0){
            return "redirect:/user/invitationCode?msg=2";
        }
        return "redirect:/user/invitationCode";
    }

    @AdminRequired
    @RequestMapping(path = "/invitationCode",method = RequestMethod.GET)
    public String manageCode(Model model,
                             @RequestParam(defaultValue = "0" ) int msg,
                             @RequestParam(defaultValue = "1" ) int page,
                             @RequestParam(defaultValue = "10") int limit){
        PageHelper.startPage(page,limit);
        PageInfo<InvitationCode> info = new PageInfo<>(userService.getAllCode());
        if (msg==1){
            model.addAttribute("regMsg","该邀请码已存在！");
        }
        if(msg==2){
            model.addAttribute("regMsg","该邀请码不存在！");
        }
        model.addAttribute("info",info);
        return "invitationCode/list";
    }

    @LoginRequired
    @RequestMapping(path = "/fans/{userId}",method = RequestMethod.GET)
    public String getFans(Model model,@PathVariable("userId") int userId,
                          @RequestParam(defaultValue = "1" ) int page,
                          @RequestParam(defaultValue = "10") int limit){
        User loginUser = hostHolder.getUser();
        if (loginUser.getId()!=userId){
            return "redirect:/user/blog/"+userId;
        }
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("id",userId);
        userMap.put("username",loginUser.getUsername());
        userMap.put("type",loginUser.getType());
        callbackInfo(userId, model);
        PageHelper.startPage(page,limit);
        PageInfo<Attention> info = new PageInfo<>(attentionService.SelectByFocusId(userId));
        List<Map<String,Object>> users = new ArrayList<>();
        for (Attention attention:info.getList()){
            Map<String,Object> map = new HashMap<>();
            User user = userService.findUserById(attention.getUserId());
            Userinfo userinfo = userinfoService.selectInfoByUserId(attention.getUserId());
            map.put("username",user.getUsername());
            map.put("userwork",userinfo.getWork());
            map.put("userid",attention.getUserId());
            users.add(map);
        }
        Userinfo userinfo = userinfoService.selectInfoByUserId(userId);
        String work;
        if (userinfo==null||userinfo.getWork()==null){
            work = "";
        }else {
            work = userinfo.getWork();
        }
        model.addAttribute("work",work);
        model.addAttribute("user",userMap);
        model.addAttribute("users",users);
        model.addAttribute("info",info);
        int followeeCount = attentionService.CountAttentionByUserId(userId);
        model.addAttribute("followeeCount", followeeCount);
        int followerCount = attentionService.CountAttentionByFocusId(userId);
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("followLabel","fans");
        return "user/flow";
    }
    @LoginRequired
    @RequestMapping(path = "/follow/{userId}",method = RequestMethod.GET)
    public String getFollow(Model model,@PathVariable("userId") int userId,
                          @RequestParam(defaultValue = "1" ) int page,
                          @RequestParam(defaultValue = "10") int limit){
        User loginUser = hostHolder.getUser();
        if (loginUser.getId()!=userId){
            return "redirect:/user/blog/"+userId;
        }
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("id",userId);
        userMap.put("username",loginUser.getUsername());
        userMap.put("type",loginUser.getType());
        callbackInfo(userId, model);
        PageHelper.startPage(page,limit);
        PageInfo<Attention> info = new PageInfo<>(attentionService.SelectByUserId(userId));
        List<Map<String,Object>> users = new ArrayList<>();
        for (Attention attention:info.getList()){
            Map<String,Object> map = new HashMap<>();
            User user = userService.findUserById(attention.getFocusId());
            Userinfo userinfo = userinfoService.selectInfoByUserId(attention.getFocusId());
            map.put("username",user.getUsername());
            if (userinfo==null){
                map.put("userwork","");
            }
            else {
                map.put("userwork",userinfo.getWork());
            }
            map.put("userid",attention.getFocusId());
            users.add(map);
        }
        Userinfo userinfo = userinfoService.selectInfoByUserId(userId);
        String work;
        if (userinfo==null||userinfo.getWork()==null){
            work = "";
        }else {
            work = userinfo.getWork();
        }
        model.addAttribute("work",work);
        model.addAttribute("user",userMap);
        model.addAttribute("users",users);
        model.addAttribute("info",info);
        int followeeCount = attentionService.CountAttentionByUserId(userId);
        model.addAttribute("followeeCount", followeeCount);
        int followerCount = attentionService.CountAttentionByFocusId(userId);
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("followLabel","follow");
        return "user/flow";
    }
    @AdminRequired
    @RequestMapping(path = "/approval/{checkLabel}",method = RequestMethod.GET)
    public String approvalAll(Model model,@PathVariable int checkLabel//label 1234分别代表4种类型，0代表不审批
                    /*@RequestParam(defaultValue = "1" ) int page,
                    @RequestParam(defaultValue = "10") int limit*/){
        PageHelper.startPage(1,100);
        model.addAttribute("checkLabel",checkLabel);
        switch (checkLabel){
            case 1:
                PageInfo<Blog> info1 = new PageInfo<>( blogService.SelectAllByStatus(1));
                model.addAttribute("info",info1);
                return "blog/list";
            case 2:
                PageInfo<Question> info2 = new PageInfo<>(questionService.SelectAllByStatus(1));
                model.addAttribute("info",info2);
                return "question/list";
            case 3:
                PageInfo<Paper> info3 = new PageInfo<>(paperOfClassService.selectPaperOnlyByStatus(1));
                List<Paper> newPage3 = new ArrayList<>();
                if (info3.getList()!=null){
                    for (Paper p:info3.getList()){
                        User user=userService.findUserById(p.getUserid());
                        if (user==null){
                            //paperOfClassService.selectPaperOnlyByStatus(0);
                            user=userService.findUserById(p.getUserid());
                        }
                        p.setFilepath(user.getUsername());
                        //p.setFilepath(commentService.SelectUsernameById(p.getUserid()));
                        newPage3.add(p);
                    }
                }

                info3.setList(newPage3);
                model.addAttribute("info",info3);
                return "paper/list";
            case 4:
                PageInfo<Paper> info4 = new PageInfo<>(paperOfClassService.NewSelectByThreeStatus(3,7,9));
                List<Paper> newPage4 = new ArrayList<>();
                if (info4.getList()!=null){
                    for (Paper p:info4.getList()){
                        User user=userService.findUserById(p.getUserid());
                        if (user==null){
                            //paperOfClassService.selectPaperOnlyByStatus(0);
                            user=userService.findUserById(p.getUserid());
                        }
                        p.setFilepath(user.getUsername());
                        switch (p.getStatus()){
                            case 3:
                                p.setFatherid("代码");
                                break;
                            case 7:
                                p.setFatherid("软件");
                                break;
                            case 9:
                                p.setFatherid("文献");
                                break;
                        }
                        //p.setFilepath(commentService.SelectUsernameById(p.getUserid()));
                        newPage4.add(p);
                    }
                }
                info4.setList(newPage4);
                model.addAttribute("info",info4);
                return "source/list";
        }
        return "eror/404";
    }
    @LoginRequired
    @RequestMapping(path = "/attention/{label}/{id}",method = RequestMethod.GET)
    public String userLike(@PathVariable String label,@PathVariable int id,
                           @RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "limit", defaultValue = "10") int limit){
        User user = hostHolder.getUser();
        int relation = attentionService.FindById(user.getId(),id);
        if (relation==0 ){
            attentionService.InsertAttention(user.getId(),id);
        }
        else {
            attentionService.DeleteById(user.getId(),id);
        }
        return "redirect:/user/"+label+"/"+id+"?page="+page+"&limit="+limit;
        //return "redirect:/user/blog/"+user.getId();
    }
    @LoginRequired
    @RequestMapping(path = "/settingInfo",method = RequestMethod.GET)
    public String settingInfo(Model model){
        User user = hostHolder.getUser();
        callbackInfo(user.getId(), model);
        user.setPassword(null);
        user.setActivationCode(null);
        user.setSalt(null);
        user.setEmail(null);
        model.addAttribute("user", user);
        Userinfo userInfo = userinfoService.selectInfoByUserId(user.getId());
        if (userInfo==null){
            userInfo = new Userinfo();
            userInfo.setUserid(user.getId());
            userInfo.setNickname("");
            userInfo.setRealname("");
            userInfo.setQq("");
            userInfo.setWechat("");
            userInfo.setEmail("");
            userInfo.setPhone("");
            userInfo.setWork("");
            userInfo.setAddress("");
            userInfo.setIntro("");
        }
        model.addAttribute("userInfo", userInfo);
        int followeeCount = attentionService.CountAttentionByUserId(user.getId());
        model.addAttribute("followeeCount", followeeCount);
        int followerCount = attentionService.CountAttentionByFocusId(user.getId());
        model.addAttribute("followerCount", followerCount);
        return "user/setting";

    }
    public void callbackInfo(int userId, Model model) {
        //获取用户信息
        Userinfo userInfo = userinfoService.selectInfoByUserId(userId);
        //model.addAttribute("userInfo", userInfo);
        String major = null;
        if (userInfo!=null){
            major = userInfo.getWork();
        }
        //不为null 也不为""时
        if (major != null && !major.isEmpty()) {
            model.addAttribute("major", major);
        }
        //回显 问题数  博客数 回复数
        int qcount = questionService.CountByAuthorId(userId);
        int bcount = blogService.CountByAutherId(userId);
        int pcount = paperOfClassService.CountByAutherIdAndStatus(userId,0);
        int scount = paperOfClassService.CountByAutherIdAndStatus(userId,2);

        model.addAttribute("qcount", qcount);
        model.addAttribute("bcount", bcount);
        model.addAttribute("pcount", pcount);
        model.addAttribute("scount", scount);
    }
    @LoginRequired
    @RequestMapping(path = "/updateInfo",method = RequestMethod.POST)
    public String updateInfo(Userinfo userinfo){
        User user = hostHolder.getUser();
        userinfo.setUserid(user.getId());
        userinfoService.updateByUserid(userinfo);
        return "redirect:/user/blog/"+user.getId();
    }
    @LoginRequired
    @RequestMapping(path = "/blog/{id}",method = RequestMethod.GET)
    public String userBlog(@PathVariable int id, Model model,
                           @RequestParam(value = "searchResult",defaultValue = "0") int searchResult,
                           @RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "limit", defaultValue = "10") int limit){
        User user = userService.findUserById(id);
        if (user==null){
            throw new RuntimeException("该用户不存在!");
        }
        user.setPassword(null);
        user.setActivationCode(null);
        user.setSalt(null);
        user.setEmail(null);
        model.addAttribute("user", user);
        callbackInfo(id, model);
        PageHelper.startPage(page, limit);
        PageInfo<Blog> info = new PageInfo<>(blogService.SelectByAuthorId(id));
        model.addAttribute("info", info);
        int followeeCount = attentionService.CountAttentionByUserId(user.getId());
        model.addAttribute("followeeCount", followeeCount);
        int followerCount = attentionService.CountAttentionByFocusId(user.getId());
        model.addAttribute("followerCount", followerCount);
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            if(attentionService.FindById(hostHolder.getUser().getId(), id)!=0){
                hasFollowed = true;
            }
        }
        if (searchResult==1){
            model.addAttribute("regMsg","未查找到该用户！");
        }
        model.addAttribute("label2",page);
        model.addAttribute("label","blog");
        model.addAttribute("hasFollowed", hasFollowed);
        return "user/index";
    }
    @LoginRequired
    @RequestMapping(path = "/question/{id}",method = RequestMethod.GET)
    public String userQuestion(@PathVariable int id, Model model,
                           @RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "limit", defaultValue = "10") int limit){
        User user = userService.findUserById(id);
        if (user==null){
            throw new RuntimeException("该用户不存在!");
        }
        user.setPassword(null);
        user.setActivationCode(null);
        user.setSalt(null);
        user.setEmail(null);
        model.addAttribute("user", user);
        callbackInfo(id, model);
        PageHelper.startPage(page, limit);
        PageInfo<Question> info = new PageInfo<>(questionService.SelectByAuthorId(id));
        model.addAttribute("info", info);
        int followeeCount = attentionService.CountAttentionByUserId(user.getId());
        model.addAttribute("followeeCount", followeeCount);
        int followerCount = attentionService.CountAttentionByFocusId(user.getId());
        model.addAttribute("followerCount", followerCount);
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            if(attentionService.FindById(hostHolder.getUser().getId(), id)!=0){
                hasFollowed = true;
            }
        }
        model.addAttribute("label2",page);
        model.addAttribute("label","question");
        model.addAttribute("hasFollowed", hasFollowed);
        return "user/index";
    }
    @LoginRequired
    @RequestMapping(path = "/paper/{id}",method = RequestMethod.GET)
    public String userPaper(@PathVariable int id, Model model,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "limit", defaultValue = "10") int limit){
        User user = userService.findUserById(id);
        if (user==null){
            throw new RuntimeException("该用户不存在!");
        }
        user.setPassword(null);
        user.setActivationCode(null);
        user.setSalt(null);
        user.setEmail(null);
        model.addAttribute("user", user);
        callbackInfo(id, model);
        PageHelper.startPage(page, limit);
        PageInfo<Paper> info = new PageInfo<>(paperOfClassService.SelectByAuthorIdAndStatus(id,0));
        List<Paper> newPage = new ArrayList<>();
        if (info.getList()!=null){
            for (Paper p:info.getList()){
                User user1=userService.findUserById(p.getUserid());
                if (user1==null){
                    //paperOfClassService.selectPaperOnlyByStatus(0);
                    user1=userService.findUserById(p.getUserid());
                }
                p.setFilepath(user1.getUsername());
                //p.setFilepath(commentService.SelectUsernameById(p.getUserid()));
                newPage.add(p);
            }
        }
        info.setList(newPage);
        model.addAttribute("info", info);
        int followeeCount = attentionService.CountAttentionByUserId(user.getId());
        model.addAttribute("followeeCount", followeeCount);
        int followerCount = attentionService.CountAttentionByFocusId(user.getId());
        model.addAttribute("followerCount", followerCount);
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            if(attentionService.FindById(hostHolder.getUser().getId(), id)!=0){
                hasFollowed = true;
            }
        }
        model.addAttribute("label2",page);
        model.addAttribute("label","paper");
        model.addAttribute("hasFollowed", hasFollowed);
        return "user/index";
    }
    @LoginRequired
    @RequestMapping(path = "/source/{id}",method = RequestMethod.GET)
    public String userSource(@PathVariable int id, Model model,
                             @RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "limit", defaultValue = "10") int limit){
        User user = userService.findUserById(id);
        if (user==null){
            throw new RuntimeException("该用户不存在!");
        }
        user.setPassword(null);
        user.setActivationCode(null);
        user.setSalt(null);
        user.setEmail(null);
        model.addAttribute("user", user);
        callbackInfo(id, model);
        PageHelper.startPage(page, limit);
        PageInfo<Paper> info = new PageInfo<>(paperOfClassService.NewSelectByAutherIdAndThreeStatus(id,2,6,8));
        List<Paper> newPage = new ArrayList<>();
        if (info.getList()!=null){
            for (Paper p:info.getList()){
                User user1=userService.findUserById(p.getUserid());
                if (user1==null){
                    //paperOfClassService.selectPaperOnlyByStatus(0);
                    user1=userService.findUserById(p.getUserid());
                }
                p.setFilepath(user1.getUsername());
                switch (p.getStatus()){
                    case 2:
                        p.setFatherid("代码");
                        break;
                    case 6:
                        p.setFatherid("软件");
                        break;
                    case 8:
                        p.setFatherid("文献");
                        break;
                }
                //p.setFilepath(commentService.SelectUsernameById(p.getUserid()));
                newPage.add(p);
            }
        }
        info.setList(newPage);
        model.addAttribute("info", info);
        int followeeCount = attentionService.CountAttentionByUserId(user.getId());
        model.addAttribute("followeeCount", followeeCount);
        int followerCount = attentionService.CountAttentionByFocusId(user.getId());
        model.addAttribute("followerCount", followerCount);
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            if(attentionService.FindById(hostHolder.getUser().getId(), id)!=0){
                hasFollowed = true;
            }
        }
        model.addAttribute("label2",page);
        model.addAttribute("label","source");
        model.addAttribute("hasFollowed", hasFollowed);
        return "user/index";
    }


//！！！！！！！！！！！！！！！！！！！！！！！！！！！！！前期工作分界线
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
            //List<Paper> statusof1 = paperOfClassService.selectpaperByStatus(1,page.getOffset(),page.getLimit());
            List<Paper> statusof1 = paperOfClassService.selectpaperByStatus(1,page.getOffset(),page.getLimit());
            /*如果报错，研究下这里取出来的数据*/
            if (statusof1!=null){
                for (Paper paper:statusof1){
                    Map<String,Object> map = new HashMap<>();
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
    @LoginRequired
    @RequestMapping(path = "/search" ,method = RequestMethod.GET)
    public String search(String keyword, String fieldname, String sortname, Page page, Model model){
        if (sortname == null){
            sortname = "gmtcreate";
        }
        org.springframework.data.domain.Page<Paper> searchpapers = elasticsearchService.searchPaperByFieldname(keyword,0,fieldname,sortname,page.getCurrent() - 1,page.getLimit());
        List<Map<String ,Object>> papers = new ArrayList<>();
        if (searchpapers !=null){
            for (Paper paper:searchpapers){/*针对论文的搜索，是否需要写针对悬赏的搜索*/
                    Map<String,Object> map = new HashMap<>();
                    paper.setFatherid((userService.findUserById(paper.getUserid())).getUsername());
                    map.put("paper",paper);
                    papers.add(map);
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
       paper.setGmtcreate(dayy);
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
        paper.setGmtcreate(dayy);
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
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        String sqlFilepath = domain+"/user/file/"+fileName;
        Paper paper = paperOfClassService.selectByFilepath(sqlFilepath);
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //文件前缀
        String title = fileName.substring(0,fileName.lastIndexOf("."));
        String paperName = paper.getTitle()+suffix;
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 设置类型为文件
        response.setContentType("application/octet-stream");

        response.setHeader("Content-Disposition",
                "attachment; filename=" + new String(paperName.getBytes("gb2312"),"ISO8859-1"));
        //response.setCharacterEncoding("UTF-8");
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
            os.close();
            fis.close();
        } catch (IOException e) {
            logger.error("下载文件失败: " + e.getMessage());
        }

        paper.setDownloadcount(paper.getDownloadcount()+1);
        paperOfClassService.updateDownloadcount(paper.getId(),paper.getDownloadcount());
        paperRepository.save(paper);
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

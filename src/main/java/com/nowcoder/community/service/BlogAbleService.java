package com.nowcoder.community.service;

import com.nowcoder.community.dao.BlogAbleMapper;
import com.nowcoder.community.dao.GroupMapper;
import com.nowcoder.community.dao.GroupMemberMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogAbleService {
    @Autowired
    private BlogAbleMapper blogAbleMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private GroupMemberMapper groupMemberMapper;
    @Autowired
    private UserMapper userMapper;
    public void deleteBlogAbleById(int blogAbleId,int userId){
        BlogAble blogAble = blogAbleMapper.selectBlogAbleById(blogAbleId);
        if (blogAble!=null){
            if (blogAble.getUserId()==userId){
                blogAbleMapper.deleteById(blogAbleId);
            }
        }
    }
    public void passBlogAbleById(int blogAbleId,int userId){
        BlogAble blogAble = blogAbleMapper.selectBlogAbleById(blogAbleId);
        if (blogAble!=null){
            if (blogAble.getUserId()==userId){
                blogAble.setType(2);
                blogAbleMapper.updateBlogAble(blogAble);
            }
        }
    }
    public List<BlogAble> selectBlogAbleByUserIdAndType(int userId,int type){
        return blogAbleMapper.selectByUserIdAndType(userId,type);
    }
    public int deleteBlogAbleByBlogId(int blogId){
        return blogAbleMapper.deleteByBlogId(blogId);
    }
    public void insertApplyForBlog(int blogId,int blogUserId,int userId){
        List<BlogAble> blogAbleList = blogAbleMapper.selectByBlogId(blogId);
        if (!blogAbleList.isEmpty()){
            BlogAble blogAble1 = blogAbleMapper.selectByBlogIdAndTypeAndEntityId(blogId,0,userId);
            if (blogAble1==null){
                BlogAble blogAble = new BlogAble();
                blogAble.setEntityId(userId);
                blogAble.setBlogId(blogId);
                blogAble.setUserId(blogUserId);
                blogAble.setType(0);
                blogAbleMapper.insertBlogAble(blogAble);
            }
        }
    }
    public int ableToViewBlog(int blogId,int userId){
        List<BlogAble> blogAbleList = blogAbleMapper.selectByBlogId(blogId);
        int x = 1;
        if (!blogAbleList.isEmpty()){
            x = 0;
            for (BlogAble blogAble:blogAbleList){
                if (blogAble.getType()==2){
                    if (blogAble.getEntityId()==userId){
                        x = 1;
                        break;
                    }
                }
                if (blogAble.getType()==1){
                    GroupMember groupMember = groupMemberMapper.selectByGroupIdAndUserId(blogAble.getEntityId(),userId);
                    if (groupMember!=null){
                        x = 1;
                        break;
                    }
                }
            }
        }
        return x;
    }
    public void deleteBlogAble(int blogId,int type,int entityId){
        BlogAble blogAble = blogAbleMapper.selectByBlogIdAndTypeAndEntityId(blogId,type,entityId);
        if (blogAble!=null){
            blogAbleMapper.deleteById(blogAble.getId());
        }
    }
    public void addUserToBlogAble(String[] userList,int blogId,int blogUserId){
        for (int i = 1;i<userList.length;i++){
            User user = userMapper.selectByName(userList[i]);
            if (user == null){
                continue;
            }
            BlogAble blogAble = blogAbleMapper.selectByBlogIdAndTypeAndEntityId(blogId,2,user.getId());
            if (blogAble!=null){
                continue;
            }
            blogAble = new BlogAble();
            blogAble.setType(2);
            blogAble.setBlogId(blogId);
            blogAble.setUserId(blogUserId);
            blogAble.setEntityId(user.getId());
            blogAbleMapper.insertBlogAble(blogAble);
        }
    }
    public void addGroupToBlogAble(String[] groupList,int blogId,int userId,int blogUserId){
        for (int i = 1;i<groupList.length;i++){
            Group group = groupMapper.selectGroupByGroupNameAndOwnerId(groupList[i],userId);
            if (group == null){
                continue;
            }
            BlogAble blogAble = blogAbleMapper.selectByBlogIdAndTypeAndEntityId(blogId,1,group.getId());
            if (blogAble!=null){
                continue;
            }
            blogAble = new BlogAble();
            blogAble.setBlogId(blogId);
            blogAble.setType(1);
            blogAble.setUserId(blogUserId);
            blogAble.setEntityId(group.getId());
            blogAbleMapper.insertBlogAble(blogAble);
        }
    }
    public List<BlogAble> SelectByBlogAndType(int blogId,int type){
        return blogAbleMapper.selectByBlogIdAndType(blogId,type);
    }
    public int InsertBlogAble(BlogAble blogAble){return blogAbleMapper.insertBlogAble(blogAble);}
    public List<BlogAble> SelectByBlogId(int blogId){return blogAbleMapper.selectByBlogId(blogId);}
    public List<Group> selectGroupByOwnerId(int ownerId){return groupMapper.selectGroupByOwnerId(ownerId);}
    public Group setectGroupById(int id){return groupMapper.selectGroupById(id);}
    public List<GroupMember> selectGroupMemberByGroupId(int groupId){
        return groupMemberMapper.selectByGroupId(groupId);
    }
}

package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Loginticket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LoginticketMapper {
    Loginticket selectByTicket(String ticket);
    int insertTicket(Loginticket loginticket);
    int deleteByTicket(String ticket);
    @Update({
            "update login_ticket set status=#{status} where ticket=#{ticket}"
    })
    int updateStatus(String ticket,int status);
    /*sql可写动态sql*/
    /*这里其实用第二行就行了，多的是为了演示动态sql。<script>表示脚本，在里面就可以加if标签。“”要加\转译。条件成立加上and那字符串*/
    /*@Update({
            "<script>",
            "update login_ticket set status=#{status} where ticket=#{ticket} ",
            "<if test=\"ticket!=null\"> ",
            "and 1=1 ",
            "</if>",
            "</script>"
    })
    int updateStatus(String ticket, int status);*/
    /*修改状态位为1，即无效*/
}


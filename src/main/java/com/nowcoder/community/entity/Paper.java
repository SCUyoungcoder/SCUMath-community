package com.nowcoder.community.entity;


import org.attoparser.dom.INestableNode;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;


/*elasticsearth.在类上加这个注解，表示DiscussPost实体和discusspost索引有对应关系。。配置索引名字，类型，分片，及备份数*/
/*type将来会弱化，所以写一个固定的就行了.后面两个根据服务器处理能力配置*/
@Document(indexName = "paper",type = "_doc",shards =6 ,replicas =3 )
public class Paper {

    @Id
    private int id;
    @Field(type = FieldType.Integer)
    private int fatherid;
    @Field(type = FieldType.Integer)
    private int userid;
    /*拆分存储。存储时尽量多拆分，搜索时没必要拆分这么细....？？？英文分词器？？？*/
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String title;
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String content;
    @Field(type = FieldType.Text)
    private String filepath;
    @Field(type = FieldType.Integer)
    private int status;
    @Field(type = FieldType.Date)
    private Date createtime;
    @Field(type = FieldType.Integer)
    private int downloadcount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFatherid() {
        return fatherid;
    }

    public void setFatherid(int fatherid) {
        this.fatherid = fatherid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public int getDownloadcount() {
        return downloadcount;
    }

    public void setDownloadcount(int downloadcount) {
        this.downloadcount = downloadcount;
    }

    @Override
    public String toString() {
        return "paper{" +
                "id=" + id +
                ", father_id=" + fatherid +
                ", user_id='" + userid + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", file_path='" + filepath + '\'' +
                ", status=" + status +
                ", create_time=" + createtime +
                ", download_count=" + downloadcount +
                '}';
    }
}
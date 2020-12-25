package com.nowcoder.community.service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.stereotype.Service;
import com.nowcoder.community.dao.elasticsearth.PaperRepository;
import com.nowcoder.community.entity.Paper;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ElasticsearchClassService {
    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;//高亮显示，应该不需要

    public void savepaper(Paper post){
        paperRepository.save(post);
    }//向ES里提交论文信息
    public void deletepaper(int id){
        paperRepository.deleteById(id);
    }

    //Page是Spring提供的一种类型  ； 分页条件：*current当前页码，从0开始。limit每页最大多少条数据*/
    public Page<Paper> searchPaperByClass(String classname,int current,int limit){
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(classname,"fatherid"))
                .withSort(SortBuilders.fieldSort("downloadcount").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createtime").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))
                .withPageable(PageRequest.of(current,limit ))
                .build();

        return elasticsearchTemplate.queryForPage(searchQuery, Paper.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                SearchHits hits = response.getHits();//获取命中的数据
                if (hits.getTotalHits()<=0){
                    return null;
                }//对它做了一个判断

                List<Paper> list = new ArrayList<>();//实例化一个集合
                for (SearchHit hit:hits){
                    Paper post = new Paper();//集合内实例化了一个实体

                    //根据命中的数据去构造这个实体
                    String id = hit.getSourceAsMap().get("id").toString();
                    post.setId(Integer.valueOf(id));

                    String fatherid = hit.getSourceAsMap().get("fatherid").toString();
                    post.setFatherid(fatherid);

                    String userid = hit.getSourceAsMap().get("userid").toString();
                    post.setUserid(Integer.valueOf(userid));

                    String title = hit.getSourceAsMap().get("title").toString();
                    post.setTitle(title);

                    String content = hit.getSourceAsMap().get("content").toString();
                    post.setContent(content);

                    /*String filepath = String.valueof(hit.getSourceAsMap().get("filepath"));*/
                    String f = String.valueOf(hit.getSourceAsMap().get("filepath"));
                    post.setFilepath(f);

                    String status = hit.getSourceAsMap().get("status").toString();
                    post.setStatus(Integer.valueOf(status));

                    /*String createtime = hit.getSourceAsMap().get("createtime").toString();*/
                    String createtime = String.valueOf(hit.getSourceAsMap().get("createtime"));
                    /*ParsePosition pos = new ParsePosition(8);
                    Date createtime2 = formatter*/
                    post.setCreatetime(new Date(Long.valueOf(createtime)));

                    String downloadcount = hit.getSourceAsMap().get("downloadcount").toString();
                    post.setDownloadcount(Integer.valueOf(downloadcount));

                    list.add(post);
                }
                return new AggregatedPageImpl(list,pageable,hits.getTotalHits(),response.getAggregations(),
                        response.getScrollId(),hits.getMaxScore());
            }
        });
    }
}

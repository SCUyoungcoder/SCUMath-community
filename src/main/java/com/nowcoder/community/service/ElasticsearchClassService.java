package com.nowcoder.community.service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
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
import org.elasticsearch.index.query.QueryBuilders;  //boolQuery，可以设置多个条件的查询方式

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ElasticsearchClassService {
    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;//高亮显示，应该不需要

    public void savepaper(Paper paper){
        paperRepository.save(paper);
    }//向ES里提交论文信息
    public void deletepaper(int id){
        paperRepository.deleteById(id);
    }

    public List<Paper> NewSearchPaperByClassAndThreeStatus(String classname,int status1,int status2,int status3){
        Pageable pageable = new PageRequest(0,1000);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("fatherid",classname))
                        .must(QueryBuilders.boolQuery()
                                .should(QueryBuilders.matchQuery("status",status1))
                                .should(QueryBuilders.matchQuery("status",status2))
                                .should(QueryBuilders.matchQuery("status",status3))))
                /*.withSort(SortBuilders.fieldSort("downloadcount").order(SortOrder.DESC))*/
                .withSort(SortBuilders.fieldSort("gmtcreate").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))
                .withPageable(pageable)
                .build();
        return elasticsearchTemplate.queryForList(searchQuery,Paper.class);
    }
    public List<Paper> searchPaperByClassAndStatus(String classname,int status){
        Pageable pageable = new PageRequest(0,1000);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("fatherid",classname)).must(QueryBuilders.matchQuery("status",status)))
                /*.withSort(SortBuilders.fieldSort("downloadcount").order(SortOrder.DESC))*/
                .withSort(SortBuilders.fieldSort("gmtcreate").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))
                .withPageable(pageable)
                .build();
        return elasticsearchTemplate.queryForList(searchQuery,Paper.class);
    }
    //提供一个搜索方法，返回Page，里面封装的是Paper的实体
    //Page是Spring提供的一种类型  ； 分页条件：*current当前页码，从0开始。limit每页最大多少条数据*/
    public Page<Paper> searchPaperByClass(String classname,int current,int limit){
        //【弃用】FunctionScoreQueryBuilder SearchQueryBuilder = QueryBuilders.functionScoreQuery()
                  //.add(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("fatherid",classname)))
                  //.add(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("status","0")));
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("fatherid",classname)).must(QueryBuilders.matchQuery("status","0")))//构造查询条件——classname，在fatherid里面查;且status==0的论文
                .withSort(SortBuilders.fieldSort("downloadcount").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("gmtcreate").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))
                .withPageable(PageRequest.of(current,limit ))//分页展示【2021.1.15，在业务层Service不再做分页而是取到所有的查询数据，并将status==0的论文传给表现层controller】
                .build();
        return elasticsearchTemplate.queryForPage(searchQuery, Paper.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                SearchHits hits = response.getHits();//获取命中的数据
                if (hits.getTotalHits()<=0){
                    return null;
                }//判断是否查询到数据（数据量是否大于0）

                //System.out.print(hits);

                List<Paper> list = new ArrayList<>();//实例化一个集合，把数据封装到这个集合里
                for (SearchHit hit:hits){//遍历命中的数据，找到并作处理，处理完放进集合里
                        Paper post = new Paper();//实例化了一个实体，每得到一个数据，将其包装到实体类中，然后返回
                        //根据命中的数据去构造这个实体
                        String id = hit.getSourceAsMap().get("id").toString();
                        post.setId(Integer.valueOf(id));//先作toString，再转为Int

                        String fatherid = hit.getSourceAsMap().get("fatherid").toString();
                        post.setFatherid(fatherid);//本身就是Spring，不用转了

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
                        String gmtcreate = String.valueOf(hit.getSourceAsMap().get("gmtcreate"));
                    /*ParsePosition pos = new ParsePosition(8);
                    Date createtime2 = formatter*/
                        post.setGmtcreate(new Date(Long.valueOf(gmtcreate)));

                        String downloadcount = hit.getSourceAsMap().get("downloadcount").toString();
                        post.setDownloadcount(Integer.valueOf(downloadcount));

                        list.add(post);//处理完的数据加进集合里

                }

                //System.out.print(list);//测试用

                return new AggregatedPageImpl(list,pageable,
                        hits.getTotalHits(),response.getAggregations(), response.getScrollId(),hits.getMaxScore());//返回的类型是AggregatedPage，所以需要传很多参数来建立
            }
        });
    }
}

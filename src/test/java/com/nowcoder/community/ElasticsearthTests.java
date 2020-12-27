package com.nowcoder.community;

import com.nowcoder.community.dao.PaperMapper;
import com.nowcoder.community.dao.elasticsearth.PaperRepository;
import com.nowcoder.community.entity.Paper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ElasticsearthTests {
    /*Elasticsearth数据来源是MySQL的数据获取后转存到Elasticsearth的*/
    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void esinsert(){
        /*从数据库中获取数据插入se中，若插入时发现没有这个索引会自动创建。save每次插入一条数据*/
        /*主键如果相同则覆盖*/
        paperRepository.save(paperMapper.selectById(5));
        /*paperRepository.save(paperMapper.selectById(3));
        paperRepository.save(paperMapper.selectById(4));*/
    }
    @Test
    public void esinsertList(){
        paperRepository.saveAll(paperMapper.selecyByUserid(112));
        paperRepository.saveAll(paperMapper.selecyByUserid(111));
    }
    @Test
    public void esupdate(){
        Paper paper = paperMapper.selectById(2);
        paper.setContent("计算数学++应用数学");
        paperRepository.save(paper);
    }
    @Test
    public void sedelete(){
        /*paperRepository.deleteById(2);*/

        /*删除所有数据*/
        paperRepository.deleteAll();
    }
    @Test
    public void searchByRepository(){
        /*6.4，42min搜索后可排序(方式设置)/分页(方式设置)/高亮显示(返回的搜索词两边加标签)*/
        /*withQuery构造搜索条件;withSort构造排序方式SortOrder.DESC(倒序);withPageable构造分页条件page表明这是第几页，size表明这一页有几条*/
        /*withHighlightFields指定哪些属性字段高亮显示，对哪些词高亮显示，怎么高亮显示。在html中设置cssstyle*/
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                /*text里"计算数学"被拆分为“计算”和“数学”进行搜索,英文必须严格按空格分割*/
                .withQuery(QueryBuilders.multiMatchQuery("basics application","title","content"))
                .withSort(SortBuilders.fieldSort("downloadcount").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createtime").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0,10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("<em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("<em>")
                ).build();

        // elasticTemplate.queryForPage(searchQuery, class, SearchResultMapper)
        // 底层获取得到了高亮显示的值, 但是没有返回,SearchResultMapper出现问题.（即没有高亮功能，后一个测试实现高亮）
        Page<Paper> page = paperRepository.search(searchQuery);

        /*查到多少条数据*/
        System.out.println(page.getTotalElements());
        /*按照当前分页方式有多少页*/
        System.out.println(page.getTotalPages());
        /*当前处在第几页*/
        System.out.println(page.getNumber());
        /*每一页最多显示多少数据*/
        System.out.println(page.getSize());
        /*遍历输出，即使返回结果大于10也最大输出10行*/
        for (Paper post : page) {
            System.out.println(post);
        }
    }
    @Test/*在搜索项加上标签，高亮基本用不上...数据类型错误，不影响暂时不想弄了。。*/
    public void searchByTemplate(){
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                /*text里"计算数学"被拆分为“计算”和“数学”进行搜索,英文必须严格按空格分割*/
                .withQuery(QueryBuilders.multiMatchQuery("basics application","title","content"))
                .withSort(SortBuilders.fieldSort("downloadcount").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createtime").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0,10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("<em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("<em>")
                ).build();
        /*自己实现SearchResultMapper接口*/
        Page<Paper> page = elasticsearchTemplate.queryForPage(searchQuery, Paper.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                SearchHits hits = response.getHits();
                if (hits.getTotalHits()<=0){
                    return null;
                }
                List<Paper> list = new ArrayList<>();
                for (SearchHit hit:hits){
                    Paper post = new Paper();

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

                    /*处理高亮显示的结果*/
                    HighlightField titleField = hit.getHighlightFields().get("title");
                    if (titleField != null){
                        /*title里可能匹配多段，取第一段高亮*/
                        post.setTitle(titleField.getFragments()[0].toString());
                    }
                    HighlightField contentField = hit.getHighlightFields().get("content");
                    if (contentField != null){
                        /*title里可能匹配多段，取第一段高亮*/
                        post.setTitle(contentField.getFragments()[0].toString());
                    }
                    list.add(post);
                }
                return new AggregatedPageImpl(list,pageable,hits.getTotalHits(),response.getAggregations(),
                        response.getScrollId(),hits.getMaxScore());
            }
        });
        /*查到多少条数据*/
        System.out.println(page.getTotalElements());
        /*按照当前分页方式有多少页*/
        System.out.println(page.getTotalPages());
        /*当前处在第几页*/
        System.out.println(page.getNumber());
        /*每一页最多显示多少数据*/
        System.out.println(page.getSize());
        /*遍历输出，即使返回结果大于10也最大输出10行*/
        for (Paper post : page) {
            System.out.println(post);
        }
    }

}


















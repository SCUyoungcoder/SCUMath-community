package com.nowcoder.community.service;

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
import org.springframework.stereotype.Service;

/*6.6  13:00min顺序看下来*/
@Service
public class ElasticsearchService {
    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public void savepaper(Paper post){
        paperRepository.save(post);
    }
    public void deletepaper(int id){
        paperRepository.deleteById(id);
    }
    /*current当前页码，从0开始。limit每页最大多少个*/
    public Page<Paper> searchPaperByFieldname(String keyword,String fieldname , String sortname, int current,int limit){
        /*6.4，42min搜索后可排序(方式设置)/分页(方式设置)/高亮显示(返回的搜索词两边加标签)*/
        /*withQuery构造搜索条件;withSort构造排序方式SortOrder.DESC(倒序);withPageable构造分页条件page表明这是第几页，size表明这一页有几条*/
        /*withHighlightFields指定哪些属性字段高亮显示，对哪些词高亮显示，怎么高亮显示。在html中设置cssstyle*/
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                /*text里"计算数学"被拆分为“计算”和“数学”进行搜索,英文必须严格按空格分割*/
                .withQuery(QueryBuilders.multiMatchQuery(keyword,fieldname))
                .withSort(SortBuilders.fieldSort(sortname).order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))
                .withPageable(PageRequest.of(current,limit ))
                .build();

        return paperRepository.search(searchQuery);
    }


}

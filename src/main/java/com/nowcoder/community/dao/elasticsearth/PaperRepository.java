package com.nowcoder.community.dao.elasticsearth;

import com.nowcoder.community.entity.Paper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaperRepository extends ElasticsearchRepository<Paper,Integer>/*声明处理的实体类与主键*/ {

}

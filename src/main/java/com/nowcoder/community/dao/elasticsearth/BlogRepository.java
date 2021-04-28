package com.nowcoder.community.dao.elasticsearth;


import com.nowcoder.community.entity.Blog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends ElasticsearchRepository<Blog,Integer> {
}

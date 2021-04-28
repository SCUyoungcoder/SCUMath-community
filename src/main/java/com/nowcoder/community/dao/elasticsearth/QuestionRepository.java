package com.nowcoder.community.dao.elasticsearth;


import com.nowcoder.community.entity.Question;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends ElasticsearchRepository<Question,Integer> {
}

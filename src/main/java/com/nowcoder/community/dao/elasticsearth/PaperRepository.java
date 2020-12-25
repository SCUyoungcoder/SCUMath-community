package com.nowcoder.community.dao.elasticsearth;

import com.nowcoder.community.entity.Paper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

//数据访问层的代码（ES可看作一个特殊的数据库），加上Spring提供的针对数据访问层的注解@Repository(@Mapper是Mybatis专有注解)
@Repository
public interface PaperRepository extends ElasticsearchRepository<Paper,Integer>/*声明处理的实体类与主键类型*/ {
//此接口不用声明任何方法，只用继承于ElasticsearchRepository 这个Spring给我们提供的默认接口就行
}

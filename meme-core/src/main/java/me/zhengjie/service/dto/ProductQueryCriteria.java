
package me.zhengjie.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductQueryCriteria {
    private String id;

    @Query(type = Query.Type.EQUAL)
    private String productId;

    @Query(type = Query.Type.INNER_LIKE)
    private String productName;

    @Query
    private String phone;
    @Query
    private Integer type;

    @Query
    private Integer status;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
//
//    @Query(type = Query.Type.BETWEEN)
//    private List<Timestamp> todayDate;
}
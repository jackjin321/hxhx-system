
package me.zhengjie.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.zhengjie.annotation.Query;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductFilterQueryCriteria {

    @Query(type = Query.Type.NOT_IN)
    private List<Long> id;

    @Query(type = Query.Type.INNER_LIKE)
    private String productName;

}
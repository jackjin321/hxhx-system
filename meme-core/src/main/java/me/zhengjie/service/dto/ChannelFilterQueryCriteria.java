
package me.zhengjie.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
public class ChannelFilterQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private String channelName;

    @Query(type = Query.Type.INNER_LIKE)
    private String channelId;

    @Query(type = Query.Type.INNER_LIKE)
    private String productName;

    @Query(type = Query.Type.INNER_LIKE)
    private String productId;

    @Query(type = Query.Type.INNER_LIKE)
    private String planName;

    @Query(type = Query.Type.INNER_LIKE)
    private String planId;

//    @Query
//    private Boolean enabled;

    private Integer page;
    private Integer limit;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> todayDate;
}
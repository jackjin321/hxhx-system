
package me.zhengjie.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
public class ChannelQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private String channelName;

    @Query(type = Query.Type.INNER_LIKE)
    private Long channelId;

    private Integer page;
    private Integer limit;


    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;

}
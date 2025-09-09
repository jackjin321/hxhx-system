package me.zhengjie.modules.union.dto;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.IdUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestIdUtil {


    public String buildRequestId() {
        String uuid = UUID.fastUUID().toString();
        String prefix = uuid.split(StrPool.DASHED)[0];
        long snowflakeNextId = IdUtil.getSnowflakeNextId();
        return prefix + "-" + snowflakeNextId;
    }


    public String buildRequestId(String prefix) {
        return prefix + "-" + IdUtil.getSnowflakeNextId();
    }

}

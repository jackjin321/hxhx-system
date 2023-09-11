package me.zhengjie.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@Slf4j
public class MyIdGeneratorConfig implements IdentifierGenerator {

    /**
     * 终端ID
     */
    public static long WORKER_ID = 1;

    /**
     * 数据中心id
     */
    public static long DATACENTER_ID = 1;

//    private Snowflake snowflake = IdUtil.getSnowflake(WORKER_ID, DATACENTER_ID);
    private Snowflake snowflake;

    @PostConstruct
    public void init() {
//        WORKER_ID = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
//        log.info("当前机器的workId:{}", WORKER_ID);
        Long workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr()) >> 16 & 31;
        Long dataCenterId = 1L;
        snowflake = IdUtil.getSnowflake(workerId, dataCenterId);
    }

    public synchronized long snowflakeId() {
        return snowflake.nextId();
    }

    public synchronized long snowflakeId(long workerId, long datacenterId) {
        Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);
        return snowflake.nextId();
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {
        return snowflakeId(WORKER_ID, DATACENTER_ID);
    }
}

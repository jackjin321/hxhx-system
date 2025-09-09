package me.zhengjie.constants;


public interface RedisKeyPattern {

    String MODULE_LOAN = "module_loan:";
    /**
     * h5渠道撞库结果缓存，LOAN
     */
    String LOAN_H5_CHANNEL_MATCH_RESULT_CACHE = MODULE_LOAN + "h5_channel_match_result_cache:%s";

    String LOAN_H5_CHANNEL_MATCH_ACTION_CHECK = MODULE_LOAN + "h5_channel_match_action_check:%s_%s";

}

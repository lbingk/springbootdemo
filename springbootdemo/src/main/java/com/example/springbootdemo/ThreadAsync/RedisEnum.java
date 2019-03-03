package com.example.springbootdemo.ThreadAsync;

import lombok.Data;

/**
 * Redis 枚举类
 *
 * @author yclimb
 * @date 2018/4/19
 */
public enum RedisEnum {

    /**
     * 数据字典Service - 根据字典类型查询字典数据
     */
    ORDER_SALORDERHEADSERVICE_AUTOCREATECUSCODE(
            RedisUtils.KEY_PREFIX, "SalOrderHeadService", "autoCreateCusCode", "需要创建的客户Redis缓存");

    /**
     * 系统标识
     */
    private String keyPrefix;
    /**
     * 模块名称
     */
    private String module;
    /**
     * 方法名称
     */
    private String func;
    /**
     * 描述
     */
    private String remark;

    RedisEnum(String keyPrefix, String module, String func, String remark) {
        this.keyPrefix = keyPrefix;
        this.module = module;
        this.func = func;
        this.remark = remark;
    }
    public String getKeyPrefix(){
        return this.keyPrefix;
    }
    public String getModule(){
        return this.module;
    }
    public String getFunc(){
        return this.func;
    }
    public String getRemark(){
        return this.remark;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
    public void setModule(String module) {
        this.module = module;
    }
    public void setFunc(String func) {
        this.func = func;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
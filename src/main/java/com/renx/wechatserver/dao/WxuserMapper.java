package com.renx.wechatserver.dao;

import com.renx.wechatserver.model.Wxuser;
import org.apache.ibatis.annotations.Param;

public interface WxuserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Wxuser record);

    int insertSelective(Wxuser record);

    Wxuser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Wxuser record);

    int updateByPrimaryKey(Wxuser record);

    /**
     * 根据条件查询Wxuser
     * @param innerAppid 内部APPID
     * @param openId 微信OPENID
     * @return
     */
    Wxuser selectOne(@Param("innerAppid")String innerAppid, @Param("openId")String openId);

    /**
     * 根据身份证号查询
     * @param innerAppid 内部APPID
     * @param idNo 身份证号
     * @return
     */
    Wxuser findByIdNo(@Param("innerAppid") String innerAppid, @Param("idNo")String idNo);
}

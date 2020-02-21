package com.renx.wechatserver.dao;

import com.renx.wechatserver.model.WechatMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WxmenuMapper {
    int deleteByPrimaryKey(Integer menuId);

    int insert(WechatMenu record);

    int insertSelective(WechatMenu record);

    WechatMenu selectByPrimaryKey(Integer menuId);

    List<WechatMenu> selectByGTLevel(@Param("innerAppid") String innerAppid, @Param("level") Integer level);
    List<WechatMenu> selectByLTLevel(@Param("innerAppid") String innerAppid, @Param("level") Integer level);

    int updateByPrimaryKeySelective(WechatMenu record);

    int updateByPrimaryKey(WechatMenu record);
}

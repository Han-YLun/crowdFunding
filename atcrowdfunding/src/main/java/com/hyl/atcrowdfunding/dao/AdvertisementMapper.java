package com.hyl.atcrowdfunding.dao;

import com.hyl.atcrowdfunding.model.Advertisement;
import java.util.List;

public interface AdvertisementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Advertisement record);

    Advertisement selectByPrimaryKey(Integer id);

    List<Advertisement> selectAll();

    int updateByPrimaryKey(Advertisement record);
}
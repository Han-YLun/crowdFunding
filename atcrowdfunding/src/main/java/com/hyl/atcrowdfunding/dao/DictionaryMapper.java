package com.hyl.atcrowdfunding.dao;

import com.hyl.atcrowdfunding.model.Dictionary;

import java.util.List;

public interface DictionaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Dictionary record);

    Dictionary selectByPrimaryKey(Integer id);

    List<Dictionary> selectAll();

    int updateByPrimaryKey(Dictionary record);
}
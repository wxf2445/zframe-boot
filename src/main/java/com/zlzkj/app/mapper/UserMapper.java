package com.zlzkj.app.mapper;

import com.zlzkj.app.model.Row;
import com.zlzkj.app.model.index.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectByRoleId(Integer roleId);

    List<Row> selectByMap(Map<String, Object> map);

    int countByMap(Map<String, Object> map);

    int updateByPrimaryKey(User record);

    User selectByUserName(String username);
}
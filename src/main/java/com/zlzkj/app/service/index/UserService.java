package com.zlzkj.app.service.index;

import com.zlzkj.app.model.Row;
import com.zlzkj.app.model.index.User;
import com.zlzkj.app.util.Page;

import java.util.List;
import java.util.Map;

public interface UserService {
    public Integer delete(int id);

    public Integer update(User entity);

    public Integer save(User entity);

    public User findById(int id);

    public List<Row> findByMap(Map<String,Object> map);

    public Page findByMap(Map<String,Object> parmMap, Integer nowPage);

    public User findByUserName(String username);

}
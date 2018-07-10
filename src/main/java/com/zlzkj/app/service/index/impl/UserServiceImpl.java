package com.zlzkj.app.service.index.impl;

import com.zlzkj.app.model.Row;
import com.zlzkj.app.model.index.User;
import com.zlzkj.app.mapper.UserMapper;
import com.zlzkj.app.service.index.UserService;
import com.zlzkj.app.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

//业务层接口实现
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {


    @Value("${page.size}")
    private int PAGE_SIZE;

    @Autowired
    private UserMapper mapper;

    @Override public User findById(int id ){

        User user = new User(id,"测试");
        mapper.updateByPrimaryKey(user);
        if(id == 0){
            throw new RuntimeException("ID 不能为 0 " );
        }
        return mapper.selectByPrimaryKey(id);
    }
    @Override public Integer delete(int id){
        try{
            return mapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            throw new RuntimeException("删除失败");
        }
    }

    @Override public Integer update(User entity){
        return mapper.updateByPrimaryKey(entity);
    }

    @Override public Integer save(User entity) {
        return mapper.insert(entity);
    }


    @Override public List<Row> findByMap(Map<String,Object> map){
        return mapper.selectByMap(map);
    }

    @Override public Page findByMap(Map<String,Object> parmMap, Integer nowPage){
        if(nowPage == null) nowPage = 1;
        parmMap.put("start",(nowPage-1)*PAGE_SIZE);
        parmMap.put("end",PAGE_SIZE);
        //PageHelper.startPage(1, 3);

        return new Page(findByMap(parmMap),mapper.countByMap(parmMap),nowPage,PAGE_SIZE);
    }

    @Override
    public User findByUserName(String username) {
        return mapper.selectByUserName(username);
    }
}
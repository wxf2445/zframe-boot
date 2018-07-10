package com.zlzkj.app.controller.json;

import com.zlzkj.app.model.index.User;
import com.zlzkj.app.service.index.UserService;
import com.zlzkj.app.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


@RestController
public class IndexJsonApplication {

    @Autowired
    private UserService userService;
    @RequestMapping("/get")
    User getUser(Integer id)  {
        return userService.findById(id);
    }

    @RequestMapping("/check")
    User check(Integer id)  {
        return userService.findById(id);
    }

    @RequestMapping("/findByPage")
    Page findByMap(Integer id, @RequestParam(defaultValue = "1") Integer nowPage)  {
        return userService.findByMap(new HashMap<>(),nowPage);
    }
}
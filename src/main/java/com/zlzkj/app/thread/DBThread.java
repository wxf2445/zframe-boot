package com.zlzkj.app.thread;

import com.zlzkj.app.model.index.User;
import com.zlzkj.app.service.index.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

//线程池中工作的线程
@Component
@Scope("prototype")//spring 多例
public class DBThread implements Runnable {
    private String msg;
    private Logger log = LoggerFactory.getLogger(DBThread.class);

    @Autowired
    UserService userService;



    @Override
    public void run() {
        //模拟在数据库插入数据
        User user = new User();
        user.setName("name");
        //userService.test(msg);
        //systemLogService.insert(systemlog);
        log.info("insert-> " + msg);
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
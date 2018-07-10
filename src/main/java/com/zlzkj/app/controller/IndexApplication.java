package com.zlzkj.app.controller;

import com.zlzkj.app.thread.ThreadPoolManager;
import com.zlzkj.app.util.FileUtils;
import com.zlzkj.app.util.Md5Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class IndexApplication {
    @Autowired
    ThreadPoolManager tpm;

    @Value("${web.upload-path}")
    private String FILE_PATH;

    @Value("${application.hello:Hello Jsp}")
    private String hello = "Hello Jsp";


    @RequestMapping("/index/pool")
    public
    @ResponseBody
    Object test() {
        for (int i = 0; i < 500; i++) {
            //模拟并发500条记录
            tpm.processOrders(Integer.toString(i));
        }

        return "ok";
    }

    @RequestMapping(value = {"/","/index","/index/index"})
    public String hello(Model model) {
        Subject user = SecurityUtils.getSubject();
        if(user.isRemembered()||user.isAuthenticated()){
            model.addAttribute("hello", hello);
            return "index/index";
        }else{
            return "index/login";
        }
    }

    @RequestMapping(value = "/login")
    public String login(Model model,String account,String password,boolean rememberMe) {

        Subject user = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(account, Md5Util.getMD5(account+password));

        token.setRememberMe(rememberMe);
		/*String code = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
		if(!submitCode.equals(code)){
			redirectAttributes.addFlashAttribute("errors", "submit_code_error");
			return "redirect:/";
		}*/
        try {
            user.login(token);

			/*session.setAttribute("CUR_USER",shiroUserService.getLoginUser());*/
            System.out.println("Login success!");
        } catch (AuthenticationException e) {
            //redirectAttributes.addFlashAttribute("errors", "account_or_password_error");
            e.printStackTrace();
        }

        return "redirect:/";
    }

    @RequestMapping(value = "/logout")
    public String logout(Model model) {
        model.addAttribute("hello", "logout");
        return "index/index";
    }

    @RequestMapping(value = "index/login")
    public String indexLogin(Model model) {
        model.addAttribute("hello", hello);
        return "index/login";
    }

    @RequestMapping(value = "/greeting")
    public String greeting(Model model) {
        model.addAttribute("title", "欢迎使用jsp");
        return "index/greeting";
    }

    @RequestMapping(value = "/file_upload")
    public String upload(Model model, MultipartFile file) {
        try {
            FileUtils.write(file.getInputStream(),FILE_PATH + "test/" + file.getOriginalFilename());
        }catch (IOException e){
            e.printStackTrace();
        }
        model.addAttribute("title", "上传成功！文件目录" + FILE_PATH + "/test/" + file.getOriginalFilename());
        //System.out.println("上传成功！文件目录" + FILE_PATH + "/test/" + file.getOriginalFilename());
        model.addAttribute("filePath", "/test/" + file.getOriginalFilename());
        return "index/greeting";
    }


}
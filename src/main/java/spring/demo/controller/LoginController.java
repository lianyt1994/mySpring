package spring.demo.controller;

import spring.annotation.MyAutowired;
import spring.annotation.MyController;
import spring.annotation.MyRequestMapping;
import spring.annotation.MyRequstParam;
import spring.demo.service.UserService;
import spring.enums.RequestMethod;
import spring.model.User;
import spring.springmvc.MyModelAndView;

import java.util.HashMap;
import java.util.Map;

@MyController
public class LoginController {

    @MyAutowired
    private UserService userService;


    //测试用的@MyRequstParam(value = "userName") String userName,  @MyRequstParam(value = "passWord") Integer passWord
    //返回值只支持MyModelAndView，数据模型和视图模型相结合
    @MyRequestMapping(value = "/hello", method = RequestMethod.POST)
    public MyModelAndView login(@MyRequstParam(value = "userName") String userName, @MyRequstParam(value = "passWord") Integer passWord) {

        MyModelAndView myModelAndView = new MyModelAndView("success");

        Map<String,Object> myModel = new HashMap<>();
        myModel.put("userName", userName);
        myModel.put("passWord", passWord);
        myModelAndView.setModelMap(myModel);

        return myModelAndView;

    }

    @MyRequestMapping("/hello22")
    public String test() {
        return "success";
    }

}

package spring.demo.service.impl;

import spring.annotation.MyAutowired;
import spring.annotation.MyService;
import spring.demo.repository.RegisterDao;
import spring.demo.repository.UserDao;
import spring.demo.repository.UserMapper;
import spring.demo.service.UserService;
import spring.model.User;

@MyService
public class UserServiceImpl implements UserService {

    @MyAutowired
    private UserDao userDao;

    @MyAutowired
    private RegisterDao registerDao;

    /*********************************/
    @MyAutowired
    private UserMapper userMapper;
    /*********************************/

    @Override
    public User queryUser(String userName, String passWord) {
        return userMapper.queryUser(userName,passWord);
    }
}

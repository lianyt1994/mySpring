package spring.demo.service;


import spring.model.User;

public interface UserService {

     User queryUser(String userName, String passWord);
}

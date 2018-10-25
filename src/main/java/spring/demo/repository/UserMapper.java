package spring.demo.repository;

import spring.model.User;

public interface UserMapper {

    User queryUser(String userName, String passWord);

}

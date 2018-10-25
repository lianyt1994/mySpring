package spring.demo.repository.impl;

import spring.annotation.MyRepository;
import spring.demo.repository.UserDao;

@MyRepository
public class UserDaoImpl implements UserDao {

    @Override
    public void test() {
        System.out.println("我是UserDao");
    }
}

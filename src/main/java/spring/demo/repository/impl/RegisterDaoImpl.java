package spring.demo.repository.impl;

import spring.annotation.MyRepository;
import spring.demo.repository.RegisterDao;

@MyRepository
public class RegisterDaoImpl implements RegisterDao {

    @Override
    public void register() {
        System.out.println("我是RegisterDao");
    }
}

package com.itheima.dao.impl;

import com.itheima.dao.UserDao;
import com.itheima.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Properties;

@Repository("userDao")
public class UserDaoImpl implements UserDao {


    private List<String> strList;
    private Map<String, User> userMap;
    private Properties properties;

    public void setStrList(List<String> strList) {
        this.strList = strList;
    }

    public void setUserMap(Map<String, User> userMap) {
        this.userMap = userMap;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Value("${userName}")
    private String username;

    @Value("${age}")
    private String age;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public UserDaoImpl(){
        System.out.println("UserDaoImpl创建。。。。");
    }
    public void save() {
        System.out.println(username+"=="+age);
        System.out.println(strList);
        System.out.println(userMap);
        System.out.println(properties);
        System.out.println("save running");
    }

    public void init(){
        System.out.println("初始化方法。。。。。");
    }

    public void destory(){
        System.out.println("销毁方法。。。。。");
    }
}

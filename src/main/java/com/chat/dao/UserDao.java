package com.chat.dao;

import com.chat.vo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {
    void insert(User user);
    void update(User user);

    List<User> getUserByEmailAndPassword(@Param("eMail") String eMail, @Param("password") String password);

    List<User> getUserByEmail(@Param("eMail") String eMail);

    User getUserById(@Param("userId") long userId);

}

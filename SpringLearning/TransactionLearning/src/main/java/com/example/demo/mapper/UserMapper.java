package com.example.demo.mapper;

import com.example.demo.entity.Userinfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    public int insert(Userinfo userinfo);

}

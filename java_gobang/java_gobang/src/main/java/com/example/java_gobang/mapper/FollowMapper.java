package com.example.java_gobang.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FollowMapper {

    int insertUserFollow(@Param("userId") int userId, @Param("followId") int followId);

    int deleteUserFollow(@Param("userId") int userId);
}
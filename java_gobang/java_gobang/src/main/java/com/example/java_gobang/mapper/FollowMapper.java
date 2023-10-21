package com.example.java_gobang.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FollowMapper {

    int insertUserFollow(@Param("userId") int userId, @Param("followedId") int followedId);

    int deleteUserFollow(@Param("userId") int userId);

    Integer judgementIsFollow(@Param("userId") Integer userId, @Param("followedId") Integer followedId);

    List<Integer> getFollowedId(@Param("userId") int userId);

    List<Integer> getFansId(@Param("followedId") int followedId);
}
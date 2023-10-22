package com.example.java_gobang.mapper;

import com.example.java_gobang.entity.vo.UserVOHall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FollowMapper {

    int insertUserFollow(@Param("userId") int userId, @Param("followedId") int followedId);

    int deleteUserFollow(@Param("userId") int userId, @Param("followedId") int followedId);

    Integer judgementIsFollow(@Param("userId") Integer userId, @Param("followedId") Integer followedId);

    List<Integer> selectFollowedId(@Param("userId") int userId);

    List<Integer> selectFansId(@Param("followedId") int followedId);

    List<UserVOHall> selectFollowedUserHallList(@Param("userId") int userId);

    List<UserVOHall> selectFansUserHallList(@Param("followedId") int followedId);
}
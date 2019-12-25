package com.wisewintech.erp.dao;

import com.wisewintech.erp.entity.UserBO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserDAO {
    List<UserBO> getUserBOList();
    Integer addUserBO();
}

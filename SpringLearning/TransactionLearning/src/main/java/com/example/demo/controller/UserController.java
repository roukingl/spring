package com.example.demo.controller;

import com.example.demo.entity.Userinfo;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;

    @RequestMapping("/add")
    @Transactional
    public int add(Userinfo userinfo) {
        TransactionStatus transactionStatus =
                dataSourceTransactionManager.getTransaction(transactionDefinition);
        int row = userService.add(userinfo);
        // 提交事务
//        dataSourceTransactionManager.commit(transactionStatus);
        dataSourceTransactionManager.rollback(transactionStatus);
        return row;
    }

}

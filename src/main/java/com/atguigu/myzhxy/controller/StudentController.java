package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.Util.JwtHelper;
import com.atguigu.myzhxy.Util.MD5;
import com.atguigu.myzhxy.Util.Result;
import com.atguigu.myzhxy.pojo.Student;
import com.atguigu.myzhxy.service.StudentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.rmi.CORBA.Util;
import java.util.List;

/**
 * @Description:
 * @Param:
 * @return:
 * @Author: BIbo
 * @Date:
 */
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @ApiOperation("删除一个或者多个学生信息")
    @DeleteMapping("/delStudentById")
    public Result delStudentById( @ApiParam("多个学生id的JSON") @RequestBody List<Integer> ids){
        studentService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("增加学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(@RequestBody Student student){
       if (!StringUtils.isEmpty(student.getPassword())){
           student.setPassword(MD5.encrypt(student.getPassword()));
       }
       studentService.saveOrUpdate(student);
       return Result.ok();
    }

    @ApiOperation("查询学生信息,分页带条件")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentsByOpr(
            @ApiParam("页码数") @PathVariable("pageNo")Integer pageNo,
            @ApiParam("页大小") @PathVariable("pageSize")Integer pageSize,
            @ApiParam("查询条件转换后端数据模型") Student student
    ){
        // 准备分页信息封装的page对象
        Page<Student> page =new Page<>(pageNo,pageSize);
        // 获取分页的学生信息
        IPage<Student> iPage = studentService.getStudentByOpr(page, student);
        // 返回学生信息
        return Result.ok(iPage);
    }
}

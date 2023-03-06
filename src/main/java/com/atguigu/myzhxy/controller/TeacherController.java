package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.Util.Result;
import com.atguigu.myzhxy.pojo.Teacher;
import com.atguigu.myzhxy.service.TeacherService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Param:
 * @return:
 * @Author: BIbo
 * @Date:
 */
@Api(tags = "教师控制层")
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;


    @ApiOperation("删除一个或者多个教师信息")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(@RequestBody List<Integer> ids){
        teacherService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("添加和修改教师信息")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(
         @RequestBody Teacher teacher
    ){
      teacherService.saveOrUpdate(teacher);
      return Result.ok();
    }

    @ApiOperation("获取教师信息,分页带条件")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @ApiParam("分页数") @PathVariable("pageNo") Integer PageNo,
            @ApiParam("分页保函数据的数量") @PathVariable("pageSize") Integer PageSize,
            @ApiParam("教师所需要的get参数") Teacher teacher)
            {
                Page<Teacher> page = new Page<>(PageNo, PageSize);
                IPage<Teacher> iPage = teacherService.getTeacherByOpr(page,teacher);
                return Result.ok(iPage);
    }


}

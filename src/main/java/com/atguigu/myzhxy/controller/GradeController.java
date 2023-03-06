package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.Util.Result;
import com.atguigu.myzhxy.pojo.Grade;
import com.atguigu.myzhxy.service.GradeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Param:
 * @return:
 * @Author: BIbo
 * @Date:
 */
@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @PostMapping("/saveOrUpdateGrade")
    @ApiOperation("保存或者修改年级信息")
    public Result saveOrUpdateGrade(@ApiParam("要修改或者保存的信息") @RequestBody Grade grade){
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    @ApiOperation("删除年级信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(@ApiParam("要删除的年级信息的id") @RequestBody List<Integer> ids){
        gradeService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("获取所有Grade信息")
    @GetMapping("/getGrades")
    public Result getGrades(){
       List<Grade> grades = gradeService.getGrade();
       return Result.ok(grades);
    }


    @ApiOperation("查询年级信息,分页带条件")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGradeByOpr(
            @ApiParam("分页查询页码数") @PathVariable(value = "pageNo") Integer pageNo, // 页码数
            @ApiParam("分页查询页大小") @PathVariable(value = "pageSize") Integer pageSize, // 页大小
            @ApiParam("分页查询模糊匹配班级名") String gradeName)// 模糊查询条件
    {
        // 设置分页信息
        Page<Grade> page = new Page<>(pageNo, pageSize);
        // 调用服务层方法,传入分页信息,和查询的条件
        IPage<Grade> pageRs = gradeService.getGradeByOpr(page, gradeName);
        return Result.ok(pageRs);
    }
}
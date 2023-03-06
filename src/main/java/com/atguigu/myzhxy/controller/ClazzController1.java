//package com.atguigu.myzhxy.controller;
//
//import com.atguigu.myzhxy.Util.Result;
//import com.atguigu.myzhxy.pojo.Clazz;
//import com.atguigu.myzhxy.service.ClazzService1;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * @Description:
// * @Param:
// * @return:
// * @Author: BIbo
// * @Date:
// */
//@Api(tags = "班级控制器")
//@RestController
//@RequestMapping("/ssm/clazzController")
//public class ClazzController1 {
//
//    @Autowired
//    private ClazzService1 clazzService;
//
//        @ApiOperation("查询班级信息,分页带条件")
//        @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
//        public Result getClazzsByOpr(
//                @ApiParam("页码数")  @PathVariable("pageNo") Integer pageNo,
//                @ApiParam("页大小")  @PathVariable("pageSize") Integer pageSize,
//                @ApiParam("查询条件") Clazz clazz
//        ){
//            //设置分页信息
//            Page<Clazz> page =new Page<>(pageNo,pageSize);
//            IPage<Clazz> iPage = clazzService.getClazzsByOpr(page, clazz);
//            return Result.ok(iPage);
//        }

//    @ApiOperation("获取所有班级的JSON")
//    @GetMapping("/getClazzs")
//    public Result getClazzs(){
//        List<Clazz> clazzs = clazzService.getClazzs();
//        return Result.ok(clazzs);
//    }
//        @ApiOperation("获取所有班级的JSON")
//        @GetMapping("/getClazzs")
//        public Result getClazzs(){
//            List<Clazz> clazzList = clazzService.getClazzs();
//            return Result.ok(clazzList);
//        }
//}

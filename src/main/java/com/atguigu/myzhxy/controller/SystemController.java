package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.Util.*;
import com.atguigu.myzhxy.pojo.Admin;
import com.atguigu.myzhxy.pojo.LoginForm;
import com.atguigu.myzhxy.pojo.Student;
import com.atguigu.myzhxy.pojo.Teacher;
import com.atguigu.myzhxy.service.AdminService;
import com.atguigu.myzhxy.service.StudentService;
import com.atguigu.myzhxy.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description:
 * @Param:
 */
@RestController
@RequestMapping("/sms/system")
public class SystemController {


    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;


    @ApiOperation("修改密码")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(@RequestHeader("token") String token,
                            @PathVariable("oldPwd") String oldPwd,
                            @PathVariable("newPwd") String newPwd){
        if (JwtHelper.isExpiration(token)){
            return Result.fail().message("token过期");
        }
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);
        if (userType == 1){
            QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", userId.intValue()).eq("password", oldPwd);
            Admin admin = adminService.getOne(queryWrapper);
            if (admin != null){
                admin.setPassword(newPwd);
                adminService.saveOrUpdate(admin);
            }else {
                return Result.fail().message("原密码错误");
            }

        }else if (userType == 2){
            QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", userId.intValue()).eq("password", oldPwd);
            Student student = studentService.getOne(queryWrapper);
            if (student != null){
                student.setPassword(newPwd);
                studentService.saveOrUpdate(student);
            }else {
                return Result.fail().message("原密码错误");
            }
        }else {
            QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", userId.intValue()).eq("password", oldPwd);
            Teacher teacher = teacherService.getOne(queryWrapper);
            if (teacher != null){
                teacher.setPassword(newPwd);
                teacherService.saveOrUpdate(teacher);
            }else {
                return Result.fail().message("原密码错误");
            }
        }

        return Result.ok();
    }


    @ApiOperation("头像上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(@RequestPart("multipartFile") MultipartFile multipartFile){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        String newFileName = uuid + originalFilename.substring(i);


        //保存文件
        String path = "D:\\Study\\Java\\study\\dayT1\\myzhxy\\src\\main\\resources\\public\\upload".concat(newFileName);
        try {
            multipartFile.transferTo(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //响应文件的位置
        String paths = "upload/".concat(newFileName);
        return Result.ok(paths);
    }

    @ApiOperation("获取验证+码图片")
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response){
        //获取验证码图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        //获取验证码字符串
        String verifiCode =new String(CreateVerifiCodeImage.getVerifiCode());

        /*将验证码放入当前请求域*/
        request.getSession().setAttribute("verifiCode",verifiCode);
        try {
            //将验证码图片通过输出流做出响应
            ImageIO.write(verifiCodeImage,"JPEG",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    校验登录成功 result转换成jose
    //记录一下，P15 如果一直报”验证码有误”，需要改一下LoginForm中的属性名，
// 因为前端的表单中验证码对应的input标签name=verifiCode，使用@RequestBody将请求体中的JSON格式数据转化为LoginForm对象时，
// 如果类中对应的属性名不是verifiCode，
// 值是放不进去的，所以会一直报验证码有误，因为loginForm中无法获取到这个属性值
//    @PostMapping("/login")
//    public Result Login(@RequestBody LoginForm loginForm, HttpServletRequest request){
//        //验证码校验
//        HttpSession session = request.getSession();
//        String sessionVerifiCode = (String)session.getAttribute("verifiCode");
//        String loginVerifiCode = loginForm.getVerifiCode();
//        if ("".equals(sessionVerifiCode) || null == sessionVerifiCode) {
//            return Result.fail().message("验证码失效，请重新在有效时间内输入");
//        }
//        if (!sessionVerifiCode.equalsIgnoreCase(loginVerifiCode)){
//            return Result.fail().message("验证码错误，请重新输入");
//        }
//        //验证码正确，移除现有验证码
//        session.removeAttribute("verifiCode");
//        //分用户类型进行校验
//        //准备Map用来存放响应数据
//        Map<String,Object> map = new LinkedHashMap<>();
//        switch (loginForm.getUserType()){
//            case 1:
//                try {
//                    Admin admin = adminService.login(loginForm);
//                    if (admin != null){
//                        map.put("token", JwtHelper.createToken(admin.getId().longValue(), 1));
//                    }else {
//                        throw new RuntimeException("用户名或者密码错误");
//                    }
//                    return Result.ok(map);
//                } catch (RuntimeException e) {
//                    e.printStackTrace();
//                    return Result.fail().message(e.getMessage());
//                }
//            case 2:
//                try {
//                    Student student = studentService.login(loginForm);
//                    if (student != null){
//                        map.put("token", JwtHelper.createToken(student.getId().longValue(), 2));
//                    }else {
//                        throw new RuntimeException("用户名或者密码错误");
//                    }
//                    return Result.ok(map);
//                } catch (RuntimeException e) {
//                    e.printStackTrace();
//                    return Result.fail().message(e.getMessage());
//                }
//            case 3:
//                try {
//                    Teacher teacher = teacherService.login(loginForm);
//                    if (teacher != null){
//                        map.put("token", JwtHelper.createToken(teacher.getId().longValue(), 3));
//                    }else {
//                        throw new RuntimeException("用户名或者密码错误");
//                    }
//                    return Result.ok(map);
//                } catch (RuntimeException e) {
//                    e.printStackTrace();
//                    return Result.fail().message(e.getMessage());
//                }
//        }
//        return Result.fail().message("查无此人");
//    }

    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token){
     boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            return Result.build(null, ResultCodeEnum.LOGIN_ERROR);
        }
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        Map<String, Object> map = new LinkedHashMap<>();
        switch (userType){
            case 1:
               Admin admin = adminService.getAdminById(userId);
               map.put("userType",1);
               map.put("user",admin);
               break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType",2);
                map.put("user",student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType",3);
                map.put("user",teacher);
                break;
        }
        return Result.ok(map);
    }


    @ApiOperation("登录请求验证")
    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpServletRequest request){
        // 获取用户提交的验证码和session域中的验证码
        HttpSession session = request.getSession();
        String systemVerifiCode =(String) session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if("".equals(systemVerifiCode)){
            // session过期,验证码超时,
            return Result.fail().message("验证码失效,请刷新后重试");
        }
        if (!loginVerifiCode.equalsIgnoreCase(systemVerifiCode)){
            // 验证码有误
            return Result.fail().message("验证码有误,请刷新后重新输入");
        }
        // 验证码使用完毕,移除当前请求域中的验证码
        session.removeAttribute("verifiCode");


        // 准备一个Map集合,用户存放响应的信息
        Map<String,Object> map=new HashMap<>();
        // 根据用户身份,验证登录的用户信息
        switch (loginForm.getUserType()){
            case 1:// 管理员身份
                try {
                    // 调用服务层登录方法,根据用户提交的LoginInfo信息,查询对应的Admin对象,找不到返回Null
                    Admin login = adminService.login(loginForm);
                    if (null != login) {
                        // 登录成功,将用户id和用户类型转换为token口令,作为信息响应给前端
                        map.put("token",JwtHelper.createToken(login.getId().longValue(), 1));
                    }else{
                        throw  new RuntimeException("用户名或者密码有误!");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    // 捕获异常,向用户响应错误信息
                    return Result.fail().message(e.getMessage());
                }

            case 2:// 学生身份
                try {
                    // 调用服务层登录方法,根据用户提交的LoginInfo信息,查询对应的Student对象,找不到返回Null
                    Student login = studentService.login(loginForm);
                    if (null != login) {
                        // 登录成功,将用户id和用户类型转换为token口令,作为信息响应给前端
                        map.put("token",JwtHelper.createToken(login.getId().longValue(), 2));
                    }else{
                        throw  new RuntimeException("用户名或者密码有误!");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    // 捕获异常,向用户响应错误信息
                    return Result.fail().message(e.getMessage());
                }
            case 3:// 教师身份
                // 调用服务层登录方法,根据用户提交的LoginInfo信息,查询对应的Teacher对象,找不到返回Null
                try {
                    // 调用服务层登录方法,根据用户提交的LoginInfo信息,查询对应的Student对象,找不到返回Null
                    Teacher login = teacherService.login(loginForm);
                    if (null != login) {
                        // 登录成功,将用户id和用户类型转换为token口令,作为信息响应给前端
                        map.put("token",JwtHelper.createToken(login.getId().longValue(), 3));
                    }else{
                        throw  new RuntimeException("用户名或者密码有误!");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    // 捕获异常,向用户响应错误信息
                    return Result.fail().message(e.getMessage());
                }
        }
        // 查无此用户,响应失败
        return Result.fail().message("查无此用户");
    }
}

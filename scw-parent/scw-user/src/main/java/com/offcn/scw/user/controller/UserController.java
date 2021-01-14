package com.offcn.scw.user.controller;

import com.offcn.scw.user.bean.User;
import com.offcn.scw.user.pojo.TMember;
import com.offcn.scw.user.service.UserService;
import com.offcn.scw.user.utils.SmsTemplate;
import com.offcn.scw.user.vo.UserReqVo;
import com.offcn.scw.user.vo.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("users")
@Api(tags = "用户控制器 -- 用于控制层")
public class UserController {

    @Autowired
    private SmsTemplate smsTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserService userService;

    //get请求
    @ApiOperation("用户模块对外提供的按照id查询功能")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id",value = "用户ID",required = true)
    })
    @GetMapping("{id}")
    public ResponseEntity<UserRespVo> getName(@PathVariable("id")Integer id){
        TMember member = userService.findOne(id);
        UserRespVo vo = new UserRespVo();
        BeanUtils.copyProperties(member,vo);
        return ResponseEntity.ok(vo);
    }

    //post请求
    @ApiOperation("用户模块对外提供添加用户的功能 -- 注册功能")
    @PostMapping("register")
    public ResponseEntity<Object> saveUser(UserReqVo user){
        //判断验证码是否有效、判断验证码是否正确

        //先判断验证码是否有效 -- 从Redis中获取验证码 ，如果获取不到则说明失效
        String code = redisTemplate.boundValueOps(user.getLoginacct()).get();
        if(StringUtils.isEmpty(code)){
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("验证码已经失效，请重新生成");
        }

        //判断页面送来的验证码和从Redis获取的验证码是否一致
        if(!code.equals(user.getCode())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("验证码不正确，请重新输入");
        }


        //从vo对象转存数据到member
        TMember member = new TMember();
        BeanUtils.copyProperties(user,member);

        try {
            userService.registerUser(member);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("注册失败");
        }

        return ResponseEntity.ok("注册成功");
    }


    //发送短信，实现发送验证码的功能
    @ApiOperation("用户模块对外提供发送短信验证码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "tel",value = "注册手机号码",required = true)
    })
    @PostMapping("sendCode")
    public ResponseEntity<String> sendCode(String tel){
        //生成随机验证码
        String code = UUID.randomUUID().toString().substring(0,4);
        //将验证码保存到Redis中
        redisTemplate.boundValueOps(tel).set(code,1, TimeUnit.DAYS);//TODO --- 在开发阶段时间有意加长，等功能测试完成再改回

        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", tel);
        querys.put("param", "code:"+code);
        querys.put("tpl_id", "TP1711063");
        String resp = smsTemplate.sendSms(querys);
        if(resp.equals("") || resp.equals("fail")){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("发送失败");
        }
        return ResponseEntity.ok(resp);
    }

    //登录功能
    @PostMapping("login")
    @ApiOperation("用户模块对外提供登录验证功能")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "loginacct",value = "用户账号",required = true),
            @ApiImplicitParam(name = "password",value = "密码",required = true)
    })
    public ResponseEntity<Object>login(String loginacct,String password){
        TMember member = userService.login(loginacct, password);
        if(member != null){
            UserRespVo vo = new UserRespVo();
            //生成令牌
            String tocken = UUID.randomUUID().toString().replaceAll("-", "");
            vo.setAccessToken(tocken);
            //把令牌放入Redis
            redisTemplate.opsForValue().set(tocken,member.getId()+"",1,TimeUnit.DAYS);//TODO 开发测试阶段令牌在Redis中保存时间较长，在生产阶段再改回
            BeanUtils.copyProperties(member,vo);
            return ResponseEntity.ok(vo);
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("用户名或者密码有误");
    }


}

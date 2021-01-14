package com.offcn.scw.user.service.impl;

import com.offcn.scw.user.bean.UserException;
import com.offcn.scw.user.dao.TMemberMapper;
import com.offcn.scw.user.enums.UserExceptionEnum;
import com.offcn.scw.user.pojo.TMember;
import com.offcn.scw.user.pojo.TMemberExample;
import com.offcn.scw.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    //持久层对象注入
    @Autowired
    private TMemberMapper memberMapper;

    //实现注册功能
    @Override
    public void registerUser(TMember member) {
        //判断当前要注册的账号是否存在，如果存在则抛出自定义异常，如果不存在则正常注册
        String loginacct = member.getLoginacct();
        //查询账号是否存在
        TMemberExample example = new TMemberExample();
        TMemberExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(loginacct);

        long l = memberMapper.countByExample(example);
        //账号存在
        if(l > 0){
            throw new UserException(UserExceptionEnum.LOGINACCT_EXIST);
        }

        //账号不存在 -- 完成注册
        //密码的加密处理
        String userpswd = member.getUserpswd();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userpswd = encoder.encode(userpswd);
        member.setUserpswd(userpswd);

        member.setUsername(member.getLoginacct());

        member.setAuthstatus("0");
        member.setUsertype("0");
        member.setAccttype("2");

        //int i = 10 / 0;

        memberMapper.insertSelective(member);
    }

    //实现登录功能
    @Override
    public TMember login(String username, String password) {
        //先根据用户名查用户信息 如果没有查到返回null，如果查到，然后再进一步判断比较
        TMemberExample example = new TMemberExample();
        TMemberExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);

        List<TMember> tMembers = memberMapper.selectByExample(example);
        if(tMembers != null && tMembers.size() == 1){
            //此时再按照密码判断验证，如果两次密码不一致返回null，如果两次密码一致此时登录成功
            TMember member = tMembers.get(0);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            //将未加密的密码进行加密之后和表中的加密密码进行比对
            boolean matches = encoder.matches(password, member.getUserpswd());
            if(matches){
                return member;
            }
        }

        return null;
    }

    //按照主键查询
    @Override
    public TMember findOne(Integer id) {
        return memberMapper.selectByPrimaryKey(id);
    }
}

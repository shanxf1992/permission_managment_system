package com.itheima.acl.service;

import com.google.common.base.Preconditions;
import com.itheima.acl.beans.PageQuery;
import com.itheima.acl.beans.PageResult;
import com.itheima.acl.common.RequestHolder;
import com.itheima.acl.domain.SysUser;
import com.itheima.acl.domain.SysUserExample;
import com.itheima.acl.exception.ParamException;
import com.itheima.acl.mapper.SysUserMapper;
import com.itheima.acl.param.UserParam;
import com.itheima.acl.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("sysUserService")
public class SysUserService {

    @Value("#{sysUserMapper}")
    private SysUserMapper sysUserMapper;
    @Value("#{sysLogService}")
    private SysLogService sysLogService;

    // 新增用户信息
    public void save(UserParam userParam) {

        //1 验证传入的参数是否合法
        BeanValidator.check(userParam);

        //2 如果信息合法, 检查该用户的 邮箱, 手机号码是否重复
        //验证邮箱
        if (existEmail(userParam.getMail(), userParam.getId())) {
            throw new ParamException("该邮件已注册");
        }
        //验证手机
        if (existTelephone(userParam.getTelephone(), userParam.getId())) {
            throw new ParamException("该手机号码已注册");
        }

        /**
         * 3 填充 SysUser 的其他字段信息, 需要填充的参数
         *      password: 自动生成, 通过邮件发送给用户
         *      operator: 最后一次操作者
         *      operateTime: 最后一次操作时间
         *      operateIp : 操作者的 IP 地址
         */
        SysUser sysUser = SysUser.builder()
                .id(userParam.getId())
                .username(userParam.getUsername())
                .telephone(userParam.getTelephone())
                .mail(userParam.getMail())
                .deptId(userParam.getDeptId())
                .status(userParam.getStatus())
                .remark(userParam.getRemark())
                .build();

        //自动生成密码
        String password = PasswordUtils.randomPassword();
        //密码加密处理
        String encrpyptedPassword = MD5Util.encrypt(password);
        //最后一次的操作者, 时间, ip
        String operator = RequestHolder.getUserThreadLocal().get().getUsername();
        Date operateTime = new Date();
        String operateIp = IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get());

        sysUser.setPassword(encrpyptedPassword);
        sysUser.setOperator(operator);
        sysUser.setOperateTime(operateTime);
        sysUser.setOperateIp(operateIp);

        //4 将自动生成的 password 通过 Email 发送给用户
        MailUtils.sendMail(userParam.getMail(), userParam.getUsername(), password);

        //3 调用数据层, 添加数据
        sysUserMapper.insertSelective(sysUser);

        //4 保存日志
        sysLogService.saveUserLog(null, sysUser);
    }

    //检查手机号码是否已被注册
    private boolean existTelephone(String telephone, Integer id) {
        return sysUserMapper.selectUserByTelephone(telephone, id) > 0;
    }

    //检查邮箱是否已经被注册
    private boolean existEmail(String mail, Integer id) {
        return sysUserMapper.selectUserByMail(mail, id) > 0;
    }

    // 更新用户信息
    public void update(UserParam userParam) {
        //1 验证传入的参数是否合法
        BeanValidator.check(userParam);

        //2 如果信息合法, 检查该用户的 邮箱, 手机号码是否重复
        //验证邮箱
        if (existEmail(userParam.getMail(), userParam.getId())) {
            throw new ParamException("该邮件已注册");
        }
        //验证手机
        if (existTelephone(userParam.getTelephone(), userParam.getId())) {
            throw new ParamException("该手机号码已注册");
        }

        //3 判断待更新的用户是否存在
        SysUser before = sysUserMapper.selectByPrimaryKey(userParam.getId());
        Preconditions.checkNotNull(before);

        /**
         * 3 填充 SysUser 的其他字段信息, 需要填充的参数
         *      password: 不需要进行更新
         *      operator: 最后一次操作者
         *      operateTime: 最后一次操作时间
         *      operateIp : 操作者的 IP 地址
         */
        SysUser after = SysUser.builder()
                .id(userParam.getId())
                .username(userParam.getUsername())
                .telephone(userParam.getTelephone())
                .mail(userParam.getMail())
                .deptId(userParam.getDeptId())
                .status(userParam.getStatus())
                .remark(userParam.getRemark())
                .build();

        String operator = "system";
        Date operateTime = new Date();
        String operateIp = "127.0.0.1";

        after.setOperator(operator);
        after.setOperateTime(operateTime);
        after.setOperateIp(operateIp);
        after.setPassword(before.getPassword());

        //3 调用数据层, 添加数据
        sysUserMapper.updateByPrimaryKeySelective(after);

        //4 记录日志
        sysLogService.saveUserLog(before, after);
    }

    //查询登陆用户信息是否合法
    public SysUser getUserByUserNameAndPassword(String username, String password) {
        password = MD5Util.encrypt(password);
        return sysUserMapper.selectUserByUsernameAndPassword(username, password);
    }

    //分页列表展示数据
    public PageResult<SysUser> selectUserByDept(Integer deptId, PageQuery page) {

        int start = page.getOffset();

        List<SysUser> list = sysUserMapper.selectPageByDeptId(deptId, start, page.getPageSize());

        // 获取该部门下的用户的总记录数
        int total = list.size();

        PageResult<SysUser> pageResult = new PageResult<>();
        pageResult.setTotal(total);
        pageResult.setData(list);


        return pageResult;
    }

    //获取所用用户
    public List<SysUser> getAll() {
        List<SysUser> userList = sysUserMapper.selectByExample(null);
        return userList;
    }

}

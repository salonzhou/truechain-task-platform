package com.truechain.task.plat.form.controller;

import com.truechain.task.plat.form.core.BusinessException;
import com.truechain.task.plat.form.core.WrapMapper;
import com.truechain.task.plat.form.core.Wrapper;
import com.truechain.task.plat.form.model.entity.AuthRole;
import com.truechain.task.plat.form.model.entity.AuthUser;
import com.truechain.task.plat.form.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户Controller
 */
@RestController
@RequestMapping("/user")
public class UserController extends BasicController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 获取用户列表
     */
    @PostMapping("/getUserPage")
    public Wrapper getUserPage(@RequestParam int pageIndex, @RequestParam int pageSize) {
        Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
        Page<AuthUser> userPage = userService.getUserPageByCriteria(null, pageable);
        return WrapMapper.ok(userPage);
    }

    /**
     * 获取对应用户角色
     */
    @GetMapping("/getUserRoleList")
    public Wrapper getUserRoleList(@RequestParam Integer userId) {
        AuthUser user = userService.getUserById(userId);
        if (null == user) {
            throw new BusinessException("用户不存在");
        }
        List<AuthRole> roleList = user.getRoles();
        return WrapMapper.ok(roleList);
    }

    /**
     * 获取角色关联的(roleId)对应用户列表
     */
    @GetMapping("/getUserListByRoleId")
    public Wrapper getUserListByRoleId(@RequestParam Integer roleId, @RequestParam Integer pageIndex, @RequestParam Integer pageSize) {
        Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
        AuthUser authUser = new AuthUser();
        authUser.setRoleId(roleId);
        Page<AuthUser> userPage = userService.getUserPageByCriteria(authUser, pageable);
        return WrapMapper.ok(userPage);
    }

    /**
     * 获取角色未关联的用户列
     */
    @GetMapping("/getUserListExtendByRoleId")
    public Wrapper getUserListExtendByRoleId(@RequestParam Integer roleId, @RequestParam Integer pageIndex, @RequestParam Integer pageSize) {
        Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
        AuthUser authUser = new AuthUser();
        authUser.setRoleId(roleId);
        Page<AuthUser> userPage = userService.getUserPageByCriteria(authUser, pageable);
        return WrapMapper.ok(userPage);
    }

    /**
     * 给用户授权添加角色
     */
    @PostMapping("/authorityUserRole")
    public Wrapper authorityUserRole(@RequestParam Integer userId, @RequestParam Integer roleId) {
        userService.addUserRole(userId, roleId);
        return WrapMapper.ok();
    }

    /**
     * 删除已经授权的用户角色
     */
    @DeleteMapping("/deleteAuthorityUserRole")
    public Wrapper deleteAuthorityUserRole(@RequestParam Integer userId, @RequestParam Integer roleId) {
        userService.deleteUserRole(userId, roleId);
        return WrapMapper.ok();
    }


    /**
     * 用户登出
     */
    @PostMapping("/exit")
    public Wrapper accountExit(HttpServletRequest request) {

        return WrapMapper.ok();
    }
}

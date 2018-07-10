package com.zlzkj.app.shiro;


import com.zlzkj.app.model.index.Authority;
import com.zlzkj.app.model.index.Role;
import com.zlzkj.app.model.index.User;
import com.zlzkj.app.service.index.PermissionService;
import com.zlzkj.app.service.index.ShiroUserService;
import com.zlzkj.app.service.index.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Class description
 *
 * @author
 * @version 1.0.0, 16/03/31
 */
@Configuration
public class ShiroRealm extends AuthorizingRealm {
    private ShiroUserService shiroUserService;
    private UserService userService;
    private PermissionService permissionService;


   /* private String prefix;

    public void setKeyPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        return super.getAuthorizationCacheKey(principals) + prefix;
    }*/


    /**
     *
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
            throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        User user = shiroUserService.getAuthenticatedUser(authcToken);
        System.out.println(" doGetAuthenticationInfo:" + user.getUsername());
        return new SimpleAuthenticationInfo(user,
                String.valueOf(token.getPassword()),
                user.getUsername());

    }

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        //index.out.println("To doGetAuthorizationInfo:");
        //String userName = (String) getAvailablePrincipal(principals);

        User user = shiroUserService.getLoginUser();
        List<Role> roles = permissionService.selectRolesByRoleId(user.getRoleId());
        List<Authority> auths = permissionService.selectAuthorityByRoleId(user.getRoleId());

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        boolean isHos = false;
        if (user != null) {
            for (Role role : roles) {
                info.addRole(role.getCode());
            }
            List<String> permissions = new ArrayList<>();
            for (Authority auth : auths) {
                permissions.add(auth.getCode());
            }

            //index.out.println(" doGetAuthorizationInfo:" + user.getUsername());
            info.addStringPermissions(permissions);
            return info;
        }

        return null;
    }
    public void clearAuthz() {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
    public void clearAuthz(PrincipalCollection principalCollection) {
        this.clearCachedAuthorizationInfo(principalCollection);
    }
    /**
     * Method description getPermissionService
     *
     * @return PermissionService
     */
    public PermissionService getPermissionService() {
        return permissionService;
    }

    /**
     * Method description setPermissionService
     *
     * @param permissionService
     */
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * Method description getShiroUserService
     *
     * @return ShiroUserService
     */
    public ShiroUserService getShiroUserService() {
        return shiroUserService;
    }

    /**
     * Method description setShiroUserService
     *
     * @param shiroUserService
     */
    public void setShiroUserService(ShiroUserService shiroUserService) {
        this.shiroUserService = shiroUserService;
    }

    /**
     * Method description getUserService
     *
     * @return UserService
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * Method description setUserService
     *
     * @param userService
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com

package cn.wolfcode.shiro.realm;

import cn.wolfcode.shiro.dao.IPermissionDAO;
import cn.wolfcode.shiro.dao.IRoleDAO;
import cn.wolfcode.shiro.dao.IUserDAO;
import cn.wolfcode.shiro.domain.User;
import lombok.Setter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Changi
 * @Date: 2019/8/9 09:40
 * @Description:
 * @Msg: good good study, day day up!
 */
public class UserRealm extends AuthorizingRealm {

    @Setter
    private IUserDAO userDAO;
    @Setter
    private IRoleDAO roleDAO;
    @Setter
    IPermissionDAO permissionDAO;


    @Override
    public String getName() {
        return "UserRealm";
    }

    //授权认证
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();

        List<String> permissions = new ArrayList<>();
        List<String> roles = new ArrayList<>();
        if ("admin".equals(user.getUsername())) {

            permissions.add("*:*");
            roles = roleDAO.getAllRoleSn();

        } else {
            roles = roleDAO.getRoleSnByUserId(user.getId());
            permissions = permissionDAO.getPermissionResourceByUserId(user.getId());
        }
//        if ("zhangsan".equals(user.getUsername())){
//            permissions.add("employee:edit");
//        }else if ("admin".equals(user.getUsername())){
//            permissions.add("*:*");
//        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissions);
        info.addRoles(roles);
        return info;
    }
    //登录认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //从token获取登录的用户名，查询数据库返回用户信息
        String username = (String) authenticationToken.getPrincipal();
        User user = userDAO.getUserByUsername(username);

        if (user == null) {
            return null;
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(),
                ByteSource.Util.bytes(user.getUsername()),
                getName());
        return info;
    }

    //清除缓存  --------------要手动调用---------------
    public void clearCached(){
//      获取当前登陆的用户凭证，并清除
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();

        super.clearCache(principals);
    }

}

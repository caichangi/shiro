package cn.wolfcode.shiro.realm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Auther: Changi
 * @Date: 2019/8/9 16:48
 * @Description:
 * @Msg: good good study, day day up!
 */

/**
 * 标志权限的名称
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionName {
    String value();
}

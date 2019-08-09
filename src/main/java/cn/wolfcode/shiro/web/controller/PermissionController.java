package cn.wolfcode.shiro.web.controller;

import cn.wolfcode.shiro.dao.IPermissionDAO;
import cn.wolfcode.shiro.domain.Permission;
import cn.wolfcode.shiro.realm.PermissionName;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
public class PermissionController {

    //请求映射处理映射器
    //springmvc在启动时候将所有贴有请求映射标签，RequestMapping方法收集起来封装到该对象中
    @Autowired
    private RequestMappingHandlerMapping rmhm;

    @Autowired
    private IPermissionDAO permissionDAO;

    @RequestMapping("/reload")
    public String reload() throws Exception {

        //将系统中所有权限表达式加载进数据库

        //0.从数据库中查询所有的权限表达式
        List<String> resourcesList = permissionDAO.getAllResources();

        //1.获取controller中所有带有@requestMapper标签的方法

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = rmhm.getHandlerMethods();
        Collection<HandlerMethod> methods = handlerMethods.values();
        for (HandlerMethod method : methods) {

            //2.遍历所有方法，判断当前方法是否贴有@RequiresPermissions权限控制标签
            RequiresPermissions anno = method.getMethodAnnotation(RequiresPermissions.class);
            if (anno != null) {
                //权限表达式
                String resources = anno.value()[0];
                //表示去除重复的
                if (resourcesList.contains(resources)){
                    continue;
                }

                Permission p = new Permission();
                p.setResource(resources);
                p.setName(method.getMethodAnnotation(PermissionName.class).value());
//                System.out.println(method.getMethodAnnotation(PermissionName.class).value());
                //保存到数据库
                permissionDAO.save(p);

            }

        }


        //3.如果有，解析得到的权限表达式，封装成Permission对象保存到Permission表中

        return "main";
    }

}

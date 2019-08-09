package cn.wolfcode.shiro;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @Auther: Changi
 * @Date: 2019/8/9 20:20
 * @Description:
 * @Msg: good good study, day day up!
 */
public class MD5Test {
    public static void main(String[] args) {
        Md5Hash hash = new Md5Hash("666","admin",3);
        System.out.println(hash);
    }
}

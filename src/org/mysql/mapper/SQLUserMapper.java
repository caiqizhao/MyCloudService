package org.mysql.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mysql.entity.User;




public interface SQLUserMapper {

    /**
     * 查询用户
     * @param user_name
     * @return
     */
    @Select("select * from user where user_name=#{user_name} ")
    User getUser(@Param("user_name") String user_name);

    /**
     * 判断用户是否拥有扩展资格
     * @param user_name
     * @param user_password
     * @return
     */
    @Select("select count(*) from user where user_name=#{user_name} and user_password=#{user_password} and is_user='1'")
    int isUserSize(@Param("user_name")String user_name,@Param("user_password") String user_password);


    /**
     * 校验用户账户密码
     * @param user_name
     * @param user_password
     * @return
     */
    @Select("select count(*) from user where user_name=#{user_name} and user_password=#{user_password} ")
    int isUserPassword(@Param("user_name")String user_name,@Param("user_password") String user_password);


    /**
     * 用户存储空间扩展容量
     * @param user_name
     * @param user_password
     */
    @Update("update user set is_user = '0', user_size = '2097152' " +
            "where user_name = #{user_name} and user_password = #{user_password}")
    void updateIs_user( @Param("user_name")String user_name,
                      @Param("user_password")String user_password);


    /**
     * 修改用户昵称
     * @param user_name
     * @param user_password
     * @param name
     */
    @Update("update user set name=#{name} where user_name = #{user_name} and user_password = #{user_password}")
    void updateName(@Param("user_name")String user_name, @Param("user_password")String user_password,
                    @Param("name") String name);

    /**
     * 修改用户密码
     * @param user_name
     * @param user_password
     */
    @Update("update user set user_password=#{uer_password} where user_name = #{user_name}")
    void updatePassword(@Param("user_name")String user_name, @Param("user_password")String user_password);

    /**
     * 插入用户
     * @param uer_name
     * @param user_password
     */
    @Insert("insert into user(user_name,user_password,is_user) value(#{user_name},#{user_password},1)")
    void addUser(@Param("user_name") String uer_name,@Param("user_password") String user_password);

}

package org.mysql.mapper;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mysql.entity.Directory;
import org.mysql.entity.UserFile;

import java.util.ArrayList;

public interface SQLFileMapper {


    /**
     * 查询用户的文件
     * @param user_name
     * @return
     */
    @Select("select * from userfile where user_name=#{user_name}")
    ArrayList<UserFile> getUserFile(@Param("user_name") String user_name);


    /**
     * 下载文件查找
     * @param user_name
     * @param file_name
     * @return
     */
    @Select("select * from userfile where user_name=#{user_name} and file_name = #{file_name} ")
    UserFile getDownloadFile(@Param("user_name") String user_name,@Param("file_name")String file_name);



    /**
     * 查询用户的所属目录
     * @param user_name
     * @return
     */
    @Select("select * from directory where user_name=#{user_name}")
    ArrayList<Directory> getUserDir(@Param("user_name") String user_name);



}

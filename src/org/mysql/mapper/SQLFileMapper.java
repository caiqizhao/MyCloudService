package org.mysql.mapper;


import org.apache.ibatis.annotations.*;
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
    UserFile getNotDirDownloadFile(@Param("user_name") String user_name,@Param("file_name")String file_name);


    /**
     * 下载跟文件下的文件
     * @param user_name
     * @param file_name
     * @param dir_id
     * @return
     */
    @Select("select * from userfile where user_name=#{user_name} and file_name = #{file_name} and dir_id=#{dir_id}")
    UserFile getDownloadFile(@Param("user_name") String user_name,@Param("file_name")String file_name
            ,@Param("dir_id")Integer dir_id);


    /**
     * 查询用户的所属目录
     * @param user_name
     * @return
     */
    @Select("select * from directory where user_name=#{user_name}")
    ArrayList<Directory> getUserDir(@Param("user_name") String user_name);

    /**
     * 查询文件的hash值是否存在
     * @param file_hash
     * @return
     */
    @Select("select count(*) from filehash where file_hash=#{file_hash}")
    int selectFileHASH(@Param("file_hash")String file_hash);


    /**
     * 更新hash索引值
     * @param file_hash
     */
    @Update("update filehash set file_count = file_count+1 where file_hash=#{file_hash}")
    void upadteFileHASHCount(@Param("file_hash")String file_hash);


    /**
     * 用户上传文件插入记录
     * @param user_name
     * @param file_name
     * @param dir_id
     * @param file_date
     * @param file_hash
     * @param size
     */
    @Insert("insert into userfile(user_name,file_name,dir_id,file_date,file_hash,file_size)" +
            "value(#{user_name},#{file_name},#{dir_id},#{file_date},#{file_hash},#{file_size})")
    void addUserFile(@Param("user_name")String user_name,@Param("file_name")String file_name,
                     @Param("dir_id")Integer dir_id,@Param("file_date")String file_date,
                     @Param("file_hash")String file_hash,@Param("file_size")String size);


    /**
     * 新增加文件(服务器不持该hash值文件)
     * @param file_hash
     */
    @Insert("insert into filehash(file_hash,file_count)" +
            "value(#{file_hash},1)")
    void addFileHash(@Param("file_hash")String file_hash);

    @Update("update filehash set file_count=file_count-1 where file_hash=#{file_hash}")
    void updateFileHash(@Param("file_hash")String file_hash);


    /**
     * 新建文件夹
     * @param user_name
     * @param dir_parent
     * @param dir_name
     */
    @Insert("insert into directory(user_name,dir_parent,dir_name) " +
            "value(#{user_name},#{dir_parent},#{dir_name})")
    void addDir(@Param("user_name")String user_name,@Param("dir_parent")Integer dir_parent
            ,@Param("dir_name")String dir_name);


    /**
     * 查找相应文件夹
     * @param user_name
     * @param dir_parent
     * @param dir_name
     */
    @Select("select * from directory where user_name=#{user_name} and dir_parent=#{dir_parent} and dir_name=#{dir_name}")
    Directory selectDir(@Param("user_name")String user_name,@Param("dir_parent")Integer dir_parent,@Param("dir_name")String dir_name);

    /**
     * 删除文件
     * @param user_name
     * @param dir_id
     * @param file_name
     */
    @Delete("delete from userfile where user_name=#{user_name} and dir_id=#{dir_id} and file_name=#{file_name}")
    void deleteFile(@Param("user_name")String user_name,@Param("file_name")String file_name,@Param("dir_id")Integer dir_id);



    /**
     * 根目录文件的删除文件
     * @param user_name
     * @param file_name
     */
    @Delete("delete from userfile where user_name=#{user_name} and file_name=#{file_name}")
    void deleteNotDirFile(@Param("user_name")String user_name,@Param("file_name")String file_name);


}

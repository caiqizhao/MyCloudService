package org.caiqizhao.serlvet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.caiqizhao.util.PasswordMD5Util;
import org.mysql.entity.Directory;
import org.mysql.entity.User;
import org.mysql.entity.UserFile;
import org.mysql.mapper.SQLFileMapper;
import org.mysql.util.MySQLdb;
import org.mysql.mapper.SQLUserMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class LoginServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        SqlSessionFactory sqlSessionFactory = MySQLdb.getSqlSession();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = null;
        SQLUserMapper sqlUtil = sqlSession.getMapper(SQLUserMapper.class);
        user = sqlUtil.getUser(username);
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        if(user != null){
            if(user.getUser_password().equals(PasswordMD5Util.generateMD5(password))) {
                JSONObject user_json = JSONObject.fromObject(user);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user",user_json);

                SQLFileMapper fileMapper = sqlSession.getMapper(SQLFileMapper.class);

                //得到用户的文件
                ArrayList<UserFile> user_file_list = fileMapper.getUserFile(username);
                if(!user_file_list.isEmpty()) {
                    JSONArray user_file_json = JSONArray.fromObject(user_file_list);
                    jsonObject.put("user_file",user_file_json);
                }

                //得到用户的目录
                ArrayList<Directory> user_dir_list = fileMapper.getUserDir(username);
                if (!user_dir_list.isEmpty()) {
                    JSONArray user_dir_json = JSONArray.fromObject(user_dir_list);
                    jsonObject.put("user_dir", user_dir_json);
                }
                System.out.println(jsonObject);
                out.print(jsonObject);
            }else {
                out.print("密码错误");
            }
        }else {
            out.print("账户不存在");
        }
        out.flush();
        out.close();
        sqlSession.close();

    }
}

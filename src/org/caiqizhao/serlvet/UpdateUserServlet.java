package org.caiqizhao.serlvet;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.caiqizhao.util.PasswordMD5Util;
import org.mysql.mapper.SQLUserMapper;
import org.mysql.util.MySQLdb;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UpdateUserServlet",urlPatterns = "/updateuser")
public class UpdateUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        SqlSessionFactory sqlSessionFactory = MySQLdb.getSqlSession();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        SQLUserMapper sqlUtil = sqlSession.getMapper(SQLUserMapper.class);
        PrintWriter out = null;
        response.setContentType("text/plain;charset=UTF-8");
        switch (Integer.parseInt(request.getParameter("code"))){
            case 0:
                try {
                    if(sqlUtil.isUserSize(username,password)!=0) {
    
                        sqlUtil.updateIs_user(username,password );
                        sqlSession.commit();
                        out = response.getWriter();
                        out.print("升级成功");
                    }else {
                        out = response.getWriter();
                        out.print("升级失败");
                    }
                } catch (IOException e) {
                    out = response.getWriter();
                    out.print("升级失败");
                }finally {
                    out.flush();
                    out.close();
                    sqlSession.close();
                }
                break;
            case 1:
                try {
                    String name = request.getParameter("name");
                    sqlUtil.updateName(username,password,name);
                    sqlSession.commit();
                    out = response.getWriter();
                    out.print("修改昵称成功");
                }catch (Exception e){
                    out = response.getWriter();
                    out.print("修改昵称失败");
                }finally {
                    out.flush();
                    out.close();
                    sqlSession.close();
                }
                break;
            case 2:
                try {
                    if(sqlUtil.isUserPassword(username,password)!=0){
                        password = request.getParameter("newpassword");
                        sqlUtil.updatePassword(username, PasswordMD5Util.generateMD5(password));
                        sqlSession.commit();
                        out = response.getWriter();
                        out.print("修改密码成功");
                    }
                }catch (Exception e){
                    out = response.getWriter();
                    out.print("修改密码失败");
                }finally {
                    out.flush();
                    out.close();
                    sqlSession.close();
                }
                break;
        }
        sqlSession.close();

    }
}

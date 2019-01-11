package org.caiqizhao.serlvet;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.caiqizhao.util.PasswordMD5Util;
import org.mysql.entity.User;
import org.mysql.mapper.SQLUserMapper;
import org.mysql.util.MySQLdb;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RegisterServlet",urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        SqlSessionFactory sqlSessionFactory = MySQLdb.getSqlSession();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = null;
        SQLUserMapper sqlUtil = sqlSession.getMapper(SQLUserMapper.class);
        user = sqlUtil.getUser(username);
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = null;
        if (user != null){
            out = response.getWriter();
            out.print("用户名已被占用");
        }else {
            sqlUtil.addUser(username, PasswordMD5Util.generateMD5(password));
            out = response.getWriter();
            out.print("注册成功");
        }
        out.flush();
        out.close();
        sqlSession.commit();
        sqlSession.close();
    }
}

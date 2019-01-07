package org.caiqizhao.serlvet;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mysql.entity.User;
import org.mysql.entity.UserFile;
import org.mysql.mapper.SQLFileMapper;
import org.mysql.mapper.SQLUserMapper;
import org.mysql.util.MySQLdb;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(name = "DownloadFileServlet",urlPatterns = "/downloadfile")
public class DownloadFileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String filename = request.getParameter("filename");
        SqlSessionFactory sqlSessionFactory = MySQLdb.getSqlSession();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserFile user_file = null;
        SQLFileMapper sqlUtil = sqlSession.getMapper(SQLFileMapper.class);
        user_file = sqlUtil.getDownloadFile(username,filename);

        response.setContentType("text/plain;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        FileInputStream in = null;
        in = new FileInputStream("/Users/caiqizhao/CQZ/"+user_file.getFile_hash());
        byte[] b = new byte[1024*5];
        int size;
        while ( (size = in.read(b)) != -1) {
            System.out.println(size);
            out.write(b,0,size);
        }

        out.flush();
        sqlSession.close();
    }
}

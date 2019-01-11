package org.caiqizhao.serlvet;

import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mysql.entity.Directory;
import org.mysql.entity.UserFile;
import org.mysql.mapper.SQLFileMapper;
import org.mysql.util.MySQLdb;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AddDIrServlet",urlPatterns = "/addDir")
public class AddDIrServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String dir = request.getParameter("dir");
        Directory directory = (Directory) JSONObject.toBean(JSONObject.fromObject(dir),Directory.class);
        SqlSessionFactory sqlSessionFactory = MySQLdb.getSqlSession();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        SQLFileMapper sqlFileMapper = sqlSession.getMapper(SQLFileMapper.class);
        if(directory.getDir_parent() == null ||directory.getDir_parent()==0)
            sqlFileMapper.addDir(directory.getUser_name(),0,directory.getDir_name());
        else
            sqlFileMapper.addDir(directory.getUser_name(),directory.getDir_parent(),directory.getDir_name());

        sqlSession.commit();
        sqlSession.close();
        sqlSessionFactory = MySQLdb.getSqlSession();
        sqlSession = sqlSessionFactory.openSession();
        sqlFileMapper = sqlSession.getMapper(SQLFileMapper.class);
        System.out.println(directory);
        if(directory.getDir_parent() == null ||directory.getDir_parent()==0)
            directory = sqlFileMapper.selectDir(directory.getUser_name(),0,directory.getDir_name());
        else
            directory = sqlFileMapper.selectDir(directory.getUser_name(),directory.getDir_parent(),directory.getDir_name());
        PrintWriter out = response.getWriter();
        out.print(directory.getDir_id());
        out.flush();
        sqlSession.close();
        out.close();
    }
}

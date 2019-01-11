package org.caiqizhao.serlvet;

import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mysql.entity.UserFile;
import org.mysql.mapper.SQLFileMapper;
import org.mysql.util.MySQLdb;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdateFileNameServlet",urlPatterns = "/updateFileName")
public class UpdateFileNameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SqlSessionFactory sqlSessionFactory = MySQLdb.getSqlSession();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        SQLFileMapper sqlFileMapper = sqlSession.getMapper(SQLFileMapper.class);
        String file = request.getParameter("file");
        String fileName = request.getParameter("fileName");
        UserFile userFile = (UserFile) JSONObject.toBean(JSONObject.fromObject(file),UserFile.class);
        if(userFile.getDir_id()==null||userFile.getDir_id()==0){
            sqlFileMapper.deleteNotDirFile(userFile.getUser_name(),fileName);
            sqlSession.commit();
            sqlFileMapper.addUserFile(userFile.getUser_name(),userFile.getFile_name(),null
            ,userFile.getFile_date(),userFile.getFile_hash(),userFile.getFile_size());
        }else {
           sqlFileMapper.deleteFile(userFile.getUser_name(),fileName,userFile.getDir_id());
           sqlSession.commit();
            sqlFileMapper.addUserFile(userFile.getUser_name(),userFile.getFile_name(),userFile.getDir_id()
                    ,userFile.getFile_date(),userFile.getFile_hash(),userFile.getFile_size());
        }
        sqlSession.commit();
        sqlSession.close();
    }
}

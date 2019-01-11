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
import java.io.PrintWriter;

@WebServlet(name = "UploadFileHASHServlet",urlPatterns = "/uploadFileHASH")
public class UploadFileHASHServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String file_json = request.getParameter("file_json");
        UserFile userFile = (UserFile) JSONObject.toBean(JSONObject.fromObject(file_json),UserFile.class);
        SqlSessionFactory sqlSessionFactory = MySQLdb.getSqlSession();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        SQLFileMapper sqlFileMapper = sqlSession.getMapper(SQLFileMapper.class);
        PrintWriter out = response.getWriter();
        if(sqlFileMapper.selectFileHASH(userFile.getFile_hash())==0){
            out.print(1);
        }else {
            sqlFileMapper.upadteFileHASHCount(userFile.getFile_hash());
            sqlFileMapper.addUserFile(userFile.getUser_name(),userFile.getFile_name(),userFile.getDir_id(),
                    userFile.getFile_date(),userFile.getFile_hash(),userFile.getFile_size());
            out.print(0);
        }
        out.flush();
        out.close();
        sqlSession.commit();
        sqlSession.close();
    }
}

package org.caiqizhao.serlvet;

import com.mysql.cj.xdevapi.JsonArray;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
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
import java.util.List;

@WebServlet(name = "DeleteFileServlet",urlPatterns = "/deleteFile")
public class DeleteFileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SqlSessionFactory sqlSessionFactory = MySQLdb.getSqlSession();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        SQLFileMapper sqlFileMapper = sqlSession.getMapper(SQLFileMapper.class);
        String files = request.getParameter("file");
        if(files!=null) {
            JSONArray jsonArray = JSONArray.fromObject(files);
            List<UserFile> userFileList = JSONArray.toList(jsonArray,new UserFile(), new JsonConfig());
            for (UserFile userFile  : userFileList) {
                if (userFile.getDir_id()==null||userFile.getDir_id()==0){
                    sqlFileMapper.deleteNotDirFile(userFile.getUser_name(),userFile.getFile_name());
                }else {
                    sqlFileMapper.deleteFile(userFile.getUser_name(),userFile.getFile_name(),userFile.getDir_id());
                }
                sqlFileMapper.updateFileHash(userFile.getFile_hash());
            }
        }
        sqlSession.commit();
        sqlSession.close();
    }
}

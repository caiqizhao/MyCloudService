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
        String dir_id = request.getParameter("dir_id");
        SqlSessionFactory sqlSessionFactory = MySQLdb.getSqlSession();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserFile user_file = null;
        SQLFileMapper sqlUtil = sqlSession.getMapper(SQLFileMapper.class);
        if(dir_id==null||dir_id.equals("0")) {
            user_file = sqlUtil.getNotDirDownloadFile(username, filename);
        }else{
            user_file = sqlUtil.getDownloadFile(username,filename,Integer.parseInt(dir_id));
        }
        response.setContentType("text/plain;charset=UTF-8");
        //将文件名添加到响应头中
        response.setHeader("Content - disposition","attachment;filename="+filename);
        //获取要下载的文件对象
        File file = new File("/Users/caiqizhao/CQZ/"+user_file.getFile_hash());
        //返回文件的总长度
        response.setContentLength((int) file.length());
        OutputStream out = response.getOutputStream();
        RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rw");
        //若请求为下载请求移动下载指针（有下载请求和获取文件长度请求）
        if(request.getHeader("RANGE")!=null){
            randomAccessFile.seek(Integer.parseInt(request.getHeader("RANGE").split("=")[1].split("-")[0]));
        }
        byte[] b = new byte[1024*5];
        int size;
        while ( (size = randomAccessFile.read(b)) != -1) {
            out.write(b,0,size);
        }
        randomAccessFile.close();
        out.flush();
        sqlSession.close();
    }
}

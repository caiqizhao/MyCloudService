package org.caiqizhao.serlvet;

import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.caiqizhao.util.UploadUtils;
import org.mysql.entity.UserFile;
import org.mysql.mapper.SQLFileMapper;
import org.mysql.util.MySQLdb;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@WebServlet(name = "UploadFileServlet",urlPatterns = "/uploadFile")
public class UploadFileServlet extends HttpServlet {

    UserFile userFile;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**
         * 文件上传的基本操作
        * 1、创建一个磁盘文件项工厂对象
        * 2、创建一个核心解析类
         * 3、解析request请求,返回的是List集合，List集合中存放的是FileItem对象
        * 4、遍历集合，获取每个FileItem，判断是表单项还是文件上传项
        */
        try{

            String url = "/Users/caiqizhao/CQZ/";

            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();

            ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);

            List<FileItem> list = servletFileUpload.parseRequest(request);

            for(FileItem fileItem : list) {
                //判断是表单项还是文件上传项
                if(fileItem.isFormField()) {
                    //普通表单项
                    //接收表单项参数的值
                    String name = fileItem.getFieldName(); //获得表单项的name属性的值
                    String file_json = fileItem.getString("UTF-8"); //获得表单项的值

                    //接收复选框的数据
                    if("file_json".equals(name)) {
                       userFile  = (UserFile) JSONObject.toBean(JSONObject.fromObject(file_json),UserFile.class);
                    }

                }else {
                    //文件上传项；
                    //文件上传功能
                    //获得文件名
                    String fileName = fileItem.getName();
                    if(fileName!=null && !"".equals(fileName)) {
                        //获得唯一文件名
                        String uuidFileName = UploadUtils.getUUIDFileName(fileName);
                        //获得文件上传的数据:
                        InputStream is = fileItem.getInputStream();
                        //将输入流对接到输出流
                        url+=uuidFileName;
                        OutputStream os = new FileOutputStream(url);
                        int len = 0;
                        byte[] b = new byte[1024];
                        while((len = is.read(b))!=-1) {
                            os.write(b, 0, len);
                        }
                        is.close();
                        os.close();
                    }
                }
            }

            FileOutputStream newfile = new FileOutputStream(new File("/Users/caiqizhao/CQZ",userFile.getFile_hash()));
            File file = new File(url);
            FileInputStream oldfile = new FileInputStream(file);
            int len = 0;
            byte[] b = new byte[1024];
            while((len = oldfile.read(b))!=-1) {
                newfile.write(b, 0, len);
            }
            newfile.close();
            oldfile.close();
            file.delete();
            SqlSessionFactory sqlSessionFactory = MySQLdb.getSqlSession();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            SQLFileMapper sqlFileMapper = sqlSession.getMapper(SQLFileMapper.class);
            sqlFileMapper.addFileHash(userFile.getFile_hash());
            sqlSession.commit();
            sqlFileMapper.addUserFile(userFile.getUser_name(),userFile.getFile_name(),userFile.getDir_id(),
                    userFile.getFile_date(),userFile.getFile_hash(),userFile.getFile_size());
            sqlSession.commit();
            sqlSession.close();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
    }
}

package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import stringutil.StringUtils;

public class UploadHandleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UploadHandleServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		// 得到上传文件的保存目录，将上传的文件存放到外界不能直接访问的WEB-INF目录下
//		String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
		String savePath = "E://Code/jee/File_upload_download/WebContent/WEB-INF/upload";
		File file = new File(savePath);
		// 判断上传文件的目录是否存在
		if (!file.exists() && !file.isDirectory()) {
			System.out.println(savePath + "  Need to Make Directory named ‘upload’！");
			// 开始创建目录
			file.mkdir();
		}

		// 消息提示
		String message = "";
		try {
			// 使用Apache上传组件处理文件上传的步骤
			// 1、创建一个DIskFileItemFactory工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 2、创建一个文件上传解析器
			ServletFileUpload uploadparser = new ServletFileUpload(factory);
			// 3、解决上传文件的中文乱码；判断提交上来的数据是否是上传表单的数据
			uploadparser.setHeaderEncoding("UTF-8");
			if (!ServletFileUpload.isMultipartContent(request)) {
				// 不是表单数据，则按照传统方式获取数据
				return;
			}
			// 4、使用ServletFileUpload解析器解析上传的数据，解析结果返回的是一个List<File>集合，每个Item对应一个表单的输入项
			List<FileItem> files = uploadparser.parseRequest(request);
			for (FileItem fileitem : files) {
				// 如果fileitem中封装的是普通的输入项的数据
				if (fileitem.isFormField()) {
					String name = fileitem.getFieldName();
					// 解决普通输入项的数据的中文乱码问题
					String value = fileitem.getString("UTF-8");
					System.out.println(name + "  =   " + value);
				} else {// 如果fileitem里面封装的是上传的文件，则是用处理文件的方式处理
					String filename = fileitem.getName();
					System.out.println(fileitem);
					if (filename == null || filename.trim().equals("")) {
						continue;
					}
					// 注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件是带有其客户端本机路径的，有些则不带。所以我们要对上传文件处理，只得到文件名称即可
					filename = StringUtils.getFileName(filename);
					// 获取fileitem的上传文件的输入流
					InputStream is = fileitem.getInputStream();
					// 创建一个文件输出流
					FileOutputStream fos = new FileOutputStream(savePath + "\\" + filename);
					// 创建一个缓冲区
					byte[] buffer = new byte[1024];
					// 判断输入流中的数据是否已经读完的标识
					int len = 0;
					while ((len = is.read(buffer)) > 0) {
						// 将数据写入到服务器的对应的文件中
						fos.write(buffer, 0, len);
					}
					is.close();
					fos.close();
					fileitem.delete();
					message = new String("Upload Success!".getBytes(), "UTF-8");

				}
			}

		} catch (Exception e) {
			message = new String("Upload Failed!".getBytes(), "UTF-8") + e;
			e.printStackTrace();
		}
		request.setAttribute("message", message);
		request.getRequestDispatcher("/message.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

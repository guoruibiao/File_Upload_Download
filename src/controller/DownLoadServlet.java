package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stringutil.StringUtils;

/**
 * Servlet implementation class DownLoadServlet
 */
@WebServlet("/DownLoadServlet")
public class DownLoadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownLoadServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 得到要下载的文件名
		String filename = request.getParameter("filename");
		filename = new String(filename.getBytes("iso8859-1"), "UTF-8");
		// 上传的文件都是保存在刚才的那个upload的文件夹下.

		String fileSaveRootPath = "E://Code/jee/File_upload_download/WebContent/WEB-INF/upload";
		// 通过文件名找出文件所在的目录
		// 由于路径有点问题，这里采用绝对路径来进行处理
		// String filePath =
		// findFileSavePathByFileName(filename,fileSaveRootPath);
		String filePath = fileSaveRootPath;
		// 得到要下载的文件
		File file = new File(filePath + "\\" + filename);
		// 如果文件不存在
		if (!file.exists()) {
			request.setAttribute("message", "The File you want to download doesn't exists!");
			request.getRequestDispatcher("/message.jsp").forward(request, response);
			return;
		}
		// 处理文件名
		String realname = StringUtils.getFileName(filename);
		// 设置响应头
		response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(realname, "UTF-8"));
		// 读取要下载的文件，保存到文件输入流
		FileInputStream fis = new FileInputStream(filePath + "\\" + filename);
		// 创建输入流
		OutputStream os = response.getOutputStream();
		// 创建缓冲区
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = fis.read(buffer)) > 0) {
			os.write(buffer, 0, len);
		}
		fis.close();
		os.close();

	}

	private String findFileSavePathByFileName(String filename, String fileSaveRootPath) {
		int hashcode = filename.hashCode();
		int dir1 = hashcode & 0xf; // 0--15
		int dir2 = (hashcode & 0xf0) >> 4; // 0--15
		String dir = fileSaveRootPath + "\\" + dir1 + "\\" + dir2;
		File file = new File(dir);
		if (!file.exists()) {
			// 创建目录
			file.mkdir();
		}
		return dir;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

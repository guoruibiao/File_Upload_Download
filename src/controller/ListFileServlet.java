package controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stringutil.StringUtils;


/**
 * Servlet implementation class ListFileServlet
 */
@WebServlet("/ListFileServlet")
public class ListFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ListFileServlet() {
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
		// 获取上传文件的目录
		String uploadPath = "E://Code/jee/File_upload_download/WebContent/WEB-INF/upload";
		// 存储要下载的文件名
		Map<String, String> filenameMap = new HashMap<String, String>();
		// 递归遍历filePath下面的所有的文件和目录，将文件的文件名称存储到Map集合中
		listFile(new File(uploadPath), filenameMap);
		// 将集合存入域找那个，方便页面展示层获取数据
		request.setAttribute("filenameMap", filenameMap);
		request.getRequestDispatcher("/listfiles.jsp").forward(request, response);
	}

	public void listFile(File file, Map<String, String> filenameMap) {
		// 如果file代表的不是一个文件，而是一个目录
		if (!file.isFile()) {
			// 列出该目录下面的所有的文件和目录
			File[] files = file.listFiles();
			// 遍历files[] 数组
			for (File f : files) {
				// 递归
				listFile(f, filenameMap);
			}
		} else {
			// 使用自己的那个strigutil来获取文件的名称，而不是路径名称
			String realName = StringUtils.getFileName(file.getName());
			try {
				filenameMap.put(new String(file.getName().getBytes("iso8859-1"),"UTF-8"), realName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

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

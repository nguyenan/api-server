package com.wut.protocols.upload;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.commons.fileupload.DiskFileUpload;
//import org.apache.commons.fileupload.FileItem;

public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1359493015290104325L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handle(req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handle(req, resp);
	}

	private void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		PrintWriter out = response.getWriter();
//		//out.println("hiiiya!!");
//
//		try {
//			System.out.println("Content Type =" + request.getContentType());
//
//			DiskFileUpload fu = new DiskFileUpload();
//			// If file size exceeds, a FileUploadException will be thrown
//			fu.setSizeMax(1000000);
//
//			List fileItems = fu.parseRequest(request);
//			Iterator itr = fileItems.iterator();
//
//			while (itr.hasNext()) {
//				FileItem fi = (FileItem) itr.next();
//
//				// Check if not form field so as to only handle the file inputs
//				// else condition handles the submit button input
//				if (!fi.isFormField()) {
//					System.out.println("nNAME: " + fi.getName());
//					System.out.println("SIZE: " + fi.getSize());
//					// System.out.println(fi.getOutputStream().toString());
//					File fNew = new File("data/", fi.getName());
//
//					System.out.println(fNew.getAbsolutePath());
//
//					fi.write(fNew);
//
//				} else {
//					System.out.println("Field =" + fi.getFieldName());
//				}
//			}
//
//		} catch (Exception e) {
//			out.print("{\"message\":\"invalid request no upload data found\"}");
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return;
//		}
//		
//		out.print("{\"message\":\"success\"}");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		handle(request, response);
	}
}

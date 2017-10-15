package com.bigdata2017.emaillist.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bigdata2017.emaillist.dao.EmaillistDao;
import com.bigdata2017.emaillist.vo.EmaillistVo;

/**
 * controller를 따로 빼 내서 jsp에서 처리하던 controll들을 여기서 다 처리하게 함
 * jsp같은 페이지는 View만 보여주게 함
 */
@WebServlet("/el")
public class EmaillistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String actionName = request.getParameter( "a" );

		if( "form".equals( actionName ) ) {
			RequestDispatcher rd = request.getRequestDispatcher( "/WEB-INF/views/form.jsp" );
			rd.forward( request, response );
		} else if( "insert".equals( actionName ) ) {
			request.setCharacterEncoding( "UTF-8" );

			String firstName = request.getParameter( "fn" );
			String lastName = request.getParameter( "ln" );
			String email = request.getParameter( "email" );
			
			EmaillistVo vo = new EmaillistVo();
			vo.setFirstName(firstName);
			vo.setLastName(lastName);
			vo.setEmail(email);
			
			new EmaillistDao().insert(vo);
			
			// 이제 controller가 있는 MVC패턴에서는 자기 자신으로 돌아와야 하니 /el로 돌아오게 함
			response.sendRedirect( request.getContextPath() + "/el" );
		} else {
			/* defulat 요청 처리 (list) => 굳이 list, form, insert가 아닐 때 잘못된 요청이라고 하기보다는
			 * list를 보여주는 것이 세련된 방법 */
			EmaillistDao dao = new EmaillistDao();
			List<EmaillistVo> list = dao.getList();
			
			request.setAttribute( "list", list );
			
			/* 
			 * client에서 들어오는 request에 대해 redirection할 곳을 지정
			 * -----redirection 표현 3가지------
			 * 1. request extension
			 * 2. request dispatch(controller를 빼 내었으니까)
			 * 3. forword 
			 * 
			 * request로 controll servlet에 들어온 request를 다른 페이지로 forword시키니까 response를 넣어서 forword
			 */
			RequestDispatcher rd = request.getRequestDispatcher( "/WEB-INF/views/index.jsp" );
			rd.forward( request, response );
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

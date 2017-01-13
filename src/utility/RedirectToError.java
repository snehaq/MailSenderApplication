package utility;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectToError {
	
	public static void errorPage(HttpServletRequest request,HttpServletResponse response, String errMsg){

		request.setAttribute("errorMessage", errMsg);
		RequestDispatcher rd = request.getRequestDispatcher("pages/Error.jsp");
		try {
			rd.forward(request, response);
		} catch (ServletException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}

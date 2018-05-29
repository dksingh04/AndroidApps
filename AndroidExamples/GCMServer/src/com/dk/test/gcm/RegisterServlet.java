package com.dk.test.gcm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

/**
 * Servlet implementation class RegisterServlet
 */

public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	public static final String SERVER_API_KEY= "AIzaSyBD2n812qpoIL8ZcPGRpKaUvBHRiOiMwhE";   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Register to Device to Database and send Registration Id to GCM Server");
		String SERVER_API_KEY= "AIzaSyBD2n812qpoIL8ZcPGRpKaUvBHRiOiMwhE";
		String regId = request.getParameter("RegId");
		String userId = request.getParameter("UserId");
		String userName = request.getParameter("UserName");
		System.out.println("RegID : "+regId+" UserId : "+userId+" UserName: "+userName);
		// Store these Information to database
		System.out.println("Before Message Created Successfully");
		// Send information to GCM Server
		 Sender sender = new Sender(SERVER_API_KEY);
		 Message message = new Message.Builder()
         .addData("Server Notification", "Hello from gcm-server")
         .build();
		 System.out.println("Message Created Successfully");
		  try{
	            Result reslt = sender.send(message,regId,0);
	            System.out.println("Canonical Registration Id: "+reslt.getCanonicalRegistrationId());
	         //   logger.info(reslt.toString());
	        }catch(IOException e){
	        	e.printStackTrace();
	           // logger.warning("send IOException : " + e.toString());
	        }
		  
		  
		 
		
	}
	
	
}

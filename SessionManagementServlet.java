

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SessionManagementServlet
 */
@WebServlet("/")
public class SessionManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static ConcurrentHashMap <UUID, session> map= new ConcurrentHashMap <UUID, session>();
    
	public SessionManagementServlet() {
        super();
    }
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			                                  throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			                                   throws ServletException, IOException {
		//clear expired sessions in map
		for (session Session : map.values()){
			if ((new Date()).compareTo(Session.getExpiration()) > 0)
				map.remove(Session.getSessionID());
		}
		
		String[] locations = {request.getLocalAddr().toString()};
		String location = locations[0];
		
		session presentSession = null;
		boolean newCookie = false;
		
		Cookie[] sessionCookies = request.getCookies();	
		Cookie sessionCookie = null;
		
		//check if request has cookie
		if(sessionCookies != null){
			for(Cookie cookie : sessionCookies){
			    if(cookie.getName().equals("CS5300PROJ1SESSION")){
			    	sessionCookie = cookie;
			    	break;
			    }
		    }
		}
		
		if (sessionCookies == null || sessionCookie == null){
			//request without cookie - first log in or cookie expires
			//create new session, new cookie
            session newSession = new session();		
			
            sessionCookie = new Cookie("CS5300PROJ1SESSION", newSession.getSessionID() 
            		                                      + "_" + 1 + "_" + location);
            sessionCookie.setMaxAge(10);
            newCookie = true;
            
            map.put(newSession.getSessionID(), newSession);
                      
            presentSession = newSession; 
		}else{
			//request with cookie
			//get cookie session
			String[] cookieValue = sessionCookie.getValue().split("_");
	    	UUID SessionID = UUID.fromString(cookieValue[0]);
	    	
	    	boolean mapContains = false;
	  
	    	if (map.containsKey(SessionID)){
	    		mapContains = true;
	    		
	    		if ((new Date()).compareTo(map.get(SessionID).getExpiration()) > 0){
		    		map.remove(SessionID);
		    		mapContains = false;
		    	}
	    	}
	 
	    	if (!mapContains){
	    		//if map doesn't contain this session or contains but session expires
		    	//create new session, new cookie
	    		session newSession = new session();		
				
	    		sessionCookie = new Cookie("CS5300PROJ1SESSION", newSession.getSessionID() 
                                                              + "_" + 1 + "_" + location);
	            sessionCookie.setMaxAge(10);
	            newCookie = true;
	            
	            map.put(newSession.getSessionID(), newSession);
	                       
	            presentSession = newSession;
		    }else{
		    	//if map contains this session and session doesn't expire
		    	//get old session from map
		    	presentSession = map.get(SessionID);
		    }
		}
		
		//if press replace
		//update old session (new session case is solved above)
		if (request.getParameter("Replace") != null){
			if (!newCookie){
				String newMessage = request.getParameter("newMessage");
				assert newMessage.length() < 512;
				int newVersion = presentSession.getVersion() + 1;
				Date newExpiration = new Date((new Date()).getTime() + 10000L);
				
				sessionCookie.setValue(presentSession.getSessionID()+ "_" 
				                              + newVersion + "_" + location);
				sessionCookie.setMaxAge(10);
		
				presentSession.setVersion(newVersion);
				presentSession.setMesssage(newMessage);
				presentSession.setExpiration(newExpiration);
				map.replace(presentSession.getSessionID(), presentSession);
			}
		}
		
		//if press refresh
	    //update old session (new session case is solved above)
		if (request.getParameter("Refresh") != null){
			if (!newCookie){
				int newVersion = presentSession.getVersion() + 1;
				Date newExpiration = new Date((new Date()).getTime() + 10000L);
				
				sessionCookie.setValue(presentSession.getSessionID()+ "_" 
                                                  + newVersion + "_" + location);
				sessionCookie.setMaxAge(10);
		
				presentSession.setVersion(newVersion);
				presentSession.setExpiration(newExpiration);
				map.replace(presentSession.getSessionID(), presentSession);
			}
	    }
		
		//if press logout
		if(request.getParameter("Logout") != null){
			sessionCookie.setMaxAge(0);
			map.remove(presentSession.getSessionID());
           
			response.addCookie(sessionCookie);

			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("Log out Successfully!");
			
			return;			
		}

		response.addCookie(sessionCookie);
		
		request.setAttribute("SessionID", presentSession.getSessionID() + ""); 
		request.setAttribute("Version", presentSession.getVersion() + "");
		request.setAttribute("Message", presentSession.getMessege());
		request.setAttribute("Cookie", sessionCookie.getValue());
		request.setAttribute("Expires", presentSession.getExpiration() + "");
	
		request.getRequestDispatcher ("message.jsp").forward(request, response);
	}	
}	



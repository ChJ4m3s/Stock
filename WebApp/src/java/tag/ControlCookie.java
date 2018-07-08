package tag;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ControlCookie extends SimpleTagSupport {

    @Override
    public void doTag() {
        PageContext context = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        Cookie cookies[] = request.getCookies();
        int i = 0;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("email") || c.getName().equals("password")) {
                    HttpSession s = request.getSession();
                    s.setAttribute(c.getName(), c.getValue());
                    i++;
                }             
            }
        }
        if (i == 2) {
            HttpServletResponse response = (HttpServletResponse) context.getResponse();
            try {
                response.sendRedirect("profile.jsp");
            } catch (IOException ex) {
                Logger.getLogger(ControlCookie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}

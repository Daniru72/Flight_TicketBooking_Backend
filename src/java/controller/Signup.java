package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Status;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Mail;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "Signup", urlPatterns = {"/Signup"})
public class Signup extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject requestobject = gson.fromJson(req.getReader(), JsonObject.class);
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("Success", Boolean.FALSE);

        String fname = requestobject.get("fname").getAsString();
        String lname = requestobject.get("lname").getAsString();
        String email = requestobject.get("email").getAsString();
        String password = requestobject.get("password").getAsString();
        String mobile = requestobject.get("mobile").getAsString();

        if (fname.isEmpty()) {
            responseJson.addProperty("message", "Fill your First name");
        } else if (fname.length() < 45) {
            responseJson.addProperty("message", "Firstname must be lower than 45 caractors");
        } else if (!fname.matches("[a-zA-Z]+")) {
            responseJson.addProperty("message", "First name accept only letters");
            // Validate first name for being non-blank and alphabetic only
        } else if (lname.isEmpty()) {
            responseJson.addProperty("message", "Fill your Lirst name");
        } else if (lname.length() < 45) {
            responseJson.addProperty("message", "Lastname must be lower than 45 caractores");
        } else if (!lname.matches("[a-zA-Z]+")) {
            responseJson.addProperty("message", "First name accept only letters");
            // Validate first name for being non-blank and alphabetic only

        } else if (email.isEmpty()) {
            responseJson.addProperty("message", "Fill your Email address");
        }else if (!Validation.isEmailValid(email)) {
            responseJson.addProperty("message", "Invalide Email address");
        }else if (mobile.isEmpty()) {
            responseJson.addProperty("message", "Fill your Mobile number");
        } else if (!mobile.matches("^07[1,2,4,5,6,7,8,0][0-9]{7}$")) {
            responseJson.addProperty("message", "Invalide Mobile Number");
        }else if (password.isEmpty()) {
            responseJson.addProperty("message", "Fill your password");
        } else if (password.length() >= 5) {
            responseJson.addProperty("message", "password must be grater than 5 caractors");
        } else {

            try {

                Session session = HibernateUtil.getSessionFactory().openSession();

                Criteria criteria1 = session.createCriteria(User.class);
                criteria1.add(Restrictions.eq("email", email));

                if (!criteria1.list().isEmpty()) {
                    //User already Registered
                    responseJson.addProperty("message", "User already registerd");
                } else {
                    //New user
                    
                    //generate verification code
                    int code = (int) (Math.random() * 100000);

                    final User user = new User();
                    user.setEmail(email);
                    user.setFname(fname);
                    user.setLname(lname);
                    user.setMobile(mobile);
                    user.setPassword(password);
                    user.setJoined_date(new Date());

                    Status status = (Status) session.get(Status.class, 1);
                    user.setUser_Status(status);
                    
                    user.setVerification(String.valueOf(code));
                    
                     //send verification code
                Thread sendMailThread = new Thread(){
                    @Override
                    public void run() {
                        Mail.sendMail(user.getEmail(),"SKYBIRD Verification", "<h1>Your Verification Code: "+user.getVerification()+"</h1>");
                    }
                  
                    
                
                };
//               sendMailThread.start();

                session.save(user);
                session.beginTransaction().commit();
                responseJson.addProperty("Success", Boolean.TRUE);

                }
                
                 session.close();

            } catch (Exception e) {
                responseJson.addProperty("message", "Server side Error");
                e.printStackTrace();
            }

        }
        
       
         
           resp.setContentType("application/json");
         resp.getWriter().write(gson.toJson(responseJson));

    }

}

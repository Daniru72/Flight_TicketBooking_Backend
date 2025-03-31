/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;



@WebServlet(name = "Verification", urlPatterns = {"/Verification"})
public class Verification extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       
        Gson gson = new Gson();
        
        JsonObject requestobject = gson.fromJson(req.getReader(), JsonObject.class);
        
         String verification = requestobject.get("verification").getAsString();
         String email = requestobject.get("email").getAsString();
        
         JsonObject responseJson = new JsonObject();
        responseJson.addProperty("Success", "false");
        
        if(email.isEmpty()){
           responseJson.addProperty("message", "empty email");
        }else if(verification.isEmpty()){
            responseJson.addProperty("message", "empty Verification");
        }else{
            
            try {
               
               Session session = HibernateUtil.getSessionFactory().openSession();
          
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", email));
            criteria1.add(Restrictions.eq("verification", verification));
            
          if(!criteria1.list().isEmpty()){
              
              
             User user = (User) criteria1.list().get(0);
             user.setVerification("Verified");
             
             session.update(user);
             session.beginTransaction().commit();
              responseJson.addProperty("Success", "true");
              responseJson.addProperty("message", "Verification Successfil");
              
             
          }else{
          responseJson.addProperty("message", "Empty email and verification");
          
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

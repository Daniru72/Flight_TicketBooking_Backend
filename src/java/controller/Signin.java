package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "Signin", urlPatterns = {"/Signin"})
public class Signin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();

        JsonObject requestobject = gson.fromJson(req.getReader(), JsonObject.class);

        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("Success", Boolean.FALSE);

        String email = requestobject.get("email").getAsString();
        String password = requestobject.get("password").getAsString();

        if (email.isEmpty()) {
            responseJson.addProperty("message", "Fill your Email address");
        } else if (!Validation.isEmailValid(email)) {
            responseJson.addProperty("message", "Invalide Email address");
        } else if (password.isEmpty()) {
            responseJson.addProperty("message", "Fill your password");
        } else if (password.length() >= 5) {
            responseJson.addProperty("message", "password must be grater than 5 caractors");
        } else {

            try {

                Session session = HibernateUtil.getSessionFactory().openSession();

                Criteria criteria1 = session.createCriteria(User.class);
                criteria1.add(Restrictions.eq("email", email));
                criteria1.add(Restrictions.eq("password", password));
                criteria1.add(Restrictions.eq("password", password));

                if (!criteria1.list().isEmpty()) {

                    User user = (User) criteria1.list().get(0);

                    if (user.getVerification().equals("Verified")) {
                        //verified user

                        responseJson.addProperty("message", "userlogin");
                        responseJson.add("user", gson.toJsonTree(user));
                        responseJson.addProperty("Success", Boolean.TRUE);

                    } else {
                        //unverified user
                        responseJson.addProperty("message", "Unverified");

                    }

                } else {

                    responseJson.addProperty("message", "Invalide Details!");
                }

                session.close();

            } catch (Exception e) {
                e.printStackTrace();
                responseJson.addProperty("message", "Server error");
            }

        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));

    }

}

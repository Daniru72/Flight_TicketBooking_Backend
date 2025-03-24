
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User implements Serializable{
    
     @Id
    @Column(name = "email", length = 50, nullable = false,unique = true)
    private String email;
     
     @Column(name = "fname", length = 45, nullable = false)
    private String fname;
    
      @Column(name = "lname", length = 45, nullable = false)
    private String lname;
    
      @Column(name = "mobile", length = 10, nullable = false)
    private String mobile;
      
     @Column(name = "password", length = 10, nullable = false)
    private String password;
     
     @Column(name = "verification_code", length = 45, nullable = false)
    private String verification;
     
      @Column(name = "joined_date", nullable = false)
    private Date joined_date;
      
     @ManyToOne
    @JoinColumn(name = "user_status_status_id")
    private Status user_Status;
    
      public User(){
      
      }  

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public Date getJoined_date() {
        return joined_date;
    }

    public void setJoined_date(Date joined_date) {
        this.joined_date = joined_date;
    }

    public Status getUser_Status() {
        return user_Status;
    }

    public void setUser_Status(Status user_Status) {
        this.user_Status = user_Status;
    }
      
      
    
}

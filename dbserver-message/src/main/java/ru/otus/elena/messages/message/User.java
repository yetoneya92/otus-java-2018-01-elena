
package ru.otus.elena.messages.message;


import java.util.ArrayList;
import java.util.Objects;
import javax.servlet.http.HttpSession;

public class User{

    private String name;
    private HttpSession session;
    private ArrayList<String>answer;

    public User(String name,HttpSession session) {
        this.name=name;
        this.session=session;
    }
    public String getName(){
        return name;
    }
    public HttpSession getSession(){
        return session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        if(user.getName().equals(this.getName())&&user.getSession().equals(this.getSession())){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name,session);
    }
    public String toString(){
        return name+session.toString();
}

}

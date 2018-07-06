
package newpackage;

import java.util.ArrayList;
import java.util.Set;
import ru.otus.elena.dbservice.dataset.Phone;
import ru.otus.elena.dbservice.dataset.base.DataSet;

public class UnknownEmployee extends DataSet{
    public String name;
    public UnknownPhone phone;
    public Set<UnknownFriend>friends;

    public UnknownEmployee() {
    }
    
    public UnknownEmployee(String name, UnknownPhone phone,Set<UnknownFriend>friends) {
        super(0);
        this.name = name;
        this.phone = phone;
        this.friends=friends;
    }
    public UnknownEmployee(String name, UnknownPhone phone,Set<UnknownFriend>friends, long id) {
        super(id);
        this.name = name;
        this.phone = phone;
        this.friends=friends;
    }

    public String getName() {
        return name;
    }

    public UnknownPhone getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(UnknownPhone phone) {
        this.phone = phone;
    }

    public void setId(long id) {
        super.setId(id);
    }

    @Override
    public long getId() {
        return super.getId(); 
    }

    public void setFriends(Set<UnknownFriend> friends) {
        this.friends = friends;
    }

    public Set<UnknownFriend> getFriends() {
        return friends;
    }
    
    @Override
    public String toString() {
        return "unknownEmployee: name="+name+", "+phone.toString()+", friends="+friends.toString()+", id="+id;
    }
    
}

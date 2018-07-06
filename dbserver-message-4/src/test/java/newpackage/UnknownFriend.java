package newpackage;

import ru.otus.elena.dbservice.dataset.base.DataSet;

public class UnknownFriend extends DataSet{
    public String name;
    public int phone;

    public UnknownFriend() {
    }

    public UnknownFriend(String name, int phone, long id) {
        super(id);
        this.name = name;
        this.phone = phone;
    }

    public UnknownFriend(String name, int phone) {
        super(0);
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    @Override
    public long getId() {
        return super.getId();
    }

    public int getPhone() {
        return phone;
    }

    @Override
    public void setId(long id) {
        super.setId(id); //To change body of generated methods, choose Tools | Templates.
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "["+name+","+phone+","+id+"]";
    }
    
    
}

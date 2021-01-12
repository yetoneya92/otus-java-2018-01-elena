
package ru.otus.elena.dbservice.dataset;

import ru.otus.elena.dbservice.dataset.base.DataSet;
import java.io.Serializable;

public class Baby extends DataSet implements Serializable{
    
    public String name;   
    public Phone phone;

    public Baby() {
    }

    public Baby(String name, Phone phone) {
        super(0);
        this.name = name;
        this.phone = phone;
    }

    public Baby(String name, Phone phone, long id) {
        super(id);
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "baby="+getName()+" "+phone.toString()+" id="+id; 
    }
    
}

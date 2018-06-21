
package ru.otus.elena.dbservice.dataset;

import ru.otus.elena.dbservice.dataset.base.DataSet;

public class Phone extends DataSet {

    public int phone;

    public Phone(int phone, long id) {
        super(id);
        this.phone = phone;
    }

    public Phone(int phone) {
        super(0);
        this.phone = phone;
    }

    public Phone() {
    }
    
    public void setPhone(int phone){
        this.phone=phone;
    }
    public int getPhone(){
        return phone;
    }
    public String toString(){
        return "[phone="+phone+" id="+id+"]";
    }
}

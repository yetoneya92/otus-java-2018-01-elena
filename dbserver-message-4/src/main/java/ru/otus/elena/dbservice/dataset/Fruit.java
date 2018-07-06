
package ru.otus.elena.dbservice.dataset;

import java.io.Serializable;
import ru.otus.elena.dbservice.dataset.base.DataSet;


public class Fruit extends DataSet{
    public String name;
    public int number;
    

    public Fruit(String name,int number){
        super(0);
        this.name=name;
        this.number=number;
    }

    public Fruit(String name, int number, long id) {
        super(id);
        this.name = name;
        this.number = number;
    }

    public Fruit() {
    }
    
    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return "fruit="+name+","+number+", id="+super.id; 
    }
    
}

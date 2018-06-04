
package ru.otus.elena.dbservice.dataset;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import ru.otus.elena.dbservice.dataset.base.DataSet;

@Entity
@Table(name="fruit")
public class Fruit extends DataSet implements Serializable{
    
    @Column(name = "fruit_name")
    public String name;
    
    @Column(name = "fruit_number")
    public int number;
    
    @ManyToOne
    
    @JoinColumn(name="fruit_compote_id",nullable=false)    
    public Compote compote;
    
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

    public Compote getCompote() {
        return compote;
    }

    public void setCompote(Compote compote) {
        this.compote = compote;
    }

    @Override
    public String toString() {
        return "fruit="+name+","+number+", id="+super.id; 
    }
    
}

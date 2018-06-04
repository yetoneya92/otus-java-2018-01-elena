
package ru.otus.elena.dbservice.dataset;

import java.io.Serializable;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name="compote")
public class Compote extends DataSet implements Serializable{
    
    @Column(name="compote_name")
    public String name;    
    
    @OneToMany(mappedBy = "compote",cascade = CascadeType.ALL,orphanRemoval=true,fetch=FetchType.LAZY)
    
    public Set<Fruit>fruits=new HashSet<>();

    public Compote(String name, Set<Fruit> fruits, long id) {
        super(id);
        this.name = name;
        this.fruits.addAll(fruits);
        fruits.forEach(s->s.setCompote(this));
    }

    public Compote() {
    }

    public Compote(String name, Set<Fruit> fruits) {
        super(0);
        this.name = name;
        this.fruits.addAll(fruits);
        fruits.forEach(s->s.setCompote(this));
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFruit(Set<Fruit> fruits) {
        this.fruits = fruits;
    }

    public Set<Fruit> getFruit() {
        return fruits;
    }
    public void addFruit(Fruit fruit){
        fruits.add(fruit);
    }

    @Override
    public String toString() {
        StringBuilder result=new StringBuilder("compote="+name+" from[");
        fruits.forEach(s->result.append(s.toString()).append(" "));
        result.append("]").append(" id=").append(super.id);
        return result.toString();
    }
    

}

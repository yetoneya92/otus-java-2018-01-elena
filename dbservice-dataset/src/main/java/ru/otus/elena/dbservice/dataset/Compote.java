
package ru.otus.elena.dbservice.dataset;

import java.io.Serializable;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import java.util.HashSet;
import java.util.Set;

public class Compote extends DataSet{
    
    public String name;        
    public Set<Fruit>fruits=new HashSet<>();

    public Compote(String name, Set<Fruit> fruits, long id) {
        super(id);
        this.name = name;
        this.fruits.addAll(fruits);
    }

    public Compote() {
    }

    public Compote(String name, Set<Fruit> fruits) {
        super(0);
        this.name = name;
        this.fruits.addAll(fruits);
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

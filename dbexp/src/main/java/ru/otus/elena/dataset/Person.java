
package ru.otus.elena.dataset;

public class Person extends DataSet{
    public String name;
    public int age;
    public Owner owner;
    public Person(String name,int age,Owner owner){
        super(0);
        this. name=name;
        this.age=age;
        this.owner=owner;
    }
        public Person(String name,int age,Owner owner,long id){
        super(id);    
        this. name=name;
        this.age=age;
        this.owner=owner;
    }
    public long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public int getAge(){
        return age;
    }
    public Owner getOwner(){
        return owner;
    }
    @Override
    public String toString(){
        return "[name="+name+", age="+age+" ,owner="+owner.toString()+"]";
    }
}

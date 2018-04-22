
package ru.otus.elena.dataset;

public class Owner extends DataSet{
    public String name;
    public int age;

    public Owner(String name, int age) {
        super(0);
        this.name = name;
        this.age = age;
    }

    public Owner(String name, int age, long id) {
        super(id);
        this.name = name;
        this.age = age;
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
    @Override
    public String toString(){
        return "[name="+name+", age="+age+"]";
    }
}

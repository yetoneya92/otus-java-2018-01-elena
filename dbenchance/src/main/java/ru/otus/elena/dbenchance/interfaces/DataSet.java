
package ru.otus.elena.dbenchance.interfaces;
import javax.persistence.*;

@MappedSuperclass        
public class DataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public long id;
    public DataSet(long id){
        this.id=id;
    }
    public DataSet(){
    }
    
    long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }
}

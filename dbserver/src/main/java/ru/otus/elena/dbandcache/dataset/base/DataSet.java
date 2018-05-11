
package ru.otus.elena.dbandcache.dataset.base;
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
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

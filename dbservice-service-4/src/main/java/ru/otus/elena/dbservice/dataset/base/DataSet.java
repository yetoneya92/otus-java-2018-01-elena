
package ru.otus.elena.dbservice.dataset.base;
        
public class DataSet {

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

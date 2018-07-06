
package newpackage;

import ru.otus.elena.dbservice.dataset.base.DataSet;


public class UnknownPhone extends DataSet{
      public int homePhone;
      public int cellPhone;

    public UnknownPhone() {
    }

    public UnknownPhone(int homePhone, int cellPhone) {
        super(0);
        this.homePhone = homePhone;
        this.cellPhone = cellPhone;
    }

    public UnknownPhone(int homePhone, int cellPhone, long id) {
        super(id);
        this.homePhone = homePhone;
        this.cellPhone = cellPhone;
    }

    public int getCellPhone() {
        return cellPhone;
    }

    public int getHomePhone() {
        return homePhone;
    }

    public void setCellPhone(int cellPhone) {
        this.cellPhone = cellPhone;
    }

    public void setHomePhone(int homePhone) {
        this.homePhone = homePhone;
    }

    @Override
    public long getId() {
        return super.getId();
    }

    @Override
    public void setId(long id) {
        super.setId(id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return "unknownPhone=[cellphone="+cellPhone+", homephone="+homePhone+",id="+id+"]";
    }      
}
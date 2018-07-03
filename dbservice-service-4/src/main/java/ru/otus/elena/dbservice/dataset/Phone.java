
package ru.otus.elena.dbservice.dataset;

import ru.otus.elena.dbservice.dataset.base.DataSet;

public class Phone extends DataSet {

    public int cellPhone;
    public int homePhone;

    public Phone(int cellPhone, int homePhone, long id) {
        super(id);
        this.cellPhone = cellPhone;
    }

    public Phone(int cellPhone, int homePhone) {
        super(0);
        this.cellPhone = cellPhone;
        this.homePhone=homePhone;
    }

    public Phone() {
    }

    public void setCellPhone(int cellPhone) {
        this.cellPhone = cellPhone;
    }

    public int getCellPhone() {
        return cellPhone;
    }

    public void setHomePhone(int homePhone) {
        this.homePhone = homePhone;
    }

    public int getHomePhone() {
        return homePhone;
    }

    @Override
    public String toString() {
        return "phone=[cellPhone="+cellPhone+", homePhone="+homePhone+", id="+id+"]";
    }
    


}

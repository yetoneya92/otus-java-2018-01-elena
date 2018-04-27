
package ru.otus.elena.dbenchance.dao;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ru.otus.elena.dbenchance.interfaces.DataSet;

public class DataSetDAO {

    private Session session;

    public DataSetDAO(Session session) {
        this.session = session;
    }

    public <T extends DataSet> void save(T object) {
        session.save(object);
    }

    public <T extends DataSet> T loadById(long id, Class<T> clazz) {
        return session.load(clazz, id);
    }

    public <T extends DataSet> T loadByName(String name, Class<T> clazz) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        Root<T> from = criteria.from(clazz);
        criteria.where(builder.equal(from.get("name"), name));
        Query<T> query = session.createQuery(criteria);
        return query.uniqueResult();
    }

    public <T extends DataSet> List<T> readAll(Class<T> clazz) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        criteria.from(clazz);
        return session.createQuery(criteria).list();
    }

}


package ru.otus.elena.dbservice.execution;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.otus.elena.dbservice.dataset.Compote;
import ru.otus.elena.dbservice.dataset.Fruit;
import ru.otus.elena.dbservice.dbservice.ServiceConnection;
import ru.otus.elena.dbservice.executors.PreparedExecutor;
import ru.otus.elena.dbservice.executors.TExecutor;

@Service
public class SaveCompote {

    private static final String INSERT_INTO_COMPOTE = "insert into compote (id,compote_name) values (null,?)";
    private static final String INSERT_INTO_FRUIT = "insert into fruit (id,fruit_name,fruit_number,fruit_compote_id) values (null,?,?,?)";
    private ServiceConnection serviceConnection;

    @Autowired
    public SaveCompote(ServiceConnection serviceConnection) {
        this.serviceConnection = serviceConnection;
    }
  
    

    public void removeDuplicate(ArrayList<Compote> compoteList, ArrayList<String> messages) {
        try {
            String command = "select*from compote";
            
            TExecutor exec = new TExecutor(serviceConnection.getConnection());
            exec.execQuery(command, result -> {
                while(result.next()){
                    Iterator iterator=compoteList.iterator();
                    while(iterator.hasNext()){
                        Compote compote=(Compote) iterator.next();
                        if(compote.getName().toLowerCase().equals(result.getString(2).toLowerCase())){                            
                            iterator.remove();
                            messages.add("Compote "+compote.getName()+" already exists");
                        }
                    }
                }
                result.close();
                return null;
            });
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            messages.add(sqle.getMessage());
        }
    }

    public int save(ArrayList<Compote> compotes, ArrayList<String> messages) {
        int[]counter=new int[1];
        try {            
            PreparedExecutor execCompote = new PreparedExecutor(serviceConnection.getConnection());
            PreparedExecutor execFruit = new PreparedExecutor(serviceConnection.getConnection());
            serviceConnection.getConnection().setAutoCommit(false);
            execCompote.execUpdate(INSERT_INTO_COMPOTE, stp -> {
                for (Compote compote : compotes) {
                    stp.setString(1, compote.getName());
                    int p = stp.executeUpdate();
                    counter[0] += p;
                    if (p != 0) {
                        ResultSet compoteKeys = stp.getGeneratedKeys();
                        compoteKeys.next();
                        long compoteId = compoteKeys.getLong(1);
                        compote.setId(compoteId);
                        compoteKeys.close();
                        counter[0]++;
                        execFruit.execUpdate(INSERT_INTO_FRUIT, stb -> {
                            for (Fruit fruit : compote.getFruit()) {
                                stb.setString(1, fruit.getName());
                                stb.setInt(2, fruit.getNumber());
                                stb.setLong(3, compoteId);
                                int b = stb.executeUpdate();
                                if (b != 0) {
                                    ResultSet fruitKey = stb.getGeneratedKeys();
                                    fruitKey.next();
                                    long fruitId = fruitKey.getLong(1);
                                    fruit.setId(fruitId);
                                    fruitKey.close();
                                    counter[0]++;
                                }
                            }
                        });
                        messages.add("has been saved: " + compote.toString());
                    }
                }
            });
            serviceConnection.getConnection().commit();
        } catch (SQLException e) {
            try {
                serviceConnection.getConnection().rollback();
                counter[0] = 0;
            } catch (SQLException ex) {
                messages.add(ex.getMessage());
            }
            messages.add(e.getMessage());
        } finally {
            try {
                serviceConnection.getConnection().setAutoCommit(true);
            } catch (SQLException ex) {
                messages.add(ex.getMessage());
            }
        }
        return counter[0];
    }
}

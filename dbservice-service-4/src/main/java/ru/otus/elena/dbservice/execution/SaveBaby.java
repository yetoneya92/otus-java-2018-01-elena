package ru.otus.elena.dbservice.execution;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dbservice.ServiceConnection;
import ru.otus.elena.dbservice.executors.PreparedExecutor;
import ru.otus.elena.dbservice.executors.TExecutor;

@Component
public class SaveBaby {

    private static final String INSERT_INTO_BABY = "insert into baby (id,baby_name,baby_phone_id) values (null,?,?)";
    private static final String INSERT_INTO_PHONE = "insert into phone (id,phone_phone) values (null,?)";

    @Autowired
    private ServiceConnection serviceConnection;

    public void removeDuplicate(ArrayList<Baby> babyList, ArrayList<String> messages) {
        try {
            String command = "select*from phone";
            TExecutor exec = new TExecutor(serviceConnection.getConnection());
            exec.execQuery(command, result -> {
                while (result.next()) {
                    Iterator iterator = babyList.iterator();
                    while (iterator.hasNext()) {
                        Baby baby = (Baby) iterator.next();
                        if (result.getInt(2) == baby.getPhone().getPhone()) {
                            iterator.remove();
                            messages.add("already exists: phone=" + baby.getPhone().getPhone());
                        }
                    }
                }
                result.close();
                return null;
            });
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            messages.add(sqle.toString());
        }
    }

    public int save(ArrayList<Baby> babies, ArrayList<String> messages) {
        int[]counter=new int[1];
        try {            
            PreparedExecutor execPhone = new PreparedExecutor(serviceConnection.getConnection());
            PreparedExecutor execBaby = new PreparedExecutor(serviceConnection.getConnection());
            serviceConnection.getConnection().setAutoCommit(false);
            execPhone.execUpdate(INSERT_INTO_PHONE, stp -> {
                for (Baby baby : babies) {
                    stp.setInt(1, baby.getPhone().getPhone());
                    int p = stp.executeUpdate();
                    counter[0] += p;
                    if (p != 0) {
                        ResultSet phoneKeys = stp.getGeneratedKeys();
                        phoneKeys.next();
                        long phoneId = phoneKeys.getLong(1);
                        phoneKeys.close();
                        baby.getPhone().setId(phoneId);
                        execBaby.execUpdate(INSERT_INTO_BABY, stb -> {
                            stb.setString(1, baby.getName());
                            stb.setLong(2, phoneId);
                            int b = stb.executeUpdate();
                            counter[0] += b;
                            if (b != 0) {
                                ResultSet babyKeys = stb.getGeneratedKeys();
                                babyKeys.next();
                                long babyId = babyKeys.getLong(1);
                                baby.setId(babyId);
                                babyKeys.close();
                            }
                            if (p != 0 && b != 0) {
                                messages.add("has been saved " + baby.toString());
                            }
                        });
                    }
                }
            });
            serviceConnection.getConnection().commit();
        } catch (SQLException e) {
            try {
                serviceConnection.getConnection().rollback();
                counter[0]=0;
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


package ru.otus;

import ru.otus.DAO.DataSetDAO;
import ru.otus.DAO.DataSetDAOImpl;
import ru.otus.DBCommon.ConnectionHelper;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;

import java.sql.Connection;
import java.sql.SQLException;

public class MainClass {
    public static void main(String[] args) throws MyOrmException {
        try (final Connection connection = ConnectionHelper.getMyPostgresqlConnection()){
            DataSetDAO dao = new DataSetDAOImpl(connection);

            dao.deleteAll(UserDataSet.class);

            UserDataSet user1 = new UserDataSet("Sergey", 33);
            user1 = dao.create(user1);
            System.out.println("new user1 = " + user1);

            UserDataSet user2 = new UserDataSet("Ivan", 22);
            user2 = dao.create(user2);
            System.out.println("new user2 = " + user2);

            UserDataSet user3 = new UserDataSet("Vasya", 44);
            user3 = dao.create(user3);
            System.out.println("new user3 = " + user3);

            System.out.println("all users before delete: " + dao.getAll(UserDataSet.class));
            System.out.println("delete user: " + user2.getId());
            dao.deleteById(user2.getId(), UserDataSet.class);
            System.out.println("all users after delete: " + dao.getAll(UserDataSet.class));

            System.out.println("get user by id (" + user1.getId() + ") = " + dao.getById(user1.getId(), UserDataSet.class));

            System.out.println("user param before update: " + user3);
            if (user3 != null) {
                user3.setName("Tutu");
                user3.setAge(10);
                user3 = dao.update(user3);
                System.out.println("user param after update: " + user3);
            }

            System.out.println("all users: " + dao.getAll(UserDataSet.class));
        } catch (SQLException e) {
            throw new MyOrmException(e);
        }
    }

}

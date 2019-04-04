package ru.otus.DBCommon;

import ru.otus.Annotation.TableName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {

    public static String getTable(Class t) {
        TableName a = (TableName) t.getAnnotation(TableName.class);
        if (a != null) {
            return a.value();
        } else {
            return t.getSimpleName();
        }
    }

    public static Connection getMyPostgresqlConnection() throws SQLException {
        String url = "jdbc:postgresql://" +  //db type
                "localhost:" +               //host name
                "5432/" +                    //port
                "postgres?" +                //db name
                "user=postgres&" +           //login
                "password=123456789";        //password

        return DriverManager.getConnection(url);
    }
}
package ru.otus.Executor;

import ru.otus.DBCommon.ConnectionHelper;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class ExecutorUtilites {

    static List<Field> getAllFields(Class<?> objectClass) {
        List<Field> fields = new ArrayList<>(Arrays.asList(objectClass.getDeclaredFields()));

        if (objectClass.getSuperclass() != null) {
            fields.addAll(getAllFields(objectClass.getSuperclass()));
        }
        return fields;
    }

    static List<Field> getAllFieldsWithoutID(Class<?> objectClass) {
        List<Field> fields = new ArrayList<>(Arrays.asList(objectClass.getDeclaredFields()));
        return fields;
    }

    private static <T> T create(ResultSet resultSet, Class<T> t) throws SQLException, IllegalAccessException, InstantiationException {
        List<Field> fields = getAllFields(t);
        T dataSet = t.newInstance();
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(dataSet, resultSet.getObject(field.getName()));
        }
        return dataSet;
    }

    static <T> T extract(ResultSet resultSet, Class<T> t) throws SQLException, InstantiationException, IllegalAccessException {
        if (!resultSet.next()) {
            return null;
        }
        return create(resultSet, t);
    }

    static <T> List<T> extractAll(ResultSet resultSet, Class<T> t) throws SQLException, InstantiationException, IllegalAccessException {
        final List<T> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(create(resultSet, t));
        }
        return result;
    }

    static <T> String getSaveQuery(Class<T> t) {
        List<Field> fields = getAllFieldsWithoutID(t);
        if (fields.size() > 0) {
            String queryStringPart1 = fields.stream()
                    .map(Field::getName)
                    .collect(Collectors.joining(", ", "INSERT INTO " + ConnectionHelper.getTable(t) + " (", ")"));
            String queryStringPart2 = fields.stream()
                    .map(x -> "?")
                    .collect(Collectors.joining(", ", " VALUES (", ")"));
            return queryStringPart1 + queryStringPart2;
        }
        return "";
    }

    static <T> String getLoadQuery(Class<T> t) {
        List<Field> fields = getAllFields(t);
        if (fields.size() > 0) {
            return fields.stream().map(Field::getName)
                    .collect(Collectors.joining(", ", "SELECT ", " FROM " + ConnectionHelper.getTable(t) + " WHERE id = ?"));
        }
        return "";
    }

    static <T> String getLoadAllQuery(Class<T> t) {
        List<Field> fields = getAllFields(t);
        if (fields.size() > 0) {
            return fields.stream().map(Field::getName)
                    .collect(Collectors.joining(", ", "SELECT ", " FROM " + ConnectionHelper.getTable(t)));
        }
        return "";
    }

    static <T> String getUpdateQuery(Class<T> t) {
        List<Field> fields = getAllFields(t);
        if (fields.size() > 0) {
            return fields.stream().map(x -> x.getName() + " = ?")
                    .collect(Collectors.joining(", ", "UPDATE " + ConnectionHelper.getTable(t) + " SET ", " WHERE id = ?"));
        }
        return "";
    }

    static <T> String getDeleteByIdQuery(Class<T> t) {
        return "DELETE FROM " + ConnectionHelper.getTable(t) + " WHERE id = ?";
    }

    static <T> String getDeleteAllQuery(Class<T> t) {
        return "DELETE FROM " + ConnectionHelper.getTable(t);
    }

    static <T> void fillQueryParameters(PreparedStatement preparedStatement, List<Field> fields, T t) throws SQLException, IllegalAccessException {
        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setAccessible(true);
            preparedStatement.setObject(i + 1, fields.get(i).get(t));
        }
    }

    static <T> String getCreateTableIfNotExistQuery(Class<T> objectClass){
        StringBuilder queryString = new StringBuilder();
        queryString.append("create table if not exists " + ConnectionHelper.getTable(objectClass) + " ( id serial primary key");
        List<Field> fields = new ArrayList<>(Arrays.asList(objectClass.getDeclaredFields()));
        for (Field field : fields) {
            Class<?> valClass = field.getType();
            field.setAccessible(true);
            queryString.append(", " + field.getName() + " ");
            if (Character.class.isAssignableFrom(valClass) || String.class.isAssignableFrom(valClass))
                queryString.append("varchar(255)");
            else if (Integer.class.isAssignableFrom(valClass))
                queryString.append("int");
        }
        queryString.append(");");
        return queryString.toString();
    }
}

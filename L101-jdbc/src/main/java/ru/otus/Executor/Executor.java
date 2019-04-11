package ru.otus.Executor;

import ru.otus.DataSet.DataSet;
import ru.otus.Exception.MyOrmException;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;

import static ru.otus.Executor.ExecutorUtilites.*;

public class Executor {
    private static ClassMetaDataHolder classData = new ClassMetaDataHolder();

    public static <T extends DataSet> T save(Connection connection, T t) throws MyOrmException {
        String queryString = classData.getClassSaveQuery(t.getClass());
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString, new String[]{"id"})){
                List<Field> fields = classData.getClassAllFieldsWithoutID(t.getClass());
                fillQueryParameters(preparedStatement, fields, t);
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    t.setId(resultSet.getLong(1));
                    return t;
                }
            } catch (SQLException e) {
                throw new MyOrmException(e);
            } catch (IllegalAccessException e) {
                throw new MyOrmException(e);
            }
        }
        return null;
    }

    public static <T extends DataSet> T load(Connection connection, long id, Class<T> t) throws MyOrmException {
        String queryString = classData.getClassLoadQuery(t);
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                return extract(resultSet, t);
            } catch (SQLException | IllegalAccessException | InstantiationException e) {
                throw new MyOrmException(e);
            }
        }
        return null;
    }

    public static <T extends DataSet> List<T> loadAll(Connection connection, Class<T> t) {
        String queryString = classData.getClassLoadAllQuery(t);
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                ResultSet resultSet = preparedStatement.executeQuery();
                return extractAll(resultSet, t);
            } catch (SQLException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T extends DataSet> T update(Connection connection, T t) throws MyOrmException {
        String queryString = classData.getClassUpdateQuery(t.getClass());
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                List<Field> fields = classData.getClassAllFields(t.getClass());
                fillQueryParameters(preparedStatement, fields, t);
                preparedStatement.setLong(fields.size() + 1, t.getId());
                preparedStatement.executeUpdate();
                return (T)load(connection, t.getId(), t.getClass());
            } catch (SQLException e) {
                throw new MyOrmException(e);
            } catch (IllegalAccessException e) {
                throw new MyOrmException(e);
            }
        }
        return null;
    }

    public static <T extends DataSet> void deleteById(Connection connection, Class<T> t, long id) throws MyOrmException {
        String queryString = getDeleteByIdQuery(t);
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new MyOrmException(e);
            }
        }
    }

    public static <T extends DataSet> void deleteAll(Connection connection, Class<T> t) throws MyOrmException {
        String queryString = getDeleteAllQuery(t);
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new MyOrmException(e);
            }
        }
    }

    public static <T extends DataSet> void createTableIfNotExist(Connection connection, Class<T> t) throws MyOrmException {
        String queryString = getCreateTableIfNotExistQuery(t);
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new MyOrmException(e);
            }
        }
    }
}
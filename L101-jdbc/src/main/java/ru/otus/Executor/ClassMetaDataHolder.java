package ru.otus.Executor;

import ru.otus.DBCommon.ConnectionHelper;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ClassMetaDataHolder {
    private Map<Class, List<Field>> cachedClassFields;
    private Map<Class, List<Field>> cachedClassFieldsWithoutID;
    private Map<Class, String> cachedSaveQuery;
    private Map<Class, String> cachedLoadQuery;
    private Map<Class, String> cachedLoadAllQuery;
    private Map<Class, String> cachedUpdateQuery;
    private Map<Class, String> cachedDeleteByIdQuery;
    private Map<Class, String> cachedDeleteAllQuery;

    public List<Field> getClassAllFields(Class objectClass){
        boolean addClassFields = false;

        if (cachedClassFields == null) {
            cachedClassFields = new LinkedHashMap<>();
            addClassFields = true;
        } else {
            addClassFields = !cachedClassFields.containsKey(objectClass);
        }

        if (addClassFields) {
            List<Field> fields = new ArrayList<>(Arrays.asList(objectClass.getDeclaredFields()));
            if (objectClass.getSuperclass() != null) {
                fields.addAll(getClassAllFields(objectClass.getSuperclass()));
            }
            cachedClassFields.put(objectClass, fields);
        }
        return cachedClassFields.get(objectClass);
    }

    public List<Field> getClassAllFieldsWithoutID(Class objectClass){
        boolean addClassFields = false;

        if (cachedClassFieldsWithoutID == null) {
            cachedClassFieldsWithoutID = new LinkedHashMap<>();
            addClassFields = true;
        } else {
            addClassFields = !cachedClassFieldsWithoutID.containsKey(objectClass);
        }
        if (addClassFields) {
            cachedClassFieldsWithoutID.put(objectClass, new ArrayList<>(Arrays.asList(objectClass.getDeclaredFields())));
        }
        return cachedClassFieldsWithoutID.get(objectClass);
    }

    public String getClassSaveQuery(Class objectClass){
        boolean addQuery = false;
        if(cachedSaveQuery == null) {
            cachedSaveQuery = new LinkedHashMap<>();
            addQuery = true;
        } else {
            addQuery = !cachedSaveQuery.containsKey(objectClass);
        }
        if (addQuery) {
            List<Field> fields = this.getClassAllFieldsWithoutID(objectClass);
            if (fields.size() > 0) {
                String queryStringPart1 = fields.stream()
                        .map(Field::getName)
                        .collect(Collectors.joining(", ", "INSERT INTO " + ConnectionHelper.getTable(objectClass) + " (", ")"));
                String queryStringPart2 = fields.stream()
                        .map(x -> "?")
                        .collect(Collectors.joining(", ", " VALUES (", ")"));

                cachedSaveQuery.put(objectClass, queryStringPart1 + queryStringPart2);
            } else cachedSaveQuery.put(objectClass, "");

        }
        return cachedSaveQuery.get(objectClass);
    }

    public String getClassUpdateQuery(Class objectClass){
        boolean addQuery = false;

        if (cachedUpdateQuery == null) {
            cachedUpdateQuery = new LinkedHashMap<>();
            addQuery = true;
        } else {
            addQuery = !cachedUpdateQuery.containsKey(objectClass);
        }

        if (addQuery) {
            List<Field> fields = getClassAllFields(objectClass);
            if (fields.size() > 0) {
                cachedUpdateQuery.put(objectClass, fields.stream().map(x -> x.getName() + " = ?")
                        .collect(Collectors.joining(", ", "UPDATE " + ConnectionHelper.getTable(objectClass) + " SET ", " WHERE id = ?")));
            } else cachedUpdateQuery.put(objectClass, "");

        }
        return cachedUpdateQuery.get(objectClass);
    }

    public String getClassLoadAllQuery(Class objectClass){
        boolean addQuery = false;

        if (cachedLoadAllQuery == null) {
            cachedLoadAllQuery = new LinkedHashMap<>();
            addQuery = true;
        } else {
            addQuery = !cachedLoadAllQuery.containsKey(objectClass);
        }

        if (addQuery) {
            List<Field> fields = this.getClassAllFields(objectClass);
            if (fields.size() > 0) {
                cachedLoadAllQuery.put(objectClass, fields.stream().map(Field::getName)
                        .collect(Collectors.joining(", ", "SELECT ", " FROM " + ConnectionHelper.getTable(objectClass))));
            } else cachedLoadAllQuery.put(objectClass, "");

        }
        return cachedLoadAllQuery.get(objectClass);
    }

    public String getClassLoadQuery(Class objectClass){
        boolean addQuery = false;

        if (cachedLoadQuery == null) {
            cachedLoadQuery = new LinkedHashMap<>();
            addQuery = true;
        } else {
            addQuery = !cachedLoadQuery.containsKey(objectClass);
        }

        if (addQuery) {
            List<Field> fields = this.getClassAllFields(objectClass);
            if (fields.size() > 0) {
                cachedLoadQuery.put(objectClass, fields.stream().map(Field::getName)
                        .collect(Collectors.joining(", ", "SELECT ", " FROM " + ConnectionHelper.getTable(objectClass) + " WHERE id = ?")));
            } else cachedLoadQuery.put(objectClass, "");

        }
        return cachedLoadQuery.get(objectClass);
    }

    public String getClassDeleteByIdQuery(Class objectClass){
        boolean addQuery = false;

        if (cachedDeleteByIdQuery == null) {
            cachedDeleteByIdQuery = new LinkedHashMap<>();
            addQuery = true;
        } else {
            addQuery = !cachedDeleteByIdQuery.containsKey(objectClass);
        }

        if (addQuery) {
            cachedDeleteByIdQuery.put(objectClass, "DELETE FROM " + ConnectionHelper.getTable(objectClass) + " WHERE id = ?");
        }
        return cachedDeleteByIdQuery.get(objectClass);
    }
    public String getClassDeleteAllQuery(Class objectClass){
        boolean addQuery = false;

        if (cachedDeleteAllQuery == null) {
            cachedDeleteAllQuery = new LinkedHashMap<>();
            addQuery = true;
        } else {
            addQuery = !cachedDeleteAllQuery.containsKey(objectClass);
        }

        if (addQuery) {
            cachedDeleteAllQuery.put(objectClass, "DELETE FROM " + ConnectionHelper.getTable(objectClass));
        }
        return cachedDeleteAllQuery.get(objectClass);
    }
}

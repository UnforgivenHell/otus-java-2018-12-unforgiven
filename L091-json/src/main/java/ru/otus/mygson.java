package ru.otus;

import javax.json.*;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

public class mygson {

    public static String toJson(Object object) {
        if ( object == null ) return null;
        else return convertToJson(object);
    }

    private static String convertToJson(Object object) {
        Class<?> objectClass = object.getClass();

        if (isNumberOrBoolean(objectClass)) return object.toString();
        else if (isCharacterOrString(objectClass)) return String.format("\"%s\"", object);
        else if (isCollection(objectClass)) return convertCollectionToJson(object);
        else if (objectClass.isArray()) return convertArrayToJson(object);
        else return convertFieldsToJson(object, objectClass);
    }

    private static String replaceCharacters(final JsonValue value)
    {
        return value.toString()
                .replace("\"{", "{")
                .replace("}\"", "}")
                .replace("\\\"", "\"")
                .replace("\"[", "[")
                .replace("]\"", "]");
    }

    private static boolean isCollection(final Class<?> c)
    {
        return Collection.class.isAssignableFrom(c);
    }

    private static boolean isNumberOrBoolean(Class<?> c)
    {
        return Number.class.isAssignableFrom(c) || Boolean.class.isAssignableFrom(c);
    }

    private static boolean isCharacterOrString(Class<?> c)
    {
        return Character.class.isAssignableFrom(c) || String.class.isAssignableFrom(c);
    }

    private static String convertCollectionToJson(Object object) {
        Collection<?> coll = (Collection<?>) object;
        Object[] arr = coll.toArray();
        return convertToJson(arr);
    }

    private static String convertArrayToJson(Object object) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (int i = 0; i < Array.getLength(object); i++) {
            Object val = Array.get(object, i);
            if (val != null ) {
                Class<?> valClass = val.getClass();
                if (isCharacterOrString(valClass)) arrayBuilder.add(String.valueOf(val));
                else if (valClass.equals(Integer.class)) arrayBuilder.add((Integer) val);
                else if (valClass.equals(Double.class)) arrayBuilder.add((Double) val);
                else if (valClass.equals(Short.class)) arrayBuilder.add((Short) val);
                else if (valClass.equals(Byte.class)) arrayBuilder.add((Byte) val);
                else if (valClass.equals(Float.class)) arrayBuilder.add((Float) val);
                else if (valClass.equals(Long.class)) arrayBuilder.add((Long) val);
                else if (valClass.equals(Boolean.class)) arrayBuilder.add((Boolean) val);
                else arrayBuilder.add(convertToJson(val));
            } else arrayBuilder.addNull();
        }
        return replaceCharacters(arrayBuilder.build());
    }

    private static String convertFieldsToJson(Object object, Class<?> objectClass) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        do {
            Field[] fields = objectClass.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);
            for (Field f : fields) {
                boolean isNotStatic = !Modifier.isStatic(f.getModifiers());
                boolean isNotTransient = !Modifier.isTransient(f.getModifiers());
                if (isNotStatic && isNotTransient) {
                    try {
                        Class<?> valClass = f.getType();
                        Object val = f.get(object);

                        if (val != null) {
                            if (isCharacterOrString(valClass)) objectBuilder.add(f.getName(), String.valueOf(val));
                            else if (valClass.equals(int.class)) objectBuilder.add(f.getName(), (int) val);
                            else if (valClass.equals(double.class)) objectBuilder.add(f.getName(), (double) val);
                            else if (valClass.equals(short.class)) objectBuilder.add(f.getName(), (short) val);
                            else if (valClass.equals(byte.class)) objectBuilder.add(f.getName(), (byte) val);
                            else if (valClass.equals(float.class)) objectBuilder.add(f.getName(), (float) val);
                            else if (valClass.equals(long.class)) objectBuilder.add(f.getName(), (long) val);
                            else if (valClass.equals(boolean.class)) objectBuilder.add(f.getName(), (boolean) val);
                            else objectBuilder.add(f.getName(), convertToJson(val));
                        }
                    }
                    catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            objectClass = objectClass.getSuperclass();
        } while (objectClass != null);

        return replaceCharacters(objectBuilder.build());
    }
}

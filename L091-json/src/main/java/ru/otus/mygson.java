package ru.otus;

import javax.json.*;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class mygson {

    public static String toJson(Object object) {
        if ( object == null ) {
            return null;
        } else {
            return convertToJson(object);
        }
    }

    private static String convertToJson(Object object) {
        String vRes = "";
        Class<?> objectClass = object.getClass();

        if (isNumberOrBoolean(objectClass)) {
            return object.toString();
        }

        if (isCharacterOrString(objectClass)) {
            return String.format("\"%s\"", object);
        }

        if (isCollection(objectClass)) {
            Collection<?> coll = (Collection<?>) object;
            Object[] arr = coll.toArray();
            return convertToJson(arr);
        }

        if (objectClass.isArray()) {
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            for (int i = 0; i < Array.getLength(object); i++) {
                Object val = Array.get(object, i);
                if (val != null ) {
                    Class<?> valClass = val.getClass();
                    if (valClass.equals(Integer.class)) arrayBuilder.add((Integer) val);
                    else if (valClass.equals(Double.class)) arrayBuilder.add((Double) val);
                    else if (valClass.equals(Short.class)) arrayBuilder.add((Short) val);
                    else if (valClass.equals(Byte.class)) arrayBuilder.add((Byte) val);
                    else if (valClass.equals(Float.class)) arrayBuilder.add((Float) val);
                    else if (valClass.equals(Long.class)) arrayBuilder.add((Long) val);
                    else if (valClass.equals(Character.class)) arrayBuilder.add(String.valueOf(val));
                    else if (valClass.equals(String.class)) arrayBuilder.add(String.valueOf(val));
                    else if (valClass.equals(Boolean.class)) arrayBuilder.add((Boolean) val);
                    else arrayBuilder.add(convertToJson(val));
                } else {
                    arrayBuilder.addNull();
                }
            }
            return replaceCharacters(arrayBuilder.build());
        } else {
            JsonObjectBuilder ObjectBuilder = Json.createObjectBuilder();

            do {
                Field[] fields = objectClass.getDeclaredFields();
                AccessibleObject.setAccessible(fields, true);
                for (Field f : fields) {
                    boolean isNotStatic = !Modifier.isStatic(f.getModifiers());
                    boolean isNotTransient = !Modifier.isTransient(f.getModifiers());
                    if (isNotStatic && isNotTransient) {
                        try {
                            Class<?> t = f.getType();
                            Object val = f.get(object);

                            if (val != null) {
                                if (t.equals(int.class)) ObjectBuilder.add(f.getName(), (int) val);
                                else if (t.equals(double.class)) ObjectBuilder.add(f.getName(), (double) val);
                                else if (t.equals(short.class)) ObjectBuilder.add(f.getName(), (short) val);
                                else if (t.equals(byte.class)) ObjectBuilder.add(f.getName(), (byte) val);
                                else if (t.equals(float.class)) ObjectBuilder.add(f.getName(), (float) val);
                                else if (t.equals(long.class)) ObjectBuilder.add(f.getName(), (long) val);
                                else if (t.equals(char.class)) ObjectBuilder.add(f.getName(), String.valueOf(val));
                                else if (t.equals(String.class)) ObjectBuilder.add(f.getName(), String.valueOf(val));
                                else if (t.equals(boolean.class)) ObjectBuilder.add(f.getName(), (boolean) val);
                                else ObjectBuilder.add(f.getName(), convertToJson(val));
                            }
                        }
                        catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                objectClass = objectClass.getSuperclass();
            } while (objectClass != null);

            return replaceCharacters(ObjectBuilder.build());
        }

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
}

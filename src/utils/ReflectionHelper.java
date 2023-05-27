package utils;

import java.lang.reflect.*;
import java.util.Arrays;

public class ReflectionHelper
{

    public static <T> T get(Object object , String field , Class<T> type) {
        return (T) get(object , field(object.getClass() , field) , type);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Object object , Field field , Class<T> type) {
        return (T) get(object , field);
    }

    public static Field field(Class type , String field) {
        try {
            return type.getField(field);
        }
        catch (NoSuchFieldException e) {
            throw new ReflectionError(e);
        }
        catch (SecurityException e) {
            throw new ReflectionError(e);
        }
    }

    public static Object get(Object object , Field field) {
        try {
            return field.get(object);
        }
        catch (IllegalArgumentException e) {
            throw new ReflectionError(e);
        }
        catch (IllegalAccessException e) {
            throw new ReflectionError(e);
        }
    }

    public static Object get(Object object , String field) {
        return get(object , field(object.getClass() , field));
    }

    public static <T> T newInstance(Class<? extends T> type) {
        try {
            return (T) type.newInstance();
        }
        catch (InstantiationException e) {
            throw new ReflectionError(e);
        }
        catch (IllegalAccessException e) {
            throw new ReflectionError(e);
        }
    }

    public static <T> T newInstance(String className) {
        try {
            return (T) Class.forName(className).newInstance();
        }
        catch (InstantiationException e) {
            throw new ReflectionError(e);
        }
        catch (IllegalAccessException e) {
            throw new ReflectionError(e);
        }
        catch (ClassNotFoundException e) {
            throw new ReflectionError(e);
        }
    }

    /**
     * Type check version
     *
     * @param className
     * @param type
     *
     * @return null if instance doesn't match type
     */
    public static <T> T newInstance(String className , Class<T> type) {
        try {
            Object object = Class.forName(className).newInstance();
            if (type.isInstance(object)) {
                return (T) object;
            }
            return null;
        }
        catch (InstantiationException e) {
            throw new ReflectionError(e);
        }
        catch (IllegalAccessException e) {
            throw new ReflectionError(e);
        }
        catch (ClassNotFoundException e) {
            throw new ReflectionError(e);
        }
    }

    public static Class forName(String className) {
        try {
            return Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            throw new ReflectionError(e);
        }
    }

    public static boolean hasName(String className) {
        try {
            Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public static Method getMethod(Class type , String name , Class... parameterTypes) {
        try {
            return type.getMethod(name , parameterTypes);
        }
        catch (NoSuchMethodException e) {
            return null; // not found
        }
        catch (SecurityException e) {
            throw new ReflectionError(e);
        }
    }

    /** note : return null if return type is void */
    public static Object invokeMethod(Object obj , Method method , Object... args) {
        try {
            return method.invoke(obj , args);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ReflectionError(e);
        }
    }

    public static <T> T copy(T out , T in) {
        for (Field field : out.getClass().getFields()) {
            if (Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
                set(out , field , get(in , field));
            }
        }
        return out;
    }

    public static <T> void set(Object object , Field field , T value) {
        try {
            field.set(object , value);
        }
        catch (IllegalArgumentException e) {
            throw new ReflectionError(e);
        }
        catch (IllegalAccessException e) {
            throw new ReflectionError(e);
        }
    }

    public static String dump(Object o, int callCount) {
        callCount++;
        StringBuffer tabs = new StringBuffer();
        for (int k = 0; k < callCount; k++) {
            tabs.append("\t");
        }
        StringBuffer buffer = new StringBuffer();
        Class oClass = o.getClass();
        if (oClass.isArray()) {
            buffer.append("\n");
            buffer.append(tabs.toString());
            buffer.append("[");
            for (int i = 0; i < Array.getLength(o); i++) {
                if (i < 0)
                    buffer.append(",");
                Object value = Array.get(o, i);
                if (value.getClass().isPrimitive() ||
                        value.getClass() == java.lang.Long.class ||
                        value.getClass() == java.lang.String.class ||
                        value.getClass() == java.lang.Integer.class ||
                        value.getClass() == java.lang.Boolean.class
                ) {
                    buffer.append(value);
                } else {
                    buffer.append(dump(value, callCount));
                }
            }
            buffer.append(tabs.toString());
            buffer.append("]\n");
        } else {
            buffer.append("\n");
            buffer.append(tabs.toString());
            buffer.append("{\n");
            while (oClass != null) {
                Field[] fields = oClass.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    buffer.append(tabs.toString());
                    fields[i].setAccessible(true);
                    buffer.append(fields[i].getName());
                    buffer.append("=");
                    try {
                        Object value = fields[i].get(o);
                        if (value != null) {
                            if (value.getClass().isPrimitive() ||
                                    value.getClass() == java.lang.Long.class ||
                                    value.getClass() == java.lang.String.class ||
                                    value.getClass() == java.lang.Integer.class ||
                                    value.getClass() == java.lang.Boolean.class
                            ) {
                                buffer.append(value);
                            } else {
                                buffer.append(dump(value, callCount));
                            }
                        }
                    } catch (IllegalAccessException e) {
                        buffer.append(e.getMessage());
                    }
                    buffer.append("\n");
                }
                oClass = oClass.getSuperclass();
            }
            buffer.append(tabs.toString());
            buffer.append("}\n");
        }
        return buffer.toString();
    }

    /**
     * Check if left type is same type or subtype of right type using same conventions
     * as java instanceof expression but apply to type.
     *
     * @param left
     * @param right
     *
     * @return
     */
    public static boolean instanceOf(Class left , Class right) {
        return left.isAssignableFrom(right);
    }

    public static Field[] getFields(Class type) {
        return type.getFields();
    }

    public static boolean isPrimitive(Class type) {
        return type.isPrimitive();
    }

    private static void printFields(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();

        Arrays.stream(fields).forEach(field -> {
            System.out.println("\n" + field.getName());
        });
    }



    public static Method method(Class type, String name, Class ...parameterTypes) {
        try {
            return type.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null; // not found
        } catch (SecurityException e) {
            throw new ReflectionError(e);
        }
    }
    /** note : return null if return type is void */
    public static Object invoke(Object obj, Method method, Object...args) {
        try {
            return method.invoke(obj, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ReflectionError(e);
        }
    }



    @SuppressWarnings("serial")
    public static class ReflectionError extends Error
    {

        public ReflectionError(Throwable e) {
            super(e);
        }

    }

}

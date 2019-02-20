package ru.otus.hw051.framework;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import ru.otus.hw051.annotations.After;
import ru.otus.hw051.annotations.Before;
import ru.otus.hw051.annotations.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FrameworkDemo
{
    public static void main(String[] args) throws IOException
    {
        String packageName = "ru.otus.hw051.tests";

        ImmutableSet<ClassPath.ClassInfo> testClasses = getClassesFromPackage(packageName);

        processTestClasses(testClasses);
    }

    private static ImmutableSet<ClassPath.ClassInfo> getClassesFromPackage(final String packageName) throws IOException
    {
        return ClassPath.from(ClassLoader.getSystemClassLoader()).getTopLevelClasses(packageName);
    }

    private static void processTestClasses(final ImmutableSet<ClassPath.ClassInfo> classes)
    {
        for (final ClassPath.ClassInfo classInfo : classes) {
            try {
                declaredMethodsProcessor(Class.forName(classInfo.getName()));
            }
            catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void declaredMethodsProcessor(Class<?> cl)
    {
        Method[] methods = cl.getDeclaredMethods();

        Optional<Method> before = getAnnotatedMethodFrom(methods, Before.class);
        Optional<Method> after = getAnnotatedMethodFrom(methods, After.class);

        for (final Method m : methods) {
            if (m.isAnnotationPresent(Test.class)) {

                try {
                    Object o = cl.newInstance();

                    if (before.isPresent()) {
                        String beforeMethodName = before.get().getName();
                        cl.getMethod(beforeMethodName).invoke(o);
                    }

                    String testMethodName = m.getName();
                    cl.getMethod(testMethodName).invoke(o);

                    if (after.isPresent()) {
                        String afterMethodName = after.get().getName();
                        cl.getMethod(afterMethodName).invoke(o);
                    }
                }
                catch (IllegalAccessException | InvocationTargetException |
                    NoSuchMethodException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Optional<Method> getAnnotatedMethodFrom(final Method[] methods, final Class<? extends Annotation> annotation) {
        try (Stream<Method> methodStream = Stream.of(methods)) {
            List<Method> annotated = methodStream
                    .filter(method -> method.isAnnotationPresent(annotation))
                    .collect(Collectors.toList());

            if (annotated.size() > 1) {
                throw new IllegalStateException("@" + annotation.getSimpleName() + " can be declared just once in a class.");
            }

            return annotated.size() == 0 ? Optional.empty() : Optional.of(annotated.get(0));
        }
    }
}
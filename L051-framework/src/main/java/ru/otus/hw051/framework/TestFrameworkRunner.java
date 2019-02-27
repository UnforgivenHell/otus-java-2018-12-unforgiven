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

public class TestFrameworkRunner{
    private static final String RED_BOLD = "\033[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    private int countSuccess = 0;
    private int countFail = 0;

    public void runTestsByPackageName(String packageName) throws IOException {
        ImmutableSet<ClassPath.ClassInfo> testClasses = getClassesFromPackage(packageName);

        processTestClasses(testClasses);
    }

    private static ImmutableSet<ClassPath.ClassInfo> getClassesFromPackage(final String packageName) throws IOException {
        return ClassPath.from(ClassLoader.getSystemClassLoader()).getTopLevelClasses(packageName);
    }

    private void processTestClasses(final ImmutableSet<ClassPath.ClassInfo> classes) {
        for (final ClassPath.ClassInfo classInfo : classes) {
            try {
                declaredMethodsProcessor(Class.forName(classInfo.getName()));
            }
            catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void declaredMethodsProcessor(Class<?> cl) {
        Method[] methods = cl.getDeclaredMethods();

        Optional<Method> before = getAnnotatedMethodFrom(methods, Before.class);
        Optional<Method> after = getAnnotatedMethodFrom(methods, After.class);

        countSuccess = 0;
        countFail = 0;
        for (final Method m : methods) {
            if (m.isAnnotationPresent(Test.class)) {
                try {
                    Object o = cl.newInstance();

                    if (before.isPresent()) {
                        before.get().invoke(o);
                    }

                    m.invoke(o);

                    if (after.isPresent()) {
                        after.get().invoke(o);
                    }
                    countSuccess++;
                }
                catch (IllegalAccessException | InvocationTargetException | InstantiationException | RuntimeException e) {
                    countFail++;
                }
            }
        }

        if (countSuccess >= 1){
            System.out.printf("Success: %d%s ", countSuccess, ANSI_RESET);
        }
        if (countFail >= 1){
            System.out.printf("%sError: %d%s", RED_BOLD, countFail, ANSI_RESET);
        }
        System.out.println("\n----------------------------------------");
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
package com.win.utils;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Utils {

    /**
     * This function check stream is null or not
     * @param collection
     * @return
     * @param <T>
     */
    public static <T> Stream<T> streamCheck(Collection<T> collection){
        if(Objects.isNull(collection)){
            return Stream.empty();
        }
        return collection.stream();
    }

    /**
     * The function to resolve null
     * @param supplier
     * @return
     * @param <T>
     */
    public static <T>Optional<T> resolveNull(Supplier<T> supplier){
        try {
            T value = supplier.get();
            return Optional.ofNullable(value);
        }catch (NullPointerException exception){
            // Log
        }
        return Optional.empty();
    }

    /**
     * The method fetch value
     * @param supplier
     * @return
     * @param <T>
     */
    public static <T> T fetchValue(Supplier<T> supplier){
        return resolveNull(() -> supplier.get()).orElse(null);
    }
}

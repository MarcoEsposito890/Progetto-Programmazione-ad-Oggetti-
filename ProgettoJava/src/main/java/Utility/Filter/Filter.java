package Utility.Filter;

import java.util.Collection;

public interface Filter<E,T> {
	abstract Collection<E> filterField(String fieldName, String operator, T value) throws NoSuchFieldException, SecurityException;
}
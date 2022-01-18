package moh.logistics.lib.reports;


public interface IPredicate<T> {
    boolean apply(T type);
}

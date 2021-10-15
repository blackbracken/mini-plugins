package black.bracken.oakin.util.functional;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Maybe<E> {

    static <E> Maybe<E> of(E nullableElement) {
        return nullableElement != null ? just(nullableElement) : nothing();
    }

    static <E> Maybe<E> just(E element) {
        return new Just<>(element);
    }

    static <E> Maybe<E> nothing() {
        return new Nothing<>();
    }

    E orElse(E defaultValue);

    Maybe<E> ifPresent(Consumer<E> predicate);

    Maybe<E> ifNotPresent(Runnable predicate);

    <R> Maybe<R> map(Function<E, R> mapping);

    Maybe<E> takeIf(Predicate<E> filter);

    class Just<E> implements Maybe<E> {
        private final E element;

        Just(E element) {
            this.element = element;
        }

        @Override
        public E orElse(E defaultValue) {
            return element;
        }

        @Override
        public Maybe<E> ifPresent(Consumer<E> predicate) {
            predicate.accept(element);
            return this;
        }

        @Override
        public Maybe<E> ifNotPresent(Runnable predicate) {
            return this;
        }

        @Override
        public <R> Maybe<R> map(Function<E, R> mapping) {
            return Maybe.just(mapping.apply(element));
        }

        @Override
        public Maybe<E> takeIf(Predicate<E> filter) {
            return filter.test(element) ? this : Maybe.nothing();
        }

    }

    class Nothing<E> implements Maybe<E> {

        @Override
        public E orElse(E defaultValue) {
            return defaultValue;
        }

        @Override
        public Maybe<E> ifPresent(Consumer<E> predicate) {
            return this;
        }

        @Override
        public Maybe<E> ifNotPresent(Runnable predicate) {
            predicate.run();
            return this;
        }

        @Override
        public <R> Maybe<R> map(Function<E, R> mapping) {
            return Maybe.nothing();
        }

        @Override
        public Maybe<E> takeIf(Predicate<E> filter) {
            return this;
        }

    }

}

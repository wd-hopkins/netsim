package org.netsim.util;

import java.util.*;

public class ObservableQueue<E> extends AbstractQueue<E> {

    private final Queue<E> delegate;
    private final List<Listener<E>> listeners = new ArrayList<>();

    public ObservableQueue(Queue<E> delegate) {
        this.delegate = delegate;
    }

    public void registerListener(Listener<E> listener) {
        listeners.add(listener);
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public boolean offer(E e) {
        if (delegate.offer(e)) {
            listeners.forEach(listener -> listener.onElementAdded(e));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public E poll() {
        return delegate.poll();
    }

    @Override
    public E peek() {
        return delegate.peek();
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public Iterator<E> iterator() {
        return delegate.iterator();
    }

    @FunctionalInterface
    public interface Listener<E> {
        void onElementAdded(E element);
    }
}

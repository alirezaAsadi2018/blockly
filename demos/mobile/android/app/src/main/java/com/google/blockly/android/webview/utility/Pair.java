package com.google.blockly.android.webview.utility;

public class Pair<E, V> {
    private E first;
    private V second;

    public E getFirst() {
        return first;
    }

    public void setFirst(E first) {
        this.first = first;
    }

    public V getSecond() {
        return second;
    }

    public void setSecond(V second) {
        this.second = second;
    }

    public Pair(E first, V second) {
        this.first = first;
        this.second = second;
    }
}

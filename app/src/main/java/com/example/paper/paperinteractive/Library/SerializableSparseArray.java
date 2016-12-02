package com.example.paper.paperinteractive.Library;

import android.util.SparseArray;

import java.io.Serializable;

//TODO Göra den Parcelable istället för Serializable (Performance)
public class SerializableSparseArray<E> extends SparseArray<E> implements Serializable {
}

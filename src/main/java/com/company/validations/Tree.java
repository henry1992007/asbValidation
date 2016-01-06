package com.company.validations;

import java.util.List;

/**
 * Created by henry on 16/1/1.
 */
public class Tree<T> {

    private TreeNode<T> root;

    private Tree(T rootNode) {
        this.root = new TreeNode<>(rootNode);
    }

    public Tree<T> newTree(T root) {
        return new Tree<>(root);
    }


    public void set(T parent, T child) {

    }

    public void set(T parent, List<T> children) {

    }


    static class TreeNode<T> {
        private TreeNode<T> parent;
        private List<TreeNode<T>> children;

        private T reference;

        public TreeNode(T o) {
            this.reference = o;
        }
    }

}

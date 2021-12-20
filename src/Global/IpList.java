package Global;

import java.util.Iterator;

public class IpList<T, P> implements Iterable<IpList.INode<T, P>>{
    private INode<T,P> entry;
    private INode<T,P> last;
    private P val;
    private int node_num;
    public IpList(P p){
        this.val=p;
        node_num=0;
        entry=last=null;
    }

    public INode<T, P> getEntry() {
        return entry;
    }

    public void setEntry(INode<T, P> entry) {
        this.entry = entry;
    }

    public INode<T, P> getLast() {
        return last;
    }

    public void setLast(INode<T, P> last) {
        this.last = last;
    }

    public P getVal() {
        return val;
    }

    public void setVal(P val) {
        this.val = val;
    }

    public int getNode_num() {
        return node_num;
    }

    public void setNode_num(int node_num) {
        this.node_num = node_num;
    }

    @Override
    public Iterator<INode<T, P>> iterator() {
        return new IIterator(entry);
    }

    class IIterator implements Iterator<INode<T, P>> {

        INode<T, P> temp = new INode<>(null);
        INode<T, P> nxt = null;

        IIterator(INode<T, P> head) {
            temp.next = head;
        }

        @Override
        public boolean hasNext() {
            return nxt != null || temp.next != null;
        }

        @Override
        public INode<T, P> next() {
            if (nxt == null) {
                temp = temp.next;
            } else {
                temp = nxt;
            }
            nxt = null;
            return temp;
        }

    }
    public static class INode<T, P>{
        private T val;
        private INode<T,P> prev = null;
        private INode<T,P> next = null;
        private IpList<T,P> parent;

        public T getVal() {
            return val;
        }

        public void setVal(T val) {
            this.val = val;
        }

        public INode<T, P> getPrev() {
            return prev;
        }

        public void setPrev(INode<T, P> prev) {
            this.prev = prev;
        }

        public INode<T, P> getNext() {
            return next;
        }

        public void setNext(INode<T, P> next) {
            this.next = next;
        }

        public IpList<T, P> getParent() {
            return parent;
        }

        public void setParent(IpList<T, P> parent) {
            this.parent = parent;
        }

        public INode(T t){
            this.val =t;
        }
        public void insert_before(INode<T,P> node){

            node.prev=this;
            this.prev=node.prev;
            this.next=node;
            this.prev.next=this;
            this.parent=node.parent;
            this.parent.node_num++;
        }
        public void insert_after(INode<T,P> node){

            node.next=this;
            this.prev=node;
            this.next=node.next;
            this.next.prev=this;
            this.parent=node.parent;
            this.parent.node_num++;
        }
        public void insert_entry(IpList<T,P> parent){
            this.setParent(parent);
            if(parent.getEntry() == null && parent.getLast()==null){
                parent.node_num++;
                parent.setEntry(this);
                parent.setLast(this);
            }else {
                insert_before(parent.entry);
            }
        }
        public void insert_end(IpList<T,P> parent){
            this.setParent(parent);
            if(parent.getEntry() == null && parent.getLast()==null){
                parent.node_num++;
                parent.setEntry(this);
                parent.setLast(this);
                this.prev = null;
                this.next = null;
            }else {
                insert_after(parent.last);
            }
        }

    }
}

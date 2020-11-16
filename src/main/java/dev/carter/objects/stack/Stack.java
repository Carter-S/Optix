package dev.carter.objects.stack;

public abstract class Stack {
    private int size;
    private String[] items;
    int lastPos;

    public Stack(int size){
        this.size = size;
        this.items = new String[this.size];
        this.lastPos = 0;
    }
    //Adds to top of stack
    public void push(String item) {
        if(!isFull()){
            items[lastPos] = item;
            lastPos++;
        }else{
            System.out.println("Unable to execute push, stack is full");
        }
    }
    //Removes from top of stack
    public String pop() {
        if(!isEmpty()){
            lastPos--;
            String lastItem = items[lastPos];
            items[lastPos] = null;
            return lastItem;
        }else{
            return "Unable to execute pop, stack is empty";
        }
    }
    //Checks if stack is full
    public boolean isFull(){
        return lastPos == size-1;
    }
    //Checks if stack is empty
    public boolean isEmpty(){
        return lastPos == 0;
    }
}


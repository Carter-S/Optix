package dev.carter.objects.stack;

public class History extends Stack{

    //Maximum of visiting 100 pages per session
    private static final int historySize = 100;

    public History() {
        super(historySize);
    }

}

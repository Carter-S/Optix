package dev.carter.objects.stack;

public class History extends Stack{

    //Maximum of visiting 100 pages per session
    private static final int historySize = 100;

    private String currentPage;

    public History() {
        super(historySize);
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }
}

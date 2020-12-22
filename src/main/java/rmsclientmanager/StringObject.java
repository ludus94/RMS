package rmsclientmanager;

public class StringObject {
    private String out;
    private int count;

    public StringObject() {
        out="";
        count=0;
    }

    public String getOut() {
        return out;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setOut(String out, boolean refresh) {
        if(!refresh) {
            this.out = this.out + out + "\n";
            count = count + 1;
        }
        else{
            this.out="";
            count=0;
        }
    }
}

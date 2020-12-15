package rmsserver;

public class StringObject {
    private String out;

    public StringObject() {
        out="";
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out,boolean refresh) {
        if(!refresh)
            this.out =this.out+out;
        else{
            this.out="";
        }
    }
}

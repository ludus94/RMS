package rmsclientmanager;

public class StringObject {
    private String out;
    private int count;

    /***
     * Costructior of object StringObject
     */
    public StringObject() {
        out="";
        count=0;
    }

    /***
     * Get Method return a String
     * @return
     */
    public String getOut() {
        return out;
    }

    /***
     * Set Method for count row in the string
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /***
     * Get Method for count row in a string
     * @return
     */
    public int getCount() {
        return count;
    }

    /***
     * Set method to Sting if refresh is false, append the string else reinitialize a string
     * @param out
     * @param refresh
     */
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

package simpleserver;

public class Response {
    private String status;
    private int entries;
    private Data[] data;

    public Response(String status, int entries, Data[] data){
        this.status = status;
        this.entries = entries;
        this.data = data;
    }

    public String getStatusString(){
        return this.status;
    }

    public int getEntries() {
        return this.entries;
    }

    public Data[] getData() {
        return this.data;
    }


}

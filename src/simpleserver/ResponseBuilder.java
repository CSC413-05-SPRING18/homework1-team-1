package simpleserver;


public class ResponseBuilder {

    private Response response;
    private Data[] data;
    private StatusCode statusCode;
    private int count;

    public enum StatusCode {
        OK,
        ERROR_GENERAL,
    }

    public void setStatus(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public Response build() {
        String statusString;
        switch (this.statusCode) {
            case OK:
                statusString = "OK";
                break;
            default:
                statusString = "ERROR_GENERAL";
        }
        if (data == null){
            data = new Data[0];
    }
        return new Response(statusString, data.length, data);
    }

    public int getDataLength()
    {
        return data.length;
    }

    public void setData(Data[] theData)
    {
        if (data == null) {
            data = new Data[0];
        }
        if (data.length < theData.length) {
            data = new Data[theData.length];
        }
            for (int i = 0; i < theData.length; i++) {
                data[i] = theData[i];
            }
    }

    public void setData(Data theData)
    {
        if (data == null) {
            data = new Data[0];
        }
            data = new Data[1];
            data[1] = theData;

    }


}


package simpleserver;

public class Parse {
    private String firstParameter = "";
    private String secondParameter = "";
    private int id = -1;

    public Parse (String mainRequestLine)
    {
        String lineParts[] = mainRequestLine.split(" ");
        String resourceString = lineParts[1];
        String lineParts2[] = resourceString.split("\\?");
        String resourceString2 = null;
        String lineParts3[] = null;

        if (lineParts2.length == 2) {

            resourceString2 = lineParts2[1];
            lineParts3 = resourceString2.split("=");
            this.secondParameter = lineParts3[0];
            this.id = Integer.parseInt(lineParts3[1]);
        }
        else {
            this.secondParameter = null;
        }


        this.firstParameter = lineParts2[0];

    }

    public String getFirstParameter()
    {
        return this.firstParameter;
    }

    public String getSecondParameter()
    {
        return this.secondParameter;
    }

    public int getId()
    {
        return this.id;
    }

    public boolean doWeHaveSecondParameter()
    {
        if (this.secondParameter == null)
            return false;
        return true;
    }

    public boolean isOurIntValid()
    {
        if (this.id == -1)
            return false;
        return true;
    }




}

package skeleton;

public enum BuildStatus{
    SUCCESS("success"),
    FAILURE("failure"),
    ERROR ("error"),
    PENDING("pending");

    public final String label;

    BuildStatus(String label){
        this.label = label;
    }

    @Override
    public String toString(){
        return label;
    }
}
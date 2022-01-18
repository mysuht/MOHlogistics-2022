package jo.gov.moh.contraceptives.model.entities;

public class Facility {
    private int id;
    private String name;
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Facility() {
        super();
    }
}

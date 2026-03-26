package model;

public class Pessoa {
    private String name;
    private String email;
    private String number;
    private String dateBirthday;

    public Pessoa() {
    }

    public Pessoa(String name, String email, String number, String dateBirthday) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.dateBirthday = dateBirthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDateBirthday() {
        return dateBirthday;
    }

    public void setDateBirthday(String dateBirthday) {
        this.dateBirthday = dateBirthday;
    }
}

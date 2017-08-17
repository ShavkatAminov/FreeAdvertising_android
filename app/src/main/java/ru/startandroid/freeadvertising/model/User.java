package ru.startandroid.freeadvertising.model;



public class User {
    private long id;
    private String firstname;
    private String lastname;
    private String telefon;
    private String login;
    private String parol;

    public User() {
        this.id = 0;
        this.firstname = "";
        this.lastname = "";
        this.telefon = "";
        this.login = "";
        this.parol = "";
    }

    public User(long id, String firstname, String lastname, String telefon, String login, String parol) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.telefon = telefon;
        this.login = login;
        this.parol = parol;
    }
    public User(String firstname, String lastname, String telefon, String login, String parol) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.telefon = telefon;
        this.login = login;
        this.parol = parol;
    }

    public long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getLogin() {
        return login;
    }

    public String getParol() {
        return parol;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setParol(String parol) {
        this.parol = parol;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
}

package ru.yourport.scheduler1c;

public class Resultat {
    String name;
    int age;

    public String getResultat() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Resultat(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Resultat() {
    }

    public Resultat(String name) {
        this.name = name;

    }


}

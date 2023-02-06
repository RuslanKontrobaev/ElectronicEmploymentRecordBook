package ru.istu.employmenthistoryapp;

public class Employee {
    private Integer id;
    private String surName, name, patronymic, dateBirth, education, profession;

    public Employee(Integer id, String surName, String name, String patronymic, String dateBirth, String education, String profession) {
        this.id = id;
        this.surName = surName;
        this.name = name;
        this.patronymic = patronymic;
        this.dateBirth = dateBirth;
        this.education = education;
        this.profession = profession;
    }

    public Integer getId() {
        return id;
    }

    public String getSurName() {
        return surName;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getEducation() {
        return education;
    }

    public String getProfession() {
        return profession;
    }

    public String getDateBirth() {
        return dateBirth;
    }
}

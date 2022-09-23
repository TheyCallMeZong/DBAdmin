package com.database.dbadmin.models;

import lombok.Data;
import java.util.Calendar;
import java.util.Date;

@Data
public class Client {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private Date birth;
    private String passportSeries;
    private String passportNumber;
    private String passportIssued;
    private Date dateOfIssue;
    private String photo;

    public Client(String name,
                  String surname,
                  String patronymic,
                  Date birth,
                  String passportSeries,
                  String passportNumber,
                  String passportIssued,
                  Date dateOfIssue,
                  String photo) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.birth = birth;
        this.passportSeries = passportSeries;
        this.passportNumber = passportNumber;
        this.passportIssued = passportIssued;
        this.dateOfIssue = dateOfIssue;
        this.photo = photo;
    }

    public Client(){

    }

    public void setBirth(Calendar calendar){
        birth = new Date(calendar.getTime().getTime());
    }

    public void setDateOfIssue(Calendar calendar){
        dateOfIssue = new Date(calendar.getTime().getTime());
    }
}

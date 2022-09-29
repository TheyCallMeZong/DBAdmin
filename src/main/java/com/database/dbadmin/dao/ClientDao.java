package com.database.dbadmin.dao;

import com.database.dbadmin.database.ClientPostgresSql;
import com.database.dbadmin.models.Client;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Date;
import java.util.regex.Pattern;

public class ClientDao {
    private ClientPostgresSql clientPostgresSql;

    public ClientDao(){
        clientPostgresSql = ClientPostgresSql.getInstance();
    }

    public void update(){
        clientPostgresSql.updateClient();
    }

    public boolean add(String fullName, String issued, String seriesAndNumberOfPassport,
                    LocalDate birth, LocalDate dateOfIssue) {
        if (fullName.isEmpty() || issued.isEmpty() || seriesAndNumberOfPassport.isEmpty()){
            return false;
        }
        Client client = createClient(fullName, issued, seriesAndNumberOfPassport, birth, dateOfIssue);
        if (client == null){
            return false;
        }
        return clientPostgresSql.addClient(client);
    }

    private Client createClient(String fullName, String issued, String seriesAndNumberOfPassport,
                                LocalDate birth, LocalDate dateOfIssue){
        Client client;
        String regexSeries = "^\\d{4}";
        String regexNumber = "^\\d{6}";
        Pattern patternPassportSeries = Pattern.compile(regexSeries);
        Pattern patternPassportNumber = Pattern.compile(regexNumber);
        try {
            String[] array = fullName.split(" ");
            String patronymic = null;
            if (array.length == 3) {
                patronymic = fullName.split(" ")[2];
            }
            String name = fullName.split(" ")[0];
            String surname = fullName.split(" ")[1];
            String seriesPassport = seriesAndNumberOfPassport.split(" ")[0];
            String numberPassport = seriesAndNumberOfPassport.split(" ")[1];
            if (!patternPassportNumber.matcher(numberPassport).matches()
                    || !patternPassportSeries.matcher(seriesPassport).matches()) {
                return null;
            }
            Date birthClient = java.sql.Date.valueOf(birth.toString());
            Date dateOfIssuePassport = java.sql.Date.valueOf(dateOfIssue.toString());

            client = new Client(name, surname, patronymic, birthClient, seriesPassport,
                    numberPassport, issued, dateOfIssuePassport, " ");
            return client;
        } catch (DateTimeException ex){
            System.out.println(ex);
            return null;
        }
    }
}

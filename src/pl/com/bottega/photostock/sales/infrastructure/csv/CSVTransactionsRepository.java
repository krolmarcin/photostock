package pl.com.bottega.photostock.sales.infrastructure.csv;

import com.sun.deploy.util.StringUtils;
import pl.com.bottega.photostock.sales.model.client.Transaction;
import pl.com.bottega.photostock.sales.model.money.Money;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class CSVTransactionsRepository {

    private String folderPath;

    public CSVTransactionsRepository(String folderPath) {
        this.folderPath = folderPath;
    }

    public void saveTransactions(String clientNumber, Collection<Transaction> transactions) {
        try (PrintWriter pw = new PrintWriter(getRepositoryPath(clientNumber))) {
            for (Transaction t : transactions) {
                String[] components = new String[]{
                        t.getValue().toString(),
                        t.getDescription(),
                        t.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME)
                };
                pw.println(StringUtils.join(Arrays.asList(components), ","));
            }
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }
    }

    //value,description,timestamp
    public Collection<Transaction> getTransactions(String clientNumber) {
        Collection<Transaction> transactionList = new LinkedList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(getRepositoryPath(clientNumber)))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] attributes = line.trim().split(",");
                String[] valueString = attributes[0].split(" ");
                Money value = Money.valueOf(valueString[0]);
                String description = attributes[1];
                LocalDateTime timestamp = LocalDateTime.parse(attributes[2]);
                Transaction transaction = new Transaction(value, description, timestamp);
                transactionList.add(transaction);
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        return transactionList;
    }

    private String getRepositoryPath(String clientNumber) {
        return folderPath + File.separator + "clients-" + clientNumber + "-transactions" + ".csv";
    }

}

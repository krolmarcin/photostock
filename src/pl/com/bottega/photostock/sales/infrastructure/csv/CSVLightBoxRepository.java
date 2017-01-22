package pl.com.bottega.photostock.sales.infrastructure.csv;

import com.sun.deploy.util.StringUtils;
import pl.com.bottega.photostock.sales.model.client.Client;
import pl.com.bottega.photostock.sales.model.lightbox.LightBox;
import pl.com.bottega.photostock.sales.model.lightbox.LightBoxRepository;
import pl.com.bottega.photostock.sales.model.product.Product;
import pl.com.bottega.photostock.sales.model.product.ProductRepository;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class CSVLightBoxRepository implements LightBoxRepository {

    private String path, tmpPath, folderPath;
    private ProductRepository productRepository;

    public CSVLightBoxRepository(ProductRepository productRepository, String folderPath) {
        this.productRepository = productRepository;
        this.folderPath = folderPath;
        this.path = folderPath + File.separator + "lightBoxRepository.csv";
        this.tmpPath = path + ".tmp";
    }

    @Override
    public void put(LightBox l) {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(path, true))) {
            String[] components = {l.getOwner().getNumber(), l.getName(), getProductNumbers(l.getProducts())};
            ensureLightBoxIsNotInRepository(l);
            printWriter.println(StringUtils.join(Arrays.asList(components), ","));
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private void ensureLightBoxIsNotInRepository(LightBox l) {
        if (findLightBox(l.getOwner(), l.getName()) != null)
            throw new IllegalArgumentException(String.format("LightBox %s is already exists", l.getName()));
    }

    @Override
    public Collection<LightBox> getFor(Client client) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            Collection<LightBox> lightBoxes = new LinkedList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] components = line.split(",");
                if (components[0].equals(client.getNumber()))
                    lightBoxes.add(new LightBox(client, components[1], getProductFromRepository(components[2])));
            }
            return lightBoxes;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Collection<String> getLightBoxNames(Client client) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            Collection<String> lightBoxNames = new LinkedList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] components = line.split(",");
                if (components[0].equals(client.getNumber()))
                    lightBoxNames.add(components[1]);
            }
            return lightBoxNames;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private Collection<Product> getProductFromRepository(String productNumber) {
        Collection<Product> products = new LinkedList<>();
        String[] numbers = productNumber.split("\\|");
        for (String number : numbers) {
            Product product = productRepository.get(number);
            if (product == null)
                throw new IllegalArgumentException(String.format("Product %s does not exist in ProductRepository", product.getNumber()));
            products.add(product);
        }
        return products;
    }

    @Override
    public LightBox findLightBox(Client client, String lightBoxName) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] components = line.split(",");
                if (components[0].equals(client.getNumber()) && components[1].equals(lightBoxName))
                    return new LightBox(client, lightBoxName, getProductFromRepository(components[2]));
            }
            return null;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void update(LightBox l) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
             PrintWriter printWriter = new PrintWriter(new FileWriter(path))
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] components = line.split(",");
                String number = components[0];
                if (number.equals(l.getOwner().getNumber()) && components[1].equals(l.getName()))
                    writeLightBox(l, printWriter);
                else
                    printWriter.println(line);
            }

        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        replaceFiles();

    }

    private void replaceFiles() {
        File file = new File(tmpPath);
        new File(path).delete();
        file.renameTo(new File(path));
    }

    private void writeLightBox(LightBox l, PrintWriter printWriter) {
        String[] components = {l.getOwner().getNumber(), l.getName(), getProductNumbers(l.getProducts())};
        printWriter.print(StringUtils.join(Arrays.asList(components), ","));
    }

    private String getProductNumbers(Collection<Product> products) {
        StringBuilder sb = new StringBuilder();
        for (Product product : products) {
            sb.append(product.getNumber()).append("|");
        }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}

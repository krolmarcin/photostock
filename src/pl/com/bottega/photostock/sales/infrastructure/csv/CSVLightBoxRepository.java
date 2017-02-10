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
import java.util.HashSet;
import java.util.LinkedList;

public class CSVLightBoxRepository implements LightBoxRepository {

    private String path, tmpPath, folderPath;

    private ProductRepository productRepository;

    public CSVLightBoxRepository(String folderPath, ProductRepository productRepository) {
        this.folderPath = folderPath;
        this.path = folderPath + File.separator + "lightboxes.csv";
        this.productRepository = productRepository;
        this.tmpPath = path + ".tmp";
    }

    //clientNumber, lightBoxName, numberOfProduct|numberOfProduct itd.
    @Override
    public void put(LightBox lightBox) {
        Client owner = lightBox.getOwner();
        boolean isNewLightBoxForClient = true;
        ensureCSVExists();
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
                PrintWriter printWriter = new PrintWriter(new FileWriter(tmpPath))
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] attributes = line.trim().split(",");
                String numberOfClient = attributes[0];
                if (numberOfClient.equals(owner.getNumber()) && lightBox.getName().equals(attributes[1])) {
                    writeLightBox(lightBox, printWriter);
                    isNewLightBoxForClient = false;
                } else
                    printWriter.println(line);
            }
            if (isNewLightBoxForClient) {
                writeLightBox(lightBox, printWriter);
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        replaceFiles();
    }

    private void ensureCSVExists() {
        try {
            new File(path).createNewFile();
        } catch (IOException e) {
            throw new DataAccessException(e);
        }
    }

    private void writeLightBox(LightBox lightBox, PrintWriter printWriter) {
        Collection<String> productNumbers = new LinkedList<>();
        for (Product product : lightBox) {
            productNumbers.add(product.getNumber());
        }
        String[] attributes = new String[]{
                lightBox.getOwner().getNumber(),
                lightBox.getName(),
                StringUtils.join(productNumbers, "|")
        };
        printWriter.println(StringUtils.join(Arrays.asList(attributes), ","));
    }

    //clientNumber, lightBoxName, numberOfProducts
    @Override
    public Collection<LightBox> getFor(Client client) {
        Collection<LightBox> lightBoxes = new HashSet<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] attributes = line.trim().split(",");
                String clientNumber = attributes[0];
                if (clientNumber.equals(client.getNumber())) {
                    lightBoxes.add(new LightBox(client, attributes[1], getProducts(attributes)));
                }
            }
            return lightBoxes;

        } catch (FileNotFoundException e) {
            return lightBoxes;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private Collection<Product> getProducts(String[] components) {
        Collection<Product> products = new LinkedList<>();
        if (components.length < 3)
            return products;
        String productNumbers = components[2];
        String[] numbers = productNumbers.split("\\|");
        for (String number : numbers) {
            Product product = productRepository.get(number);
            if (product == null)
                throw new IllegalArgumentException(String.format("Product %s does not exist in productRepository", product.getNumber()));
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
                    return new LightBox(client, lightBoxName, getProducts(components));
            }
            return null;
        } catch (FileNotFoundException e) {
            return null;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }


    @Override
    public Collection<String> getLightBoxNames(Client client) {
        Collection<String> lightBoxNames = new LinkedList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] components = line.split(",");
                if (components[0].equals(client.getNumber()))
                    lightBoxNames.add(components[1]);
            }
        } catch (FileNotFoundException e) {

        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        return lightBoxNames;
    }


    private void replaceFiles() {
        File file = new File(tmpPath);
        new File(path).delete();
        file.renameTo(new File(path));
    }

}

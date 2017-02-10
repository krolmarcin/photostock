package pl.com.bottega.photostock.sales.model.lightbox;

import pl.com.bottega.photostock.sales.model.client.Client;

import java.sql.SQLException;
import java.util.Collection;

public interface LightBoxRepository {

    void put(LightBox lightBox) throws SQLException;

    Collection<LightBox> getFor(Client client) throws SQLException;

    Collection<String> getLightBoxNames(Client client) throws SQLException;

    LightBox findLightBox(Client client, String lightBoxName) throws SQLException;

}

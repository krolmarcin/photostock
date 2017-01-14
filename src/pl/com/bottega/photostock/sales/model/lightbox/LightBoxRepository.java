package pl.com.bottega.photostock.sales.model.lightbox;

import pl.com.bottega.photostock.sales.model.client.Client;

import java.util.Collection;

public interface LightBoxRepository {
    void put(LightBox lightBox);

    Collection<LightBox> getFor(Client client);

    Collection<String> getLightBoxNames(Client client);

    LightBox findLightBox(Client client, String lightBoxName);
}

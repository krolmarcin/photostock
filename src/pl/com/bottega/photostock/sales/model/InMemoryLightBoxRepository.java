package pl.com.bottega.photostock.sales.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class InMemoryLightBoxRepository implements LightBoxRepository {

    private Map<Client, Collection<LightBox>> repository = new HashMap<>();

    @Override
    public void put(LightBox lightBox) {
        Client owner = lightBox.getOwner();
        repository.putIfAbsent(owner, new HashSet<>());
        Collection<LightBox> ownerLighboxes = repository.get(owner);
        ownerLighboxes.add(lightBox);
    }

    @Override
    public Collection<LightBox> getFor(Client client) {
        return repository.get(client);
    }

}

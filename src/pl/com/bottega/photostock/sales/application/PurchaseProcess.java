package pl.com.bottega.photostock.sales.application;

import pl.com.bottega.photostock.sales.model.*;

public class PurchaseProcess {

    private ClientRepository clientRepository;

    private ReservationRepository reservationRepository;

    private ProductRepository productRepository;

    public PurchaseProcess(ClientRepository clientRepository, ReservationRepository reservationRepository, ProductRepository productRepository) {
        this.clientRepository = clientRepository;
        this.reservationRepository = reservationRepository;
        this.productRepository = productRepository;
    }

    public String getReservation(String clientNumber) {
        Client client = clientRepository.get(clientNumber);
        if (!client.isActive())
            throw new IllegalArgumentException(String.format("Client %s is nit active", clientNumber));
        Reservation reservation = reservationRepository.getActiveReservationForClient(clientNumber);
        if (reservation == null) {
            reservation = new Reservation(client);
            reservationRepository.put(reservation);
        }
        return reservation.getNumber();
    }

    public void add(String reservationNumber, String productNumber) {
        Reservation reservation = reservationRepository.get(reservationNumber);
        if (reservation == null)
            throw new IllegalArgumentException(String.format("Reservation %s doesn't exist", reservationNumber));
        Product product = productRepository.get(productNumber);
        if (product == null)
            throw new IllegalArgumentException(String.format("Product %s doesn't exist", productNumber));
        reservation.add(product);
    }

    public Offer calculateOffer(String reservationNumber) {
        Reservation reservation = reservationRepository.get(reservationNumber);
        if (reservation == null)
            throw new IllegalArgumentException(String.format("Reservation %s doesn't exist", reservationNumber));
        return reservation.generateOffer();
    }

}

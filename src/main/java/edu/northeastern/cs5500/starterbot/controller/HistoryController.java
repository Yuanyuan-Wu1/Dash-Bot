package edu.northeastern.cs5500.starterbot.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.model.Address;
import edu.northeastern.cs5500.starterbot.model.Cart;
import edu.northeastern.cs5500.starterbot.model.History;
import edu.northeastern.cs5500.starterbot.model.HistoryOrder;
import edu.northeastern.cs5500.starterbot.model.Payment;
import edu.northeastern.cs5500.starterbot.model.Status;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bson.types.ObjectId;

@Singleton
public class HistoryController {

    GenericRepository<History> historyRepository;

    /**
     * Inject the historys from the MongoDB repository
     *
     * @param historyRepository - representing the user's order history in Generic Repository.
     */
    @Inject
    public HistoryController(GenericRepository<History> historyRepository) {
        this.historyRepository = historyRepository;
    }

    /**
     * This method is to show the history for the user
     *
     * @param discordId - String, representing user's discordUserId
     * @return the user's history based on their memberid.
     */
    @Nullable
    public History getHistoryForUser(String discordId) {
        Collection<History> userHistory = historyRepository.getAll();
        for (History currUserHistory : userHistory) {
            if (currUserHistory.getDiscordUserId().equals(discordId)) {
                return currUserHistory;
            }
        }
        History history = new History();
        history.setDiscordUserId(discordId);
        history.setHistoryOrder(new ArrayList<HistoryOrder>());
        historyRepository.add(history);
        return history;
    }

    /**
     * This method is to add the current order into the user history
     *
     * @param discordUserId - String, representing user discord ID
     * @param historyOrder - ArrayList, representing the history order list
     * @return the history repo
     */
    public GenericRepository<History> setHistoryForUser(
            String discordUserId, HistoryOrder historyOrder) {
        Collection<History> userHistory = historyRepository.getAll();
        for (History currUserHistory : userHistory) {
            if (currUserHistory.getDiscordUserId().equals(discordUserId)) {
                currUserHistory.getHistoryOrder().add(historyOrder);
                historyRepository.update(currUserHistory);
                return historyRepository;
            }
        }
        History history = new History();
        history.setDiscordUserId(discordUserId);
        history.setHistoryOrder(new ArrayList<HistoryOrder>());
        history.getHistoryOrder().add(historyOrder);
        historyRepository.add(history);
        return historyRepository;
    }

    /**
     * This method is to set the history order for user
     *
     * @param shoppingCart - Cart, representing user's final shoppingCart
     * @param address - Address, representing user's final shoppingCart
     * @param payment - Payment, representing user's final shoppingCart
     * @param total - Integer, representing user's final order price
     * @param date - Date, representing user's order time
     * @param status - Status, representing user's order status
     * @return the user's history based on their memberid.
     */
    @Nonnull
    public HistoryOrder setHistoryOrderForUser(
            Cart shoppingCart,
            Address address,
            Payment payment,
            Double total,
            LocalDateTime date,
            Status status) {

        HistoryOrder newHistoryOrder = new HistoryOrder();
        newHistoryOrder.setAddress(address);
        newHistoryOrder.setPayment(payment);
        newHistoryOrder.setShoppingCart(shoppingCart);
        newHistoryOrder.setTotal(total);
        newHistoryOrder.setDate(date);
        newHistoryOrder.setStatus(status);

        return newHistoryOrder;
    }

    /**
     * This method is to update order status, and will be use in the callback function when order is
     * delivered.
     *
     * @param discordId the user's discord id
     * @param id the object id of the order
     */
    public void changeStatus(String discordId, ObjectId id) {
        History userHistory = getHistoryForUser(discordId);
        ArrayList<HistoryOrder> historyOrders = userHistory.getHistoryOrder();
        for (HistoryOrder pastOrder : historyOrders) {
            if (pastOrder.getShoppingCart().getId().equals(id)) {
                pastOrder.setStatus(Status.DELIVERED);
            }
        }
        historyRepository.update(userHistory);
    }
}

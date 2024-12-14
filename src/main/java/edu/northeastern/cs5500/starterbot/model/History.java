package edu.northeastern.cs5500.starterbot.model;

import java.util.ArrayList;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class History implements Model {
    protected ObjectId id;

    // This is the "snowflake id" of the user
    protected String discordUserId;

    // user's history order info
    protected ArrayList<HistoryOrder> historyOrder;
}

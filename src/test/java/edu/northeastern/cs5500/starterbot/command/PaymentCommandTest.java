package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import java.awt.Color;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

public class PaymentCommandTest {
    @Test
    void testPaymentMatchesData() {
        PaymentCommand paymentCommand = new PaymentCommand();

        String type = paymentCommand.getName();

        CommandData commandData = paymentCommand.getCommandData();

        assertThat(type).isEqualTo(commandData.getName());
    }

    @Test
    void testEmbedMessage() {
        PaymentCommand paymentCommand = new PaymentCommand();
        MessageEmbed embed1 = paymentCommand.reviewSetPayment("payment");
        MessageEmbed embed2 = paymentCommand.reviewChangePayment("payment1", "payment2");
        assertThat(embed1.getTitle()).isNotEmpty();
        assertThat(embed1.getTitle())
                .isEqualTo("Hi, your payment info :credit_card:  states as follow:");
        assertThat(embed1.getFields()).hasSize(1);
        assertThat(embed1.getFooter()).isNotNull();
        assertThat(embed2.getTitle()).isNotEmpty();
        assertThat(embed2.getTitle())
                .isEqualTo("Hi, your payment information :credit_card:  has been changed: ");
        assertThat(embed2.getFields()).hasSize(2);
        assertThat(embed2.getColor()).isEqualTo(Color.GREEN);
        assertThat(embed2.getFooter()).isNotNull();
    }
}

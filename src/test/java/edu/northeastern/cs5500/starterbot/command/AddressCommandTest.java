package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import java.awt.Color;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

public class AddressCommandTest {
    @Test
    void testNameMatchesData() {
        AddressCommand addressCommand = new AddressCommand();

        String type = addressCommand.getName();

        CommandData commandData = addressCommand.getCommandData();

        assertThat(type).isEqualTo(commandData.getName());
        assertThat(commandData.getOptions().size()).isEqualTo(4);
        assertThat(commandData.getOptions().get(0).getName()).isEqualTo("address");
        assertThat(commandData.getOptions().get(1).getChoices().size()).isEqualTo(22);
        assertThat(commandData.getOptions().get(2).getChoices().size()).isEqualTo(1);
        assertThat(commandData.getOptions().get(3).getChoices().size()).isEqualTo(5);
    }

    @Test
    void testEmbedMessage() {
        AddressCommand addressCommand = new AddressCommand();
        MessageEmbed embed1 = addressCommand.reviewSetAddress("address");
        MessageEmbed embed2 = addressCommand.reviewChangeAddress("address1", "address2");
        assertThat(embed1.getTitle()).isNotEmpty();
        assertThat(embed1.getTitle())
                .isEqualTo("Hi, your delivery address   :truck:   has been set to: ");
        assertThat(embed1.getFields()).hasSize(1);
        assertThat(embed1.getFooter()).isNotNull();
        assertThat(embed2.getTitle()).isNotEmpty();
        assertThat(embed2.getTitle())
                .isEqualTo("Hi, your delivery address   :truck:   has been changed: ");
        assertThat(embed2.getFields()).hasSize(2);
        assertThat(embed2.getColor()).isEqualTo(Color.GREEN);
        assertThat(embed2.getFooter()).isNotNull();
    }
}

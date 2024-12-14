package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

class OrderCommandTest {
    @Test
    void testOrderNameMatchesData() {
        OrderCommand orderCommand = new OrderCommand();
        String name = orderCommand.getName();
        CommandData commandData = orderCommand.getCommandData();

        assertThat(name).isEqualTo(commandData.getName());
    }
}

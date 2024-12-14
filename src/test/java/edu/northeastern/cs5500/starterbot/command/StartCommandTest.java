package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

class StartCommandTest {

    @Test
    void testNameMatchesData() {
        StartCommand startCommand = new StartCommand();
        String name = startCommand.getName();
        CommandData commandData = startCommand.getCommandData();

        assertThat(name).isEqualTo(commandData.getName());
    }
}

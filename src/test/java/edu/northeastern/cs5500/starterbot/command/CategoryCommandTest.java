package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

public class CategoryCommandTest {
    @Test
    void testNameMatchesData() {
        CategoryCommand categoryCommand = new CategoryCommand();

        String type = categoryCommand.getName();

        CommandData commandData = categoryCommand.getCommandData();

        assertThat(type).isEqualTo(commandData.getName());
    }
}

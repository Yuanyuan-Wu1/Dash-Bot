package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

public class SetPostalCodeCommandTest {
    @Test
    void testUserPreferenceNameMatchesData() {
        SetPostalCodeCommand setPostalCodeCommand = new SetPostalCodeCommand();
        String name = setPostalCodeCommand.getName();
        CommandData commandData = setPostalCodeCommand.getCommandData();

        assertThat(name).isEqualTo(commandData.getName());
    }
}

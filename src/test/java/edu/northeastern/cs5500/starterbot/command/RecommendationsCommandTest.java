package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

class RecommendationsCommandTest {
    @Test
    void testPostalCodeMatchesData() {
        RecommendationsCommand RecommendationsCommand = new RecommendationsCommand();
        String postalcode = RecommendationsCommand.getName();
        CommandData commandData = RecommendationsCommand.getCommandData();

        assertThat(postalcode).isEqualTo(commandData.getName());
    }
}

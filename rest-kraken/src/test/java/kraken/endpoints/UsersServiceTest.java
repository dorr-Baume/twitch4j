package kraken.endpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.github.twitch4j.kraken.domain.KrakenUser;
import com.github.twitch4j.kraken.domain.KrakenUserList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag("integration")
public class UsersServiceTest extends AbstractKrakenServiceTest {

    @Test
    @DisplayName("getUsers")
    @Disabled
    public void getUsers() {
    	KrakenUserList resultList = getTwitchKrakenClient().getUsersByLogin(Arrays.asList("twitch4j")).execute();
        assertTrue(resultList.getUsers().size() == 1, "Number of found users was not 1!");
        KrakenUser user = resultList.getUsers().get(0);
        assertTrue(user.getId().equals(149223493l), "Twitch4J user id should be 149223493!");
        assertTrue(user.getBio().equals("Twitch4J IntegrationTest User"), "Twitch4J user bio should be \"Twitch4J IntegrationTest User\"!");
        assertEquals(user.getName(), "twitch4j", "Twitch4J user name should be twitch4j!");
        assertEquals(user.getDisplayName(), "twitch4j", "Twitch4J user display name should be twitch4j!");
        assertEquals(user.getType(), "user", "Twitch4J user type should be user!");
        assertEquals(user.getCreatedAt().getTime(), 1488456578184L, "Twitch4J user creation date is invalid!");
    }
}

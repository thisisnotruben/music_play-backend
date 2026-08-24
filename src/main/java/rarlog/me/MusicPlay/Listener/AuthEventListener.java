package rarlog.me.MusicPlay.Listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import rarlog.me.MusicPlay.Config;
import rarlog.me.MusicPlay.exception.UserNotFoundException;
import rarlog.me.entity.AppUser;
import rarlog.me.repository.AppUserRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthEventListener {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final AppUserRepository appUserRepository;

    @RabbitListener(queues = {Config.AUTH_USER_EVENT_QUEUE_NAME})
    public void consume(String jsonString) throws JsonProcessingException {

        JsonNode payload = mapper.readTree(jsonString);

        if (!payload.hasNonNull("type")
        && !payload.hasNonNull("details")
        && !payload.hasNonNull("user_id")) {
            return;
        }

        JsonNode details = payload.get("details");
        String userId = details.get("user_id").asText();

        switch (payload.get("type").asText()) {
            case "REGISTER":
                appUserRepository.save(AppUser.builder()
                        .userId(userId)
                        .email(details.get("email").asText())
                        .firstName(details.get("first_name").asText())
                        .lastName(details.get("last_name").asText()).build());
                break;

            case "UPDATE_PROFILE":
                AppUser editedUser = appUserRepository.findByUserId(userId)
                        .orElseThrow(() -> new UserNotFoundException(userId));

                if (details.hasNonNull("updated_first_name")) {
                    String updatedFirstName = details.get("updated_first_name").asText();
                    editedUser.setFirstName(updatedFirstName);
                }

                if (details.hasNonNull("updated_last_name")) {
                    String updatedLastName = details.get("updated_last_name").asText();
                    editedUser.setLastName(updatedLastName);
                }

                if (details.hasNonNull("updated_email")) {
                    String updatedEmail = details.get("updated_email").asText();
                    editedUser.setEmail(updatedEmail);
                }

                appUserRepository.save(editedUser);
                break;
        }
    }

}

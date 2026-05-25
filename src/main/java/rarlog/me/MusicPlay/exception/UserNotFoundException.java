package rarlog.me.MusicPlay.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(String.format("User not found. %s", message));
    }

}

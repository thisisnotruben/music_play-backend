package rarlog.me.MusicPlay.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
    
    private LocalDateTime time;
    private String message;

}

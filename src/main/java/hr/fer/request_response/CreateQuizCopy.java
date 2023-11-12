package hr.fer.request_response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuizCopy {

    private Long id;
    private List<Answer> answerList;
    private Long userId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Answer{
        private Long questionId;
        private Long answerId;
    }

}

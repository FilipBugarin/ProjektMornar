package hr.fer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "quiz", schema = "public")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String quizName;
    private boolean privateQuiz;
    private String description;
    private boolean masterQuiz;
    private boolean randomOrder;
    private boolean finished;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questionList;

    @JsonIgnore
    @ManyToOne
    private Quiz masterQuizObject;

    @ManyToOne
    private QuizCategory category;

    @ManyToOne
    private User takenBy;

    @ManyToOne
    private User createdBy;

    public Quiz(Quiz that) {
        this(that.getId(), that.getQuizName(), that.isPrivateQuiz(), that.getDescription(), that.isMasterQuiz(), that.isRandomOrder(), that.isFinished(), that.getQuestionList(), that.getMasterQuizObject(), that.getCategory(), that.getTakenBy(), that.getCreatedBy());
    }
}

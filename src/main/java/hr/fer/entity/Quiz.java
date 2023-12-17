package hr.fer.entity;

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

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questionList;

    @ManyToOne
    private User takenBy;

    @ManyToOne
    private User createdBy;

    public Quiz(Quiz that) {
        this(that.getId(), that.getQuizName(), that.isPrivateQuiz(), that.getDescription(), that.isMasterQuiz(), that.isRandomOrder(), that.getQuestionList(), that.getTakenBy(), that.getCreatedBy());
    }
}

package hr.fer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
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

    //copy constructor
    public Quiz(Quiz copy, User takenBy){
        this.id = null;
        this.quizName = copy.getQuizName();
        this.privateQuiz = copy.isPrivateQuiz();
        this.description = copy.getDescription();
        this.masterQuiz = false;
        this.randomOrder = copy.isRandomOrder();
        this.takenBy = takenBy;
        this.createdBy = copy.getCreatedBy();

        List<Question> list = new ArrayList<>();
        for(Question q: copy.getQuestionList()){
            list.add(new Question(q, this));
        }
        this.questionList = list;
    }
}
